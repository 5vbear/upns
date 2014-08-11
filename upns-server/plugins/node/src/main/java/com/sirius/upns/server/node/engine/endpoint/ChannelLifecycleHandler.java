/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint;

import com.myctu.platform.spring.ext.BeanLocator;
import com.sirius.upns.protocol.business.route.SubRouter;
import com.sirius.upns.protocol.business.sub.Close;
import com.sirius.upns.protocol.business.sub.Error;
import com.sirius.upns.server.node.NodeServerException;
import com.sirius.upns.server.node.domain.Subscriber;
import com.sirius.upns.server.node.engine.monitor.TCPEndpointMBean;
import com.sirius.upns.server.node.service.SubscriberRouterTable;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;


/**
 * @author pippo
 * @project node-server
 * @date 2013-8-31-下午4:06:38
 */
public class ChannelLifecycleHandler extends ChannelDuplexHandler implements TCPEndpointConstants, TCPEndpointMBean {

    private static final Logger logger = LoggerFactory.getLogger(ChannelLifecycleHandler.class);

    private static final AtomicLong connectionCount = new AtomicLong(0);

    private static SubscriberRouterTable routerTable;

    public ChannelLifecycleHandler() {
        if (routerTable == null) {
            routerTable = BeanLocator.getBean(SubscriberRouterTable.class);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);

        //TODO
        /* 每五分钟内尝试去发一个ping */
        /* 此功能应放到connector中,并可配置关闭 */
        PingHelper.schedule(ctx);

		/* increase count */
        long current = connectionCount.incrementAndGet();
        if (logger.isDebugEnabled()) {
            logger.debug("registed new channel:[{}], current count is:[{}]", ctx.channel().remoteAddress(), current);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        /* destory router */
        SubRouter router = SubRouterHelper.getRouter(ctx);
        if (router != null) {
            routerTable.deregist(router);
            SubRouterHelper.bind(ctx, null);
        }

		/* destory subscriber */
        final Subscriber subscriber = SubscriberHelper.get(ctx);
        if (subscriber != null) {
            subscriber.stop();
            SubscriberHelper.bind(ctx, null);
        }

        //TODO
        /* 每五分钟内尝试去发一个ping */
        /* 此功能应放到connector中,并可配置关闭 */
        PingHelper.stop(ctx);

		/* decrease count */
        long current = connectionCount.decrementAndGet();
        if (logger.isDebugEnabled()) {
            logger.debug("unregisted channel:[{}], current count is:[{}]", ctx.channel().remoteAddress(), current);
        }

        super.channelUnregistered(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        SubscriberHelper.active(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
        SubscriberHelper.active(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("the channel:[{}] caught exception:[{}], channel will be close",
                ctx.channel(),
                ExceptionUtils.getStackTrace(cause));

        Error error = new Error();
        if (cause instanceof NodeServerException) {
            error.code = ((NodeServerException) cause).getCode();
            error.description = cause.getMessage();
        } else {
            error.code = 90000;
            error.description = ExceptionUtils.getStackTrace(cause);
        }

        ctx.writeAndFlush(error);
        ctx.close();
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("close channel:[{}]", ctx.channel());
        }

		/* send close packet and close channel */
        ctx.writeAndFlush(new Close());
        //.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public long getConnectionCount() {
        return connectionCount.get();
    }

}
