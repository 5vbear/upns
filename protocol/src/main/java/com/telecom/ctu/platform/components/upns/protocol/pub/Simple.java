/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.pub;

import java.util.Map;

import com.telecom.ctu.platform.components.upns.protocol.MessagePackage;

/**
 * @project protocol
 * @date 2013-8-18-下午1:39:12
 * @author pippo
 */
public class Simple extends MessagePackage {

	private static final long serialVersionUID = 2261870989549855713L;

	public static final String GROUP_ID = "g";

	public static final String TITLE = "t";

	public static final String CONTENT = "c";

	public Simple() {
		super();
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = super.toMap();
		out.put(GROUP_ID, groupId);
		out.put(TITLE, title);
		if (content != null && content.length() > 0) {
			out.put(CONTENT, content);
		}
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		super.fromMap(in);
		this.groupId = (String) in.get(GROUP_ID);
		this.title = (String) in.get(TITLE);
		this.content = (String) in.get(CONTENT);
	}

	/* 发到那个组 */
	public String groupId;

	public String title;

	public String content;

	@Override
	public String toString() {
		return String.format("Simple [groupId=%s, title=%s, content=%s, id=%s, appId=%s, time=%s]",
			groupId,
			title,
			content,
			id,
			appId,
			time);
	}

}
