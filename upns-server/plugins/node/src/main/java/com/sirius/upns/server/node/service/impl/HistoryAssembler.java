package com.sirius.upns.server.node.service.impl;

import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.protocol.business.msg.History;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.domain.model.MessageACK;
import com.sirius.upns.server.node.repository.MessageACKClosure;
import com.sirius.upns.server.node.repository.MessageClosure;
import com.sirius.upns.server.node.repository.MessageRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by pippo on 14-6-5.
 */
@Component
public class HistoryAssembler {

	@Resource(name = "ctu.upns.messageRepository")
	private MessageRepository messageRepository;

	public void assemble(History history) {
		/* 组装广播消息,未读的广播添加ack=false的记录 */
		assembleBroadcastMessage(history);
		/* 组装用户消息 */
		assembleUserMessage(history);
	}

	private void assembleBroadcastMessage(History history) {
		Message example = new Message();
		example.braodcast = true;
		example.createTime = history.timestamp;
		messageRepository.iterateMessage(example, history.limit, new BroadcastMessageAssembler(history));
	}

	private void assembleUserMessage(History history) {
		MessageACK example = new MessageACK();
		example.userId = history.userId;
		example.appId = history.appId;
		/*调用的时候是"是否标记为ack",实际记录的是"是否已读",所以要取个反*/
		example.ack = history.unread == null ? null : !history.unread;
		example.createTime = history.timestamp;
		messageRepository.iterateACK(example, history.limit, new UserMessageAssembler(history));
	}

	private Custom getCustom(Message message) {
		Custom custom = new Custom();
		custom.id = message.id;
		custom.appId = message.appId;
		custom.groupId = message.groupId;
		custom.title = message.title;
		custom.content = message.content;
		custom.extension = message.extension;
		custom.time = message.createTime;
		return custom;
	}

	private class BroadcastMessageAssembler implements MessageClosure {

		private BroadcastMessageAssembler(History history) {
			this.history = history;
		}

		private History history;

		@Override
		public void execute(Message message) {
			/*要看看广播消息在不在用户的timeline中*/
			MessageACK ack = messageRepository.getACK(history.userId, message.id);
			/*如果没有就加一条,并且ack=false*/
			if (ack == null) {
				ack = new MessageACK();
				ack.appId = message.appId;
				ack.messageId = message.id;
				ack.createTime = message.createTime;
				ack.ack = false;
				ack.userId = history.userId;
				messageRepository.save(ack);
			}
		}
	}

	private class UserMessageAssembler implements MessageACKClosure {

		public UserMessageAssembler(History history) {
			this.history = history;
		}

		private History history;

		@Override
		public void execute(MessageACK ack) {
			Message message = messageRepository.getMessage(ack.messageId);
			Custom custom = getCustom(message);

			/* 将消息加入history的messages中 */
			history.messages.add(custom);
			/* 用最后一条消息的创建时间更新时间戳 */
			history.timestamp = custom.time;
			/* 标记为已读 */
			if (Boolean.TRUE.equals(history.ack)) {
				messageRepository.ack(ack.id, true);
			}
		}
	}
}
