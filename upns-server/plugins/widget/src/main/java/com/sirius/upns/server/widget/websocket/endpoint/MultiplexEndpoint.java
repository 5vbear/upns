/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.widget.websocket.endpoint;

import com.myctu.platform.protocol.transform.json.JacksonSupport;
import com.sirius.upns.server.widget.websocket.SessionManager;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * User: pippo
 * Date: 14-2-24-12:14
 */
@ServerEndpoint(value = "/ctu.web-socket/{user}", subprotocols = {"echo", "text", "json"})
public class MultiplexEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(MultiplexEndpoint.class);

    private static SessionManager sessionManager;

    public static void setSessionManager(SessionManager sessionManager) {
        MultiplexEndpoint.sessionManager = sessionManager;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig, @PathParam("user") String user) {
        sessionManager.onOpen(session, user);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason, @PathParam("user") String user) {
        sessionManager.onClose(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable, @PathParam("user") String user) {
        logger.error("session:[{}] on error:[{}]", session, ExceptionUtils.getStackTrace(throwable));
        sessionManager.onClose(session);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("user") String user) {
        switch (session.getNegotiatedSubprotocol()) {
            case "text":
                getTextMessageHandler().onMessage(message, session, user);
                break;
            case "json":
                try {
                    getJsonMessageHandler().onMessage(JacksonSupport.objectMapper.readTree(message), session, user);
                } catch (Exception e) {
                    logger.error("process message:[{}] due to error:[{}]", message, ExceptionUtils.getStackTrace(e));
                }
                break;
            case "echo":
                session.getAsyncRemote().sendText(String.format("reply from server:[%s]", message));
                break;
        }
    }

    private static TextMessageHandler textMessageHandler;

    private static JSONMessageHandler jsonMessageHandler;

    public static TextMessageHandler getTextMessageHandler() {
        Validate.notNull(textMessageHandler, "textMessageHandler can not be null");
        return textMessageHandler;
    }

    public static void setTextMessageHandler(TextMessageHandler textMessageHandler) {
        MultiplexEndpoint.textMessageHandler = textMessageHandler;
    }

    public static JSONMessageHandler getJsonMessageHandler() {
        Validate.notNull(jsonMessageHandler, "jsonMessageHandler can not be null");
        return jsonMessageHandler;
    }

    public static void setJsonMessageHandler(JSONMessageHandler jsonMessageHandler) {
        MultiplexEndpoint.jsonMessageHandler = jsonMessageHandler;
    }
}
