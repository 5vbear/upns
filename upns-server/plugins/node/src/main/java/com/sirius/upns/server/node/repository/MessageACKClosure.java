/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository;

import com.sirius.upns.server.node.domain.model.MessageACK;

/**
 * @project node-server
 * @date 2013-9-25-下午1:39:16
 * @author pippo
 */
public interface MessageACKClosure {

	void execute(MessageACK ack);
}
