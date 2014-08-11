/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business;

import com.myctu.platform.protocol.MapSerializable;
import com.sirius.upns.protocol.business.msg.APPUnread;
import com.sirius.upns.protocol.business.msg.BatchReceipt;
import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.protocol.business.msg.History;
import com.sirius.upns.protocol.business.msg.Receipt;
import com.sirius.upns.protocol.business.msg.Simple;
import com.sirius.upns.protocol.business.sub.Close;
import com.sirius.upns.protocol.business.sub.ConnectRQ;
import com.sirius.upns.protocol.business.sub.ConnectRS;
import com.sirius.upns.protocol.business.sub.Error;
import com.sirius.upns.protocol.business.sub.Ping;
import com.sirius.upns.protocol.business.sub.RegistRQ;
import com.sirius.upns.protocol.business.sub.RegistRS;

/**
 * @project protocol
 * @date 2013-8-26-下午3:18:01
 * @author pippo
 */
public enum BusinessPacketType {

	/**************************************/

	PING((byte) 1, Ping.class),

	CONNECT_RQ((byte) 2, ConnectRQ.class),

	CONNECT_RS((byte) 3, ConnectRS.class),

	REGIST_RQ((byte) 4, RegistRQ.class),

	REGIST_RS((byte) 5, RegistRS.class),

	MSG_SIMPLE((byte) 7, Simple.class),

	MSG_CUSTOM((byte) 8, Custom.class),

	MSG_RECEIPT((byte) 9, Receipt.class),

	ERROR((byte) 10, Error.class),

	CLOSE((byte) 11, Close.class),

	APPUNREAD((byte) 12, APPUnread.class),

	MSG_BATCH_RECEIPT((byte) 13, BatchReceipt.class),

	HISTORY((byte) 14, History.class);

	private BusinessPacketType(byte code, Class<? extends BusinessPacket> clazz) {
		this.code = code;
		this.clazz = clazz;
	}

	public final byte code;

	public final Class<? extends BusinessPacket> clazz;

	public static BusinessPacketType from(byte code) {
		for (BusinessPacketType type : BusinessPacketType.values()) {
			if (type.code == code) {
				return type;
			}
		}

		throw new IllegalArgumentException(String.format("invalid type code:[%s]", code));
	}

	public static BusinessPacketType from(Class<? extends MapSerializable> clazz) {
		for (BusinessPacketType type : BusinessPacketType.values()) {
			if (type.clazz == clazz) {
				return type;
			}
		}

		throw new IllegalArgumentException(String.format("invalid type clazz:[%s]", clazz));
	}
}
