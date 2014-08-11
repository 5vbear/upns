/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.endpoint;

import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.msg.APPUnread;
import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.protocol.business.msg.History;
import com.sirius.upns.protocol.business.msg.Receipt;
import com.sirius.upns.protocol.transfer.MarshallerType;
import com.sirius.upns.server.node.engine.endpoint.TCPEndpointConstants;
import com.sirius.upns.server.node.engine.endpoint.inbound.BinaryDecoder;
import com.sirius.upns.server.node.engine.endpoint.inbound.ProtocolDecoder;
import com.sirius.upns.server.node.engine.endpoint.outbound.BinaryEncoder;
import com.sirius.upns.server.node.engine.endpoint.outbound.ProtocolEncoder;
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

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-18-下午5:12:15
 */
public class TCPClient {

	public static String host = "0.0.0.0";
	public static int port = 8156;
	public static String userId;
	public static String deviceToken;

	public TCPClient(MarshallerType marshallerType) {
		connection = new Connection();
		connection.marshallerType = marshallerType;
	}

	private Connection connection = new Connection();

	private ExecutorService daemon = Executors.newSingleThreadExecutor();

	public void start() throws Exception {
		daemon.execute(connection);
		connection.started.tryAcquire(30, TimeUnit.SECONDS);
	}

	public void stop() throws Exception {
		daemon.shutdown();
	}

	public void setCallback(PacketCallback callback) {
		connection.callback = callback;
	}

	public void write(BusinessPacket _package) {
		connection.ctx.writeAndFlush(_package);
	}

	public static interface PacketCallback {

		void onPackage(BusinessPacket _package);

	}

	public static class Connection implements Runnable {

		private Semaphore started = new Semaphore(0);

		private MarshallerType marshallerType;

		private PacketCallback callback;

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
										TCPEndpointConstants.MarshallerTypeHelper.bind(ctx, marshallerType);
										started.release();
									}

									@Override
									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
										System.out.println("channelRead#" + msg);
										try {
											if (callback != null) {
												onMessage(ctx, (BusinessPacket) msg);
											}
										} catch (Exception e) {
											e.printStackTrace();
											ctx.fireExceptionCaught(e);
										}

									}

									private void onMessage(ChannelHandlerContext ctx, BusinessPacket msg) {
										callback.onPackage(msg);

										if (msg instanceof APPUnread) {
											APPUnread unread = (APPUnread) msg;
											History example = new History();
											/* userid可以不传,server会取当前socket所bind的userid */
											example.appId = unread.appId;
											example.limit = 10;
											example.timestamp = System.currentTimeMillis();
											/*只取未读消息*/
											example.unread = true;
											/*标记为已读*/
											example.ack = true;
											ctx.writeAndFlush(example);
										}

										if (msg instanceof Custom) {
											Receipt receipt = new Receipt();
											receipt.messageId = ((Custom) msg).id;
											receipt.deviceToken = deviceToken;
											receipt.time = System.currentTimeMillis();
											ctx.writeAndFlush(receipt);
										}
									}

									@Override
									public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
										// Close the connection when an exception is raised.
										//logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
										cause.printStackTrace();
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
