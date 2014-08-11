/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node;

/**
 * @project node-server
 * @date 2013-8-26-下午4:04:03
 * @author pippo
 */
public class NodeServerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6972678302915401580L;

	public NodeServerRuntimeException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NodeServerRuntimeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NodeServerRuntimeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NodeServerRuntimeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NodeServerRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
