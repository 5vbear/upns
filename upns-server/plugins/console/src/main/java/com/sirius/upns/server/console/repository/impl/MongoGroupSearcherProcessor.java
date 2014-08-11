/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.repository.impl;

import com.sirius.upns.server.console.domain.dto.GroupSearcher;
import com.sirius.upns.server.console.repository.GroupSearcherProcessor;
import com.sirius.upns.server.node.domain.dto.Page;
import com.sirius.upns.server.node.domain.dto.Pagination;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.repository.impl.MongoGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

/**
 * @project upns-console
 * @date 2013-10-31-上午9:56:46
 * @author pippo
 */
@Repository
public class MongoGroupSearcherProcessor extends MongoGroupRepository implements GroupSearcherProcessor {

	@Override
	public Pagination<Group> process(GroupSearcher searcher) {
		Page page = searcher.getPage();
		page.count = (int) groups.count();
		if (page.count == 0) {
			return Pagination.empty();
		}

		@SuppressWarnings("rawtypes") Iterable<Map> iterable = groups.find()
			.skip(page.start)
			.limit(page.limit)
			.as(Map.class);

		Pagination<Group> pagination = new Pagination<Group>(new ArrayList<Group>(page.limit), page);
		for (Map<String, Object> _in : iterable) {
			Group group = new Group();
			group.fromMap(_in);
			pagination.items.add(group);
		}

		return pagination;
	}

}
