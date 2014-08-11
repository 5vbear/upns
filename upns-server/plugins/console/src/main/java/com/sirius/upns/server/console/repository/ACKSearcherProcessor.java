/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.repository;

import com.sirius.upns.server.console.domain.dto.MessageACKSearcher;
import com.sirius.upns.server.node.domain.model.MessageACK;
import com.sirius.upns.server.node.repository.SearcherProcessor;

/**
 * @project upns-console
 * @date 2013-10-31-上午11:03:30
 * @author pippo
 */
public interface ACKSearcherProcessor extends SearcherProcessor<MessageACKSearcher, MessageACK> {

}