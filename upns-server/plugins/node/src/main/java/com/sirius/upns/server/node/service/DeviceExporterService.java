/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */
package com.sirius.upns.server.node.service;

import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.server.node.domain.model.Device;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-12-下午12:11:37
 */
public interface DeviceExporterService {

    Device regist(String userId, String deviceToken, DeviceType deviceType);

    void deregist(String userId, String deviceToken);

}
