/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository.impl;

import com.mongodb.BasicDBObject;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.server.node.domain.model.Device;
import com.sirius.upns.server.node.repository.DeviceClosure;
import com.sirius.upns.server.node.repository.DeviceRepository;
import com.sirius.upns.server.node.repository.JongoConstants;
import org.jongo.MongoCollection;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-9-上午12:06:46
 */
public class MongoDeviceRepository implements DeviceRepository, JongoConstants {

	@Resource(name = "ctu.upns.mongo.db.devices")
	private MongoCollection devices;

	@Override
	public Device regist(String userId, String deviceToken, DeviceType deviceType) {
		Device device = this.getByToken(userId, deviceToken);
		if (device != null) {
			return device;
		}

		device = new Device(userId, deviceToken, deviceType);
		devices.insert(new BasicDBObject(device.toMap()));
		return device;
	}

	@Override
	public void deregist(String userId, String deviceToken) {
		devices.remove(queryByUserAndToken(), userId, deviceToken);
	}

	@Override
	public Device registApp(String userId, String deviceToken, List<Integer> appId) {
		@SuppressWarnings("unchecked")
		Map<String, Object> m = devices.findAndModify(queryByUserAndToken(),
				userId,
				deviceToken)
				.with(String.format("{'$addToSet':{%s:{$each:#}}}", Device.APPS), appId)
				.returnNew()
				.as(Map.class);

		Device device = new Device();
		device.fromMap(m);
		return device;
	}

	@Override
	public void deregistApp(String userId, String deviceToken, List<Integer> appId) {
		devices.update(queryByUserAndToken(), userId, deviceToken)
				.with(String.format("{$pullAll:{%s:#}}", Device.APPS), appId);
	}

	@Override
	public Device getByToken(String userId, String token) {
		@SuppressWarnings("unchecked")
		Map<String, Object> m = devices.findOne(queryByUserAndToken(), userId, token).as(Map.class);

		if (m == null) {
			return null;
		}

		Device device = new Device();
		device.fromMap(m);
		return device;
	}

	@Override
	public List<Device> getByType(String userId, DeviceType deviceType) {
		List<Device> _devices = new ArrayList<>();

		@SuppressWarnings("rawtypes")
		Iterable<Map> iterator = devices.find(queryByUserAndType(),
				userId,
				deviceType.code).as(Map.class);
		for (Map<String, Object> _d : iterator) {
			Device device = new Device();
			device.fromMap(_d);
			_devices.add(device);
		}

		return _devices;
	}

	@Override
	public List<Device> getByUser(String userId) {
		List<Device> _devices = new ArrayList<>();

		@SuppressWarnings("rawtypes")
		Iterable<Map> iterator = devices.find(String.format("{%s:#}", Device.USER_ID),
				userId).as(Map.class);
		for (Map<String, Object> _d : iterator) {
			Device device = new Device();
			device.fromMap(_d);
			_devices.add(device);
		}

		return _devices;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void iterateDevices(DeviceType deviceType, DeviceClosure closure) {
		Iterable<Map> iterable = devices.find(queryByType(), deviceType.code)
				.as(Map.class);

		for (Map in : iterable) {
			Device device = new Device();
			device.fromMap(in);
			closure.execute(device);
		}
	}

	@Override
	public void updateLastActive(String deviceId) {
		devices.update(QUERY_BY_ID, deviceId).with(String.format("{$set:{%s:#}}", Device.LAST_ACTIVE_TIME),
				System.currentTimeMillis());
	}

	private String queryByUserAndToken() {
		return String.format("{%s:#, %s:#}", Device.USER_ID, Device.TOKEN);
	}

	private String queryByUserAndType() {
		return String.format("{%s:#, %s:#}", Device.USER_ID, Device.TYPE);
	}

	private String queryByType() {
		return String.format("{%s:#}", Device.TYPE);
	}
}
