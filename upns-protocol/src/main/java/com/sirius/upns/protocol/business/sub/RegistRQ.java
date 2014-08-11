/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.sub;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @project protocol
 * @date 2013-8-18-下午3:05:12
 * @author pippo
 */
public class RegistRQ implements RQ {

	private static final long serialVersionUID = -5150502698747378825L;

	public static final String APP_ID = "a";

	public RegistRQ() {
	}

	public RegistRQ(List<Integer> appId) {
		this.appId = appId;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(APP_ID, appId);
		return out;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromMap(Map<String, Object> in) {
		this.appId = (List<Integer>) in.get(APP_ID);
	}

	public List<Integer> appId;

	@Override
	public String toString() {
		return String.format("RegistRQ [appId=%s]", appId != null ? Arrays.toString(appId.toArray()) : null);
	}
}
