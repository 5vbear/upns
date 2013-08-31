/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint.outbound;

import io.netty.handler.codec.LengthFieldPrepender;

import com.telecom.ctu.platform.components.upns.protocol.TransformPackage;

/**
 * @project node-server
 * @date 2013-8-18-上午11:41:54
 * @author pippo
 */
public class BinaryEncoder extends LengthFieldPrepender {

	//	private static final Logger logger = LoggerFactory.getLogger(BinaryEncoder.class);

	public BinaryEncoder() {
		super(TransformPackage.head_length, false);
	}

	//	@Override
	//	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
	//		logger.debug("begin binary encode");
	//		super.encode(ctx, msg, out);
	//	}

}
