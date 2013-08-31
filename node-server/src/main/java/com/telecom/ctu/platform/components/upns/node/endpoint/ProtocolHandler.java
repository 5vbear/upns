/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myctu.platform.spring.ext.BeanLocator;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessContext;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessEngine;
import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;
import com.telecom.ctu.platform.components.upns.protocol.sub.Close;

/**
 * @project node-server
 * @date 2013-8-18-下午7:58:27
 * @author pippo
 */
public class ProtocolHandler extends ChannelDuplexHandler implements TCPEndpointConstants {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			/* assemble context */
			ProcessContext context = new TCPProcessContext(ctx);
			context.setRq((BusinessPackage) msg);
			/* process */
			process(ctx, msg, context);
		} catch (Throwable cause) {
			logger.error("process msg:[{}] due to error:[{}]", msg, ExceptionUtils.getStackTrace(cause));
			ctx.fireExceptionCaught(cause);
			throw cause;
		}
	}

	private void process(ChannelHandlerContext ctx, Object msg, ProcessContext context) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("reveived rq:[{}] from client:[{}]", msg, ctx.channel());
		}

		getEngine().process(context);
		/* write and flush rs */
		if (context.getRs() != null) {
			ctx.writeAndFlush(context.getRs());
			logger.debug("write rs:[{}] to the client", context.getRs());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("the channel:[{}] caught exception:[{}], channel will be close",
			ctx.channel(),
			ExceptionUtils.getStackTrace(cause));
		ctx.channel().close();
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("close channel:[{}]", ctx.channel());
		}

		ctx.writeAndFlush(new Close()).addListener(ChannelFutureListener.CLOSE);
	}

	private static ProcessEngine engine;

	public ProcessEngine getEngine() {
		if (engine == null) {
			engine = BeanLocator.getBean(ProcessEngine.class);
		}

		return engine;
	}

}
