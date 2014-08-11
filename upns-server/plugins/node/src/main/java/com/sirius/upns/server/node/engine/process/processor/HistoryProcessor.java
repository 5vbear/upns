/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process.processor;

import com.sirius.upns.protocol.business.msg.History;
import com.sirius.upns.server.node.engine.process.AbstractProcessor;
import com.sirius.upns.server.node.engine.process.PackageProcessException;
import com.sirius.upns.server.node.engine.process.ProcessContext;
import com.sirius.upns.server.node.engine.process.ProcessEngine;
import com.sirius.upns.server.node.service.TimelineService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @project node-server
 * @date 2013-8-26-下午5:50:46
 * @author pippo
 */
@Component
public class HistoryProcessor extends AbstractProcessor<History> {

	@Override
	public void process(ProcessContext context) throws PackageProcessException {
		History example = (History) context.getRq();
		example.userId = context.getSubscriber().getUserId();
		if (example.timestamp == null) {
			example.timestamp = System.currentTimeMillis();
		}

		/* 返回未读消息 */
		context.setRs(timelineService.find(example));

		/* 标记所有消息为已读 */
		//		if (Boolean.TRUE.equals(example.ack)) {
		//			timelineService.ack(example.userId, System.currentTimeMillis());
		//		}
	}

	@PostConstruct
	public void init() {
		engine.regist(this);
	}

	@Resource
	private ProcessEngine engine;

	@Resource(name = "upns.timelineService")
	private TimelineService timelineService;

}
