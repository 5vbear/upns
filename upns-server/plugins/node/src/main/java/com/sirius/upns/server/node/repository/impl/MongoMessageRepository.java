/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository.impl;

import com.mongodb.QueryBuilder;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.domain.model.MessageACK;
import com.sirius.upns.server.node.repository.JongoConstants;
import com.sirius.upns.server.node.repository.MessageACKClosure;
import com.sirius.upns.server.node.repository.MessageClosure;
import com.sirius.upns.server.node.repository.MessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-9-上午9:35:15
 */
public class MongoMessageRepository extends MongoTimelineRepository implements MessageRepository, JongoConstants {

	@Resource(name = "ctu.upns.mongo.db.messages")
	protected MongoCollection messages;

	public void save(Message _message) {
		messages.update(QUERY_BY_ID, _message.id).upsert().merge(_message.toMap());
	}

	@Override
	public void clearBroadcast() {
		messages.remove(String.format("{%s:#}", Message.BROADCAST), true);
	}

	@Override
	public Message getMessage(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}

		@SuppressWarnings("unchecked") Map<String, Object> in = messages.findOne(QUERY_BY_ID, id).as(Map.class);
		if (in == null) {
			return null;
		}

		Message message = new Message();
		message.fromMap(in);
		return message;
	}

	@Override
	public Message getUserLast(String userId) {
		String query = String.format("{%s:#}", MessageACK.USER_ID);
		@SuppressWarnings("rawtypes") Iterable<Map> iterable = acks.find(query, userId)
				.sort(CREATE_TIME_ORDER_DESC)
				.limit(1)
				.as(Map.class);

		Message message = null;
		for (Map<String, Object> in : iterable) {
			message = getMessage((String) in.get(MessageACK.MESSAGE_ID));
			break;
		}
		return message;
	}

	@Override
	public Message getUserLast(String userId, int appId) {
		String query = String.format("{%s:#, %s:#}", MessageACK.USER_ID, MessageACK.APP_ID);
		@SuppressWarnings("rawtypes") Iterable<Map> iterable = acks.find(query, userId, appId)
				.sort(CREATE_TIME_ORDER_DESC)
				.limit(1)
				.as(Map.class);

		Message message = null;
		for (Map<String, Object> in : iterable) {
			message = getMessage((String) in.get(MessageACK.MESSAGE_ID));
			break;
		}
		return message;
	}

	@Override
	public void iterateMessage(Message example, int limit, MessageClosure closure) {
		QueryBuilder builder = QueryBuilder.start();
		if (example.groupId != null) {
			builder.and(Message.GROUP_ID).is(example.groupId);
		}
		if (example.appId != null) {
			builder.and(Message.APP_ID).is(example.appId);
		}
		if (example.braodcast != null) {
			builder.and(Message.BROADCAST).is(example.braodcast);
		}
		if (example.createTime != null) {
			builder.and(Message.CREATE_TIME).lessThanEquals(example.createTime);
		}

		@SuppressWarnings("rawtypes")
		Iterable<Map> iterable = messages.find(builder.get().toString())
				.sort(CREATE_TIME_ORDER_DESC)
				.limit(limit)
				.as(Map.class);

		for (Map<String, Object> in : iterable) {
			Message message = new Message();
			message.fromMap(in);
			closure.execute(message);
		}
	}

	@Override
	public List<Message> history(MessageACK example, int limit) {
		final List<Message> messages = new ArrayList<>();

		iterateACK(example, limit, new MessageACKClosure() {

			@Override
			public void execute(MessageACK line) {
				messages.add(getMessage(line.messageId));
			}
		});

		return messages;
	}

}
