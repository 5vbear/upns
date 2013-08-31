/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.sub;

import java.util.LinkedHashMap;
import java.util.Map;

import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;

/**
 * @project protocol
 * @date 2013-8-14-下午1:36:53
 * @author pippo
 */
public class Ping implements BusinessPackage {

	private static final long serialVersionUID = 5684716512004926213L;

	public static final String TIME = "t";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(TIME, time);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.time = in.get(TIME) != null ? ((Number) in.get(TIME)).longValue() : 0L;
	}

	public Ping() {
	}

	public Ping(long time) {
		this.time = time;
	}

	public long time;

	@Override
	public String toString() {
		return String.format("Ping [time=%s]", time);
	}

}
