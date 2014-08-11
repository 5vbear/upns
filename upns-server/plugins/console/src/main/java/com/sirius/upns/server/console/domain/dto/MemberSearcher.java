/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.domain.dto;

import com.sirius.upns.server.node.domain.dto.AbstractSearcher;
import com.sirius.upns.server.node.domain.dto.Page;
import com.sirius.upns.server.node.domain.model.GroupMember;

/**
 * @project node-server
 * @date 2013-9-24-下午12:39:20
 * @author pippo
 */
public class MemberSearcher extends AbstractSearcher<GroupMember> {

	@Override
	protected void init() {
		if (example == null) {
			example = new GroupMember();
		}
		example.id = null;
		example.createTime = null;

		if (page == null) {
			page = new Page();
		}
	}

	@Override
	public void reset() {
		init();
		page.first();
	}

	@Override
	public String toString() {
		return String.format("MemberSearcher [example=%s, page=%s]", example, page);
	}

}
