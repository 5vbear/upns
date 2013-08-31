/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.domain.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.myctu.platform.protocol.MapSerializable;
import com.myctu.platform.utils.MapUtils;

/**
 * @project node-server
 * @date 2013-8-31-下午1:30:38
 * @author pippo
 */
public class MessageTrace implements MapSerializable {

	private static final long serialVersionUID = -5146153008327217713L;

	public static final String MESSAGE_ID = "m";

	public static final String USER_ID = "u";

	public static final String TIME = "t";

	public static final String STATUS = "s";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<>();
		MapUtils.addIfNotNull(out, MESSAGE_ID, messageId);
		MapUtils.addIfNotNull(out, USER_ID, userId);
		MapUtils.addIfNotNull(out, TIME, time);
		MapUtils.addIfNotNull(out, STATUS, status.code);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		messageId = (String) in.get(MESSAGE_ID);
		userId = (String) in.get(USER_ID);
		time = in.get(TIME) != null ? ((Number) in.get(TIME)).longValue() : 0L;
		status = Status.from(in.get(STATUS) != null ? ((Number) in.get(STATUS)).intValue() : Status.pub.code);
	}

	public String messageId;

	public String userId;

	public long time;

	public Status status;

	@Override
	public String toString() {
		return String.format("MessageTrace [messageId=%s, userId=%s, time=%s, status=%s]",
			messageId,
			userId,
			time,
			status);
	}

	public static enum Status {

		pub(1),

		ack(2),

		deleted(3);

		private Status(int code) {
			this.code = code;
		}

		public final int code;

		public static Status from(int code) {
			for (Status status : Status.values()) {
				if (status.code == code) {
					return status;
				}
			}

			return pub;
		}
	}

}
