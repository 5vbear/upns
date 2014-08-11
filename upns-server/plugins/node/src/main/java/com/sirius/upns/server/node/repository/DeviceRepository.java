/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository;

import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.server.node.domain.model.Device;

import java.util.List;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-16-下午1:00:53
 */
public interface DeviceRepository {

	Device regist(String userId, String deviceToken, DeviceType deviceType);

	void deregist(String userId, String deviceToken);

	Device registApp(String userId, String deviceToken, List<Integer> appId);

	void deregistApp(String userId, String deviceToken, List<Integer> appId);

	Device getByToken(String userId, String token);

	List<Device> getByType(String userId, DeviceType deviceType);

	List<Device> getByUser(String userId);

	void iterateDevices(DeviceType deviceType, DeviceClosure closure);

	void updateLastActive(String deviceId);
}
