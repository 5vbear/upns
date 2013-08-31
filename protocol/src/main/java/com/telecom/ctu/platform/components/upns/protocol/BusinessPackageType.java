/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol;

import com.myctu.platform.protocol.MapSerializable;
import com.telecom.ctu.platform.components.upns.protocol.sub.Close;
import com.telecom.ctu.platform.components.upns.protocol.sub.ConnectRQ;
import com.telecom.ctu.platform.components.upns.protocol.sub.ConnectRS;
import com.telecom.ctu.platform.components.upns.protocol.sub.Ping;
import com.telecom.ctu.platform.components.upns.protocol.sub.RegistRQ;
import com.telecom.ctu.platform.components.upns.protocol.sub.RegistRS;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Custom;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Receipt;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Simple;

/**
 * @project protocol
 * @date 2013-8-26-下午3:18:01
 * @author pippo
 */
public enum BusinessPackageType {

	PING((byte) 1, Ping.class),

	CONNECT_RQ((byte) 2, ConnectRQ.class),

	CONNECT_RS((byte) 3, ConnectRS.class),

	REGIST_RQ((byte) 4, RegistRQ.class),

	REGIST_RS((byte) 5, RegistRS.class),

	MSG_SIMPLE((byte) 7, Simple.class),

	MSG_CUSTOM((byte) 8, Custom.class),

	MSG_RECEIPT((byte) 9, Receipt.class),

	CLOSE((byte) 10, Close.class);

	private BusinessPackageType(byte code, Class<? extends MapSerializable> clazz) {
		this.code = code;
		this.clazz = clazz;
	}

	public final byte code;

	public final Class<? extends MapSerializable> clazz;

	public static BusinessPackageType from(byte code) {
		for (BusinessPackageType type : BusinessPackageType.values()) {
			if (type.code == code) {
				return type;
			}
		}

		throw new IllegalArgumentException(String.format("invalid type code:[%s]", code));
	}

	public static BusinessPackageType from(Class<? extends MapSerializable> clazz) {
		for (BusinessPackageType type : BusinessPackageType.values()) {
			if (type.clazz == clazz) {
				return type;
			}
		}

		throw new IllegalArgumentException(String.format("invalid type clazz:[%s]", clazz));
	}
}
