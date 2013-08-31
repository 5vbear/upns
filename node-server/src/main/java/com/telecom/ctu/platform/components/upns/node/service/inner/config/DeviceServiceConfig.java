/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.inner.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.telecom.ctu.platform.components.upns.node.service.inner.DeviceService;
import com.telecom.ctu.platform.components.upns.node.service.inner.impl.MemDeviceService;

/**
 * @project node-server
 * @date 2013-8-29-下午2:37:48
 * @author pippo
 */
@Configuration
public class DeviceServiceConfig {

	@Value("${ctu.upns.service.device-service.type}")
	private String service_type;

	private DeviceService deviceService;

	@Bean(autowire = Autowire.BY_TYPE, name = "ctu.upns.deviceService")
	public DeviceService instance() {

		if (deviceService != null) {
			return deviceService;
		}

		switch (service_type) {
			case "memory":
				deviceService = new MemDeviceService();
				return deviceService;
			case "mongo":
				return null;
			default:
				throw new IllegalArgumentException(String.format("invalid service type", service_type));
		}

	}
}
