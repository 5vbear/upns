/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.widget.websocket.endpoint;

import org.codehaus.jackson.JsonNode;

import javax.websocket.Session;

/**
 * User: pippo
 * Date: 14-2-25-18:37
 */
public interface JSONMessageHandler {

    void onMessage(JsonNode json, Session session, String userId);

}
