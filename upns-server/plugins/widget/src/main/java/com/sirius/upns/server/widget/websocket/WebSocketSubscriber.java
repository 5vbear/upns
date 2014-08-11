package com.sirius.upns.server.widget.websocket;

import com.google.common.collect.ImmutableMap;
import com.myctu.platform.protocol.transform.json.JacksonSupport;
import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.server.node.domain.AbstractSubscriber;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.util.Map;

/**
 * Created by pippo on 14-3-25.
 */
public class WebSocketSubscriber extends AbstractSubscriber {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketSubscriber.class);

	public WebSocketSubscriber(String id, String userId, String deviceToken, DeviceType deviceType) {
		super(userId, deviceToken, deviceType);
		this.id = id;
	}

	private Session session;

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public void start() {
		Validate.notNull(this.session, "the websocket session can not be null");
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

		this.session = null;
	}

	@Override
	public void publish(BusinessPacket packet) {
		if (logger.isDebugEnabled()) {
			logger.debug("publish packet:[{}] to client:[{}]", packet, session);
		}

		try {
			switch (session.getNegotiatedSubprotocol()) {
				case "text":
					session.getAsyncRemote().sendObject(packet);
					break;
				case "json":
					Map<String, Object> out = ImmutableMap.<String, Object>builder()
							.put("type", packet.getClass().getSimpleName())
							.put("package", packet)
							.build();

					session.getAsyncRemote().sendText(JacksonSupport.objectMapper.writeValueAsString(out));
					break;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

}
