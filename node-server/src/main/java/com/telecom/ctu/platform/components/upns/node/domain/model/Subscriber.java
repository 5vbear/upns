/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.domain.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.myctu.platform.protocol.ExtMapUtils;
import com.myctu.platform.protocol.MapSerializable;

/**
 * @project node-server
 * @date 2013-8-18-下午1:52:15
 * @author pippo
 */
public class Subscriber implements MapSerializable {

	private static final long serialVersionUID = -7680114258322009469L;

	public static final String ID = "_id";

	public static final String DEVICES = "d";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		ExtMapUtils.addIfNotNull(out, ID, userId);
		ExtMapUtils.addIfNotNull(out, DEVICES, devices);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		userId = (String) in.get(ID);

		@SuppressWarnings("unchecked") Map<String, Object>[] _devices = (Map<String, Object>[]) in.get(devices);
		if (_devices != null) {
			devices = new ArrayList<>();
			for (Map<String, Object> _device : _devices) {
				Device device = new Device();
				device.fromMap(_device);
				devices.add(device);
			}
		}
	}

	public String userId;

	public List<Device> devices;

	@Override
	public String toString() {
		return String.format("Subscriber [userId=%s, devices=%s]",
			userId,
			devices != null ? Arrays.toString(devices.toArray()) : null);
	}

}
