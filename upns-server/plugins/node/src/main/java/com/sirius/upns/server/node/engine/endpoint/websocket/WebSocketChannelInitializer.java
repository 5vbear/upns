package com.sirius.upns.server.node.engine.endpoint.websocket;
//package com.telecom.ctu.platform.components.upns.server.node.engine.endpoint.websocket;
//
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpServerCodec;
//import org.springframework.stereotype.Component;
//
///**
// * Created by pippo on 14-3-25.
// */
//@Component
//public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
//
//    @Override
//    public void initChannel(SocketChannel ch) throws Exception {
//        ChannelPipeline pipeline = ch.pipeline();
//        pipeline.addLast("codec-http", new HttpServerCodec());
//        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
//        pipeline.addLast("handler", new WebSocketEndpointHandler());
//    }
//}