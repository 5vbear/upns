/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @project node-server
 * @date 2013-9-25-下午2:25:51
 * @author pippo
 */
public class Timeline extends AbstractEntity {

	private static final long serialVersionUID = 5339368356304472074L;

	public static final String ACKTIME = "k";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = super.toMap();
		out.put(ACKTIME, ackTime);
		return out;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromMap(Map<String, Object> in) {
		super.fromMap(in);
		ackTime = (Map<String, Long>) in.get(ACKTIME);
	}

	/* {appId:lastACKTime} */
	public Map<String, Long> ackTime = new HashMap<String, Long>();

	public String getUserId() {
		return id;
	}

	public void setUserId(String userId) {
		this.id = userId;
	}

	public Map<String, Long> getAckTime() {
		return ackTime;
	}

	public void setAckTime(Map<String, Long> ackTime) {
		this.ackTime = ackTime;
	}

	@Override
	public String toString() {
		return String.format("Timeline [userId=%s, ackTime=%s]", id, ackTime);
	}

}
