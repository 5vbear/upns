/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node.queue;

import com.sirius.upns.server.node.domain.model.Message;

/**
 * User: pippo
 * Date: 13-11-28-13:49
 */
public class APNSBroadcastTask extends TimelineUpdateTask {

	private static final long serialVersionUID = 858391282978425312L;

	public APNSBroadcastTask() {

	}

	public APNSBroadcastTask(Message message) {
		super(message);
	}

}
