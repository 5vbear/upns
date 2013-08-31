/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.outer;

import java.util.List;

import com.telecom.ctu.platform.components.upns.protocol.pub.Custom;
import com.telecom.ctu.platform.components.upns.protocol.pub.Simple;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Receipt;

/**
 * @project node-server
 * @date 2013-8-30-下午12:36:48
 * @author pippo
 */
public interface DeliveryBox {

	void publish(Simple message);

	void ack(Receipt receipt);

	void ack(String userId, String messageId);

	List<Custom> history(String userId);

}
