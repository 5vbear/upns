/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service.impl;

import com.sirius.upns.protocol.KeyHelper;
import com.sirius.upns.protocol.business.route.SubApply;
import com.sirius.upns.protocol.business.route.SubRouter;
import com.sirius.upns.server.node.service.SubscriberRouterTable;
import com.telecom.ctu.platform.common.cache.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @project node-server
 * @date 2013-8-30-下午2:04:04
 * @author pippo
 */
@Service
public class SubscriberRouterTableImpl implements SubscriberRouterTable {

	private static final Logger logger = LoggerFactory.getLogger(SubscriberRouterTableImpl.class);

	@Resource(name = "ctu.application.cacheService")
	private CacheService cacheService;

	public SubRouter validate(String applyId) {
		SubApply apply = cacheService.get(KeyHelper.apply(applyId));

		if (apply == null) {
			return null;
		}

		SubRouter router = SubRouter.from(apply);
		regist(router);

		return router;
	}

	@Override
	public void regist(SubRouter router) {
		cacheService.put(KeyHelper.router(router.id), (int) (SubRouter._EXPIRE / 1000), router);
		logger.debug("registed router:[{}] to router table", router);
	}

	@Override
	public void deregist(SubRouter router) {
		if (router == null) {
			return;
		}

		cacheService.remove(KeyHelper.router(router.id));
		logger.debug("deregisted router:[{}] from router table", router);
	}

	@Override
	public SubRouter get(String id) {
		return (SubRouter) cacheService.get(KeyHelper.router(id));
	}

}
