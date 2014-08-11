package com.sirius.upns.server.node.service.impl;

import com.myctu.platform.gateway.agent.pubsub.AbstractEventListener;
import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.protocol.business.msg.Private;
import com.sirius.upns.server.node.domain.Subscriber;
import com.sirius.upns.server.node.service.SubscriberManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pippo on 14-4-1.
 */
public class PrivateChannelListener extends AbstractEventListener {

	private static final Logger logger = LoggerFactory.getLogger(PrivateChannelListener.class);

	protected static ExecutorService executor = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() * 4);

	protected SubscriberManager subscriberManager;

	public PrivateChannelListener(SubscriberManager subscriberManager) {
		this.subscriberManager = subscriberManager;
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

	private void sendToClient(Map<String, Object> event) {
		try {
			/*其实类型是个private,直接获取userId,避免不需要的反序列化*/
			String userId = (String) event.get(Private.USER_ID);
			Set<Subscriber> _subscribers = subscriberManager.getByUserId(userId);
			if (_subscribers.isEmpty()) {
				logger.debug("can not find subscriber with userId:[{}]", userId);
				return;
			}

			Custom custom = new Custom();
			custom.fromMap(event);
			for (Subscriber subscriber : _subscribers) {
				logger.debug("publish message:[{}] to subscriber:[{}]", custom, subscriber);
				subscriber.publish(custom);
			}
		} catch (Exception e) {
			logger.error("send message:[{}] to client due to error:[{}]", event, ExceptionUtils.getStackTrace(e));
		}
	}
}
