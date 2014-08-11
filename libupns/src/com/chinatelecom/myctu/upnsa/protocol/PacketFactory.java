package com.chinatelecom.myctu.upnsa.protocol;

/**
 * Packet创建工厂
 * <p/>
 * User: snowway
 * Date: 10/23/13
 * Time: 5:02 PM
 */
public interface PacketFactory {


    /**
     * 创建连接请求包
     *
     * @param subscriberId 订阅Id,对应Routing's id
     * @return 连接请求包
     */
    Packet createConnectRQ(String subscriberId);

    /**
     * 创建消息读取回执
     *
     * @param messageId   消息id
     * @param deviceToken device token
     * @return Receipt包
     */
    Packet createReceipt(String messageId, String deviceToken);

    /**
     * 创建Ping包
     *
     * @return Ping包
     */
    Packet createPing(long timestamp);

    /**
     * 创建历史消息包
     */
    Packet createHistory(String userId, String applicationId, int limit, long timestamp);

    /**
     * 从content创建Payload
     *
     * @param content content  byte array
     * @return Payload
     */
    Payload createPayload(byte[] content);
}
