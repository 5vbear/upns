/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project protocol
 * @date 2013-8-18-下午3:00:19
 * @author pippo
 */
public abstract class MessagePackage implements BusinessPackage {

	private static final long serialVersionUID = -1694701836091890794L;

	public static final String ID = "i";

	public static final String APP_ID = "o";

	public static final String TIME = "e";

	public MessagePackage() {
		this.time = System.currentTimeMillis();
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(ID, id);
		out.put(APP_ID, appId);
		out.put(TIME, time);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.id = (String) in.get(ID);
		this.appId = (Integer) in.get(APP_ID);
		this.time = (Long) in.get(TIME);
	}

	/* 消息uuid */
	public String id;

	/* 那个应用发出 */
	public int appId;

	/* 什么时候发出 */
	public long time;

}