/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service;

import com.sirius.upns.protocol.business.msg.APPUnread;
import com.sirius.upns.protocol.business.msg.History;
import com.sirius.upns.server.node.domain.model.Message;

import java.util.List;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-12-上午11:58:01
 */
public interface TimelineService extends TimelineExporterService {

    Message getMessage(String messageId);

    void trace(Message message);

    void ack(String userId, String messageId);

    void ackAll(String userId, long timestamp);

    void clearACK(String userId);

	void clearBroadcast();

    List<APPUnread> unread(String userId);

    History find(History example);
}
