/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node.engine.apns;

import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: pippo
 * Date: 13-11-23-18:05
 */
public class PushNotificationManagerPool extends GenericObjectPool<PushNotificationManager> {

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationManagerPool.class);

    public PushNotificationManagerPool(
            Config config, AppleNotificationServer appleNotificationServer) {
        super(new PushNotificationManagerFactory(appleNotificationServer), config);
    }


    @Override
    public PushNotificationManager borrowObject() {
        try {
            return super.borrowObject();
        } catch (Exception e) {
            logger.error("borrow object from pool due to error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnObject(PushNotificationManager manager) {
        try {
            super.returnObject(manager);
        } catch (Exception e) {
            logger.error("return manager to pool due to error, will invalidate it", e);
            invalidateObject(manager);
        }
    }

    @Override
    public void invalidateObject(PushNotificationManager manager) {
        try {
            super.invalidateObject(manager);
        } catch (Exception e) {
            logger.error("invalidate manager due to error", e);
        }
    }


    private static class PushNotificationManagerFactory extends BasePoolableObjectFactory<PushNotificationManager> {

        private PushNotificationManagerFactory(AppleNotificationServer appleNotificationServer) {
            this.appleNotificationServer = appleNotificationServer;
        }

        private AppleNotificationServer appleNotificationServer;

        @Override
        public PushNotificationManager makeObject() throws Exception {
            PushNotificationManager manager = new PushNotificationManager();
            manager.initializeConnection(appleNotificationServer);
            return manager;
        }

        @Override
        public void destroyObject(PushNotificationManager manager) throws Exception {
            manager.stopConnection();
        }

        @Override
        public boolean validateObject(PushNotificationManager manager) {
            PushedNotification notification = null;
            try {
                notification = manager.sendNotification(new BasicDevice(RandomStringUtils.randomNumeric(64)),
                        PushNotificationPayload.test());
            } catch (Exception e) {
                logger.error("validate manager:[{}] due to error:[{}]", manager, ExceptionUtils.getStackTrace(e));
            }
            return notification != null && notification.isSuccessful();
        }

    }

}
