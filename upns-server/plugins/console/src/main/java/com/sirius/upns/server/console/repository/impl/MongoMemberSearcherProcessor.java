/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.repository.impl;

import com.mongodb.QueryBuilder;
import com.sirius.upns.server.console.domain.dto.MemberSearcher;
import com.sirius.upns.server.console.repository.MemberSearcherProcessor;
import com.sirius.upns.server.node.domain.dto.Page;
import com.sirius.upns.server.node.domain.dto.Pagination;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.repository.impl.MongoMemberRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

/**
 * @project upns-console
 * @date 2013-10-31-上午9:56:46
 * @author pippo
 */
@Repository
public class MongoMemberSearcherProcessor extends MongoMemberRepository implements MemberSearcherProcessor {

	@Override
	public Pagination<GroupMember> process(MemberSearcher searcher) {
		QueryBuilder builder = QueryBuilder.start();

		if (StringUtils.isNotBlank(searcher.getExample().getGroupId())) {
			builder.and(GroupMember.GROUP_ID).is(searcher.getExample().getGroupId());
		}

		if (StringUtils.isNotBlank(searcher.getExample().getUserId())) {
			builder.and(GroupMember.USER_ID).is(searcher.getExample().getUserId());
		}

		String query = builder.get().toString();
		Page page = searcher.getPage();
		page.count = (int) members.count(query);
		if (page.count == 0) {
			return Pagination.empty();
		}

		@SuppressWarnings("rawtypes") Iterable<Map> iterable = members.find(query)
			.skip(page.start)
			.limit(page.limit)
			.as(Map.class);

		Pagination<GroupMember> pagination = new Pagination<>(new ArrayList<GroupMember>(page.limit), page);
		for (Map<String, Object> _in : iterable) {
			GroupMember member = new GroupMember();
			member.fromMap(_in);
			pagination.items.add(member);
		}

		return pagination;
	}

}
