/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.dto;

import com.sirius.upns.server.node.domain.model.AbstractEntity;
import com.sirius.upns.server.node.repository.SearcherProcessor;

/**
 * @project node-server
 * @date 2013-10-31-上午9:10:50
 * @author pippo
 */
public interface Searcher<E extends AbstractEntity> {

	E getExample();

	Page getPage();

	String getURLAppender();

	String getNextURLAppender();

	String getPreviousURLAppender();

	Pagination<E> search(SearcherProcessor<?, E> processor);

}
