/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint;

import com.sirius.upns.protocol.business.route.SubRouter;
import com.sirius.upns.server.node.domain.Subscriber;
import com.sirius.upns.server.node.engine.process.ProcessContext;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-31-上午10:35:05
 */
public class TCPProcessContext extends ProcessContext implements TCPEndpointConstants {

    private static final Logger logger = LoggerFactory.getLogger(TCPProcessContext.class);

    private ChannelHandlerContext ctx;

    public TCPProcessContext(ChannelHandlerContext ctx) {
        super();
        this.ctx = ctx;
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
        _subscriber.setCtx(ctx);
        SubscriberHelper.bind(ctx, _subscriber);

        return _subscriber;
    }

    @Override
    public SubRouter getSubRouter() {
        return SubRouterHelper.getRouter(ctx);
    }

    @Override
    public void bind(SubRouter router) {
        SubRouterHelper.bind(ctx, router);
    }

    @Override
    public void close() {
        if (logger.isDebugEnabled()) {
            logger.debug("close channel:[{}]", ctx.channel());
        }

        ctx.close();
    }

}
