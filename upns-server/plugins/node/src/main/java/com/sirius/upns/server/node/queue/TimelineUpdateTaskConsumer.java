/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.queue;

import com.myctu.platform.gateway.agent.pubsub.PubService;
import com.myctu.platform.gateway.agent.pubsub.PubSubException;
import com.myctu.platform.gateway.agent.queue.Consumer;
import com.myctu.platform.gateway.agent.queue.Productor;
import com.myctu.platform.gateway.agent.queue.QueueException;
import com.sirius.upns.protocol.KeyHelper;
import com.sirius.upns.protocol.business.msg.Private;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.repository.GroupRepository;
import com.sirius.upns.server.node.repository.MemberClosure;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-9-下午12:16:59
 */
@Service("ctu.upns.queue.timeline.consumer")
public class TimelineUpdateTaskConsumer implements Consumer<TimelineUpdateTask> {

	private static final Logger logger = LoggerFactory.getLogger(TimelineUpdateTaskConsumer.class);

	@Override
	public Class<TimelineUpdateTask> getEventType() {
		return TimelineUpdateTask.class;
	}

	@Resource(name = "ctu.upns.groupRepository")
	private GroupRepository groupRepository;

	@Override
	public void onEvent(final TimelineUpdateTask event) throws QueueException {
		logger.debug("consume timeline update event:[{}]", event);
		final Message message = event.getMessage();

		groupRepository.iterateMemberByGroup(message.groupId, new MemberClosure() {

			@Override
			public void execute(GroupMember member) {
				/*发布消息到直接登录的客户端*/
				sendToClient(message, member.userId);
                /*发送消息到注册过的苹果设备*/
				sendToAPNS(message, member.userId);
                /*更新ack的队列*/
				updateACK(member, event);
			}
		});
	}

	@Resource(name = "ctu.upns.pubService")
	private PubService pubService;

	@Resource(name = "ctu.upns.queue.apple.push.productor")
	private Productor apnsTaskProductor;

	@Resource(name = "ctu.upns.queue.message.ack.productor")
	private Productor ackTaskProductor;

	private void sendToClient(Message message, String userId) {
		Private _private = new Private();
		_private.id = message.id;
		_private.appId = message.appId;
		_private.groupId = message.groupId;
		_private.title = message.title;
		_private.content = message.content;
		_private.extension = message.extension;
		_private.time = message.createTime;
		_private.userId = userId;

		try {
			logger.debug("publish message:[{}] to channel:[{}]", message, KeyHelper.CHANNEL_PRIVATE);
			pubService.pub(KeyHelper.CHANNEL_PRIVATE, _private);
		} catch (PubSubException e) {
			logger.error("publish message:[{}] due to error:[{}]", message, ExceptionUtils.getStackTrace(e));
		}
	}

	private void sendToAPNS(Message message, String userId) {
		try {
			apnsTaskProductor.product(new APNSPushTask(message, userId));
		} catch (QueueException e) {
			logger.error("add apple push task due to error", e);
		}
	}

	private void updateACK(GroupMember member, TimelineUpdateTask event) {
		try {
			ackTaskProductor.product(new MessageACKUpdateTask(event.message, member.userId, false));
		} catch (QueueException e) {
			logger.error("add message ack task due to error", e);
		}
	}

}
