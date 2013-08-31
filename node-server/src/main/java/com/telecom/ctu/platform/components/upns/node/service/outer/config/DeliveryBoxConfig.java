/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.outer.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.telecom.ctu.platform.components.upns.node.service.outer.DeliveryBox;
import com.telecom.ctu.platform.components.upns.node.service.outer.impl.MemDeliveryBox;

/**
 * @project node-server
 * @date 2013-8-31-下午2:11:57
 * @author pippo
 */
@Configuration
public class DeliveryBoxConfig {

	@Value("${ctu.upns.service.deliver-box.type}")
	private String service_type;

	private DeliveryBox deliveryBox;

	@Bean(autowire = Autowire.BY_TYPE, name = "ctu.upns.deliveryBox")
	public DeliveryBox instance() {

		if (deliveryBox != null) {
			return deliveryBox;
		}

		switch (service_type) {
			case "memory":
				deliveryBox = new MemDeliveryBox();
				return deliveryBox;
			case "mongo":
				return null;
			default:
				throw new IllegalArgumentException(String.format("invalid service type", service_type));
		}
	}

}
