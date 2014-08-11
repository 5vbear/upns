/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.msg;

import com.sirius.upns.protocol.business.BusinessPacket;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project upns-protocol
 * @date 2013-9-25-下午10:49:20
 * @author pippo
 */
public class BatchReceipt implements BusinessPacket {

	private static final long serialVersionUID = -542166529641397961L;

	public static final String DEVICE_TOKEN = "o";

	public static final String TIME = "t";

	public BatchReceipt() {

	}

	public BatchReceipt(String deviceToken) {
		this.deviceToken = deviceToken;
		this.time = System.currentTimeMillis();
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(DEVICE_TOKEN, deviceToken);
		out.put(TIME, time);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.deviceToken = (String) in.get(DEVICE_TOKEN);
		this.time = in.get(TIME) != null ? ((Number) in.get(TIME)).longValue() : 0L;
	}

	/* 推送到的设备 */
	public String deviceToken;

	/* 设备接收时间,此时间之前的消息都认为ack */
	public long time;

	@Override
	public String toString() {
		return String.format("BatchReceipt [deviceToken=%s, time=%s]", deviceToken, time);
	}

}
