/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.domain.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.myctu.platform.protocol.MapSerializable;

/**
 * @author pippo
 */
public class GroupMember implements MapSerializable {

	private static final long serialVersionUID = -3851297922822564228L;

	public static final String USER_ID = "u";

	public static final String GROUP_ID = "g";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(USER_ID, userId);
		out.put(GROUP_ID, groupId);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.userId = (String) in.get(USER_ID);
		this.groupId = (String) in.get(GROUP_ID);
	}

	public String userId;

	public String groupId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		GroupMember other = (GroupMember) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("GroupMember [userId=%s, groupId=%s]", userId, groupId);
	}

}
