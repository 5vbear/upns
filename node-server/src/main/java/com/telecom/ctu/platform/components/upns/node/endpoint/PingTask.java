/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecom.ctu.platform.components.upns.node.domain.Subscriber;
import com.telecom.ctu.platform.components.upns.protocol.sub.Ping;

/**
 * @project node-server
 * @date 2013-8-27-上午10:35:19
 * @author pippo
 */
public class PingTask implements Runnable, TCPEndpointConstants {

	private static final Logger logger = LoggerFactory.getLogger(PingTask.class);

	public PingTask(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	private final ChannelHandlerContext ctx;

	@Override
	public void run() {
		Subscriber subscriber = SubscriberHelper.get(ctx);

		if (subscriber == null || !subscriber.isValid()) {
			logger.warn("the channel:[{}] not contain valid subscriber, channel will be close soon!", ctx.channel());
			ctx.channel().close();
		}

		long current = System.currentTimeMillis();

		/* 如果session五分钟内是活跃的,那么不再发送ping */
		if ((current - subscriber.getActive()) <= ping_period) {
			return;
		}

		ctx.writeAndFlush(new Ping(System.currentTimeMillis()));
	}

}
