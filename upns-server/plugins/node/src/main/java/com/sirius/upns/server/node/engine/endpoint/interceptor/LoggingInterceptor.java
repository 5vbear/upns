/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint.interceptor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.SocketAddress;

/**
 * @project node-server
 * @date 2013-8-18-上午10:45:41
 * @author pippo
 */
@Component("ctu.upns.node.server.channel.interceptor.logger")
public class LoggingInterceptor extends ChannelInterceptorAdapter {

	@PostConstruct
	public void init() {
		if (enable) {
			delegatee = new LoggingHandler(LoggingInterceptor.class, LogLevel.DEBUG);
		}
	}

	@Value("${ctu.upns.server.node.channel.interceptor.logger.enable}")
	private boolean enable;

	private LoggingHandler delegatee;

	@Override
	public boolean isEnable() {
		return enable;
	}

	public boolean isSharable() {
		return delegatee.isSharable();
	}

	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		delegatee.handlerAdded(ctx);
	}

	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		delegatee.handlerRemoved(ctx);
	}

	public void read(ChannelHandlerContext ctx) throws Exception {
		delegatee.read(ctx);
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		delegatee.channelReadComplete(ctx);
	}

	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		delegatee.channelWritabilityChanged(ctx);
	}

	public LogLevel level() {
		return delegatee.level();
	}

	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		delegatee.channelRegistered(ctx);
	}

	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		delegatee.channelUnregistered(ctx);
	}

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		delegatee.channelActive(ctx);
	}

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		delegatee.channelInactive(ctx);
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		delegatee.exceptionCaught(ctx, cause);
		cause.printStackTrace();
	}

	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		delegatee.userEventTriggered(ctx, evt);
	}

	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		delegatee.bind(ctx, localAddress, promise);
	}

	public void connect(ChannelHandlerContext ctx,
			SocketAddress remoteAddress,
			SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		delegatee.connect(ctx, remoteAddress, localAddress, promise);
	}

	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		delegatee.disconnect(ctx, promise);
	}

	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		delegatee.close(ctx, promise);
	}

	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		delegatee.deregister(ctx, promise);
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		delegatee.channelRead(ctx, msg);
	}

	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		delegatee.write(ctx, msg, promise);
	}

	public void flush(ChannelHandlerContext ctx) throws Exception {
		delegatee.flush(ctx);
	}

}
