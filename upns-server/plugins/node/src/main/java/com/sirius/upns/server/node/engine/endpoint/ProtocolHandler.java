/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint;

import com.myctu.platform.spring.ext.BeanLocator;
import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.server.node.engine.process.ProcessContext;
import com.sirius.upns.server.node.engine.process.ProcessEngine;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-18-下午7:58:27
 */
public class ProtocolHandler extends ChannelDuplexHandler implements TCPEndpointConstants {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolHandler.class);

    private static ProcessEngine engine;


    public ProtocolHandler() {
        if (engine == null) {
            engine = BeanLocator.getBean(ProcessEngine.class);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("reveived rq:[{}] from client:[{}]", msg, ctx.channel());
        }

        try {
            /* assemble context */
            ProcessContext context = new TCPProcessContext(ctx);
            context.setRq((BusinessPacket) msg);
            /* process */
            process(ctx, msg, context);
        } catch (Throwable cause) {
            logger.error("process msg:[{}] due to error:[{}]", msg, ExceptionUtils.getStackTrace(cause));
            ctx.fireExceptionCaught(cause);
            // throw cause;
        }
    }

    private void process(ChannelHandlerContext ctx, Object msg, ProcessContext context) throws Exception {
        engine.process(context);

		/* write and flush rs */
        if (context.getRs() != null) {
            ctx.writeAndFlush(context.getRs());
            logger.debug("write rs:[{}] to the client", context.getRs());
        }
    }

}
