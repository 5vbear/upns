/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository.impl;

import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.domain.model.MessageACK;
import com.sirius.upns.server.node.domain.model.Timeline;
import com.sirius.upns.server.node.repository.MessageACKClosure;
import com.sirius.upns.server.node.repository.MessageClosure;
import com.sirius.upns.server.node.repository.MessageRepository;

import java.util.Collections;
import java.util.List;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-8-下午10:42:26
 */
public class MemMessageRepository implements MessageRepository {

	@Override
	public void save(Message message) {

	}

	@Override
	public void clearBroadcast() {

	}

	@Override
	public Message getMessage(String id) {
		return null;
	}

	@Override
	public List<Message> history(MessageACK example, int limit) {
		return Collections.emptyList();
	}

	@Override
	public Timeline getTimeline(String userId) {
		return null;
	}

	@Override
	public void checkTimeline(String userId, int appId, long timestamp) {

	}

	@Override
	public void updateTimeline(String userId, long timestamp) {

	}

	@Override
	public void updateTimeline(String userId, int appId, long timestamp) {

	}

	@Override
	public MessageACK getACK(String userId, String messageId) {
		return null;
	}

	@Override
	public void save(MessageACK ack) {

	}

	@Override
	public void clearACK(String userId) {

	}

	@Override
	public int unread(String userId, int appId) {
		return 0;
	}

	@Override
	public int unread(String userId, int appId, long lastACK) {
		return 0;
	}

	@Override
	public void ack(String ackId, boolean ack) {

	}

	@Override
	public void ack(String userId, String messageId, boolean ack) {

	}

	@Override
	public void ack(String userId, long timestamp, boolean ack) {

	}

	@Override
	public void iterateACK(MessageACK example, int limit, MessageACKClosure closure) {

	}

	@Override
	public void iterateMessage(Message example, int limit, MessageClosure closure) {

	}

	@Override
	public Message getUserLast(String userId) {
		return null;
	}

	@Override
	public Message getUserLast(String userId, int appId) {
		return null;
	}

}
