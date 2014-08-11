package com.chinatelecom.myctu.upnsa.protocol.support;

import com.chinatelecom.myctu.upnsa.UpnsAgentSpecification;
import com.chinatelecom.myctu.upnsa.core.http.NetworkDetector;
import com.chinatelecom.myctu.upnsa.core.injection.Dependency;
import com.chinatelecom.myctu.upnsa.core.utils.Logger;
import com.chinatelecom.myctu.upnsa.core.utils.StringUtils;
import com.chinatelecom.myctu.upnsa.exception.UpnsAgentException;
import com.chinatelecom.myctu.upnsa.manager.UpnsAgentManager;
import com.chinatelecom.myctu.upnsa.model.Routing;
import com.chinatelecom.myctu.upnsa.protocol.Packet;
import com.chinatelecom.myctu.upnsa.protocol.PacketFactory;
import com.chinatelecom.myctu.upnsa.protocol.PacketListener;
import com.chinatelecom.myctu.upnsa.protocol.PacketTransfer;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * XSocketPacketTransfer
 * <p/>
 * User: snowway
 * Date: 10/22/13
 * Time: 5:05 PM
 */
public class XSocketPacketTransfer implements PacketTransfer, UpnsAgentSpecification {

    /**
     * JSON序列化/反序列化类型
     */
    public static final byte MARSHALLER_TYPE_JSON = (byte) 1;

    /**
     * MSGPACK序列化/反序列化类型
     */
    public static final byte MARSHALLER_TYPE_MSGPACK = (byte) 2;

    /**
     * 默认重连时间间隔
     */
    public static final int DEFAULT_RECONNECTION_TIMEOUT = 10 * 60;

    /**
     * 连接
     */
    private INonBlockingConnection connection;


    /**
     * 当前登录用户
     */
    private String currentUser;

    @Dependency
    protected PacketListener packetListener;

    @Dependency
    protected PacketFactory packetFactory;

    @Dependency
    protected NetworkDetector networkDetector;

    @Dependency
    protected UpnsAgentManager upnsAgentManager;

    private EventListener eventListener;

    private IHandler handler = new HandlerDispatcher();

