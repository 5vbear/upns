package com.chinatelecom.myctu.upnsa.exception;

/**
 * 包序列化烦序列化错误
 * <p/>
 * User: snowway
 * Date: 10/23/13
 * Time: 5:32 PM
 */
public class PacketMarshallerException extends UpnsAgentException {

    public PacketMarshallerException() {
    }

    public PacketMarshallerException(String detailMessage) {
        super(detailMessage);
    }

    public PacketMarshallerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public PacketMarshallerException(Throwable throwable) {
        super(throwable);
    }
}
