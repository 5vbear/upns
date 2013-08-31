/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import com.telecom.ctu.platform.components.upns.protocol.TransformPackage;

/**
 * @project node-server
 * @date 2013-8-18-上午11:41:54
 * @author pippo
 */
public class BinaryDecoder extends LengthFieldBasedFrameDecoder {

	public BinaryDecoder() {
		super(TransformPackage.max_length, 0, TransformPackage.head_length, 0, TransformPackage.head_length);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		return super.decode(ctx, in);
	}
}
