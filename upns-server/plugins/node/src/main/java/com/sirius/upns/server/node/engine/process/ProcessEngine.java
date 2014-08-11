/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process;

import com.sirius.upns.protocol.business.BusinessPacket;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @project node-server
 * @date 2013-8-26-下午4:10:49
 * @author pippo
 */
@Component
public class ProcessEngine {

	private static Logger logger = LoggerFactory.getLogger(ProcessEngine.class);

	@SuppressWarnings("rawtypes")
	private Map<Class, BusinessPackageProcessor> shell = new HashMap<>();

	public void regist(@SuppressWarnings("rawtypes") BusinessPackageProcessor processor) {
		logger.info("regist processor:[{}] for type:[{}]", processor, processor.supportRQ());
		shell.put(processor.supportRQ(), processor);
		logger.info("the registed processor shell is:[{}]", shell);
	}

	public void process(ProcessContext context) throws PackageProcessException {
		BusinessPacket rq = context.getRq();
		Validate.notNull(rq, "can not process null rq");

		BusinessPackageProcessor<?> processor = shell.get(rq.getClass());
		Validate.notNull(processor, "can not find processor for type:[%s]", rq.getClass());

		try {
			logger.debug("begin process context:[{}]", context);
			processor.process(context);
			logger.debug("finish process context:[{}]", context);
		} catch (Exception e) {
			logger.error("process context:[{}] due to error:[{}]", context, ExceptionUtils.getStackTrace(e));
			throw new PackageProcessException(e);
		}
	}
}
