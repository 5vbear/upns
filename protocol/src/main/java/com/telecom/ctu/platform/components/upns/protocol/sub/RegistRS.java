/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.sub;

/**
 * @project protocol
 * @date 2013-8-18-下午3:15:32
 * @author pippo
 */
public class RegistRS extends AbstractRS {

	private static final long serialVersionUID = 1754697275335392015L;

	public RegistRS() {
		super();
	}

	public RegistRS(boolean success) {
		super(success);
	}

	@Override
	public String toString() {
		return String.format("RegistRS [success=%s, error_code=%s]", success, error_code);
	}

}
