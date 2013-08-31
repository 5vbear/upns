/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.engine;

import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;

/**
 * @project node-server
 * @date 2013-8-26-下午4:01:04
 * @author pippo
 */
public interface BusinessPackageProcessor<RQ extends BusinessPackage> {

	Class<RQ> supportRQ();

	void process(ProcessContext context) throws PackageProcessException;

}
