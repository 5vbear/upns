/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.inner.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import com.telecom.ctu.platform.components.upns.node.domain.Subscriber;
import com.telecom.ctu.platform.components.upns.node.service.inner.SubscriberManager;

/**
 * @project node-server
 * @date 2013-8-26-下午6:03:13
 * @author pippo
 */
public class MemSubscriberManager implements SubscriberManager {

	private static Map<String, Subscriber> subscribers = new ConcurrentSkipListMap<String, Subscriber>();

	@Override
	public void add(Subscriber subscriber) {
		subscribers.put(subscriber.getId(), subscriber);
	}

	@Override
	public void remove(String id) {
		subscribers.remove(id);
	}

	@Override
	public Subscriber getById(String id) {
		return subscribers.get(id);
	}

}
