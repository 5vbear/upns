/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.transfer;

import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.BusinessPacketType;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @project protocol
 * @date 2013-9-9-下午3:16:27
 * @author pippo
 */
public class Payload {

	public Payload(MarshallerType marshallerType, BusinessPacket packet) {
		this.marshallerType = marshallerType;
		this.type = BusinessPacketType.from(packet.getClass());
		this.packet = packet;
	}

	public Payload(byte[] content) throws Exception {
		marshallerType = MarshallerType.from(content[0]);
		type = BusinessPacketType.from(content[1]);
		packet = marshallerType.marshaller.unmarshal(Arrays.copyOfRange(content, 2, content.length), type.clazz);
	}

	public byte[] getBytes() throws Exception {
		byte[] _payload = marshallerType.marshaller.marshal(packet);
		ByteBuffer bb = ByteBuffer.allocate(2 + _payload.length);
		bb.put(marshallerType.code);
		bb.put(type.code);
		bb.put(_payload);
		return bb.array();
	}

	public final MarshallerType marshallerType;

	public final BusinessPacketType type;

	public final BusinessPacket packet;

	@Override
	public String toString() {
		return String.format("Payload [marshallerType=%s, type=%s, packet=%s]", marshallerType, type, packet);
	}

}
