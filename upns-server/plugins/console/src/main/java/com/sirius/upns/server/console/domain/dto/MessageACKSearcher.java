/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.domain.dto;

import com.sirius.upns.server.node.domain.dto.AbstractSearcher;
import com.sirius.upns.server.node.domain.dto.Page;
import com.sirius.upns.server.node.domain.model.MessageACK;

/**
 * @project node-server
 * @date 2013-9-24-上午10:05:12
 * @author pippo
 */
public class MessageACKSearcher extends AbstractSearcher<MessageACK> {

	@Override
	protected void init() {
		if (example == null) {
			example = new MessageACK();
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
		return String.format("MessageACKSearcher [example=%s, page=%s]", example, page);
	}

}
