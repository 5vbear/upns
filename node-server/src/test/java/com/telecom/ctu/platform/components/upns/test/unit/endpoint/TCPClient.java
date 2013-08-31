/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.test.unit.endpoint;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.telecom.ctu.platform.components.upns.node.endpoint.inbound.BinaryDecoder;
import com.telecom.ctu.platform.components.upns.node.endpoint.inbound.ProtocolDecoder;
import com.telecom.ctu.platform.components.upns.node.endpoint.outbound.BinaryEncoder;
import com.telecom.ctu.platform.components.upns.node.endpoint.outbound.ProtocolEncoder;
import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;

/**
 * @project node-server
 * @date 2013-8-18-下午5:12:15
 * @author pippo
 */
public class TCPClient {

	private static String host = "0.0.0.0";

	private static int port = 8156;

	private static Semaphore started = new Semaphore(0);

	private static Connection connection = new Connection();

	private static ExecutorService daemon = Executors.newSingleThreadExecutor();

	public void start() throws Exception {
		daemon.execute(connection);
		started.tryAcquire(30, TimeUnit.SECONDS);
	}

	public void stop() throws Exception {
		daemon.shutdown();
	}

	public void setCallback(PackageCallback callback) {
		connection.callback = callback;
	}

	public void write(BusinessPackage _package) {
		connection.ctx.writeAndFlush(_package);
	}

	public static interface PackageCallback {

		void onPackage(BusinessPackage _package);

	}

	public static class Connection implements Runnable {

		private PackageCallback callback;

		private ChannelHandlerContext ctx;

		@Override
		public void run() {
			EventLoopGroup group = new NioEventLoopGroup();
			try {
				Bootstrap b = new Bootstrap();
				b.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new BinaryDecoder()).addLast(new ProtocolDecoder());
							ch.pipeline().addLast(new BinaryEncoder()).addLast(new ProtocolEncoder());

							ch.pipeline().addLast(new ChannelDuplexHandler() {

								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {
									Connection.this.ctx = ctx;
									started.release();
								}

								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
									if (callback != null) {
										callback.onPackage((BusinessPackage) msg);
									}
								}

								@Override
								public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
									// Close the connection when an exception is raised.
									//logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
									ctx.close();
								}
							});

						}

					});

				// Start the client.
				ChannelFuture f = b.connect(host, port).sync();

				// Wait until the connection is closed.
				f.channel().closeFuture().sync();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// Shut down the event loop to terminate all threads.
				group.shutdownGracefully();
			}

		}
	}
}
