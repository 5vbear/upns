/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

import com.telecom.ctu.platform.components.upns.protocol.sub.Close;

/**
 * @project node-server
 * @date 2013-8-31-下午4:06:38
 * @author pippo
 */
public class ChannelLifecycleHandler extends ChannelDuplexHandler implements TCPEndpointConstants {

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		//TODO
		/* 每五分钟内尝试去发一个ping */
		/* 此功能应放到connector中,并可配置关闭 */
		PingHelper.schedule(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		PingHelper.stop(ctx);
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SubscriberHelper.active(ctx);
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof Close) {
			ctx.close();
		} else {
			super.channelRead(ctx, msg);
		}

	}

}
