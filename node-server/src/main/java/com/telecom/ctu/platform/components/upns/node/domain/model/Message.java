/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.domain.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.myctu.platform.protocol.MapSerializable;

/**
 * @project protocol
 * @date 2013-8-14-下午1:52:32
 * @author pippo
 */
public class Message implements MapSerializable {

	private static final long serialVersionUID = -3126481420358002993L;

	public static final String ID = "_id";

	public static final String HEADER = "h";

	public static final String TITLE = "t";

	public static final String CONTENT = "c";

	public static final String EXTENSION = "e";

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(ID, header.id);
		out.put(HEADER, header);

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
		this.header = new MessageHeader();
		this.header.fromMap(in);

		this.title = (String) in.get(TITLE);
		this.content = (String) in.get(CONTENT);
		this.extension = (Map<String, Object>) in.get(EXTENSION);
	}

	public MessageHeader header;

	public String title;

	public String content;

	public Map<String, Object> extension;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Message [header=%s, title=%s, content=%s, extension=%s]",
			header,
			title,
			content,
			extension);
	}

}
