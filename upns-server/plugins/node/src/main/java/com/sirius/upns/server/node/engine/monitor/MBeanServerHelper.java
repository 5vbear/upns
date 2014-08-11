/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @project node-server
 * @date 2013-9-15-上午8:22:30
 * @author pippo
 */
public class MBeanServerHelper {

	private static final Logger logger = LoggerFactory.getLogger(MBeanServerHelper.class);

	public static MBeanServer findMBeanServer() {
		ArrayList<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
		if (servers == null || servers.isEmpty()) {
			return ManagementFactory.getPlatformMBeanServer();
		}

		return servers.get(0);
	}

	private static AtomicInteger index = new AtomicInteger(0);

	public static ObjectName getObjectName(String name) throws MalformedObjectNameException {
		return new ObjectName(String.format("com.telecom.ctu.platform:upns-monitor=%s-%s",
			name,
			index.incrementAndGet()));
	}

	public static <T> void registMBean(String name, T bean, Class<T> interfaceType) {

		try {
			MBeanServer mBeanServer = findMBeanServer();
			ObjectName objectName = getObjectName(name);
			mBeanServer.registerMBean(new StandardMBean(bean, interfaceType, true), objectName);
			logger.info("regist new statistics mbean:[{}]", objectName);
		} catch (Exception e) {
			logger.warn(String.format("regist mbean:[%s] due to error", name), e);
		}

	}

}
