/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business;

/**
 * @project node-server
 * @date 2013-8-18-下午1:59:54
 * @author pippo
 */
public enum DeviceType {

	web(0),

	android(1),

	ios(2);

	private DeviceType(int code) {
		this.code = code;
	}

	public final int code;

	public static DeviceType from(int code) {
		for (DeviceType deviceType : DeviceType.values()) {
			if (code == deviceType.code) {
				return deviceType;
			}
		}

		return web;
	}

	public static DeviceType from(String name) {
		for (DeviceType deviceType : DeviceType.values()) {
			if (name.equals(deviceType.name())) {
				return deviceType;
			}
		}

		return web;
	}
}
