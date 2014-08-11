/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.endpoint;

import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.test.unit.endpoint.TCPClient.PacketCallback;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @project node-server
 * @date 2013-9-15-下午3:07:51
 * @author pippo
 */
public class BlockCallback implements PacketCallback {

	public Semaphore received = new Semaphore(0);

	public BusinessPacket packet;

	public void block() throws InterruptedException {
		received.tryAcquire(300, TimeUnit.SECONDS);
	}

	@Override
	public void onPackage(BusinessPacket _package) {
		this.packet = _package;
		received.release();
	}

}
