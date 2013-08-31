/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.inner.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.telecom.ctu.platform.components.upns.node.service.inner.GroupService;
import com.telecom.ctu.platform.components.upns.node.service.inner.impl.MemGroupService;

/**
 * @project node-server
 * @date 2013-8-29-下午2:37:48
 * @author pippo
 */
@Configuration
public class GroupServiceConfig {

	@Value("${ctu.upns.service.group-service.type}")
	private String service_type;

	private GroupService groupService;

	@Bean(autowire = Autowire.BY_TYPE, name = "ctu.upns.groupSerivce")
	public GroupService instance() {

		if (groupService != null) {
			return groupService;
		}

		switch (service_type) {
			case "memory":
				groupService = new MemGroupService();
				return groupService;
			case "mongo":
				return null;
			default:
				throw new IllegalArgumentException(String.format("invalid service type", service_type));
		}

	}
}
