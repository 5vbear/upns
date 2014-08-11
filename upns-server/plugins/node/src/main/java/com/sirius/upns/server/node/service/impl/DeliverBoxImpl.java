/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service.impl;

import com.google.common.collect.Sets;
import com.myctu.platform.gateway.agent.pubsub.PubService;
import com.myctu.platform.gateway.agent.queue.Productor;
import com.sirius.upns.protocol.KeyHelper;
import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.queue.APNSBroadcastTask;
import com.sirius.upns.server.node.repository.GroupRepository;
import com.sirius.upns.server.node.repository.MessageRepository;
import com.sirius.upns.server.node.service.DeliverBox;
import com.telecom.ctu.platform.framework.engine.service.Exporter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-30-下午4:15:39
 */
@Service("upns.deliverBox")
@Exporter(name = "upns.deliverbox", serviceInterface = DeliverBox.class)
public class DeliverBoxImpl extends TimelineServiceImpl implements DeliverBox {

	private static final Logger logger = LoggerFactory.getLogger(DeliverBoxImpl.class);

	@Resource(name = "ctu.upns.messageRepository")
	private MessageRepository messageRepository;

	@Resource(name = "ctu.upns.groupRepository")
	private GroupRepository groupRepository;

	@Resource(name = "ctu.upns.pubService")
	private PubService pubService;

	@Resource(name = "ctu.upns.queue.apple.broadcast.productor")
	private Productor apnsTaskProductor;

	@Override
	public void publish(Message message) {
		Validate.notNull(groupRepository.get(message.groupId), "the group:[%s] to publish not exist", message.groupId);
		super.trace(message);
	}

	@Override
	public void publish2user(Message message, List<String> userIds) {
		/* create group */
		Group group = groupRepository.get(message.groupId);
		if (group == null) {
			group = new Group();
			group.id = message.groupId;
			group.appId = message.appId;
			group.name = message.title;
			groupRepository.saveGroup(group);
		}

		/* member join */
		Validate.notNull(userIds, "userIds can not be null");
		Validate.isTrue(userIds.size() <= 100, "the userIds size can not large then 100");
		Set<GroupMember> members = Sets.newHashSet();
		for (String userId : userIds) {
			GroupMember member = new GroupMember();
			member.userId = userId;
			member.groupId = message.groupId;
			members.add(member);
		}
		groupRepository.addMember(members.toArray(new GroupMember[members.size()]));

		/* publish */
		publish(message);
	}

	@Override
	public void broadcast(Message message) {
		message.groupId = KeyHelper.GROUP_ID_BROADCOST;
		message.braodcast = true;
		messageRepository.save(message);

        /*广播消息直接发送*/
		Custom custom = new Custom();
		custom.id = message.id;
		custom.appId = message.appId;
		custom.groupId = message.groupId;
		custom.title = message.title;
		custom.content = message.content;
		custom.extension = message.extension;
		custom.time = message.createTime;

		try {
			logger.debug("publish message:[{}] to channel:[{}]", message, KeyHelper.CHANNEL_BROADCAST);
			/* android 广播*/
			pubService.pub(KeyHelper.CHANNEL_BROADCAST, custom);
			/*ios广播*/
			apnsTaskProductor.product(new APNSBroadcastTask(message));
		} catch (Exception e) {
			logger.error("publish message:[{}] due to error:[{}]", message, ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void send(Message message, String userId) {
		message.groupId = userId;

		/* create group */
		Group group = groupRepository.get(message.groupId);
		if (group == null) {
			group = new Group();
			/*私信的组id就是userId*/
			group.id = message.groupId;
			group.appId = message.appId;
			group.name = "private#" + userId;
			groupRepository.saveGroup(group);

			GroupMember member = new GroupMember();
			member.userId = userId;
			member.groupId = message.groupId;
			groupRepository.addMember(member);
		}

        /* publish */
		publish(message);
	}

	@Override
	public void batchSend(Message message, List<String> userIds) {
		Validate.notNull(userIds, "userIds can not be null");
		Validate.isTrue(userIds.size() <= 100, "the userIds size can not large then 100");

		for (String userId : userIds) {
			send(message, userId);
		}
	}

}
