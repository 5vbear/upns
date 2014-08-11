/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.domain.dto;

import com.sirius.upns.server.node.domain.dto.AbstractSearcher;
import com.sirius.upns.server.node.domain.dto.Page;
import com.sirius.upns.server.node.domain.model.Message;

/**
 * @project node-server
 * @date 2013-9-23-下午7:13:47
 * @author pippo
 */
public class MessageSearcher extends AbstractSearcher<Message> {

	@Override
	protected void init() {
		if (example == null) {
			example = new Message();
		}

		example.id = null;
		example.createTime = null;
		example.braodcast = null;

		if (page == null) {
			page = new Page();
		}
	}

	@Override
	public void reset() {
		init();
		page.first();
	}

}
