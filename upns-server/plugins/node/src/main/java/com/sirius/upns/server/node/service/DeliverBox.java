/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service;

import com.sirius.upns.server.node.domain.model.Message;

import java.util.List;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-30-下午12:36:48
 */
public interface DeliverBox {


    void publish(Message message);

    void publish2user(Message message, List<String> userIds);

    void broadcast(Message message);

    void send(Message message, String userId);

    void batchSend(Message message, List<String> userIds);

    void ack(String userId, String messageId);

    void ackAll(String userId, long timestamp);
}
