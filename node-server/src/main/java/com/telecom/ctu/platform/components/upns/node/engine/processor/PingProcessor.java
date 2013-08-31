/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.engine.processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.telecom.ctu.platform.components.upns.node.engine.AbstractProcessor;
import com.telecom.ctu.platform.components.upns.node.engine.PackageProcessException;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessContext;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessEngine;
import com.telecom.ctu.platform.components.upns.protocol.sub.Ping;

/**
 * @project node-server
 * @date 2013-8-26-下午4:09:20
 * @author pippo
 */
@Component
public class PingProcessor extends AbstractProcessor<Ping> {

	@Override
	public void process(ProcessContext context) throws PackageProcessException {
		Ping rq = (Ping) context.getRq();
		rq.time = System.currentTimeMillis();
		context.setRs(rq);
	}

	@Resource
	private ProcessEngine engine;

	@PostConstruct
	public void init() {
		engine.regist(this);
	}
}
