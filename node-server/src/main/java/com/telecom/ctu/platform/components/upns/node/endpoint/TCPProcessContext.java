/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint;

import io.netty.channel.ChannelHandlerContext;

import com.telecom.ctu.platform.components.upns.node.domain.Subscriber;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessContext;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubRouter;

/**
 * @project node-server
 * @date 2013-8-31-上午10:35:05
 * @author pippo
 */
public class TCPProcessContext extends ProcessContext implements TCPEndpointConstants {

	public TCPProcessContext(ChannelHandlerContext ctx) {
		super();
		this.ctx = ctx;
	}

	private ChannelHandlerContext ctx;

	@Override
	public SubRouter getSubRouter() {
		return SubRouterHelper.getRouter(ctx);
	}

	@Override
	public void bind(SubRouter router) {
		SubRouterHelper.bind(ctx, router);
	}

	@Override
	public Subscriber getSubscriber() {
		Subscriber subscriber = SubscriberHelper.get(ctx);
		if (subscriber != null) {
			return subscriber;
		}

		/* 有route信息说明connect成功了 */
		SubRouter router = getSubRouter();
		if (router == null) {
			return null;
		}

		TCPSubscriber _subscriber = new TCPSubscriber(router.userId, router.deviceToken, router.deviceType);
		_subscriber.init(ctx);
		SubscriberHelper.bind(ctx, _subscriber);

		return _subscriber;
	}

}
