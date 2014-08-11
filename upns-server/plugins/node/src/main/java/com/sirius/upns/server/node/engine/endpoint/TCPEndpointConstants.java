/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint;

import com.myctu.platform.thread.CustomizableThreadFactory;
import com.sirius.upns.protocol.business.route.SubRouter;
import com.sirius.upns.protocol.transfer.MarshallerType;
import com.sirius.upns.server.node.domain.Subscriber;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-27-上午10:31:49
 */
public interface TCPEndpointConstants {

    class Utils {

        public static boolean isConnect(ChannelHandlerContext ctx) {
            return SubRouterHelper.getRouter(ctx) != null;
        }

        public static boolean isValid(ChannelHandlerContext ctx) {
            return SubscriberHelper.get(ctx) != null;
        }

        public static <T> Attribute<T> attr(ChannelHandlerContext ctx, AttributeKey<T> key) {
            return ctx.channel().attr(key);
            //			return ctx.attr(key);
        }
    }

    class MarshallerTypeHelper {

        private static AttributeKey<MarshallerType> marshaller_type = new AttributeKey<>("marshaller_type");

        public static MarshallerType getType(ChannelHandlerContext ctx) {
            MarshallerType type = Utils.attr(ctx, marshaller_type).get();
            return type != null ? type : MarshallerType.JSON;
        }

        public static void bind(ChannelHandlerContext ctx, MarshallerType marshallerType) {
            Utils.attr(ctx, marshaller_type).set(marshallerType);
        }

    }

    class SubRouterHelper {

        private static Logger logger = LoggerFactory.getLogger(SubRouter.class);

        private static AttributeKey<SubRouter> subscriber_route_key = new AttributeKey<>("subscriber_route_key");

        public static SubRouter getRouter(ChannelHandlerContext ctx) {
            return Utils.attr(ctx, subscriber_route_key).get();
        }

        public static void bind(ChannelHandlerContext ctx, SubRouter router) {
            Utils.attr(ctx, subscriber_route_key).set(router);

            if (logger.isDebugEnabled()) {
                logger.debug("binded router:[{}] on channel context", router);
            }
        }
    }

    class SubscriberHelper {

        private static Logger logger = LoggerFactory.getLogger(SubscriberHelper.class);

        private static AttributeKey<TCPSubscriber> subscriber_key = new AttributeKey<>("subscriber_key");

        public static TCPSubscriber get(ChannelHandlerContext ctx) {
            return getHodler(ctx).get();
        }

        public static void bind(ChannelHandlerContext ctx, TCPSubscriber subscriber) {
            getHodler(ctx).set(subscriber);

            if (logger.isDebugEnabled()) {
                logger.debug("binded subscriber:[{}] on channel context", subscriber);
            }
        }

        public static void active(ChannelHandlerContext ctx) {
            Subscriber subscriber = get(ctx);
            if (subscriber != null) {
                subscriber.active();

                if (logger.isDebugEnabled()) {
                    logger.debug("the subscriber:[{}] on active", subscriber);
                }
            }
        }

        private static Attribute<TCPSubscriber> getHodler(ChannelHandlerContext ctx) {
            return Utils.attr(ctx, subscriber_key);
        }
    }

    long ping_period = 1000 * 60 * 5;

    /* 连续三个ping收不到 */
    long keep_alive_timeout = ping_period * 3;

    class PingHelper {

        private static Logger logger = LoggerFactory.getLogger(PingHelper.class);

        private static ScheduledExecutorService pingScheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(),
                                                                                                 new CustomizableThreadFactory(
                                                                                                         "tcp_endpoint.ping"));

        private static AttributeKey<ScheduledFuture<?>> ping_schedule = new AttributeKey<>("_ping_schedule");

        public static void schedule(ChannelHandlerContext ctx) {

            ScheduledFuture<?> future = pingScheduler.scheduleAtFixedRate(new PingTask(ctx),
                                                                          1000 * 30,
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
            return Utils.attr(ctx, ping_schedule);
        }
    }

}
