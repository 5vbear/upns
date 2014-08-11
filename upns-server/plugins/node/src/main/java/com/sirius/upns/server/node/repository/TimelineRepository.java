/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository;

import com.sirius.upns.server.node.domain.model.MessageACK;
import com.sirius.upns.server.node.domain.model.Timeline;

/**
 * @project node-server
 * @date 2013-9-24-下午8:31:31
 * @author pippo
 */
public interface TimelineRepository {

	Timeline getTimeline(String userId);

	void checkTimeline(String userId, int appId, long timestamp);

	void updateTimeline(String userId, long timestamp);

	void updateTimeline(String userId, int appId, long timestamp);

	int unread(String userId, int appId);

	int unread(String userId, int appId, long lastACK);

	MessageACK getACK(String userId, String messageId);

	void save(MessageACK ack);

	void clearACK(String userId);

	void ack(String ackId, boolean ack);

	void ack(String userId, String messageId, boolean ack);

	void ack(String userId, long timestamp, boolean ack);

	/* 按照时间倒序向前迭代 */
	void iterateACK(MessageACK example, int limit, MessageACKClosure closure);

}