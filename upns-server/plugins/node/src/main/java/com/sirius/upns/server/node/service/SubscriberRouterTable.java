/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service;

import com.sirius.upns.protocol.business.route.SubRouter;

/**
 * @project node-server
 * @date 2013-8-30-下午1:46:27
 * @author pippo
 */
public interface SubscriberRouterTable {

	SubRouter validate(String applyId);

	void regist(SubRouter router);

	void deregist(SubRouter router);

	SubRouter get(String id);

}
