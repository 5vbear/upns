/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.sub;

import java.util.LinkedHashMap;
import java.util.Map;

import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;

/**
 * @project protocol
 * @date 2013-8-18-下午3:16:57
 * @author pippo
 */
public class ConnectRQ implements BusinessPackage {

	private static final long serialVersionUID = -5626659901207238459L;

	public static final String SUBSCRIBER_ID = "i";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(SUBSCRIBER_ID, subscriberId);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.subscriberId = (String) in.get(SUBSCRIBER_ID);
	}

	public ConnectRQ() {

	}

	public ConnectRQ(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	/* 为了规避客户端存在多用户登录统一应用的情况,要求客户端同时提供userId */
	/* 客户端应记录最后一次注册的userId,并将此userId作为参数提交 */
	public String subscriberId;

	@Override
	public String toString() {
		return String.format("ConnectRQ [subscriberId=%s]", subscriberId);
	}

}
