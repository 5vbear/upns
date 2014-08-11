/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.queue;

import com.myctu.platform.protocol.ExtMapUtils;
import com.myctu.platform.protocol.event.PlatformEvent;
import com.sirius.upns.server.node.domain.model.Message;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.Map;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-9-上午9:56:25
 */
public class TimelineUpdateTask extends PlatformEvent {

    private static final long serialVersionUID = -2922876681328062000L;

    private static final String MESSAGE = "m";

    public TimelineUpdateTask() {
        this.publish_time = System.currentTimeMillis();
    }

    public TimelineUpdateTask(Message message) {
        this.publish_time = System.currentTimeMillis();
        this.message = message;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = super.toMap();
        ExtMapUtils.addIfNotNull(out, MESSAGE, message);
        return out;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void fromMap(Map<String, Object> in) {
        super.fromMap(in);
        message = new Message();
        message.fromMap(ExtMapUtils.getMap(in, MESSAGE, Collections.emptyMap()));
    }

    protected Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("message", message)
                .toString();
    }
}
