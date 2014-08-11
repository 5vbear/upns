/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.model;

import java.util.Date;
import java.util.Map;

/**
 * @project protocol
 * @date 2013-8-14-下午1:52:32
 * @author pippo
 */
public class Message extends AbstractEntity {

	private static final long serialVersionUID = -3126481420358002993L;

	/* 消息默认30天后过期 */
	public static final long _default_expire_time = 1000 * 60 * 60 * 24 * 30L;

	public static final String APP_ID = "a";

	public static final String GROUP_ID = "g";

	public static final String BROADCAST = "b";

	public static final String EXPIRE = "p";

	public static final String TTL = "l";

	public static final String TITLE = "t";

	public static final String CONTENT = "c";

	public static final String EXTENSION = "e";

	public Message() {
		super();
		braodcast = false;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = super.toMap();
		out.put(APP_ID, appId);
		out.put(GROUP_ID, groupId);
		out.put(BROADCAST, braodcast);

		/* 默认30天过期 */
		if (expire == null || expire <= 0) {
			expire = System.currentTimeMillis() + _default_expire_time;
		}
		out.put(EXPIRE, expire);

		if (ttl == null) {
			ttl = new Date(expire);
		}
		out.put(TTL, ttl);

		if (title != null && title.length() > 0) {
			out.put(TITLE, title);
		}

		if (content != null && content.length() > 0) {
			out.put(CONTENT, content);
		}

		if (extension != null && extension.size() > 0) {
			out.put(EXTENSION, extension);
		}

		return out;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromMap(Map<String, Object> in) {
		super.fromMap(in);
		this.appId = (Integer) in.get(APP_ID);
		this.groupId = (String) in.get(GROUP_ID);
		this.braodcast = (Boolean) in.get(BROADCAST);
		this.expire = in.containsKey(EXPIRE) ? (Long) in.get(EXPIRE) : System.currentTimeMillis()
				+ _default_expire_time;
		this.title = (String) in.get(TITLE);
		this.content = (String) in.get(CONTENT);
		this.extension = (Map<String, String>) in.get(EXTENSION);
	}

	/* 那个应用发出 */
	public Integer appId;

	/* 发到那个组 */
	public String groupId;

	/* 是否为广播消息 */
	public Boolean braodcast;

	/* 过期时间 */
	public Long expire;

	public Date ttl;

	public String title;

	public String content;

	public Map<String, String> extension;

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Boolean getBraodcast() {
		return braodcast;
	}

	public void setBraodcast(Boolean braodcast) {
		this.braodcast = braodcast;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	public Date getTtl() {
		return ttl;
	}

	public void setTtl(Date ttl) {
		this.ttl = ttl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getExtension() {
		return extension;
	}

	public void setExtension(Map<String, String> extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return String.format("Message [id=%s, createTime=%s, appId=%s, groupId=%s, braodcast=%s, expire=%s, ttl=%s, title=%s, content=%s, extension=%s]",
			id,
			createTime,
			appId,
			groupId,
			braodcast,
			expire,
			ttl,
			title,
			content,
			extension);
	}

}
