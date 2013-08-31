/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint.outbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myctu.platform.protocol.MapSerializable;
import com.telecom.ctu.platform.components.upns.protocol.binary.JSONPackage;

/**
 * @project node-server
 * @date 2013-8-18-上午11:41:54
 * @author pippo
 */
public class ProtocolEncoder extends MessageToMessageEncoder<MapSerializable> {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, MapSerializable msg, List<Object> out) throws Exception {
		try {
			JSONPackage _package = new JSONPackage(msg);

			if (logger.isDebugEnabled()) {
				logger.debug("the package is:[{}]", _package);
			}

			byte[] array = _package.toByteArray();

			if (logger.isDebugEnabled()) {
				logger.debug("the encoded binary is:{}, the length is:[{}]", array, array.length);
			}

			out.add(Unpooled.wrappedBuffer(array));
		} catch (Throwable cause) {
			ctx.fireExceptionCaught(cause);
			throw cause;
		}
	}

}
