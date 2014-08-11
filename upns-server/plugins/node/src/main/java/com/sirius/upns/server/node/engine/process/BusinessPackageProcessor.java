/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process;

import com.sirius.upns.protocol.business.BusinessPacket;

/**
 * @project node-server
 * @date 2013-8-26-下午4:01:04
 * @author pippo
 */
public interface BusinessPackageProcessor<RQ extends BusinessPacket> {

	Class<RQ> supportRQ();

	void process(ProcessContext context) throws PackageProcessException;

}
