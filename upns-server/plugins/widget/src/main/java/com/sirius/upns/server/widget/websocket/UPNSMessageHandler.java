package com.sirius.upns.server.widget.websocket;

import com.sirius.upns.server.node.service.TimelineService;
import com.sirius.upns.server.widget.websocket.endpoint.JSONMessageHandler;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;

/**
 * Created by pippo on 14-3-25.
 */
@Component("upnsMessageHandler")
public class UPNSMessageHandler implements JSONMessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UPNSMessageHandler.class);

	@Resource(name = "upns.timelineService")
	private TimelineService timelineService;

	@Override
	public void onMessage(JsonNode json, Session session, String user) {
		LOGGER.debug("recieved msg:[{}] from user:[{}]", json, user);

		if (json.get("type") != null && "ack".equalsIgnoreCase(json.get("type").getTextValue())) {
			LOGGER.debug("ack all for user:[{}]", user);
			timelineService.ackAll(user, System.currentTimeMillis());
		}
	}

}
