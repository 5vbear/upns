/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.widget.websocket.endpoint;

import javax.websocket.Session;

/**
 * User: pippo
 * Date: 14-2-25-18:36
 */
public interface TextMessageHandler {

    void onMessage(String message, Session session, String user);

}
