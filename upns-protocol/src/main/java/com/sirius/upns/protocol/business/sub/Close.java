/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.sub;

import com.sirius.upns.protocol.business.BusinessPacket;

import java.util.Collections;
import java.util.Map;

/**
 * @project protocol
 * @date 2013-8-31-下午3:42:09
 * @author pippo
 */
public class Close implements BusinessPacket {

	private static final long serialVersionUID = -5544201373115226108L;

	@Override
	public Map<String, Object> toMap() {
		return Collections.emptyMap();
	}

	@Override
	public void fromMap(Map<String, Object> in) {

	}

}
