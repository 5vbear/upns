/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business;

/**
 * @project protocol
 * @date 2013-9-9-下午2:24:35
 * @author pippo
 */
public enum ErrorSpecification {

	INNER(90000),

	ROUTER_INVALID(90001)

	;

	private ErrorSpecification(int code) {
		this.code = code;
	}

	public final int code;

}
