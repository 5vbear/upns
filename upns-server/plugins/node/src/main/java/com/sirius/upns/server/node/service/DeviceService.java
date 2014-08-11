/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service;

import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.server.node.domain.model.Device;

import java.util.List;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-12-下午12:11:37
 */
public interface DeviceService extends DeviceExporterService {

    Device regist(String userId, String deviceToken, DeviceType deviceType);

    Device registApp(String userId, String deviceToken, List<Integer> appId);

    Device get(String userId, String deviceToken);

    void deregist(String userId, String deviceToken);

}
