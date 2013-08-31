/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.engine;

import com.telecom.ctu.platform.components.upns.node.domain.Subscriber;
import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubRouter;

/**
 * @project node-server
 * @date 2013-8-30-上午9:35:16
 * @author pippo
 */
public abstract class ProcessContext {

	public abstract Subscriber getSubscriber();

	public abstract SubRouter getSubRouter();

	public abstract void bind(SubRouter router);

	private BusinessPackage rq;

	private BusinessPackage rs;

	public BusinessPackage getRq() {
		return rq;
	}

	public void setRq(BusinessPackage rq) {
		this.rq = rq;
	}

	public BusinessPackage getRs() {
		return rs;
	}

	public void setRs(BusinessPackage rs) {
		this.rs = rs;
	}

	@Override
	public String toString() {
		return String.format("ProcessContext [rq=%s, rs=%s]", rq, rs);
	}

}
