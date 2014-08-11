/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint;

import com.myctu.platform.AppSettings;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * @project node-server
 * @date 2013-8-18-上午10:06:53
 * @author pippo
 */
@Component
public class CustomChannelInitializer extends ChannelInitializer<SocketChannel> {

	private static final Logger logger = LoggerFactory.getLogger(CustomChannelInitializer.class);

	@Resource(name = "ctu.upns.node.server.channel.handlers")
	private LinkedHashMap<String, Class<? extends ChannelHandler>> handlers;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		for (String name : handlers.keySet()) {

			/* 有些handler不是线程安全的,不能在多个channel件共享 */
			Class<? extends ChannelHandler> clazz = handlers.get(name);
			ChannelHandler handler = clazz.newInstance();

			/* 通过为每个handler指定concurrency提供SEDA特性支持 */
			/* 不设置和值小与0为关闭,在没有数据分析支撑的情况下不要打开, 过多的线程切换会降低性能 */
			int concurrency = AppSettings.getInstance()
				.getInt(String.format("ctu.upns.node.server.channel.handlers.%s.concurrency", name));
			if (concurrency > 0) {
				ch.pipeline().addLast(new LocalEventLoopGroup(concurrency), name, handler);
			} else {
				ch.pipeline().addLast(name, handler);
			}

		}

		logger.info("init channel:[remote={},local:{}] with handlers:[{}]",
			ch.remoteAddress(),
			ch.localAddress(),
			ch.pipeline().toMap());
	}
}
