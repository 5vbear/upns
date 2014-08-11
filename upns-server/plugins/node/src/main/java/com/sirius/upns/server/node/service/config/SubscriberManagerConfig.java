/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service.config;

import com.sirius.upns.server.node.service.SubscriberManager;
import com.sirius.upns.server.node.service.impl.MemSubscriberManager;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-29-下午2:37:48
 */
@Configuration
public class SubscriberManagerConfig {

    @Value("${ctu.upns.server.node.service.subscriber-manager.type}")
    private String service_type;

    private SubscriberManager subscriberManager;

    @Bean(autowire = Autowire.BY_TYPE, name = "ctu.upns.subscriberManager")
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
