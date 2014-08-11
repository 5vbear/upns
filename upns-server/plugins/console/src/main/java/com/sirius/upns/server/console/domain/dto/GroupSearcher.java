/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.domain.dto;

import com.sirius.upns.server.node.domain.dto.AbstractSearcher;
import com.sirius.upns.server.node.domain.dto.Page;
import com.sirius.upns.server.node.domain.model.Group;
import org.apache.commons.lang3.StringUtils;

/**
 * @project node-server
 * @date 2013-9-23-下午12:48:03
 * @author pippo
 */
public class GroupSearcher extends AbstractSearcher<Group> {

	@Override
	protected StringBuilder urlAppender() {
		StringBuilder appender = super.urlAppender();

		if (StringUtils.isNotBlank(userId)) {
			appender.append("userId=").append(userId).append("&");
		}

		return appender;
	}

	@Override
	protected void init() {
		example = new Group();
		example.id = null;
		example.createTime = null;

		page = page == null ? new Page() : page.first();
	}

	@Override
	protected void reset() {
		init();
		userId = null;
	}

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return String.format("GroupSearcher [userId=%s, example=%s, page=%s]", userId, example, page);
	}

}
