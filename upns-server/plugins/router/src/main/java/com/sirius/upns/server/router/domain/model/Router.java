package com.sirius.upns.server.router.domain.model;

/**
 * Created by pippo on 14-3-22.
 */
public class Router {

    public Router(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public final String host;

    public final int port;
}
