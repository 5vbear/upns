package com.sirius.upns.server.node.service.impl;

import com.sirius.upns.protocol.business.msg.APPUnread;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.domain.model.Timeline;
import com.sirius.upns.server.node.repository.MessageRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pippo on 14-6-5.
 */
@Component
public class APPUnreadLoader {

	@Resource(name = "ctu.upns.messageRepository")
	private MessageRepository messageRepository;

	public List<APPUnread> load(String userId) {
		Timeline timeline = messageRepository.getTimeline(userId);
		if (timeline == null || timeline.ackTime == null || timeline.ackTime.isEmpty()) {
			return Collections.emptyList();
		}

		List<APPUnread> unreads = new ArrayList<>();
		for (String appId : timeline.ackTime.keySet()) {
			long lastACK = timeline.ackTime.get(appId);
			assemble(appId, userId, lastACK, unreads);
		}

		return unreads;
	}

	private void assemble(String appId, String userId, long lastACK, List<APPUnread> unreads) {

		int count = messageRepository.unread(userId, Integer.valueOf(appId), lastACK);
		if (count == 0) {
			return;
		}

		APPUnread unread = new APPUnread();
		unread.appId = Integer.valueOf(appId);
		unread.unread = count;

		/* 返回最后一条记录的创建时间和标题 */
		Message last = messageRepository.getUserLast(userId, unread.appId);
		if (last != null) {
			unread.lastUnread = last.createTime;
			unread.lastTitle = last.title;
		}

		unreads.add(unread);
	}

}
