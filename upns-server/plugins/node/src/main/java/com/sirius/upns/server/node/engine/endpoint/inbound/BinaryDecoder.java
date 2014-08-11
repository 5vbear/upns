/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint.inbound;

import com.sirius.upns.protocol.transfer.TransferPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @project node-server
 * @date 2013-8-18-上午11:41:54
 * @author pippo
 */
public class BinaryDecoder extends LengthFieldBasedFrameDecoder {

	public BinaryDecoder() {
		super(TransferPacket.SIZE_PACKET, 0, TransferPacket.SIZE_LENGTH, 0, TransferPacket.SIZE_LENGTH);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		return super.decode(ctx, in);
	}
}
