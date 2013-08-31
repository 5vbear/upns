/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.sub.msg;

import java.util.LinkedHashMap;
import java.util.Map;

import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;

/**
 * @project protocol
 * @date 2013-8-14-下午4:05:26
 * @author pippo
 */
public class Receipt implements BusinessPackage {

	private static final long serialVersionUID = -542166529641397961L;

	public static final String MESSAGE_ID = "i";

	public static final String DEVICE_TOKEN = "o";

	public static final String TIME = "t";

	public Receipt() {

	}

	public Receipt(String messageId, String deviceToken) {
		this.messageId = messageId;
		this.deviceToken = deviceToken;
		this.time = System.currentTimeMillis();
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(MESSAGE_ID, messageId);
		out.put(DEVICE_TOKEN, deviceToken);
		out.put(TIME, time);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.messageId = (String) in.get(MESSAGE_ID);
		this.deviceToken = (String) in.get(DEVICE_TOKEN);
		this.time = in.get(TIME) != null ? ((Number) in.get(TIME)).longValue() : 0L;
	}

	/* 消息id */
	public String messageId;

	/* 推送到的设备 */
	public String deviceToken;

	/* 设备接收时间 */
	public long time;

	@Override
	public String toString() {
		return String.format("Receipt [messageId=%s, deviceToken=%s, time=%s]", messageId, deviceToken, time);
	}

}
