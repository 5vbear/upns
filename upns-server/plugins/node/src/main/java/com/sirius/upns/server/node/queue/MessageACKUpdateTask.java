/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.queue;

import com.sirius.upns.server.node.domain.model.Message;

import java.util.Map;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-9-上午9:56:25
 */
public class MessageACKUpdateTask extends TimelineUpdateTask {

    private static final long serialVersionUID = -2922876681328062000L;

    private static final String USER_ID = "u";

    private static final String ACK = "a";

    public MessageACKUpdateTask() {
        this.publish_time = System.currentTimeMillis();
    }

    public MessageACKUpdateTask(Message toTrace, String userId, boolean ack) {
        super(toTrace);
        this.userId = userId;
        this.ack = ack;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = super.toMap();
        out.put(USER_ID, userId);
        out.put(ACK, ack);
        return out;
    }

    @Override
    public void fromMap(Map<String, Object> in) {
        super.fromMap(in);
        userId = (String) in.get(USER_ID);
        ack = (boolean) in.get(ACK);
    }

    protected String userId;

    protected boolean ack;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    @Override
    public String toString() {
        return String.format("MessageACKUpdateTask [userId=%s, ack=%s]",
                userId,
                ack);
    }
}
