/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.outer.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.telecom.ctu.platform.common.cache.CacheService;
import com.telecom.ctu.platform.components.upns.node.service.outer.SubscriberRouterTable;
import com.telecom.ctu.platform.components.upns.protocol.DeviceType;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubApply;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubRouter;

/**
 * @project node-server
 * @date 2013-8-30-下午2:04:04
 * @author pippo
 */
@Service
public class SubscriberRouterTableImpl implements SubscriberRouterTable {

	@Resource(name = "ctu.upns.cacheService")
	private CacheService cacheService;

	@Override
	public SubApply apply(String userId, String deviceToken, DeviceType deviceType) {
		SubApply apply = new SubApply(userId, deviceToken, deviceType);
		cacheService.put(apply.id, (int) (SubApply._EXPIRE / 1000), apply);
		return apply;
	}

	public SubRouter validate(String id) {
		SubApply apply = cacheService.get(id);

		if (apply == null) {
			return null;
		}

		SubRouter router = SubRouter.from(apply);
		regist(router);

		return router;
	}

	@Override
	public void regist(SubRouter router) {
		cacheService.put(router.id, (int) (SubRouter._EXPIRE / 1000), router);
	}

	@Override
	public void deregist(String userId, String deviceToken, DeviceType deviceType) {
		cacheService.remove(SubRouter.getRouterKey(userId, deviceToken, deviceType));
	}

	@Override
	public void deregist(SubRouter router) {
		this.deregist(router.userId, router.deviceToken, router.deviceType);
	}

	@Override
	public SubRouter find(String userId, String deviceToken, DeviceType deviceType) {
		return (SubRouter) cacheService.get(SubRouter.getRouterKey(userId, deviceToken, deviceType));
	}
}
