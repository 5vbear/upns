/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service.impl;

import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.server.node.domain.model.Device;
import com.sirius.upns.server.node.repository.DeviceRepository;
import com.sirius.upns.server.node.service.DeviceService;
import com.telecom.ctu.platform.framework.engine.service.Exporter;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-12-下午12:12:51
 */
@Service("upns.deviceService")
@Exporter(name = "upns.device", serviceInterface = DeviceService.class)
public class DeviceServiceImpl implements DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Resource
    private DeviceRepository deviceRepository;

    @Override
    public Device regist(String userId, String deviceToken, DeviceType deviceType) {
        Validate.notBlank(userId, "userId can not be null");
        Validate.notBlank(deviceToken, "deviceToken can not be null");
        Validate.notNull(deviceType, "deviceToken can not be null");

        if (deviceType == DeviceType.ios) {
            Validate.isTrue(deviceToken.getBytes().length == 64, "invalid deviceToken:[%s]", deviceToken);
        }

        Device device = deviceRepository.getByToken(userId, deviceToken);

        if (device != null)

        {
            deviceRepository.updateLastActive(device.id);
            return device;
        }

        device = deviceRepository.regist(userId, deviceToken, deviceType);
        logger.debug("regist new device:[{}]", device);

        return device;
    }

    @Override
    public Device registApp(String userId, String deviceToken, List<Integer> appId) {
        Validate.notBlank(userId, "userId can not be null");
        Validate.notBlank(deviceToken, "deviceToken can not be null");

        return deviceRepository.registApp(userId, deviceToken, appId);
    }

    @Override
    public Device get(String userId, String deviceToken) {
        Validate.notBlank(userId, "userId can not be null");
        Validate.notBlank(deviceToken, "deviceToken can not be null");

        return deviceRepository.getByToken(userId, deviceToken);
    }

    @Override
    public void deregist(String userId, String deviceToken) {
        deviceRepository.deregist(userId, deviceToken);
    }
}
