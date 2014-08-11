/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.msg;

import com.sirius.upns.protocol.business.BusinessPacket;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author pippo
 * @project protocol
 * @date 2013-8-18-下午3:00:19
 */
public abstract class MessagePacket implements BusinessPacket {

    private static final long serialVersionUID = -1694701836091890794L;

    public static final String ID = "i";

    public static final String APP_ID = "o";

    public static final String TIME = "e";

    public MessagePacket() {
        this.time = System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = new LinkedHashMap<String, Object>();
        out.put(ID, id);
        out.put(APP_ID, appId);
        out.put(TIME, time);
        return out;
    }

    @Override
    public void fromMap(Map<String, Object> in) {
        this.id = (String) in.get(ID);
        this.appId = (Integer) in.get(APP_ID);
        this.time = (Long) in.get(TIME);
    }

    /* 消息uuid */
    public String id;

    /* 那个应用发出 */
    public int appId;

    /* 什么时候发出 */
    public long time;

    @Override
    public String toString() {
        return String.format("MessagePacket [id=%s, appId=%s, time=%s]",
                id,
                appId,
                time);
    }
}