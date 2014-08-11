/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process.processor;

import com.sirius.upns.protocol.business.msg.BatchReceipt;
import com.sirius.upns.server.node.engine.process.AbstractProcessor;
import com.sirius.upns.server.node.engine.process.PackageProcessException;
import com.sirius.upns.server.node.engine.process.ProcessContext;
import com.sirius.upns.server.node.engine.process.ProcessEngine;
import com.sirius.upns.server.node.service.DeliverBox;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @project node-server
 * @date 2013-8-31-下午3:56:47
 * @author pippo
 */
@Component
public class BatchReceiptProcessor extends AbstractProcessor<BatchReceipt> {

	//	private static final Logger logger = LoggerFactory.getLogger(BatchReceiptProcessor.class);

	@Resource
	private DeliverBox deliveryBox;

	@Override
	public void process(ProcessContext context) throws PackageProcessException {
		BatchReceipt receipt = (BatchReceipt) context.getRq();
		deliveryBox.ackAll(context.getSubscriber().getUserId(), receipt.time);

		//		logger.debug("ack the receipt:[{}]", receipt);
	}

	@PostConstruct
	public void init() {
		engine.regist(this);
	}

	@Resource
	private ProcessEngine engine;

}
