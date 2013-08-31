/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.inner.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;
import com.telecom.ctu.platform.components.upns.node.domain.model.Device;
import com.telecom.ctu.platform.components.upns.node.service.inner.DeviceService;
import com.telecom.ctu.platform.components.upns.protocol.DeviceType;

/**
 * @project node-server
 * @date 2013-8-29-下午2:49:05
 * @author pippo
 */
public class MemDeviceService implements DeviceService {

	private Collection<Device> devices = Lists.newCopyOnWriteArrayList();

	@Override
	public Device regist(String userId, String deviceToken, DeviceType deviceType) {
		Device device = this.getByToken(userId, deviceToken);

		if (device == null) {
			device = new Device(userId, deviceToken, deviceType);
			devices.add(device);
		}

		return device;
	}

	@Override
	public void deregist(String userId, String deviceToken) {
		Device device = this.getByToken(userId, deviceToken);

		if (device != null) {
			devices.remove(device);
		}
	}

	@Override
	public Device registApp(String userId, String deviceToken, List<Integer> appId) {
		Device device = this.getByToken(userId, deviceToken);
		Validate.notNull(device, "can not find device with userId:[%s] and token:[%s]", userId, deviceToken);
		device.addApp(appId.toArray(new Integer[appId.size()]));
		return device;
	}

	@Override
	public void deregistApp(String userId, String deviceToken, List<Integer> appId) {
		Device device = this.getByToken(userId, deviceToken);
		Validate.notNull(device, "can not find device with userId:[%s] and token:[%s]", userId, deviceToken);
		device.removeApp(appId.toArray(new Integer[appId.size()]));
	}

	@Override
	public Device getByToken(final String userId, final String token) {
		Device device = (Device) CollectionUtils.find(devices, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				Device device = (Device) object;
				return device != null && device.userId.equals(userId) && device.token.equals(token);
			}
		});

		return device;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getByUser(final String userId) {
		return (List<Device>) CollectionUtils.collect(devices, new Transformer() {

			@Override
			public Object transform(Object input) {
				Device device = (Device) input;
				if (device.userId.equals(userId)) {
					return device;
				} else {
					return null;
				}
			}
		});
	}

}
