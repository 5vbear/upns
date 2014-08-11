/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.endpoint;

import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.server.node.domain.AbstractSubscriber;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.host = ctx.channel().remoteAddress().toString();
	}

	@Override
	public void start() {
		Validate.notNull(this.ctx, "the channel context can not be null");
		this.valid = true;
		super.start();
	}

	@Override
	public void stop() {
		super.stop();

		if (this.attr != null) {
			this.attr.clear();
			this.attr = null;
		}

        this.ctx = null;
    }

	@Override
	public void onEvent(String channel, Map<String, Object> event) {
		if (logger.isDebugEnabled()) {
			logger.debug("subscriber:[{}] recieved event:[{}] on channel:[{}]", this, event, channel);
		}

		Custom message = new Custom();
		message.fromMap(event);
		publish(message);
	}

	@Override
	public void publish(BusinessPacket packet) {
		if (logger.isDebugEnabled()) {
			logger.debug("publish packet:[{}] to client:[{}]", packet, ctx.channel());
		}

		try {
			ctx.writeAndFlush(packet);
		} catch (Throwable cause) {
			ctx.fireExceptionCaught(cause);
		}
	}

	@Override
	public String toString() {
		return String.format("TCPSubscriber [id=%s, userId=%s, deviceToken=%s, deviceType=%s, host=%s, active=%s, valid=%s, attr=%s]",
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
