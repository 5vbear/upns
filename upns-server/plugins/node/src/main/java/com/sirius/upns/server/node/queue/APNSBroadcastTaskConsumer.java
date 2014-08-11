/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node.queue;

import com.myctu.platform.gateway.agent.queue.Consumer;
import com.myctu.platform.gateway.agent.queue.Productor;
import com.myctu.platform.gateway.agent.queue.QueueException;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.server.node.domain.model.Device;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.engine.apns.AppleNotificationTemplateConfiguration.Templates;
import com.sirius.upns.server.node.repository.DeviceClosure;
import com.sirius.upns.server.node.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * User: pippo
 * Date: 13-11-28-13:51
 */
@Service("ctu.upns.queue.apple.broadcast.consumer")
public class APNSBroadcastTaskConsumer implements Consumer<APNSBroadcastTask> {

	private static final Logger logger = LoggerFactory.getLogger(APNSBroadcastTaskConsumer.class);

	@Resource(name = "ctu.upns.queue.apple.push.productor")
	private Productor apnsTaskProductor;

	@Resource(name = "ctu.upns.deviceRepository")
	private DeviceRepository deviceRepository;

	/*{appId=PushNotificationTemplate}*/
	@Resource(name = "ctu.upns.apnsTemplate")
	private Templates templates;

	@Override
	public Class<APNSBroadcastTask> getEventType() {
		return APNSBroadcastTask.class;
	}

	@Override
	public void onEvent(final APNSBroadcastTask event) throws QueueException {
		logger.debug("received apple broadcast event:[{}]", event);

		deviceRepository.iterateDevices(DeviceType.ios, new DeviceClosure() {

			@Override
			public void execute(final Device device) {
				if (device.token == null || device.token.length() != 64) {
					return;
				}

				//push(device, event);
				sendToAPNS(event.getMessage(), device.userId);
			}

		});
	}

	private void sendToAPNS(Message message, String userId) {
		try {
			apnsTaskProductor.product(new APNSPushTask(message, userId));
		} catch (QueueException e) {
			logger.error("add apple push task due to error", e);
		}
	}

	//	private void push(final Device device, final APNSBroadcastTask event) {
	//		PushNotificationTemplate template = templates.payloads.get(event.message.appId);
	//
	//		template.push(new PushNotificationProcessor() {
	//			@Override
	//			public void process(PushNotificationManager manager) throws Exception {
	//				PushNotificationPayload payload = PushNotificationPayload.combined(event.message.title,
	//						1,
	//						"default");
	//				PushedNotification notification = manager.sendNotification(new BasicDevice(device.token),
	//						payload,
	//						false);
	//
	//				if (notification != null) {
	//					logger.debug(
	//							"push message:[{}] to apns[user/device]:[{}/{}], the acknowledge is:[{}], the push result is:{}",
	//							event.message.title,
	//							device.userId,
	//							device.token,
	//							notification,
	//							notification.isSuccessful());
	//				}
	//
	//			}
	//		});
	//	}
}
