/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.inner.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.telecom.ctu.platform.components.upns.node.service.inner.SubscriberManager;
import com.telecom.ctu.platform.components.upns.node.service.inner.impl.MemSubscriberManager;

/**
 * @project node-server
 * @date 2013-8-29-下午2:37:48
 * @author pippo
 */
@Configuration
public class SubscriberManagerConfig {

	@Value("${ctu.upns.service.session-manager.type}")
	private String service_type;

	private SubscriberManager subscriberManager;

	@Bean(autowire = Autowire.BY_TYPE, name = "ctu.upns.groupSerivce")
	public SubscriberManager instance() {

		if (subscriberManager != null) {
			return subscriberManager;
		}

		switch (service_type) {
			case "memory":
				subscriberManager = new MemSubscriberManager();
				return subscriberManager;
			case "mongo":
				return null;
			default:
				throw new IllegalArgumentException(String.format("invalid service type", service_type));
		}

	}
}
