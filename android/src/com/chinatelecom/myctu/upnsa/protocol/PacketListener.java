package com.chinatelecom.myctu.upnsa.protocol;

/**
 * 消息包接收监听器
 * <p/>
 * User: snowway
 * Date: 10/23/13
 * Time: 4:50 PM
 */
public interface PacketListener {

    /**
     * 当收到消息包后回调
     *
     * @param type     包类型
     * @param content  包体原始内容
     * @param transfer PacketTransfer
     * @throws Exception
     */
    void onPacket(byte type, byte[] content, PacketTransfer transfer) throws Exception;
}
