/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.sub;

import java.util.Collections;
import java.util.Map;

import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;

/**
 * @project protocol
 * @date 2013-8-31-下午3:42:09
 * @author pippo
 */
public class Close implements BusinessPackage {

	private static final long serialVersionUID = -5544201373115226108L;

	@Override
	public Map<String, Object> toMap() {
		return Collections.emptyMap();
	}

	@Override
	public void fromMap(Map<String, Object> in) {

	}

}
