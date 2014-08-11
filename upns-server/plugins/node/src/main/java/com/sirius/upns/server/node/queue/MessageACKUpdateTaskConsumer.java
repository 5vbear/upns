/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.queue;

import com.myctu.platform.gateway.agent.queue.Consumer;
import com.myctu.platform.gateway.agent.queue.QueueException;
import com.sirius.upns.protocol.KeyHelper;
import com.sirius.upns.server.node.domain.model.MessageACK;
import com.sirius.upns.server.node.repository.TimelineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-9-下午12:16:59
 */
@Service("ctu.upns.queue.message.ack.consumer")
public class MessageACKUpdateTaskConsumer implements Consumer<MessageACKUpdateTask> {

    private static final Logger logger = LoggerFactory.getLogger(MessageACKUpdateTaskConsumer.class);

    @Resource(name = "ctu.upns.messageRepository")
    private TimelineRepository timelineRepository;

    @Override
    public Class<MessageACKUpdateTask> getEventType() {
        return MessageACKUpdateTask.class;
    }

    @Override
    public void onEvent(MessageACKUpdateTask event) throws QueueException {
        logger.debug("consume ack update event:[{}]", event);

        if (!event.ack) {
            onSended(event);
        } else {
            onRecieved(event);
        }
    }

    private void onSended(MessageACKUpdateTask event) {
        MessageACK ack = new MessageACK();
        ack.appId = event.message.appId;
        ack.messageId = event.message.id;
        ack.createTime = event.message.createTime;
        ack.ack = false;
        ack.userId = event.userId;

        /* 保存未ack的标记 */
        timelineRepository.save(ack);
        logger.debug("save message ack:[{}]", ack);

        /* 修改用户未读消息的最后时间戳 */
        timelineRepository.checkTimeline(ack.userId, ack.appId, ack.createTime);
        logger.debug("update user:[{}] timeline to:[{}]", ack.userId, ack.createTime);
    }

    private void onRecieved(MessageACKUpdateTask event) {
        /* 广播消息没有为用户存timeline,在ack的时候创建一条 */
        if (KeyHelper.GROUP_ID_BROADCOST.equals(event.message.groupId)) {
            MessageACK ack = new MessageACK();
            ack.appId = event.message.appId;
            ack.messageId = event.message.id;
            ack.createTime = event.message.createTime;
            ack.ack = true;
            ack.ackTime = event.getPublish_time();
            ack.userId = event.userId;
            timelineRepository.save(ack);
        }

		/* 组播消息直接修改ack状态 */
        timelineRepository.ack(event.userId, event.message.id, true);

        /* 修改用户timeline最后一条ack的时间 */
        timelineRepository.updateTimeline(event.userId, event.message.appId, event.getPublish_time());
    }
}
