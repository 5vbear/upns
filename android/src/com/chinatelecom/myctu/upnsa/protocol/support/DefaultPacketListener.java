package com.chinatelecom.myctu.upnsa.protocol.support;


import android.telephony.TelephonyManager;
import com.chinatelecom.myctu.upnsa.UpnsAgentSpecification;
import com.chinatelecom.myctu.upnsa.core.injection.Dependency;
import com.chinatelecom.myctu.upnsa.core.utils.Logger;
import com.chinatelecom.myctu.upnsa.manager.UpnsAgentManager;
import com.chinatelecom.myctu.upnsa.model.Message;
import com.chinatelecom.myctu.upnsa.protocol.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DefaultPacketListener
 * <p/>
 * User: snowway
 * Date: 10/23/13
 * Time: 4:54 PM
 */
public class DefaultPacketListener implements PacketListener, UpnsAgentSpecification {

    public static final int HISTORY_LIMIT = 5;

    @Dependency
    protected UpnsAgentManager upnsAgentManager;

    @Dependency
    protected TelephonyManager telephonyManager;

    @Dependency
    protected PacketFactory packetFactory;

    private Map<Byte, Handler> handlers = new HashMap<Byte, Handler>();

    public DefaultPacketListener() {
        handlers.put(Packet.TYPE_CONNECT_RS, connectRSHandler);
        handlers.put(Packet.TYPE_MESSAGE, messageHandler);
        handlers.put(Packet.TYPE_FAULT, faultHandler);
        handlers.put(Packet.TYPE_CLOSE, closeHandler);
        handlers.put(Packet.TYPE_HISTORY, historyHandler);
        handlers.put(Packet.TYPE_UNREAD, unreadHandler);
        handlers.put(Packet.TYPE_PING, pingHandler);
    }

    @Override
    public void onPacket(byte type, byte[] content, PacketTransfer transfer) throws Exception {
        Payload payload = packetFactory.createPayload(content);
        Handler handler = handlers.get(type);
        if (handler != null) {
            handler.handle(transfer, payload);
            Logger.debug(String.format("处理类型为:%d的包成功", type));
        } else {
            Logger.warn(String.format("未注册包类型为:%d的处理器", type));
        }
    }


    /**
     * 处理器接口
     */
    private interface Handler {
        void handle(PacketTransfer transfer, Payload payload) throws Exception;
    }


    /**
     * 处理连接响应
     */
    private Handler connectRSHandler = new Handler() {
        @Override
        public void handle(PacketTransfer transfer, Payload payload) throws Exception {
            boolean successful = payload.getBoolean("s");
            if (!successful) {
                int errorCode = payload.getInt("e");
                Logger.info(String.format("连接服务器拒绝,错误码:%d", errorCode));
                transfer.disconnect();
            } else {
                transfer.fireEvent(PacketTransfer.EventType.CONNECTED);
            }
        }
    };

    /**
     * 处理Ping
     */
    private Handler pingHandler = new Handler() {
        @Override
        public void handle(PacketTransfer transfer, Payload payload) throws Exception {
            transfer.send(packetFactory.createPing(payload.getLong("t")));
            transfer.notifyHeartBeatReceived();
        }
    };


    /**
     * 处理未读消息
     */
    private Handler unreadHandler = new Handler() {
        @Override
        public void handle(PacketTransfer transfer, Payload payload) throws Exception {
            Logger.debug("收到APPUnread包,发起获取History请求");
            String applicationId = payload.getString("a");
            long lastUnread = payload.getLong("l");
            Packet history = packetFactory.createHistory(transfer.getCurrentUser(), applicationId,
                    HISTORY_LIMIT, lastUnread);
            transfer.send(history);
        }
    };

    /**
     * 处理历史消息请求
     */
    private Handler historyHandler = new Handler() {
        @Override
        public void handle(PacketTransfer transfer, Payload payload) throws Exception {
            String userId = payload.getString("u");
            String applicationId = payload.getString("a");
            long timestamp = payload.getLong("t");
            List<Payload> marshallers = payload.getPayloadList("m");
            List<Message> messages = new ArrayList<Message>();
            for (Payload item : marshallers) {
                Message message = createMessage(transfer, item);
                messages.add(message);
            }
            upnsAgentManager.receiveMessages(messages);
            if (marshallers.size() == HISTORY_LIMIT) {//可能还有多余的History,继续发起History请求
                Logger.debug("还有未获取的历史消息,继续发送历史请求");
                Packet history = packetFactory.createHistory(userId, applicationId, HISTORY_LIMIT, timestamp);
                transfer.send(history);
            } else {
                Logger.debug("历史消息获取完毕");
                upnsAgentManager.finishReceiveMessages(userId, applicationId);
                transfer.fireEvent(PacketTransfer.EventType.FINISH_HISTORY, applicationId);
            }
        }
    };

    /**
     * 处理连接关闭消息
     */
    private Handler closeHandler = new Handler() {
        @Override
        public void handle(PacketTransfer transfer, Payload payload) throws Exception {
            Logger.warn("服务器要求关闭连接");
            transfer.disconnect();
            transfer.fireEvent(PacketTransfer.EventType.DISCONNECTED);
        }
    };


    /**
     * 出错消息处理器
     */
    private Handler faultHandler = new Handler() {
        @Override
        public void handle(PacketTransfer transfer, Payload payload) throws Exception {
            String code = payload.getString("c");
            String description = payload.getString("d");
            Logger.warn(String.format("收到服务器报错信息,错误码:%s,错误描述:%s", code, description));
            transfer.fireEvent(PacketTransfer.EventType.FAULT,
                    Error.PUSH_SERVER_ERROR_CODE_PREFX + code,
                    Error.PUSH_SERVER_ERROR_MSG + "[" + description + "]");
        }
    };


    /**
     * 消息包处理器
     */
    private Handler messageHandler = new Handler() {
        @Override
        public void handle(PacketTransfer transfer, Payload payload) throws Exception {
            Message message = createMessage(transfer, payload);
            Packet receipt = packetFactory.createReceipt(
                    message.getMessageId(), telephonyManager.getDeviceId());
            transfer.send(receipt);
            upnsAgentManager.receiveMessage(message);
            Logger.debug("收到消息包并成功存储到sqlite,发送receipt回执");
            transfer.fireEvent(PacketTransfer.EventType.MESSAGE,
                    message.getApplicationId(), message.getMessageId());
        }
    };

    /**
     * 从PacketMarshaller创建Message
     */
    private Message createMessage(PacketTransfer transfer, Payload payload) {
        Message message = new Message();
        message.setMessageId(payload.getString("i"));
        message.setCreateTime(payload.getLong("e"));
        message.setReceiveTime(System.currentTimeMillis());
        message.setGroupId(payload.getString("g"));
        message.setTitle(payload.getString("t"));
        message.setContent(payload.getString("c"));
        message.setApplicationId(payload.getString("o"));
        message.setUserId(transfer.getCurrentUser());
        message.setExtension(payload.getMap("x"));
        if (message.getExtension() != null) {
            Logger.debug("消息扩展信息:" + message.getExtension());
        }
        return message;
    }
}
