/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process.processor;

import com.sirius.upns.protocol.business.sub.Ping;
import com.sirius.upns.server.node.domain.Subscriber;
import com.sirius.upns.server.node.engine.process.AbstractProcessor;
import com.sirius.upns.server.node.engine.process.PackageProcessException;
import com.sirius.upns.server.node.engine.process.ProcessContext;
import com.sirius.upns.server.node.engine.process.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @project node-server
 * @date 2013-8-26-下午4:09:20
 * @author pippo
 */
@Component
public class PingProcessor extends AbstractProcessor<Ping> {

	private static final Logger logger = LoggerFactory.getLogger("upns.subscriber.pong");

	@Override
	public void process(ProcessContext context) throws PackageProcessException {
		//		Ping rq = (Ping) context.getRq();
		//		rq.time = System.currentTimeMillis();
		//		context.setRs(rq);

		Subscriber subscriber = context.getSubscriber();
		subscriber.active();
		if (logger.isTraceEnabled()) {
			logger.trace("received ping from subscriber:[{}]", subscriber);
		}
	}

	@Resource
	private ProcessEngine engine;

	@PostConstruct
	public void init() {
		engine.regist(this);
	}
}
