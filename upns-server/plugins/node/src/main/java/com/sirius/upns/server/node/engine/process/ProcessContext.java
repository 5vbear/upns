/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.engine.process;

import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.route.SubRouter;
import com.sirius.upns.server.node.domain.Subscriber;

/**
 * @project node-server
 * @date 2013-8-30-上午9:35:16
 * @author pippo
 */
public abstract class ProcessContext {

	public abstract Subscriber getSubscriber();

	public abstract SubRouter getSubRouter();

	public abstract void bind(SubRouter router);

    public abstract void close();

	private BusinessPacket rq;

	private BusinessPacket rs;

	public BusinessPacket getRq() {
		return rq;
	}

	public void setRq(BusinessPacket rq) {
		this.rq = rq;
	}

	public BusinessPacket getRs() {
		return rs;
	}

	public void setRs(BusinessPacket rs) {
		this.rs = rs;
	}

	@Override
	public String toString() {
		return String.format("ProcessContext [rq=%s, rs=%s]", rq, rs);
	}

}