    /**
     * 重连Runnable
     */
    private Runnable reconnectionRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                reconnect();
            } catch (IOException ex) {
                Logger.error("不能重新连接," + ex.getMessage(), ex);
            }
        }
    };

    /**
     * ScheduledExecutorService
     */
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


    /**
     * reconnectionFuture
     */
    private ScheduledFuture reconnectionFuture;

    @Override
    public synchronized void connect(String userId, EventListener listener) {
        if (!networkDetector.isConnected()) {
            Logger.warn("无可用的网络,本次放弃连接推送服务器");
            fireEvent(EventType.FAULT, Error.NETWORK_UNAVAILABLE_CODE, Error.NETWORK_UNAVAILABLE_MSG);
            return;
        }

        if (isConnected() && (StringUtils.equals(userId, getCurrentUser()))) {
            Logger.warn(String.format("当前用户%s已成功连接至推送服务器,无需重连", userId));
            return;
        }

        /**
         * 重试次数
         */
        int retryTimes = 3;
        executeConnect(userId, listener, retryTimes);
    }


    private void executeConnect(String userId, EventListener listener, int retryTimes) {
        Logger.debug(String.format("尝试使用用户%s连接服务器", userId));
        try {
            if (isConnected()) {
                disconnect();
            }
            this.currentUser = userId;
            upnsAgentManager.userAuthenticated(userId);
            this.eventListener = listener;
            Routing routing = upnsAgentManager.getRouting(userId);
            InetAddress address = InetAddress.getByName(routing.getHost());
            this.connection = ConnectionUtils.synchronizedConnection(new NonBlockingConnection(address,
                    routing.getPort(), handler));
            this.connection.setAutoflush(false);
            this.connection.setFlushmode(IConnection.FlushMode.ASYNC);
            //连接后首先发送ConnectRQ包
            Logger.debug("发送connectRQ包");
            Packet packet = packetFactory.createConnectRQ(routing.getId());
            send(packet);
        } catch (Exception ex) {
            int currentRetryTimes = retryTimes - 1;
            UpnsAgentException cause = getRootException(ex);
            if (currentRetryTimes > 0) {
                String message = "本次不能连接服务器" + (cause == null ? "" : "[" + cause.getMessage() + "]") + ",尝试重连";
                Logger.warn(message);
                executeConnect(userId, listener, currentRetryTimes);
            } else {
                Logger.error("重试3次后仍无法连接服务器,放弃重连");
                fireEvent(EventType.FAULT, Error.CONNECT_FAILED_CODE, Error.CONNECT_FAILED_MSG + (cause == null ? "" : "[" + cause.getMessage() + "]"));
            }
        }
    }


    private UpnsAgentException getRootException(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        if (throwable instanceof UpnsAgentException) {
            return (UpnsAgentException) throwable;
        }
        return getRootException(throwable.getCause());
    }

    @Override
    public synchronized void disconnect() {
        Logger.debug("尝试关闭连接");
        try {
            if (this.connection != null) {
                this.connection.close();
                this.connection = null;
                Logger.debug("关闭连接成功");
            }
        } catch (IOException ex) {
            Logger.error("关闭连接失败", ex);
        }
    }

    @Override
    public boolean isConnected() {
        return this.connection != null && this.connection.isOpen();
    }

    @Override
    public void notifyHeartBeatReceived() {
        Logger.debug("收到心跳包,重新计算自动重连间隔");
        scheduleReconnection();
    }

    /**
     * 重连
     *
     * @throws IOException
     */
    private boolean reconnect() throws IOException {
        Logger.debug(String.format("尝试使用用户%s重新建立连接", this.getCurrentUser()));
        disconnect();
        if (StringUtils.isNotBlank(getCurrentUser()) && this.eventListener != null) {
            this.connect(getCurrentUser(), this.eventListener);
            return true;
        }
        return false;
    }

    @Override
    public String getCurrentUser() {
        //尝试得到最后一次登录用户的信息
        if (StringUtils.isBlank(this.currentUser)) {
            this.currentUser = upnsAgentManager.getConfiguration().getUserId();
        }
        return this.currentUser;
    }

    @Override
    public void fireEvent(EventType type, Object... targets) {
        if (eventListener != null) {
            eventListener.on(type, targets);
        }
    }


    /**
     * 当收到包后的分发Handler
     */
    private class HandlerDispatcher implements IDataHandler, IConnectHandler, IDisconnectHandler {

        @Override
        public boolean onConnect(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            Logger.debug(String.format("成功连接到服务器:%s:%d,当前用户:%s", connection.getRemoteAddress(),
                    connection.getRemotePort(), getCurrentUser()));
            scheduleReconnection();
            fireEvent(EventType.CONNECTED);
            return true;
        }

        @Override
        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
            Header header = readHeader(connection);
            if (header == null) {
                return true;
            }
            Logger.debug(String.format("收到包头信息,长度:%d,类型:%d", header.length, header.type));
            byte[] content = connection.readBytesByLength(header.length);
            Logger.debug(String.format("收到包体原始内容:%s", new String(content)));
            try {
                packetListener.onPacket(header.type, content, XSocketPacketTransfer.this);
            } catch (Exception ex) {
                Logger.error(String.format("调用PacketListener失败,%s", ex.getMessage()), ex);
            }
            return true;
        }

        @Override
        public boolean onDisconnect(INonBlockingConnection connection) throws IOException {
            Logger.warn("从服务器断开了连接");
            fireEvent(EventType.DISCONNECTED);
            connection.close();
            return true;
        }
    }

    /**
     * 安排重连任务
     */
    private void scheduleReconnection() {
        if (networkDetector.isConnected()) {
            if (reconnectionFuture != null) {
                reconnectionFuture.cancel(true);
                reconnectionFuture = null;
            }
            reconnectionFuture = executor.schedule(
                    reconnectionRunnable,
                    DEFAULT_RECONNECTION_TIMEOUT,
                    TimeUnit.SECONDS);
            Logger.debug("设置10分钟如果不收到心跳,将自动重连");
        }
    }


    /**
     * 写入包信息
     */
    protected void writePacket(INonBlockingConnection connection,
                               Packet packet) throws IOException {
        byte[] payloadData = packet.toByteArray();
        int length = payloadData.length + 2;
        connection.write(length);
        connection.write(MARSHALLER_TYPE_JSON);// 序列化类型
        connection.write(packet.getType());// 包类型
        connection.flush();
        connection.write(payloadData);
        connection.flush();
        Logger.debug(String.format("发送包信息,type:%s,length:%d,原始内容:%s", packet.getType(),
                length, new String(payloadData)));
    }


    /**
     * 读取包头信息
     */
    protected Header readHeader(INonBlockingConnection connection) throws IOException {
        //~transaction read begin
        connection.markReadPosition();
        try {
            int length = connection.readInt();
            byte marshallerType = connection.readByte();
            byte type = connection.readByte();
            connection.removeReadMark();//now can read content ready
            return new Header(marshallerType, type, length - 2);//去掉两个字节长度的type
        } catch (BufferUnderflowException ex) {//not enough data
            connection.resetToReadMark();//re-read from head
            return null;
        }//~transaction read end;
    }

    @Override
    public void send(Packet packet) throws IOException {
        writePacket(this.connection, packet);
    }


    /**
     * 包头
     * <p/>
     * User: snowway
     * Date: 9/5/13
     * Time: 10:47 PM
     */
    private class Header implements Serializable {

        /**
         * 包的类型
         */
        public byte type;

        /**
         * 包体长度
         */
        public int length;

        /**
         * 序列化类型
         */
        public int marshallerType;


        public Header(int marshallerType, byte type, int length) {
            this.marshallerType = marshallerType;
            this.type = type;
            this.length = length;
        }
    }
}
