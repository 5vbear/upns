package com.chinatelecom.myctu.upnsa.protocol.support;

import com.chinatelecom.myctu.upnsa.protocol.Packet;
import com.chinatelecom.myctu.upnsa.protocol.PacketFactory;
import com.chinatelecom.myctu.upnsa.protocol.PacketTransfer;
import com.chinatelecom.myctu.upnsa.protocol.Payload;

/**
 * JsonPacketFactory
 * <p/>
 * User: snowway
 * Date: 10/24/13
 * Time: 11:41 AM
 */
public class JsonPacketFactory implements PacketFactory {

    /**
     * 创建连接请求包
     *
     * @param subscriberId 订阅Id,对应Routing's id
     * @return 连接请求包
     */
    public Packet createConnectRQ(String subscriberId) {
        Payload payload = createPayload();
        Packet packet = new Packet(Packet.TYPE_CONNECT_RQ, payload);
        payload.put("i", subscriberId);
        return packet;
    }

    private Payload createPayload() {
        return new JsonPayload();
    }

    @Override
    public Payload createPayload(byte[] content) {
        return JsonPayload.from(content);
    }

    /**
     * 创建消息读取回执
     *
     * @param messageId   消息id
     * @param deviceToken device token
     * @return Receipt包
     */
    public Packet createReceipt(String messageId, String deviceToken) {
        Payload payload = createPayload();
        Packet packet = new Packet(Packet.TYPE_RECEIPT, payload);
        payload.put("i", messageId);
        payload.put("o", deviceToken);
        payload.put("t", System.currentTimeMillis());
        return packet;
    }

    /**
     * 创建Ping包
     *
     * @return Ping包
     */
    public Packet createPing(long timestamp) {
        Payload payload = createPayload();
        Packet packet = new Packet(Packet.TYPE_PING, payload);
        payload.put("t", timestamp);
        return packet;
    }

    /**
     * 创建历史消息包
     */
    public Packet createHistory(String userId, String applicationId, int limit, long timestamp) {
        Payload payload = createPayload();
        Packet packet = new Packet(Packet.TYPE_HISTORY, payload);
        payload.put("u", userId);
        payload.put("a", Integer.parseInt(applicationId));
        payload.put("l", limit);
        payload.put("t", timestamp);
        payload.put("r", true);//获取未读消息
        payload.put("k", true);//获取以后自动ACK
        return packet;
    }


}
