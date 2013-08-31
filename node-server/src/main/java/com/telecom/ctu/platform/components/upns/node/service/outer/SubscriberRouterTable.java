/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.outer;

import com.telecom.ctu.platform.components.upns.protocol.DeviceType;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubApply;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubRouter;

/**
 * @project node-server
 * @date 2013-8-30-下午1:46:27
 * @author pippo
 */
public interface SubscriberRouterTable {

	SubApply apply(String userId, String deviceToken, DeviceType deviceType);

	SubRouter validate(String applyId);

	void regist(SubRouter router);

	void deregist(String userId, String deviceToken, DeviceType deviceType);

	void deregist(SubRouter router);

	SubRouter find(String userId, String deviceToken, DeviceType deviceType);

}
