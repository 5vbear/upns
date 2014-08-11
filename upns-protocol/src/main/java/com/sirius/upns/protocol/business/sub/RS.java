/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.sub;

import com.sirius.upns.protocol.business.BusinessPacket;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project protocol
 * @date 2013-8-26-下午1:35:42
 * @author pippo
 */
public class RS implements BusinessPacket {

	private static final long serialVersionUID = -469165279322319440L;

	public static final String SUCCESS = "s";

	public static final String ERROR_CODE = "e";

	public RS() {

	}

	public RS(boolean success) {
		this.success = success;
	}

	public RS(boolean success, Integer error_code) {
		this.success = success;
		this.error_code = error_code;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(SUCCESS, success);
		/* 失败才需要error_code */
		if (!success) {
			out.put(ERROR_CODE, error_code);
		}
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		success = (Boolean) in.get(SUCCESS);
		if (!success) {
			error_code = (Integer) in.get(ERROR_CODE);
		}
	}

	public boolean success;

	public int error_code;

}