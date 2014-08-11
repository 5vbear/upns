/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.protocol;
/**
 * User: snowway
 * Date: 9/11/13
 * Time: 1:52 PM
 */

import java.io.Serializable;

/**
 * 网络传输的数据包
 * <p/>
 * User: snowway
 * Date: 9/5/13
 * Time: 2:25 PM
 */
public class Packet implements Serializable {

    public static final byte TYPE_PING = (byte) 1;
    public static final byte TYPE_CONNECT_RQ = (byte) 2;
    public static final byte TYPE_CONNECT_RS = (byte) 3;
    public static final byte TYPE_MESSAGE = (byte) 8;
    public static final byte TYPE_RECEIPT = (byte) 9;
    public static final byte TYPE_FAULT = (byte) 10;
    public static final byte TYPE_CLOSE = (byte) 11;
    public static final byte TYPE_UNREAD = (byte) 12;
    public static final byte TYPE_HISTORY = (byte) 14;

    /**
     * 包类型
     */
    private byte type;

    /**
     * 序列化接口
     */
    private Payload payload;

    public Packet(byte type, Payload payload) {
        this.type = type;
        this.payload = payload;
    }

    /**
     * @return 字节数组
     */
    public byte[] toByteArray() {
        return payload.toByteArray();
    }

    public byte getType() {
        return type;
    }

    public Payload getPayload() {
        return payload;
    }
}
