/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecom.ctu.platform.components.upns.node.endpoint.TCPEndpointConstants;
import com.telecom.ctu.platform.components.upns.protocol.binary.JSONPackage;

/**
 * @project node-server
 * @date 2013-8-18-下午4:43:42
 * @author pippo
 */
public class ProtocolDecoder extends MessageToMessageDecoder<ByteBuf> implements TCPEndpointConstants {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		try {
			byte[] array = read(msg);

			if (logger.isDebugEnabled()) {
				logger.debug("the binary to be decode is:[{}], the length is:[{}]", array, array.length);
			}

			JSONPackage _package = new JSONPackage(array);

			if (logger.isDebugEnabled()) {
				logger.debug("the package is:[{}]", _package);
			}

			out.add(_package.payload);
		} catch (Throwable cause) {
			ctx.fireExceptionCaught(cause);
			throw cause;
		}
	}

	private byte[] read(ByteBuf msg) {
		byte[] array;
		if (msg.hasArray()) {
			if (msg.arrayOffset() == 0 && msg.readableBytes() == msg.capacity()) {
				// we have no offset and the length is the same as the capacity. Its safe to reuse
				// the array without copy it first
				array = msg.array();
			} else {
				// copy the ChannelBuffer to a byte array
				array = new byte[msg.readableBytes()];
				msg.getBytes(0, array);
			}
		} else {
			// copy the ChannelBuffer to a byte array
			array = new byte[msg.readableBytes()];
			msg.getBytes(0, array);
		}
		return array;
	}

}
