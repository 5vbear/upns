/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.transfer;

/**
 * @project protocol
 * @date 2013-9-14-下午3:15:32
 * @author pippo
 */
public enum MarshallerType {

	JSON((byte) 1, new JacksonMarshaller()),

	MSGPack((byte) 2, new MSGPackMarshaller());

	private MarshallerType(byte code, PayloadMarshaller marshaller) {
		this.code = code;
		this.marshaller = marshaller;
	}

	public final byte code;

	public final PayloadMarshaller marshaller;

	public static MarshallerType from(byte code) {
		switch (code) {
			case (byte) 1:
				return JSON;
			case (byte) 2:
				return MSGPack;
			default:
				return JSON;
		}
	}

}
