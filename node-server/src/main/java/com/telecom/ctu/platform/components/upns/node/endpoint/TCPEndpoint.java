/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.myctu.platform.thread.CustomizableThreadFactory;
import com.telecom.ctu.platform.components.upns.node.endpoint.interceptor.ChannleDelegateInterceptor;

/**
 * @project node-server
 * @date 2013-8-16-下午2:10:57
 * @author pippo
 */
@Component
public class TCPEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(TCPEndpoint.class);

	@Resource
	private ChannleDelegateInterceptor channleDelegateInterceptor;

	@Resource
	private CustomChannelInitializer channelInitializer;

	@Value("${ctu.upns.node.server.host}")
	private String host;

	@Value("${ctu.upns.node.server.port}")
	private int port;

	private ExecutorService daemon;

	@PostConstruct
	public void start() throws InterruptedException {
		daemon = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("tcp_endpoint.daemon"));
		daemon.execute(new DaemonTask());
	}

	@PreDestroy
	public void stop() {
		daemon.shutdownNow();
	}

	private class DaemonTask implements Runnable {

		private ServerBootstrap b = new ServerBootstrap();
		private EventLoopGroup accepter;
		private EventLoopGroup processor;

		@Override
		public void run() {
			try {
				//TODO 可以通过配置文件配置参数

				/* tcp参数 */
				b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
					.option(ChannelOption.SO_BACKLOG, 500)
					/* 如果node-server直接面向前端应该设置这个参数,加快端口回收时间,单位second */
					.option(ChannelOption.SO_LINGER, 30)
					.option(ChannelOption.SO_REUSEADDR, true)
					.option(ChannelOption.SO_TIMEOUT, 1000)
					/* 作为node-server应该设为false,如果node-server前端无connector应设为true */
					.option(ChannelOption.TCP_NODELAY, true);

				/* 线程池 */
				accepter = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
				processor = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 4);
				b.group(accepter, processor).channel(NioServerSocketChannel.class);

				/* 拦截器 */
				if (channleDelegateInterceptor.isEnable()) {
					b.handler(channleDelegateInterceptor);
				}

				/* init event process stream */
				b.childHandler(channelInitializer);

				/* Start the server */
				ChannelFuture f = b.bind(host, port)
					.sync()
					.addListener(new GenericFutureListener<Future<? super Void>>() {

						@Override
						public void operationComplete(Future<? super Void> future) throws Exception {
							logger.info("open tcp endpoint on address:[{}:{}]", host, port);
						}
					});

				/* Wait until the server socket is closed */
				f.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {

					@Override
					public void operationComplete(Future<? super Void> future) throws Exception {
						stop();
					}
				}).sync();
			} catch (Exception e) {
				logger.error("the tcp endpoint due to error:[{}], will stop endpoint", ExceptionUtils.getRootCause(e));
				throw new RuntimeException(e);
			}
		}

		public void stop() {

			logger.info("勒个去tcp endpoint要关闭了,回家收衣服啊!!!");

			if (accepter != null) {
				accepter.shutdownGracefully();
			}

			if (processor != null) {
				processor.shutdownGracefully();
			}
		}
	}
}
