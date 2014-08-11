/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process;

import com.myctu.platform.protocol.GenericUtils;
import com.sirius.upns.protocol.business.BusinessPacket;

/**
 * @project node-server
 * @date 2013-8-26-下午4:06:05
 * @author pippo
 */
public abstract class AbstractProcessor<RQ extends BusinessPacket> implements BusinessPackageProcessor<RQ> {

	protected Class<RQ> clazz;

	@SuppressWarnings("unchecked")
	@Override
	public Class<RQ> supportRQ() {
		if (clazz == null) {
			clazz = GenericUtils.getSuperClassGenricType(getClass(), 0);
		}
		return clazz;
	}

}
