/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.sub;

import java.util.LinkedHashMap;
import java.util.Map;

import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;

/**
 * @project protocol
 * @date 2013-8-26-下午1:35:42
 * @author pippo
 */
public class AbstractRS implements BusinessPackage {

	private static final long serialVersionUID = -469165279322319440L;

	public static final String SUCCESS = "s";

	public static final String ERROR_CODE = "e";

	public AbstractRS() {

	}

	public AbstractRS(boolean success) {
		this.success = success;
	}

	public AbstractRS(boolean success, String error_code) {
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
			error_code = (String) in.get(ERROR_CODE);
		}
	}

	public boolean success;

	public String error_code;

}