/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository;

import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.domain.model.MessageACK;

import java.util.List;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-8-下午10:39:43
 */
public interface MessageRepository extends TimelineRepository {

	void save(Message message);

	void clearBroadcast();

	Message getMessage(String id);

	Message getUserLast(String userId);

	Message getUserLast(String userId, int appId);

	void iterateMessage(Message example, int limit, MessageClosure closure);

	List<Message> history(MessageACK example, int limit);

}
