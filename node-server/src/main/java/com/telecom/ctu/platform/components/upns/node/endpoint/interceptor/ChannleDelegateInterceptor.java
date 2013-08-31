/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint.interceptor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * @project node-server
 * @date 2013-8-18-上午9:20:56
 * @author pippo
 */
@Component
public class ChannleDelegateInterceptor extends ChannelInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ChannleDelegateInterceptor.class);

	@PostConstruct
	public void init() {

		if (interceptors != null && !interceptors.isEmpty()) {
			Builder<ChannelInterceptor> builder = ImmutableList.builder();
			for (ChannelInterceptor interceptor : interceptors) {
				if (interceptor.isEnable()) {
					builder.add(interceptor);
				}
			}
			interceptors = builder.build();
			logger.info("the enabled channel interceptor is:[{}]", Arrays.toString(interceptors.toArray()));
		}

		enable = enable && interceptors != null && !interceptors.isEmpty();
	}

	@Resource(name = "ctu.upns.channel.interceptors")
	private List<ChannelInterceptor> interceptors;

	@Value("${ctu.upns.channel.interceptor.enable}")
	private boolean enable = true;

	@Override
	public boolean isEnable() {
		return enable;
	}

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise future) throws Exception {

		if (!enable) {
			super.bind(ctx, localAddress, future);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.bind(ctx, localAddress, future);
		}
	}

	@Override
	public void connect(ChannelHandlerContext ctx,
			SocketAddress remoteAddress,
			SocketAddress localAddress,
			ChannelPromise future) throws Exception {

		if (!enable) {
			super.connect(ctx, remoteAddress, localAddress, future);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.connect(ctx, remoteAddress, localAddress, future);
		}
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
		if (!enable) {
			super.disconnect(ctx, future);
		}
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
		if (!enable) {
			super.close(ctx, future);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.close(ctx, future);
		}
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
		if (!enable) {
			super.deregister(ctx, future);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.deregister(ctx, future);
		}
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.read(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.read(ctx);
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (!enable) {
			super.write(ctx, msg, promise);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.write(ctx, msg, promise);
		}
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.flush(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.flush(ctx);
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.channelRegistered(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.channelRegistered(ctx);
		}
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.channelUnregistered(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.channelUnregistered(ctx);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.channelActive(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.channelActive(ctx);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.channelInactive(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.channelActive(ctx);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (!enable) {
			super.channelRead(ctx, msg);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.channelRead(ctx, msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.channelReadComplete(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.channelReadComplete(ctx);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (!enable) {
			super.userEventTriggered(ctx, evt);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.channelWritabilityChanged(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.channelWritabilityChanged(ctx);
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.handlerAdded(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.handlerAdded(ctx);
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if (!enable) {
			super.handlerRemoved(ctx);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.handlerAdded(ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (!enable) {
			super.exceptionCaught(ctx, cause);
		}

		for (ChannelInterceptor interceptor : interceptors) {
			interceptor.exceptionCaught(ctx, cause);
		}
	}

}
