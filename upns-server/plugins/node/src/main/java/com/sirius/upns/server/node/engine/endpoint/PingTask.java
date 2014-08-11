/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint;

import com.sirius.upns.protocol.business.sub.Ping;
import com.sirius.upns.server.node.domain.Subscriber;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @project node-server
 * @date 2013-8-27-上午10:35:19
 * @author pippo
 */
public class PingTask implements Runnable, TCPEndpointConstants {

	private static final Logger logger = LoggerFactory.getLogger("upns.subscriber.ping");

	public PingTask(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	private final ChannelHandlerContext ctx;

	@Override
	public void run() {
		Subscriber subscriber = SubscriberHelper.get(ctx);

		if (subscriber == null || !subscriber.isValid()) {
			logger.warn("the channel:[{}] not contain valid subscriber, channel will be close soon!", ctx.channel());
			ctx.close();
		}

		long unActive = System.currentTimeMillis() - subscriber.getActive();

		/* 超过timout时间没有收到过ping,那么关闭连接 */
		if (unActive >= keep_alive_timeout) {
			logger.warn("the subscriber unactive:[{}]ms will to be closed", unActive);
			ctx.close();
			return;
		}

		/* 如果session是活跃的,那么不再发送ping */
		if (unActive >= ping_period) {
			ctx.writeAndFlush(new Ping(System.currentTimeMillis()));
			if (logger.isTraceEnabled()) {
				logger.trace("send ping to subscriber:[{}]", subscriber);
			}
		}
	}

}
