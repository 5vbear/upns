/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.route;

import com.myctu.platform.protocol.MapSerializable;
import com.sirius.upns.protocol.business.DeviceType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @project protocol
 * @date 2013-8-30-下午1:49:34
 * @author pippo
 */
public class SubApply implements MapSerializable {

	private static final long serialVersionUID = -7625260515950443452L;

	/* 5分钟应该过期 */
	public static final long _EXPIRE = 1000 * 60 * 5;

	public static final String ID = "_id";

	public static final String USER_ID = "u";

	public static final String DEVICE_TOKEN = "o";

	public static final String DEVICE_TYPE = "t";

	public static final String HOST = "h";

	public static final String PORT = "p";

	public static final String EXPIRE = "e";

	public SubApply() {

	}

	public SubApply(String userId, String deviceToken, DeviceType deviceType) {
		this.userId = userId;
		this.deviceToken = deviceToken;
		this.deviceType = deviceType;

		this.id = UUID.randomUUID().toString();
		/* 申请有效时间为5分钟,如果5分钟内没有发起连接,那么申请过期 */
		this.expire = System.currentTimeMillis() + _EXPIRE;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(ID, id);
		out.put(USER_ID, userId);
		out.put(DEVICE_TOKEN, deviceToken);
		out.put(DEVICE_TYPE, deviceType.code);
		out.put(HOST, host);
		out.put(PORT, port);
		out.put(EXPIRE, expire);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.id = (String) in.get(ID);
		this.userId = (String) in.get(USER_ID);
		this.deviceToken = (String) in.get(DEVICE_TOKEN);
		this.deviceType = DeviceType.from((Integer) in.get(DEVICE_TYPE));
		this.host = (String) in.get(HOST);
		this.port = (Integer) in.get(PORT);
		this.expire = (Long) in.get(EXPIRE);
	}

	public String id;

	public String userId;

	public String deviceToken;

	public DeviceType deviceType;

	public String host;

	public int port;

	public long expire;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceToken == null) ? 0 : deviceToken.hashCode());
		result = prime * result + ((deviceType == null) ? 0 : deviceType.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		SubApply other = (SubApply) obj;
		if (deviceToken == null) {
			if (other.deviceToken != null)
				return false;
		} else if (!deviceToken.equals(other.deviceToken))
			return false;
		if (deviceType != other.deviceType)
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("SubApply [id=%s, userId=%s, deviceToken=%s, deviceType=%s, host=%s, port=%s, expire=%s]",
			id,
			userId,
			deviceToken,
			deviceType,
			host,
			port,
			expire);
	}

}
