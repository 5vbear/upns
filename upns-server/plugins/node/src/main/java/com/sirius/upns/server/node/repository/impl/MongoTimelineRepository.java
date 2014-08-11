/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository.impl;

import com.mongodb.QueryBuilder;
import com.sirius.upns.server.node.domain.model.MessageACK;
import com.sirius.upns.server.node.domain.model.Timeline;
import com.sirius.upns.server.node.repository.JongoConstants;
import com.sirius.upns.server.node.repository.MessageACKClosure;
import com.sirius.upns.server.node.repository.TimelineRepository;
import org.jongo.MongoCollection;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @project node-server
 * @date 2013-9-24-上午10:14:45
 * @author pippo
 */
public class MongoTimelineRepository implements TimelineRepository, JongoConstants {

	private static final String query_userId_messageId = String.format("{%s:#, %s:#}",
		MessageACK.USER_ID,
		MessageACK.MESSAGE_ID);

	@Resource(name = "ctu.upns.mongo.db.acks")
	protected MongoCollection acks;

	@Resource(name = "ctu.upns.mongo.db.timelines")
	protected MongoCollection timelines;

	@Override
	public Timeline getTimeline(String userId) {
		@SuppressWarnings("unchecked") Map<String, Object> in = timelines.findOne(QUERY_BY_ID, userId).as(Map.class);

		Timeline timeline = new Timeline();
		if (in != null) {
			timeline.fromMap(in);
		} else {
			timeline.setUserId(userId);
			timelines.insert(timeline.toMap());
		}

		return timeline;
	}

	@Override
	public void checkTimeline(String userId, int appId, long timestamp) {
		Timeline line = getTimeline(userId);
		Long lastACK = line.ackTime.get(String.valueOf(appId));
		if (lastACK == null || lastACK > timestamp) {
			updateTimeline(userId, appId, timestamp);
		}
	}

	@Override
	public void updateTimeline(String userId, long timestamp) {
		Timeline line = getTimeline(userId);
		if (line.ackTime.isEmpty()) {
			return;
		}

		for (String appId : line.ackTime.keySet()) {
			line.ackTime.put(appId, timestamp);
		}

		timelines.update(QUERY_BY_ID, userId).merge(line.toMap());
	}

	@Override
	public void updateTimeline(String userId, int appId, long timestamp) {
		String update = String.format("{$set:{'%s.%s':#}}}", Timeline.ACKTIME, String.valueOf(appId));
		timelines.update(QUERY_BY_ID, userId).with(update, timestamp);
	}

	@Override
	public int unread(String userId, int appId) {
		Timeline timeline = getTimeline(userId);
		if (timeline == null) {
			return 0;
		}
		return unread(userId, appId, timeline.ackTime.get(String.valueOf(appId)));
	}

	@Override
	public int unread(String userId, int appId, long lastACK) {
		String query = String.format("{%s:#, %s:#, %s:#, %s:{$gte:#}}",
			MessageACK.APP_ID,
			MessageACK.USER_ID,
			MessageACK.ACK,
			MessageACK.CREATE_TIME);
		return (int) acks.count(query, appId, userId, false, lastACK);
	}

	@Override
	public MessageACK getACK(String userId, String messageId) {
		@SuppressWarnings("unchecked") Map<String, Object> m = acks.findOne(query_userId_messageId, userId, messageId)
			.as(Map.class);
		if (m == null) {
			return null;
		}

		MessageACK line = new MessageACK();
		line.fromMap(m);
		return line;
	}

	public MessageACK early(String userId, int appId, boolean ack) {
		String query = String.format("{%s:# ,%s:#, %s:#}", MessageACK.USER_ID, MessageACK.APP_ID, MessageACK.ACK);
		@SuppressWarnings("rawtypes") Iterable<Map> iterable = acks.find(query, userId, appId, ack)
			.sort(CREATE_TIME_ORDER_ASC)
			.limit(1)
			.as(Map.class);

		MessageACK messageACK = null;
		for (Map<String, Object> in : iterable) {
			messageACK = new MessageACK();
			messageACK.fromMap(in);
			break;
		}

		return messageACK;
	}

	@Override
	public void save(MessageACK ack) {
		MessageACK _ack = getACK(ack.userId, ack.messageId);

		/* 这个情况是在一条消息重新发送的时候会出现 */
		/* 不要重新创建ack,只要更新ack的状态 */
		if (_ack != null && _ack.ack == false) {
			ack(ack.userId, ack.messageId, false);
		}

		/* 如果ack,那么没有创建记录 */
		if (_ack == null) {
			acks.insert(ack.toMap());
		}
	}

	@Override
	public void clearACK(String userId) {
		String query = String.format("{%s:#}", MessageACK.USER_ID);
		acks.remove(query, userId);
	}

	@Override
	public void ack(String ackId, boolean ack) {
		acks.update(QUERY_BY_ID, ackId).with(String.format("{$set:{%s:#, %s:#}}", MessageACK.ACK, MessageACK.ACK_TIME),
			ack,
			System.currentTimeMillis());
	}

	@Override
	public void ack(String userId, String messageId, boolean ack) {
		acks.update(query_userId_messageId, userId, messageId).with(String.format("{$set:{%s:#, %s:#}}",
			MessageACK.ACK,
			MessageACK.ACK_TIME),
			ack,
			System.currentTimeMillis());
	}

	@Override
	public void ack(String userId, long timestamp, boolean ack) {
		String query = String.format("{%s:#, %s:#, %s:{$lt:#}}",
			MessageACK.USER_ID,
			MessageACK.ACK,
			MessageACK.CREATE_TIME);
		String update = String.format("{$set:{%s:#, %s:#}}", MessageACK.ACK, MessageACK.ACK_TIME);
		acks.update(query, userId, !ack, timestamp).with(update, ack, System.currentTimeMillis());
	}

	@Override
	public void iterateACK(MessageACK example, int limit, MessageACKClosure closure) {
		QueryBuilder builder = QueryBuilder.start();
		builder.and(MessageACK.USER_ID).is(example.userId);
		if (example.appId != null) {
			builder.and(MessageACK.APP_ID).is(example.appId);
		}
		if (example.ack != null) {
			builder.and(MessageACK.ACK).is(example.ack);
		}
		if (example.createTime > 0) {
			builder.and(MessageACK.CREATE_TIME).lessThanEquals(example.createTime);
		}

		@SuppressWarnings("rawtypes")
		Iterable<Map> iterable = acks.find(builder.get().toString())
			.sort(CREATE_TIME_ORDER_DESC)
			.limit(limit)
			.as(Map.class);

		for (Map<String, Object> in : iterable) {
			MessageACK ack = new MessageACK();
			ack.fromMap(in);
			closure.execute(ack);
		}
	}

}