/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.router.service;

import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.protocol.business.route.SubApply;

/**
 * @project node-server
 * @date 2013-9-8-下午4:39:33
 * @author pippo
 */
public interface SubscriberRouterService {

	SubApply apply(String userId, String deviceToken, DeviceType deviceType);

}
