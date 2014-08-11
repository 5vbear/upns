package com.sirius.upns.server.node.service.impl;

import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.server.node.domain.Subscriber;
import com.sirius.upns.server.node.service.SubscriberManager;
import com.sirius.upns.server.node.service.SubscriberManager.SubscriberClosure;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by pippo on 14-4-1.
 */
public class BroadcastChannelListener extends PrivateChannelListener {

	private static final Logger logger = LoggerFactory.getLogger(BroadcastChannelListener.class);

	public BroadcastChannelListener(SubscriberManager subscriberManager) {
		super(subscriberManager);
	}

	@Override
	public void onEvent(String channel, final Map<String, Object> event) {
		logger.debug("received event:[{}] on channel:[{}]", event, channel);
		/*异步发送避免消费的太慢*/
		executor.execute(new Runnable() {

			@Override
			public void run() {
				sendToClient(event);
			}

		});
	}

	private void sendToClient(final Map<String, Object> event) {
		subscriberManager.iterate(new SubscriberClosure() {

			@Override
			public void execute(Subscriber subscriber) {
				Custom custom = new Custom();
				custom.fromMap(event);
				logger.debug("publish message:[{}] to subscriber:[{}]", custom, subscriber);
				try {
					subscriber.publish(custom);
				} catch (Exception e) {
					logger.error("send message:[{}] to client due to error:[{}]",
							event,
							ExceptionUtils.getStackTrace(e));
				}
			}

		});
	}
}
