/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.repository;

import com.sirius.upns.server.console.domain.dto.MessageSearcher;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.repository.SearcherProcessor;

/**
 * @project upns-console
 * @date 2013-10-31-上午11:03:30
 * @author pippo
 */
public interface MessageSearcherProcessor extends SearcherProcessor<MessageSearcher, Message> {

}