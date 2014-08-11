/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.transfer;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @project protocol
 * @date 2013-8-18-上午11:48:54
 * @author pippo
 */
public class TransferPacket implements Serializable {

	private static final long serialVersionUID = 4733297481691463582L;

	public static final int SIZE_LENGTH = 4;

	public static final int SIZE_PACKET = (1 << 16) - 1;

	public TransferPacket(Payload payload) throws Exception {
		this.body = payload.getBytes();
		this.length = body.length + SIZE_LENGTH;
	}

	/* header */
	private final int length;

	/* body */
	private byte[] body;

	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.putInt(length);
		buffer.put(body);
		return buffer.array();
	}

	@Override
	public String toString() {
		return String.format("TransferPacket [length=%s, body=%s]", length, Arrays.toString(body));
	}

}
