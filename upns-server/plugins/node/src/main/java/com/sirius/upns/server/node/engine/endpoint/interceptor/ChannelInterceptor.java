/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint.interceptor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

/**
 * @project node-server
 * @date 2013-8-18-上午9:34:59
 * @author pippo
 */
public interface ChannelInterceptor {

	boolean isEnable();

	void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise future) throws Exception;

	void connect(ChannelHandlerContext ctx,
			SocketAddress remoteAddress,
			SocketAddress localAddress,
			ChannelPromise future) throws Exception;

	void disconnect(ChannelHandlerContext ctx, ChannelPromise future) throws Exception;

	void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception;

	void deregister(ChannelHandlerContext ctx, ChannelPromise future) throws Exception;

	void read(ChannelHandlerContext ctx) throws Exception;

	void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception;

	void flush(ChannelHandlerContext ctx) throws Exception;

	void channelRegistered(ChannelHandlerContext ctx) throws Exception;

	void channelUnregistered(ChannelHandlerContext ctx) throws Exception;

	void channelActive(ChannelHandlerContext ctx) throws Exception;

	void channelInactive(ChannelHandlerContext ctx) throws Exception;

	void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;

	void channelReadComplete(ChannelHandlerContext ctx) throws Exception;

	void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception;

	void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception;

	void handlerAdded(ChannelHandlerContext ctx) throws Exception;

	void handlerRemoved(ChannelHandlerContext ctx) throws Exception;

	void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;

}