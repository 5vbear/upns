/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node.engine.apns;

import javapns.notification.AppleNotificationServer;
import javapns.notification.PushNotificationManager;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: pippo
 * Date: 13-11-23-10:02
 */
public class APNSClient {

    private static final Logger logger = LoggerFactory.getLogger(APNSClient.class);

    public APNSClient(AppleNotificationServer appleNotificationServer) {
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.testOnBorrow = true;
        config.testOnReturn = true;
        config.testWhileIdle = true;
        config.minIdle = 1;
        config.maxIdle = Runtime.getRuntime().availableProcessors();
        config.maxActive = Runtime.getRuntime().availableProcessors() * 4;

        this.pool = new PushNotificationManagerPool(config, appleNotificationServer);
    }

    private PushNotificationManagerPool pool;

    public void push(PushNotificationProcessor processor) {
        PushNotificationManager manager = null;

        try {
            manager = pool.borrowObject();
            processor.process(manager);
            pool.returnObject(manager);
        } catch (Exception e) {
            logger.error("push due to error", e);

            if (manager != null) {
                pool.invalidateObject(manager);
            }
        }
    }
}
