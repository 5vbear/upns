/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.outer.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myctu.platform.gateway.agent.pubsub.PubSubException;
import com.myctu.platform.gateway.agent.pubsub.PubSubService;
import com.myctu.platform.utils.MD5Utils;
import com.telecom.ctu.platform.common.cache.CacheService;
import com.telecom.ctu.platform.components.upns.node.domain.model.MessageTrace;
import com.telecom.ctu.platform.components.upns.node.domain.model.MessageTrace.Status;
import com.telecom.ctu.platform.components.upns.node.service.inner.impl.MemGroupService;
import com.telecom.ctu.platform.components.upns.node.service.outer.DeliveryBox;
import com.telecom.ctu.platform.components.upns.protocol.pub.Custom;
import com.telecom.ctu.platform.components.upns.protocol.pub.Simple;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Receipt;

/**
 * @project node-server
 * @date 2013-8-30-下午4:15:39
 * @author pippo
 */
public class MemDeliveryBox extends MemGroupService implements DeliveryBox {

	private static final Logger logger = LoggerFactory.getLogger(MemDeliveryBox.class);

	@Resource
	private PubSubService pubSubService;

	@Resource(name = "ctu.upns.cacheService")
	private CacheService cacheService;

	@Override
	public void publish(Simple message) {
		message.id = UUID.randomUUID().toString();

		createMessageTrace(message);

		try {
			logger.debug("publish message:[{}]", message);
			pubSubService.pub(message.groupId, message);
		} catch (PubSubException e) {
			logger.error("publish message:[{}] due to error:[{}]", message, ExceptionUtils.getStackTrace(e));
		}
	}

	private String genMessageTraceKey(String userId, String messageId) {
		return MD5Utils.md5Hex(String.format("%s#%s", userId, messageId));
	}

	private void createMessageTrace(Simple message) {
		Collection<String> users = this.getUserIdsByGroup(message.groupId);

		for (String userId : users) {
			MessageTrace trace = new MessageTrace();
			trace.userId = userId;
			trace.messageId = message.id;
			trace.time = message.time;
			trace.status = Status.pub;

			cacheService.put(genMessageTraceKey(userId, message.id), 60 * 60 * 30, trace);
		}
	}

	@Override
	public void ack(Receipt receipt) {
		//TODO 按设备记录回执暂不实现
	}

	@Override
	public void ack(String userId, String messageId) {
		cacheService.remove(genMessageTraceKey(userId, messageId));
	}

	@Override
	public List<Custom> history(String userId) {
		return Collections.emptyList();
	}
}
