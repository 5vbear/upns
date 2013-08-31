/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.engine.processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.telecom.ctu.platform.components.upns.node.domain.Subscriber;
import com.telecom.ctu.platform.components.upns.node.engine.AbstractProcessor;
import com.telecom.ctu.platform.components.upns.node.engine.PackageProcessException;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessContext;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessEngine;
import com.telecom.ctu.platform.components.upns.node.service.inner.DeviceService;
import com.telecom.ctu.platform.components.upns.protocol.sub.RegistRQ;
import com.telecom.ctu.platform.components.upns.protocol.sub.RegistRS;

/**
 * @project node-server
 * @date 2013-8-26-下午5:50:46
 * @author pippo
 */
@Component
public class RegistProcessor extends AbstractProcessor<RegistRQ> {

	//	private static final Logger logger = LoggerFactory.getLogger(RegistProcessor.class);

	@Resource
	private DeviceService deviceService;

	@Override
	public void process(ProcessContext context) throws PackageProcessException {
		RegistRQ rq = (RegistRQ) context.getRq();
		context.setRs(new RegistRS(rq.appId != null));

		if (rq.appId != null) {
			Subscriber subscriber = context.getSubscriber();
			deviceService.registApp(subscriber.getUserId(), subscriber.getDeviceToken(), rq.appId);
		}
	}

	@PostConstruct
	public void init() {
		engine.regist(this);
	}

	@Resource
	private ProcessEngine engine;

}
