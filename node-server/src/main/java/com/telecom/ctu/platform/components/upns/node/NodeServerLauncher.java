/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : sirius-orm
 * user created : pippo
 * date created : 2013-5-14 - 下午12:55:49
 */
package com.telecom.ctu.platform.components.upns.node;

import java.util.logging.Handler;
import java.util.logging.LogManager;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.myctu.platform.AppSettings;
import com.myctu.platform.spring.ext.BeanLocator;

/**
 * @since 2013-5-14
 * @author pippo
 */
public class NodeServerLauncher {

	private static ClassPathXmlApplicationContext context;

	public static void main(String[] args) throws Exception {
		if (context == null) {
			start();
		}
	}

	public static void start() throws Exception {
		// Jersey uses java.util.logging - bridge to slf4
		java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		SLF4JBridgeHandler.install();
		// start context
		AppSettings.init("/ctu.upns.node-server.settings.properties");
		createApplicationContext();
	}

	public static void stop() throws Exception {
		if (context != null) {
			context.destroy();
		}
	}

	private static void createApplicationContext() {
		String[] configLocations = new String[] { "/ctu.upns.node-server.context.xml" };

		context = new ClassPathXmlApplicationContext(configLocations);
		context.registerShutdownHook();
		context.start();
		BeanLocator.setApplicationContext(context);
	}

}
