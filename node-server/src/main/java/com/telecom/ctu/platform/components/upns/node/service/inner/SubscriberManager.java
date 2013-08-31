/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.inner;

import com.telecom.ctu.platform.components.upns.node.domain.Subscriber;

/**
 * @project node-server
 * @date 2013-8-26-下午2:23:16
 * @author pippo
 */
public interface SubscriberManager {

	void add(Subscriber subscriber);

	void remove(String id);

	Subscriber getById(String id);

}
