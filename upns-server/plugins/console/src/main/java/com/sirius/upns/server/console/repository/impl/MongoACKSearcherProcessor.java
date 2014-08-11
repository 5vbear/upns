/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.repository.impl;

import com.mongodb.QueryBuilder;
import com.sirius.upns.server.console.domain.dto.MessageACKSearcher;
import com.sirius.upns.server.console.repository.ACKSearcherProcessor;
import com.sirius.upns.server.node.domain.dto.Page;
import com.sirius.upns.server.node.domain.dto.Pagination;
import com.sirius.upns.server.node.domain.model.MessageACK;
import com.sirius.upns.server.node.repository.impl.MongoTimelineRepository;
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
public class MongoACKSearcherProcessor extends MongoTimelineRepository implements ACKSearcherProcessor {

	@Override
	public Pagination<MessageACK> process(MessageACKSearcher searcher) {
		QueryBuilder builder = QueryBuilder.start();
		if (StringUtils.isNotBlank(searcher.getExample().getMessageId())) {
			builder.and(MessageACK.MESSAGE_ID).is(searcher.getExample().getMessageId());
		}

		if (StringUtils.isNotBlank(searcher.getExample().getUserId())) {
			builder.and(MessageACK.USER_ID).is(searcher.getExample().getUserId());
		}

		String query = builder.get().toString();

		Page page = searcher.getPage();
		page.count = (int) acks.count(query);
		if (page.count == 0) {
			return Pagination.empty();
		}

		@SuppressWarnings("rawtypes") Iterable<Map> iterable = acks.find(query)
			.sort(CREATE_TIME_ORDER_DESC)
			.skip(page.start)
			.limit(page.limit)
			.as(Map.class);

		Pagination<MessageACK> pagination = new Pagination<>(new ArrayList<MessageACK>(page.limit), page);
		for (Map<String, Object> _in : iterable) {
			MessageACK line = new MessageACK();
			line.fromMap(_in);
			pagination.items.add(line);
		}

		return pagination;
	}

}
