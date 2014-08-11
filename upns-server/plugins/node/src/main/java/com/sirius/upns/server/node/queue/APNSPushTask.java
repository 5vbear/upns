/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node.queue;

import com.sirius.upns.server.node.domain.model.Message;

import java.util.Map;

/**
 * User: pippo
 * Date: 13-11-28-13:49
 */
public class APNSPushTask extends TimelineUpdateTask {

    private static final long serialVersionUID = 858391282978425312L;

    private static final String USER_ID = "us";

    public APNSPushTask() {

    }

    public APNSPushTask(Message toTrace, String userId) {
        super(toTrace);
        this.userId = userId;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = super.toMap();
        out.put(USER_ID, userId);
        return out;
    }

    @Override
    public void fromMap(Map<String, Object> in) {
        super.fromMap(in);
        userId = (String) in.get(USER_ID);
    }

    protected String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.format("APNSPushTask [userId=%s]",
                userId);
    }
}
