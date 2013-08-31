/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.domain.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.myctu.platform.protocol.MapSerializable;

/**
 * @author pippo
 */
public class Group implements MapSerializable {

	private static final long serialVersionUID = 5187862676586024671L;

	public static final String ID = "_id";

	public static final String NAME = "n";

	public static final String APP_ID = "a";

	public static final String CREATE_TIME = "c";

	public static final String LAST_ACTIVE_TIME = "l";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(ID, id);
		out.put(NAME, name);
		out.put(APP_ID, appId);
		out.put(CREATE_TIME, createTime);
		out.put(LAST_ACTIVE_TIME, lastActiveTime);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.id = (String) in.get(ID);
		this.name = (String) in.get(NAME);
		this.appId = in.get(APP_ID) != null ? ((Number) in.get(APP_ID)).intValue() : -1;
		this.createTime = in.get(CREATE_TIME) != null ? ((Number) in.get(CREATE_TIME)).longValue() : 0L;
		this.lastActiveTime = in.get(LAST_ACTIVE_TIME) != null ? ((Number) in.get(LAST_ACTIVE_TIME)).longValue() : 0L;
	}

	/* uuid */
	public String id;

	/* 组名 */
	public String name;

	/* 属于那个应用 */
	public int appId;

	/* 创建时间 */
	public long createTime;

	/* 最后活跃时间 */
	public long lastActiveTime;

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
		Group other = (Group) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Group [id=%s, name=%s, appId=%s, createTime=%s, lastActiveTime=%s]",
			id,
			name,
			appId,
			createTime,
			lastActiveTime);
	}

}
