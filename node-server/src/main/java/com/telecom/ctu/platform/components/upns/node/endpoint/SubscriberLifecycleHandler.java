/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Collection;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myctu.platform.gateway.agent.pubsub.PubSubException;
import com.myctu.platform.gateway.agent.pubsub.PubSubService;
import com.myctu.platform.spring.ext.BeanLocator;
import com.telecom.ctu.platform.components.upns.node.domain.Subscriber;
import com.telecom.ctu.platform.components.upns.node.domain.model.Group;
import com.telecom.ctu.platform.components.upns.node.service.inner.GroupService;
import com.telecom.ctu.platform.components.upns.node.service.inner.SubscriberManager;
import com.telecom.ctu.platform.components.upns.node.service.outer.SubscriberRouterTable;
import com.telecom.ctu.platform.components.upns.protocol.sub.ConnectRS;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubRouter;

/**
 * @project node-server
 * @date 2013-8-31-上午8:44:44
 * @author pippo
 */
public class SubscriberLifecycleHandler extends ChannelDuplexHandler implements TCPEndpointConstants {

	private static final Logger logger = LoggerFactory.getLogger(SubscriberLifecycleHandler.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		try {
			if (msg instanceof ConnectRS) {
				createSubScriber(ctx, (ConnectRS) msg);
			}

			super.write(ctx, msg, promise);
		} catch (Throwable cause) {
			ctx.fireExceptionCaught(cause);
			throw cause;
		}
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		/* destory router */
		SubRouter router = SubRouterHelper.getRouter(ctx);
		SubRouterHelper.bind(ctx, null);
		getRouterTable().deregist(router);
		logger.debug("destory router:[{}]", router);
		/* destory subscriber */
		destorySubscriber(ctx);

		super.channelUnregistered(ctx);
	}

	private void createSubScriber(ChannelHandlerContext ctx, ConnectRS rs) throws PubSubException {
		if (!rs.success) {
			return;
		}

		/* 有route信息说明connect成功了 */
		SubRouter router = SubRouterHelper.getRouter(ctx);
		Validate.notNull(router, "can not find sub router on channel:[%s]", ctx.channel());

		/* bind to channel */
		TCPSubscriber subscriber = new TCPSubscriber(router.userId, router.deviceToken, router.deviceType);
		subscriber.init(ctx);
		SubscriberHelper.bind(ctx, subscriber);
		/* add to manager */
		getSubscriberManager().add(subscriber);
		/* sub */
		Collection<Group> groups = getGroupService().getGroupByUser(router.userId);
		for (Group group : groups) {
			logger.debug("sub channel:[{}] for subscriber:[{}]", group, subscriber);
			getPubSubService().sub(group.id, subscriber);
		}

		logger.debug("create subscriber:[{}]", subscriber);
	}

	private void destorySubscriber(ChannelHandlerContext ctx) throws PubSubException {
		Subscriber subscriber = SubscriberHelper.get(ctx);
		/* unbind from channel */
		subscriber.destory();
		SubscriberHelper.bind(ctx, null);
		/* remove from manager */
		getSubscriberManager().remove(subscriber.getId());
		/* unsub */
		Collection<Group> groups = getGroupService().getGroupByUser(subscriber.getUserId());
		for (Group group : groups) {
			logger.debug("unsub channel:[{}] for subscriber:[{}]", group, subscriber);
			getPubSubService().unsub(group.id, subscriber);
		}

		logger.debug("destory subscriber:[{}]", subscriber);
	}

	private static GroupService groupService;

	public static GroupService getGroupService() {
		if (groupService == null) {
			groupService = BeanLocator.getBean(GroupService.class);
		}

		return groupService;
	}

	private static SubscriberManager subscriberManager;

	public static SubscriberManager getSubscriberManager() {
		if (subscriberManager == null) {
			subscriberManager = BeanLocator.getBean(SubscriberManager.class);
		}

		return subscriberManager;
	}

	private static PubSubService pubSubService;

	public static PubSubService getPubSubService() {
		if (pubSubService == null) {
			pubSubService = BeanLocator.getBean(PubSubService.class);
		}

		return pubSubService;
	}

	private static SubscriberRouterTable routerTable;

	public static SubscriberRouterTable getRouterTable() {
		if (routerTable == null) {
			routerTable = BeanLocator.getBean(SubscriberRouterTable.class);
		}

		return routerTable;
	}
}
