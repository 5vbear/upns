/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.widget.websocket.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: pippo
 * Date: 14-2-25-18:58
 */
public class MessageHandlerRegistry {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerRegistry.class);

    public void setTextMessageHandler(TextMessageHandler textMessageHandler) {
        logger.info("regist websocket text message handler:[{}]", textMessageHandler);
        MultiplexEndpoint.setTextMessageHandler(textMessageHandler);
    }

    public void setJsonMessageHandler(JSONMessageHandler jsonMessageHandler) {
        logger.info("regist websocket json message handler:[{}]", jsonMessageHandler);
        MultiplexEndpoint.setJsonMessageHandler(jsonMessageHandler);
    }

}
