/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.pub;

import java.util.Map;

/**
 * @project protocol
 * @date 2013-8-18-下午1:39:12
 * @author pippo
 */
public class Custom extends Simple {

	private static final long serialVersionUID = 8524455060281484014L;

	public static final String EXTENSION = "x";

	public Custom() {
		super();
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = super.toMap();
		if (extension != null && !extension.isEmpty()) {
			out.put(EXTENSION, extension);
		}

		return out;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromMap(Map<String, Object> in) {
		super.fromMap(in);
		this.extension = (Map<String, Object>) in.get(EXTENSION);
	}

	public Map<String, Object> extension;

	@Override
	public String toString() {
		return String.format("Custom [extension=%s, groupId=%s, title=%s, content=%s, id=%s, appId=%s, time=%s]",
			extension,
			groupId,
			title,
			content,
			id,
			appId,
			time);
	}

}
