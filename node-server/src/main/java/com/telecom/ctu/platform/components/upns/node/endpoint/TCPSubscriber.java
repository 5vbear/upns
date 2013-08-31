/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.endpoint;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecom.ctu.platform.components.upns.node.domain.AbstractSubscriber;
import com.telecom.ctu.platform.components.upns.protocol.DeviceType;
import com.telecom.ctu.platform.components.upns.protocol.MessagePackage;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Custom;

/**
 * @project node-server
 * @date 2013-8-30-下午1:20:26
 * @author pippo
 */
public class TCPSubscriber extends AbstractSubscriber {

	private static final Logger logger = LoggerFactory.getLogger(TCPSubscriber.class);

	public TCPSubscriber(String userId, String deviceToken, DeviceType deviceType) {
		super(userId, deviceToken, deviceType);
	}

	private ChannelHandlerContext ctx;

	public void init(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.host = ctx.channel().localAddress().toString();
		this.valid = true;
	}

	@Override
	public void onEvent(String channel, Map<String, Object> event) {
		Custom message = new Custom();
		message.fromMap(event);
		onMessage(message);
	}

	@Override
	public void onMessage(MessagePackage message) {
		if (logger.isDebugEnabled()) {
			logger.debug("write message:[{}] to client:[{}]", message, ctx.channel());
		}

		try {
			ctx.writeAndFlush(message);
		} catch (Throwable cause) {
			ctx.fireExceptionCaught(cause);
		}
	}

	@Override
	public void destory() {
		this.ctx = null;
		if (this.attr != null) {
			this.attr.clear();
			this.attr = null;
		}
	}

	@Override
	public String toString() {
		return String.format("TCPSubscriber [ctx=%s, id=%s, userId=%s, deviceToken=%s, deviceType=%s, host=%s, active=%s, valid=%s, attr=%s]",
			ctx,
			id,
			userId,
			deviceToken,
			deviceType,
			host,
			active,
			valid,
			attr);
	}

}
