/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository;

import com.sirius.upns.server.node.domain.model.AbstractEntity;

/**
 * @project node-server
 * @date 2013-9-9-上午9:40:47
 * @author pippo
 */
public interface JongoConstants {

	String QUERY_BY_ID = "{_id:#}";

	String CREATE_TIME_ORDER_DESC = String.format("{%s:-1}", AbstractEntity.CREATE_TIME);

	String CREATE_TIME_ORDER_ASC = String.format("{%s:1}", AbstractEntity.CREATE_TIME);

}
