/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.router.service.impl;

import com.sirius.upns.protocol.KeyHelper;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.protocol.business.route.SubApply;
import com.sirius.upns.server.router.domain.model.Router;
import com.sirius.upns.server.router.repository.RouterRepository;
import com.sirius.upns.server.router.service.SubscriberRouterService;
import com.telecom.ctu.platform.common.cache.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @project node-server
 * @date 2013-9-8-下午4:43:42
 * @author pippo
 */
@Service
public class SubscriberRouterServiceImpl implements SubscriberRouterService {

	@Resource(name = "ctu.application.cacheService")
	private CacheService cacheService;

	@Resource
	private RouterRepository routerRepository;

	@Override
	public SubApply apply(String userId, String deviceToken, DeviceType deviceType) {
		SubApply apply = new SubApply(userId, deviceToken, deviceType);

		Router server = routerRepository.select();
		apply.host = server.host;
		apply.port = server.port;

		cacheService.put(KeyHelper.apply(apply.id), (int) (SubApply._EXPIRE / 1000), apply);
		return apply;
	}

}
