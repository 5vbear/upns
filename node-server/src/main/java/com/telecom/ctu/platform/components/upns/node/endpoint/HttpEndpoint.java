/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.example.http.helloworld.HttpHelloWorldServerHandler;
import io.netty.handler.codec.http.HttpServerCodec;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

/**
 * @project node-server
 * @date 2013-8-26-下午1:59:44
 * @author pippo
 */
public class HttpEndpoint {

	@Value("${upns.node.server.host}")
	private String host;

	private int port = 80;

	private EventLoopGroup accepter;
	private EventLoopGroup processor;

	private ServerBootstrap b = new ServerBootstrap();

	@PostConstruct
	public void start() throws InterruptedException {
		//TODO 可以通过配置文件配置参数

		/* tcp参数 */
		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000).option(ChannelOption.SO_BACKLOG, 500)
		/* 作为node-server应该设为false,如果node-server前端五connector应设为true */
		.option(ChannelOption.TCP_NODELAY, true);

		/* 线程池 */
		accepter = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
		processor = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 4);
		b.group(accepter, processor)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast("codec", new HttpServerCodec());
					p.addLast("handler", new HttpHelloWorldServerHandler());
				}
			});

	}

}
