/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process;

import com.sirius.upns.server.node.NodeServerException;

/**
 * @project node-server
 * @date 2013-8-26-下午4:02:46
 * @author pippo
 */
public class PackageProcessException extends NodeServerException {

	public static final long serialVersionUID = 3199908458501784711L;

	/**
	 * 
	 */
	public PackageProcessException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public PackageProcessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PackageProcessException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public PackageProcessException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public PackageProcessException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
