/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.domain;

import com.myctu.platform.gateway.agent.pubsub.PubSubService.EventListener;
import com.telecom.ctu.platform.components.upns.protocol.DeviceType;
import com.telecom.ctu.platform.components.upns.protocol.MessagePackage;

/**
 * @project node-server
 * @date 2013-8-26-下午2:52:33
 * @author pippo
 */
public interface Subscriber extends EventListener {

	String getId();

	String getUserId();

	String getDeviceToken();

	DeviceType getDeviceType();

	String getHost();

	long getActive();

	boolean isValid();

	void setAttribute(String name, Object value);

	Object getAttribute(String name);

	void active();

	void onMessage(MessagePackage message);

	void destory();

}
