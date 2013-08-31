/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.engine.processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telecom.ctu.platform.components.upns.node.engine.AbstractProcessor;
import com.telecom.ctu.platform.components.upns.node.engine.PackageProcessException;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessContext;
import com.telecom.ctu.platform.components.upns.node.engine.ProcessEngine;
import com.telecom.ctu.platform.components.upns.node.service.outer.DeliveryBox;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Receipt;

/**
 * @project node-server
 * @date 2013-8-31-下午3:56:47
 * @author pippo
 */
@Component
public class ReceiptProcessor extends AbstractProcessor<Receipt> {

	private static final Logger logger = LoggerFactory.getLogger(ReceiptProcessor.class);

	@Resource
	private DeliveryBox deliveryBox;

	@Override
	public void process(ProcessContext context) throws PackageProcessException {
		Receipt receipt = (Receipt) context.getRq();
		deliveryBox.ack(context.getSubscriber().getUserId(), receipt.messageId);

		logger.debug("act the receipt:[{}]", receipt);
	}

	@PostConstruct
	public void init() {
		engine.regist(this);
	}

	@Resource
	private ProcessEngine engine;

}
