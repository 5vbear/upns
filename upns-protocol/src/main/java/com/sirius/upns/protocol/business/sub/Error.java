/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.sub;

import com.sirius.upns.protocol.business.BusinessPacket;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project protocol
 * @date 2013-9-8-下午6:01:24
 * @author pippo
 */
public class Error implements BusinessPacket {

	private static final long serialVersionUID = -1259380797243302018L;

	public static final String CODE = "c";

	public static final String DESCRIPTION = "d";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(CODE, code);
		out.put(DESCRIPTION, description);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		code = (Integer) in.get(CODE);
		description = (String) in.get(DESCRIPTION);
	}

	public Integer code;

	public String description;

	@Override
	public String toString() {
		return String.format("Error [code=%s, description=%s]", code, description);
	}

}
