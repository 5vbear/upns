/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process.processor;

import com.sirius.upns.protocol.business.sub.RegistRQ;
import com.sirius.upns.protocol.business.sub.RegistRS;
import com.sirius.upns.server.node.domain.Subscriber;
import com.sirius.upns.server.node.engine.process.AbstractProcessor;
import com.sirius.upns.server.node.engine.process.PackageProcessException;
import com.sirius.upns.server.node.engine.process.ProcessContext;
import com.sirius.upns.server.node.engine.process.ProcessEngine;
import com.sirius.upns.server.node.service.DeviceService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
