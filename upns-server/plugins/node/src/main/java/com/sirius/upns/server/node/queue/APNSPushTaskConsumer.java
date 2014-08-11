/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node.queue;

import com.myctu.platform.gateway.agent.queue.Consumer;
import com.myctu.platform.gateway.agent.queue.QueueException;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.server.node.domain.model.Device;
import com.sirius.upns.server.node.engine.apns.AppleNotificationTemplateConfiguration.Templates;
import com.sirius.upns.server.node.engine.apns.PushNotificationProcessor;
import com.sirius.upns.server.node.engine.apns.PushNotificationTemplate;
import com.sirius.upns.server.node.repository.DeviceRepository;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: pippo
 * Date: 13-11-28-13:51
 */
@Service("ctu.upns.queue.apple.push.consumer")
public class APNSPushTaskConsumer implements Consumer<APNSPushTask> {

	private static final Logger logger = LoggerFactory.getLogger(APNSPushTaskConsumer.class);

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() * 4);

	/*{appId=PushNotificationTemplate}*/
	@Resource(name = "ctu.upns.apnsTemplate")
	private Templates templates;

	@Resource(name = "ctu.upns.deviceRepository")
	private DeviceRepository deviceRepository;

	@Override
	public Class<APNSPushTask> getEventType() {
		return APNSPushTask.class;
	}

	@Override
	public void onEvent(final APNSPushTask event) throws QueueException {
		final List<Device> devices = deviceRepository.getByType(event.userId, DeviceType.ios);
		if (devices == null) {
			return;
		}

		/*加快消费速度,因为次序无所谓*/
		EXECUTOR_SERVICE.execute(new Runnable() {
			@Override public void run() {
				push(event, devices);
			}
		});
	}

	private void push(final APNSPushTask event, final List<Device> devices) {
		int appId = event.getMessage().getAppId();
		final PushNotificationTemplate pushNotificationTemplate = templates.payloads.get(appId);
		if (pushNotificationTemplate == null) {
			logger.warn("can not find pushNotificationTemplate with appId:[{}], ignore event:[{}]", appId, event);
			return;
		}

		pushNotificationTemplate.push(new PushNotificationProcessor() {

			@Override
			public void process(PushNotificationManager manager) throws Exception {
				//int unread = timelineRepository.unread(event.userId, event.message.appId) + 1;
				PushNotificationPayload payload = PushNotificationPayload.combined(event.message.title, 1, "default");

				for (Device device : devices) {
					if (device.token == null || device.token.length() != 64) {
						continue;
					}

					PushedNotification notification = manager.sendNotification(new BasicDevice(device.token), payload,
							false);

					if (notification != null) {
						logger.debug(
								"push message:[{}] to apns[user/device]:[{}/{}], the acknowledge is:[{}], the push result is:{}",
								event.message.title,
								device.userId,
								device.token,
								notification,
								notification.isSuccessful());
					}
				}

			}
		});
	}

}
