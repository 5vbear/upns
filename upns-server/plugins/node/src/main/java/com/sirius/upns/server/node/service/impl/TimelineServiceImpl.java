/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service.impl;

import com.myctu.platform.gateway.agent.queue.Productor;
import com.myctu.platform.gateway.agent.queue.QueueException;
import com.sirius.upns.protocol.business.msg.APPUnread;
import com.sirius.upns.protocol.business.msg.History;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.queue.MessageACKUpdateTask;
import com.sirius.upns.server.node.queue.TimelineUpdateTask;
import com.sirius.upns.server.node.repository.MessageRepository;
import com.sirius.upns.server.node.service.TimelineService;
import com.telecom.ctu.platform.framework.engine.service.Exporter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-12-上午11:59:22
 */
@Service("upns.timelineService")
@Exporter(name = "upns.timeline", serviceInterface = TimelineService.class)
public class TimelineServiceImpl implements TimelineService {

	private static final Logger logger = LoggerFactory.getLogger(TimelineServiceImpl.class);

	@Resource(name = "ctu.upns.queue.timeline.productor")
	private Productor traceTaskProductor;

	@Resource(name = "ctu.upns.queue.message.ack.productor")
	private Productor ackTaskProductor;

	@Resource(name = "ctu.upns.messageRepository")
	private MessageRepository messageRepository;

	@Resource
	private HistoryAssembler historyAssembler;

	@Resource
	private APPUnreadLoader appUnreadLoader;

	@Override
	public Message getMessage(String messageId) {
		return messageRepository.getMessage(messageId);
	}

	@Override
	public void trace(Message message) {
		Validate.notNull(message, "the message can not be null");
		Validate.isTrue(message.appId > 0, "invalid appId:[%s]", message.appId);
		messageRepository.save(message);

		try {
			traceTaskProductor.product(new TimelineUpdateTask(message));
		} catch (QueueException e) {
			logger.error("add timeline trace task:[{}] due to error", message, ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void ack(String userId, String messageId) {
		ack(userId, messageId, true);
	}

	protected void ack(String userId, String messageId, boolean ack) {
		Validate.notBlank(userId, "userId can not be null");

		Message message = messageRepository.getMessage(messageId);
		if (message == null) {
			logger.warn("can not find message with id:[{}], ignore user:[{}] ack", message, userId);
			return;
		}

		try {
			ackTaskProductor.product(new MessageACKUpdateTask(message, userId, ack));
		} catch (QueueException e) {
			logger.error("add message ack task due to error", e);
		}
	}

	@Override
	public void ackAll(String userId, long timestamp) {
		Validate.notBlank(userId, "userId can not be null");
		Validate.isTrue(timestamp > 0, "invalid timestamp:[%s]", timestamp);

        /* 修改ack状态 */
		messageRepository.ack(userId, timestamp, true);
		/* 修改用户timeline最后一条ack的时间 */
		messageRepository.updateTimeline(userId, timestamp);
	}

	@Override
	public void clearACK(String userId) {
		Validate.notBlank(userId, "userId can not be null");
		messageRepository.clearACK(userId);
	}

	@Override
	public void clearBroadcast() {
		messageRepository.clearBroadcast();
	}

	@Override
	public List<APPUnread> unread(String userId) {
		Validate.notBlank(userId, "userId can not be null");
		return appUnreadLoader.load(userId);
	}

	@Override
	public History find(final History history) {
		/* 设置默认值 */
		if (history.limit == null) {
			history.limit = 10;
		}
		if (history.timestamp == null) {
			history.timestamp = System.currentTimeMillis();
		}
		history.messages = new ArrayList<>();

		/*组装消息*/
		historyAssembler.assemble(history);
		return history;
	}

}
