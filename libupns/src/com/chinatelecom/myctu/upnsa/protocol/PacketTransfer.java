package com.chinatelecom.myctu.upnsa.protocol;

import java.io.IOException;

/**
 * 包传送通道接口
 * <p/>
 * User: snowway
 * Date: 10/22/13
 * Time: 5:00 PM
 */
public interface PacketTransfer {


    /**
     * 事件类型
     */
    enum EventType {
        CONNECTED, FINISH_HISTORY, DISCONNECTED, FAULT, MESSAGE,
    }

    /**
     * 事件监听器
     */
    interface EventListener {
        void on(EventType type, Object... params);
    }

    /**
     * 同步传送Packet
     *
     * @param packet 需要发送的包
     * @return 响应包
     */
    void send(Packet packet) throws IOException;


    /**
     * 连接服务器
     *
     * @param userId   用户id
     * @param listener 传输监听器
     */
    void connect(String userId, EventListener listener);


    /**
     * 关闭传输服务
     *
     */
    void disconnect();


    /**
     * @return 和服务器是否处于连接状态
     */
    boolean isConnected();


    /**
     * 通知收到了心跳包
     */
    void notifyHeartBeatReceived();

    /**
     * @return 获得当前连接的用户Id
     */
    String getCurrentUser();


    /**
     * 触发事件
     *
     * @param type    事件类型
     * @param targets 事件参数
     */
    void fireEvent(EventType type, Object... targets);
}
