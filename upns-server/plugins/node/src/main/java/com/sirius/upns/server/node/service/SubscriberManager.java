/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service;

import com.sirius.upns.server.node.domain.Subscriber;

import java.util.Set;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-26-下午2:23:16
 */
public interface SubscriberManager {

	void add(Subscriber subscriber);

	void remove(String id);

	Subscriber getById(String id);

	Set<Subscriber> getByUserId(String userId);

	void start(Subscriber subscriber);

	void stop(Subscriber subscriber);

	void iterate(SubscriberClosure closure);

	interface SubscriberClosure {

		void execute(Subscriber subscriber);

	}

}
