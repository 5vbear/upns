/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain;

import com.myctu.platform.gateway.agent.pubsub.EventListener;
import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.DeviceType;

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

	void publish(BusinessPacket packet);

	void active();

	void start();

	void stop();

}
