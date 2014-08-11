/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process.processor;

import com.myctu.platform.thread.CustomizableThreadFactory;
import com.sirius.upns.protocol.business.msg.APPUnread;
import com.sirius.upns.protocol.business.route.SubApply;
import com.sirius.upns.protocol.business.route.SubRouter;
import com.sirius.upns.protocol.business.sub.ConnectRQ;
import com.sirius.upns.protocol.business.sub.ConnectRS;
import com.sirius.upns.server.node.domain.Subscriber;
import com.sirius.upns.server.node.domain.model.Device;
import com.sirius.upns.server.node.engine.process.AbstractProcessor;
import com.sirius.upns.server.node.engine.process.PackageProcessException;
import com.sirius.upns.server.node.engine.process.ProcessContext;
import com.sirius.upns.server.node.engine.process.ProcessEngine;
import com.sirius.upns.server.node.service.DeviceService;
import com.sirius.upns.server.node.service.SubscriberRouterTable;
import com.sirius.upns.server.node.service.TimelineService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @project node-server
 * @date 2013-8-26-下午5:50:46
 * @author pippo
 */
@Component
public class ConnectProcessor extends AbstractProcessor<ConnectRQ> {

	private static final Logger logger = LoggerFactory.getLogger(ConnectProcessor.class);

	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4,
		new CustomizableThreadFactory("ConnectProcessor.history"));

	@Override
	public void process(ProcessContext context) throws PackageProcessException {
		ConnectRQ rq = (ConnectRQ) context.getRq();

		Validate.notNull(rq.subscriberId, "the subscriberId can not be null!");
		SubApply apply = routerTable.validate(rq.subscriberId);

        if (apply == null){
            logger.warn("can not find apply for subscriber:[{}]",rq.subscriberId);
        }

		ConnectRS rs = new ConnectRS(apply != null);
		context.setRs(rs);

		if (!rs.success) {
			return;
		}

		/* 注册用户设备 */
		@SuppressWarnings("unused") Device device = deviceService.regist(apply.userId,
			apply.deviceToken,
			apply.deviceType);

		/* 将router信息bind到context上 */
		SubRouter router = routerTable.get(apply.id);
		Validate.notNull(router, "can not find router with apply:[%s]", apply);
		context.bind(router);

		/* start sub */
		Subscriber subscriber = context.getSubscriber();
		subscriber.start();
		/* publish history */
		executor.execute(new HistoryTask(subscriber));
	}

	@PostConstruct
	public void init() {
		engine.regist(this);
	}

	@Resource
	private SubscriberRouterTable routerTable;

	@Resource
	private DeviceService deviceService;

	@Resource
	private ProcessEngine engine;

	@Resource(name = "upns.timelineService")
	private TimelineService timelineService;

	private class HistoryTask implements Runnable {

		private HistoryTask(Subscriber subscriber) {
			this.subscriber = subscriber;
		}

		private Subscriber subscriber;

		@Override
		public void run() {
			List<APPUnread> unreads = timelineService.unread(subscriber.getUserId());
			for (APPUnread unread : unreads) {
				subscriber.publish(unread);
				logger.debug("publish unread:[{}] to subscriber:[{}]", unread, subscriber);
			}
		}

	}

}
