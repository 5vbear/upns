/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.domain.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.myctu.platform.protocol.MapSerializable;

/**
 * @project protocol
 * @date 2013-8-14-下午2:00:12
 * @author pippo
 */
public class MessageHeader implements MapSerializable {

	private static final long serialVersionUID = -5803741304650016459L;

	public static final String ID = "_id";

	public static final String GROUP_ID = "g";

	public static final String APP_ID = "a";

	public static final String CREATE_TIME = "c";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(ID, id);
		out.put(APP_ID, appId);
		out.put(GROUP_ID, groupId);
		out.put(CREATE_TIME, createTime);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.id = (String) in.get(ID);
		this.appId = in.get(APP_ID) != null ? ((Number) in.get(APP_ID)).intValue() : -1;
		this.groupId = (String) in.get(GROUP_ID);
		this.createTime = in.get(CREATE_TIME) != null ? ((Number) in.get(CREATE_TIME)).longValue() : 0L;
	}

	/* 消息uuid */
	public String id;

	/* 那个应用发出 */
	public int appId;

	/* 发到那个组 */
	public String groupId;

	/* 什么时候发出 */
	public long createTime;

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
		MessageHeader other = (MessageHeader) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("MessageHeader [id=%s, appId=%s, groupId=%s, createTime=%s]",
			id,
			appId,
			groupId,
			createTime);
	}

}
