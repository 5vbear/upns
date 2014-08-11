package com.sirius.upns.server.node.engine.endpoint.websocket;
//package com.telecom.ctu.platform.components.upns.server.node.engine.endpoint.websocket;
//
//import com.myctu.platform.thread.CustomizableThreadFactory;
//import com.telecom.ctu.platform.components.upns.server.node.engine.endpoint.tcp.TCPEndpoint;
//import com.telecom.ctu.platform.components.upns.server.node.engine.endpoint.tcp.interceptor.ChannleDelegateInterceptor;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.util.concurrent.Future;
//import io.netty.util.concurrent.GenericFutureListener;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.annotation.Resource;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by pippo on 14-3-25.
// */
//@Component
//public class WebSocketEndpoint {
//
//    private static final Logger logger = LoggerFactory.getLogger(TCPEndpoint.class);
//
//    @Resource
//    private ChannleDelegateInterceptor channleDelegateInterceptor;
//
//    @Resource
//    private WebSocketChannelInitializer channelInitializer;
//
//    @Value("${ctu.upns.server.node.host}")
//    private String host;
//
//    @Value("${ctu.upns.server.node.port}")
//    private int port;
//
//    @Value("${ctu.upns.server.node.tcp.connect_timeout}")
//    private int connect_timeout;
//
//    @Value("${ctu.upns.server.node.tcp.so_backlog}")
//    private int so_backlog;
//
//    @Value("${ctu.upns.server.node.tcp.so_linger}")
//    private int so_linger;
//
//    @Value("${ctu.upns.server.node.tcp.so_reuseaddr}")
//    private boolean so_reuseaddr;
//
//    @Value("${ctu.upns.server.node.tcp.so_timeout}")
//    private int so_timeout;
//
//    @Value("${ctu.upns.server.node.tcp.nodelay}")
//    private boolean nodelay;
//
//    private ExecutorService daemon;
//
//    @PostConstruct
//    public void start() throws InterruptedException {
//        daemon = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("tcp_endpoint.daemon"));
//        daemon.execute(new DaemonTask());
//    }
//
//    @PreDestroy
//    public void stop() {
//        daemon.shutdownNow();
//    }
//
//    private class DaemonTask implements Runnable {
//
//        private ServerBootstrap b = new ServerBootstrap();
//        private EventLoopGroup accepter;
//        private EventLoopGroup processor;
//
//        @Override
//        public void run() {
//            try {
//
//				/* tcp参数 */
//                b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connect_timeout)
//                        .option(ChannelOption.SO_BACKLOG, so_backlog)
//                    /* 如果node-server直接面向前端应该设置这个参数,加快端口回收时间,单位second */
//                        .option(ChannelOption.SO_LINGER, so_linger)
//                        .option(ChannelOption.SO_REUSEADDR, so_reuseaddr)
//                        .option(ChannelOption.SO_TIMEOUT, so_timeout)
//                    /* 作为node-server应该设为false,如果node-server前端无connector应设为true */
//                        .option(ChannelOption.TCP_NODELAY, nodelay);
//
//				/* 线程池 */
//                accepter = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
//                processor = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 4);
//                b.group(accepter, processor).channel(NioServerSocketChannel.class);
//
//				/* 拦截器 */
//                if (channleDelegateInterceptor.isEnable()) {
//                    b.handler(channleDelegateInterceptor);
//                }
//
//				/* init event process stream */
//                b.childHandler(channelInitializer);
//
//				/* Start the server */
//                ChannelFuture f = b.bind(host, port)
//                        .sync()
//                        .addListener(new GenericFutureListener<Future<? super Void>>() {
//
//                            @Override
//                            public void operationComplete(Future<? super Void> future) throws Exception {
//                                logger.info("open tcp endpoint on address:[{}:{}]", host, port);
//                            }
//                        });
//
//				/* Wait until the server socket is closed */
//                f.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
//
//                    @Override
//                    public void operationComplete(Future<? super Void> future) throws Exception {
//                        stop();
//                    }
//                }).sync();
//            } catch (Exception e) {
//                logger.error("the tcp endpoint due to error:[{}], will stop endpoint", ExceptionUtils.getStackTrace(e));
//                throw new RuntimeException(e);
//            }
//        }
//
//        public void stop() {
//
//            logger.info("勒个去tcp endpoint要关闭了,回家收衣服啊!!!");
//
//            if (accepter != null) {
//                accepter.shutdownGracefully();
//            }
//
//            if (processor != null) {
//                processor.shutdownGracefully();
//            }
//        }
//    }
//
//}
