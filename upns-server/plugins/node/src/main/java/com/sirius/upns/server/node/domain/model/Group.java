/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.model;

import java.util.Map;

/**
 * @author pippo
 */
public class Group extends AbstractEntity {

	private static final long serialVersionUID = 5187862676586024671L;

	public static final String NAME = "n";

	public static final String APP_ID = "a";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = super.toMap();
		out.put(NAME, name);
		out.put(APP_ID, appId);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		super.fromMap(in);
		this.name = (String) in.get(NAME);
		this.appId = (Integer) in.get(APP_ID);
	}

	/* 组名 */
	public String name;

	/* 属于那个应用 */
	public Integer appId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return String.format("Group [id=%s, createTime=%s, name=%s, appId=%s]", id, createTime, name, appId);
	}

}
