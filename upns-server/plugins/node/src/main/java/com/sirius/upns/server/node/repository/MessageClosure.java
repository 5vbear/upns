package com.sirius.upns.server.node.repository;

import com.sirius.upns.server.node.domain.model.Message;

/**
 * Created by pippo on 14-6-5.
 */
public interface MessageClosure {

	void execute(Message message);

}
