/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node;

/**
 * @project node-server
 * @date 2013-8-26-下午4:03:18
 * @author pippo
 */
public class NodeServerException extends Exception {

	private static final long serialVersionUID = 5446611691265875098L;

	public NodeServerException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NodeServerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NodeServerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NodeServerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NodeServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	private int code = 90000;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
