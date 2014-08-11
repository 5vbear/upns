/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.msg;

import com.sirius.upns.protocol.business.BusinessPacket;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project upns-protocol
 * @date 2013-9-24-下午7:24:22
 * @author pippo
 */
public class APPUnread implements BusinessPacket {

	private static final long serialVersionUID = 6024955821293552776L;

	public static final String APP_ID = "a";

	public static final String UNREAD = "u";

	public static final String LAST_UNREAD = "l";

	public static final String LAST_TITLE = "t";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(APP_ID, appId);
		out.put(UNREAD, unread);
		out.put(LAST_UNREAD, lastUnread);
		out.put(LAST_TITLE, lastTitle);
		return out;
	}

	@Override
	public void fromMap(Map<String, Object> in) {
		this.appId = in.get(APP_ID) != null ? ((Number) in.get(APP_ID)).intValue() : -1;
		this.unread = in.get(UNREAD) != null ? ((Number) in.get(UNREAD)).intValue() : 0;
		this.lastUnread = in.get(LAST_UNREAD) != null ? ((Number) in.get(LAST_UNREAD)).longValue() : 0L;
		this.lastTitle = (String) in.get(LAST_TITLE);
	}

	public int appId;

	public int unread;

	/* 最后一条记录的创建时间 */
	public long lastUnread;

	/* 最后一条记录的标题 */
	public String lastTitle;

	@Override
	public String toString() {
		return String.format("APPUnread [appId=%s, unread=%s, lastUnread=%s, lastTitle=%s]",
			appId,
			unread,
			lastUnread,
			lastTitle);
	}

}
