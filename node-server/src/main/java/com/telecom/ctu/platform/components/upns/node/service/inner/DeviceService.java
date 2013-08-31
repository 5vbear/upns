/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.inner;

import java.util.List;

import com.telecom.ctu.platform.components.upns.node.domain.model.Device;
import com.telecom.ctu.platform.components.upns.protocol.DeviceType;

/**
 * @project node-server
 * @date 2013-8-16-下午1:00:53
 * @author pippo
 */
public interface DeviceService {

	Device regist(String userId, String deviceToken, DeviceType deviceType);

	void deregist(String userId, String deviceToken);

	Device registApp(String userId, String deviceToken, List<Integer> appId);

	void deregistApp(String userId, String deviceToken, List<Integer> appId);

	Device getByToken(String userId, String token);

	List<Device> getByUser(String userId);

}
