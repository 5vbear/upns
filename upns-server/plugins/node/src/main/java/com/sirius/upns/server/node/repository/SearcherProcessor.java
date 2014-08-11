/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository;

import com.sirius.upns.server.node.domain.dto.Pagination;
import com.sirius.upns.server.node.domain.dto.Searcher;
import com.sirius.upns.server.node.domain.model.AbstractEntity;

/**
 * @project node-server
 * @date 2013-10-31-上午9:13:36
 * @author pippo
 */
public interface SearcherProcessor<S extends Searcher<E>, E extends AbstractEntity> {

	Pagination<E> process(S searcher);

}