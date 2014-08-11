package com.sirius.upns.server.node.engine.apns;

import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by pippo on 14-3-22.
 */
public class AppleNotificationServerFactoryBean implements FactoryBean<AppleNotificationServer> {

    private ClassPathResource keystore;

    private String password;

    private String type;

    private String host;

    private int port;

    public void setKeystore(ClassPathResource keystore) {
        this.keystore = keystore;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public AppleNotificationServer getObject() throws Exception {
        return new AppleNotificationServerBasicImpl(keystore.getFile(), password, type, host, port);
    }

    @Override
    public Class<?> getObjectType() {
        return AppleNotificationServer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
