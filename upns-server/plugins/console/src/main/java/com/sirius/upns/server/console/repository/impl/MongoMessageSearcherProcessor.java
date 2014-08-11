/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.repository.impl;

import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.sirius.upns.server.console.domain.dto.MessageSearcher;
import com.sirius.upns.server.console.repository.MessageSearcherProcessor;
import com.sirius.upns.server.node.domain.dto.Page;
import com.sirius.upns.server.node.domain.dto.Pagination;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.repository.impl.MongoMessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @project upns-console
 * @date 2013-10-31-上午9:56:46
 * @author pippo
 */
@Repository
public class MongoMessageSearcherProcessor extends MongoMessageRepository implements MessageSearcherProcessor {

	@Override
	public Pagination<Message> process(MessageSearcher searcher) {
		QueryBuilder builder = QueryBuilder.start();
		if (StringUtils.isNotBlank(searcher.getExample().getGroupId())) {
			builder.and(Message.GROUP_ID).is(searcher.getExample().getGroupId());
		}

		if (StringUtils.isNotBlank(searcher.getExample().getTitle())) {
			builder.and(Message.TITLE).regex(Pattern.compile(searcher.getExample().getTitle()));
		}
		DBObject query = builder.get();

		Page page = searcher.getPage();
		page.count = (int) messages.getDBCollection().count(query);
		if (page.count == 0) {
			return Pagination.empty();
		}

		@SuppressWarnings("rawtypes") Iterable<Map> iterable = messages.find(query.toString())
			.sort(CREATE_TIME_ORDER_DESC)
			.skip(page.start)
			.limit(page.limit)
			.as(Map.class);

		Pagination<Message> pagination = new Pagination<>(new ArrayList<Message>(page.limit), page);
		for (Map<String, Object> _in : iterable) {
			Message message = new Message();
			message.fromMap(_in);
			pagination.items.add(message);
		}

		return pagination;
	}

}
