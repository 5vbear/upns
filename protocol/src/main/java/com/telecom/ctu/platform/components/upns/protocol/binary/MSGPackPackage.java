/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.binary;

import java.nio.ByteBuffer;
import java.util.Map;

import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;

import com.myctu.platform.protocol.MapSerializable;
import com.telecom.ctu.platform.components.upns.protocol.BusinessPackageType;
import com.telecom.ctu.platform.components.upns.protocol.TransformPackage;

/**
 * @project protocol
 * @date 2013-8-31-下午4:41:17
 * @author pippo
 */
public class MSGPackPackage implements TransformPackage {

	public MSGPackPackage(MapSerializable payload) {
		this.type = BusinessPackageType.from(payload.getClass());
		this.payload = payload;
	}

	public MSGPackPackage(byte[] _package) throws Exception {
		type = BusinessPackageType.from(_package[0]);

		MessagePack msgpack = new MessagePack();
		Value dynamic = msgpack.read(_package, 1, _package.length - 1);

		//TODO 执行不成功,必须显示的声明所有的template???
		Map<String, Object> in = new Converter(dynamic).read(Map.class);

		payload = type.clazz.newInstance();
		payload.fromMap(in);
	}

	public byte[] toByteArray() throws Exception {
		MessagePack msgpack = new MessagePack();
		byte[] _payload = msgpack.write(payload.toMap());

		ByteBuffer bb = ByteBuffer.allocate(1 + _payload.length);
		bb.put(type.code);
		bb.put(_payload);
		return bb.array();
	}

	public final BusinessPackageType type;

	public final MapSerializable payload;

	@Override
	public String toString() {
		return String.format("JSONPackage [type=%s, payload=%s]", type, payload);
	}

}
