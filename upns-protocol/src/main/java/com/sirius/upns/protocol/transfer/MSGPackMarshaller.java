/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.transfer;

import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.BusinessPacketType;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;
import org.msgpack.unpacker.Unpacker;

import java.io.IOException;

/**
 * @project protocol
 * @date 2013-9-9-下午2:45:59
 * @author pippo
 */
public class MSGPackMarshaller implements PayloadMarshaller {

	public MSGPackMarshaller() {
		messagePack.register(Object.class, new ObjectTemplate());

		for (BusinessPacketType type : BusinessPacketType.values()) {
			messagePack.register(type.clazz);
		}
	}

	public static final MessagePack messagePack = new MessagePack();

	@Override
	public <T extends BusinessPacket> T unmarshal(byte[] content, Class<T> payloadClass) throws Exception {
		return messagePack.read(content, payloadClass);
	}

	@Override
	public byte[] marshal(BusinessPacket payload) throws Exception {
		return messagePack.write(payload);
	}

	public class ObjectTemplate extends AbstractTemplate<Object> {

		private MessagePack pack = new MessagePack();

		@SuppressWarnings("unchecked")
		@Override
		public void write(Packer pk, Object target, boolean required) throws IOException {
			if (target == null) {
				if (required) {
					throw new MessageTypeException("Attempted to write null");
				}
				pk.writeNil();
				return;
			}

			@SuppressWarnings("rawtypes") Template template = pack.lookup(target.getClass());
			if (template != null) {
				template.write(pk, target, required);
			} else {
				throw new MessageTypeException(String.format("convert into unknown type:[%s] is invalid",
					target.getClass()));
			}
		}

		@Override
		public Object read(Unpacker u, Object to, boolean required) throws IOException {
			if (!required && u.trySkipNil()) {
				return null;
			}

			switch (u.getNextType()) {
				case BOOLEAN:
					return Templates.TBoolean.read(u, (Boolean) to, required);
				case INTEGER:
					return Templates.TInteger.read(u, (Integer) to, required);
				case FLOAT:
					return Templates.TFloat.read(u, (Float) to, required);
				case RAW:
					return Templates.TString.read(u, (String) to, required);
				default:
					throw new MessageTypeException("convert into unknown type is invalid");
			}

		}
	}

}
