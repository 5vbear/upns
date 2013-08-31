/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.engine.processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telecom.ctu.platform.components.upns.node.domain.model.Device;
import com.telecom.ctu.platform.components.upns.node.engine.AbstractProcessor;
import com.telecom.ctu.platform.components.upns.node.engine.PackageProcessException;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessContext;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessEngine;
import com.telecom.ctu.platform.components.upns.node.service.inner.DeviceService;
import com.telecom.ctu.platform.components.upns.node.service.outer.SubscriberRouterTable;
import com.telecom.ctu.platform.components.upns.protocol.sub.ConnectRQ;
import com.telecom.ctu.platform.components.upns.protocol.sub.ConnectRS;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubApply;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubRouter;

/**
 * @project node-server
 * @date 2013-8-26-下午5:50:46
 * @author pippo
 */
@Component
public class ConnectProcessor extends AbstractProcessor<ConnectRQ> {

	private static final Logger logger = LoggerFactory.getLogger(ConnectProcessor.class);

	@Resource
	private SubscriberRouterTable subscriberRouter;

	@Resource
	private DeviceService deviceService;

	@Override
	public void process(ProcessContext context) throws PackageProcessException {
		ConnectRQ rq = (ConnectRQ) context.getRq();
		SubApply apply = subscriberRouter.validate(rq.subscriberId);

		ConnectRS rs = new ConnectRS(apply != null);
		context.setRs(rs);

		if (!rs.success) {
			return;
		}

		Device device = deviceService.getByToken(apply.userId, apply.deviceToken);

		/* 如果设备未注册,那么注册设备 */
		if (device == null) {
			device = deviceService.regist(apply.userId, apply.deviceToken, apply.deviceType);
			logger.debug("regist new device:[{}]", device);
		}

		/* 将router信息bind到context上 */
		SubRouter router = subscriberRouter.find(apply.userId, apply.deviceToken, apply.deviceType);
		Validate.notNull(router, "can not find router with apply:[%s]", apply);
		context.bind(router);
		logger.debug("bind router:[{}] on context", router);
	}

	@PostConstruct
	public void init() {
		engine.regist(this);
	}

	@Resource
	private ProcessEngine engine;

}
