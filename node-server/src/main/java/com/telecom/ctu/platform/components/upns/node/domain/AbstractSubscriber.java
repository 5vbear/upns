/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.domain;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.telecom.ctu.platform.components.upns.protocol.DeviceType;

/**
 * @project node-server
 * @date 2013-8-27-上午9:17:57
 * @author pippo
 */
public abstract class AbstractSubscriber implements Subscriber {

	public AbstractSubscriber(String userId, String deviceToken, DeviceType deviceType) {
		this.id = UUID.randomUUID().toString();
		this.userId = userId;
		this.deviceToken = deviceToken;
		this.deviceType = deviceType;
		this.valid = false;
	}

	public void active() {
		this.active = System.currentTimeMillis();
	}

	protected String id;

	protected String userId;

	protected String deviceToken;

	protected DeviceType deviceType;

	protected String host;

	protected long active;

	protected boolean valid;

	protected Map<String, Object> attr;

	public String getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	@Override
	public DeviceType getDeviceType() {
		return deviceType;
	}

	public String getHost() {
		return host;
	}

	public long getActive() {
		return active;
	}

	public boolean isValid() {
		return valid;
	}

	public void setAttribute(String name, Object value) {
		if (attr == null) {
			attr = Maps.newHashMap();
		}

		attr.put(name, value);
	}

	public Object getAttribute(String name) {
		if (attr == null) {
			return null;
		}

		return attr.get(name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractSubscriber other = (AbstractSubscriber) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("AbstractSubscriber [id=%s, userId=%s, deviceToken=%s, deviceType=%s, host=%s, active=%s, valid=%s, attr=%s]",
			id,
			userId,
			deviceToken,
			deviceType,
			host,
			active,
			valid,
			attr);
	}

}
