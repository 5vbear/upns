/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.sub;

/**
 * @project protocol
 * @date 2013-8-18-下午3:22:20
 * @author pippo
 */
public class ConnectRS extends AbstractRS {

	private static final long serialVersionUID = 1754697275335392015L;

	public ConnectRS() {
		super();
	}

	public ConnectRS(boolean success) {
		super(success);
	}

	@Override
	public String toString() {
		return String.format("ConnectRS [success=%s, error_code=%s]", success, error_code);
	}

}
