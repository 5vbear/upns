/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint.outbound;

import com.myctu.platform.protocol.MapSerializable;
import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.transfer.Payload;
import com.sirius.upns.server.node.engine.endpoint.TCPEndpointConstants;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @project node-server
 * @date 2013-8-18-上午11:41:54
 * @author pippo
 */
public class ProtocolEncoder extends MessageToMessageEncoder<MapSerializable> implements TCPEndpointConstants {

	private static final Logger logger = LoggerFactory.getLogger(ProtocolEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, MapSerializable msg, List<Object> out) throws Exception {
		try {
			Payload payload = new Payload(MarshallerTypeHelper.getType(ctx), (BusinessPacket) msg);

			if (logger.isTraceEnabled()) {
				logger.trace("the payload to be encode is:[{}]", payload);
			}

			byte[] content = payload.getBytes();

			if (logger.isTraceEnabled()) {
				logger.trace("the encoded binary is:[{}], the length is:[{}]", content, content.length);
			}

			out.add(Unpooled.wrappedBuffer(content));
		} catch (Throwable cause) {
			ctx.fireExceptionCaught(cause);
			//	throw cause;
		}
	}

}
