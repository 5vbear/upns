/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.model;

import java.util.Date;
import java.util.Map;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-8-下午10:00:33
 */
public class MessageACK extends AbstractEntity {

    private static final long serialVersionUID = 7215402396034864345L;

    public static final String APP_ID = "a";

    public static final String USER_ID = "u";

    public static final String MESSAGE_ID = "m";

    public static final String ACK = "k";

    public static final String ACK_TIME = "t";

    public static final String TTL = "l";

    public MessageACK() {
        super();
        this.ttl = new Date(this.createTime);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = super.toMap();
        out.put(APP_ID, appId);
        out.put(USER_ID, userId);
        out.put(MESSAGE_ID, messageId);
        out.put(ACK, ack);
        out.put(ACK_TIME, ackTime);
        out.put(TTL, ttl);
        return out;
    }

    @Override
    public void fromMap(Map<String, Object> in) {
        super.fromMap(in);
        this.appId = (Integer) in.get(APP_ID);
        this.userId = (String) in.get(USER_ID);
        this.messageId = (String) in.get(MESSAGE_ID);
        this.ack = (Boolean) in.get(ACK);
        this.ackTime = (Long) in.get(ACK_TIME);
    }

    public Date getAckDate() {
        return ack ? new Date(ackTime) : null;
    }


    public Integer appId;

    public String userId;

    public String messageId;

    public Boolean ack = false;

    public Long ackTime;

    public Date ttl;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Boolean getAck() {
        return ack;
    }

    public void setAck(Boolean ack) {
        this.ack = ack;
    }

    public Long getAckTime() {
        return ackTime;
    }

    public void setAckTime(Long ackTime) {
        this.ackTime = ackTime;
    }

    public Date getTtl() {
        return ttl;
    }

    public void setTtl(Date ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return String.format("MessageACK [appId=%s, userId=%s, messageId=%s, ack=%s, ackTime=%s, ttl=%s]",
                appId,
                userId,
                messageId,
                ack,
                ackTime,
                ttl);
    }

}
