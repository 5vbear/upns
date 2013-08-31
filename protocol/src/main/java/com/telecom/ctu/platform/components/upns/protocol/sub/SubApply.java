/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.sub;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.myctu.platform.protocol.MapSerializable;
import com.telecom.ctu.platform.components.upns.protocol.DeviceType;

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

	public static final String EXPIRE = "e";

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
		this.expire = (Long) in.get(EXPIRE);
	}

	public String id;

	public String userId;

	public String deviceToken;

	public DeviceType deviceType;

	public String host;

	public long expire;

	@Override
	public String toString() {
		return String.format("SubApply [id=%s, userId=%s, deviceToken=%s, deviceType=%s, host=%s, expire=%s]",
			id,
			userId,
			deviceToken,
			deviceType,
			host,
			expire);
	}

}
