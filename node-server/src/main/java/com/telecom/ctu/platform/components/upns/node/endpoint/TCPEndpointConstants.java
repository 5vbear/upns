/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myctu.platform.thread.CustomizableThreadFactory;
import com.telecom.ctu.platform.components.upns.node.domain.Subscriber;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubRouter;

/**
 * @project node-server
 * @date 2013-8-27-上午10:31:49
 * @author pippo
 */
public interface TCPEndpointConstants {

	class Utils {

		public static boolean isConnect(ChannelHandlerContext ctx) {
			return SubRouterHelper.getRouter(ctx) != null;
		}

		public static boolean isValid(ChannelHandlerContext ctx) {
			return SubscriberHelper.get(ctx) != null;
		}

	}

	class SubRouterHelper {

		private static AttributeKey<SubRouter> subscriber_route_key = new AttributeKey<>("subscriber_route_key");

		public static SubRouter getRouter(ChannelHandlerContext ctx) {
			return ctx.channel().attr(subscriber_route_key).get();
		}

		public static void bind(ChannelHandlerContext ctx, SubRouter router) {
			ctx.channel().attr(subscriber_route_key).set(router);
		}
	}

	class SubscriberHelper {

		private static AttributeKey<TCPSubscriber> subscriber_key = new AttributeKey<>("subscriber_key");

		public static TCPSubscriber get(ChannelHandlerContext ctx) {
			return getHodler(ctx).get();
		}

		public static void bind(ChannelHandlerContext ctx, TCPSubscriber subscriber) {
			getHodler(ctx).set(subscriber);
		}

		public static void active(ChannelHandlerContext ctx) {
			Subscriber subscriber = get(ctx);
			if (subscriber != null) {
				subscriber.active();
			}
		}

		private static Attribute<TCPSubscriber> getHodler(ChannelHandlerContext ctx) {
			return ctx.channel().attr(subscriber_key);
		}
	}

	long ping_period = 1000 * 60 * 5;

	class PingHelper {

		private static Logger logger = LoggerFactory.getLogger(PingHelper.class);

		private static ScheduledExecutorService pingScheduler = Executors.newScheduledThreadPool(1,
			new CustomizableThreadFactory("tcp_endpoint.ping"));

		private static AttributeKey<ScheduledFuture<?>> ping_schedule = new AttributeKey<>("_ping_schedule");

		public static void schedule(ChannelHandlerContext ctx) {

			ScheduledFuture<?> future = pingScheduler.scheduleAtFixedRate(new PingTask(ctx),
				ping_period,
				ping_period,
				TimeUnit.MILLISECONDS);

			getHodler(ctx).set(future);
			logger.debug("start ping task for channel:[{}]", ctx.channel());
		}

		public static void stop(ChannelHandlerContext ctx) {
			ScheduledFuture<?> ping = getHodler(ctx).get();

			if (ping != null) {
				logger.debug("stop ping task for channel:[{}]", ctx.channel());
				ping.cancel(true);
			}
		}

		private static Attribute<ScheduledFuture<?>> getHodler(ChannelHandlerContext ctx) {
			return ctx.channel().attr(ping_schedule);
		}
	}

}
