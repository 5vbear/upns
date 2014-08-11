/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node.engine.apns;

import javapns.notification.AppleNotificationServer;
import javapns.notification.PushNotificationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vibur.objectpool.ConcurrentHolderLinkedPool;
import org.vibur.objectpool.Holder;
import org.vibur.objectpool.HolderValidatingPoolService;
import org.vibur.objectpool.PoolObjectFactory;

/**
 * template class for APNS PushNotificationManager
 * <p/>
 * User: snowway
 * Date: 12/3/13
 * Time: 11:10
 */
public class PushNotificationTemplate {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private HolderValidatingPoolService<PushNotificationManager> pool;

    public PushNotificationTemplate(AppleNotificationServer appleNotificationServer) {
        this.pool = new ConcurrentHolderLinkedPool<PushNotificationManager>(
                new PushNotificationManagerCreator(appleNotificationServer),
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 4, false);

    }

    private class PushNotificationManagerCreator implements PoolObjectFactory<PushNotificationManager> {

        private AppleNotificationServer appleNotificationServer;

        public PushNotificationManagerCreator(AppleNotificationServer appleNotificationServer) {
            this.appleNotificationServer = appleNotificationServer;
        }

        @Override
        public PushNotificationManager create() {
            logger.debug("vibur create PushNotificationManager");
            return new PushNotificationManager();
        }

        @Override
        public boolean readyToTake(PushNotificationManager pushNotificationManager) {
            try {
                logger.debug("vibur readyToTake PushNotificationManager");
                pushNotificationManager.initializeConnection(appleNotificationServer);
                return true;
            } catch (Exception ex) {
                logger.warn("can not initialize PushNotificationManager", ex);
                return false;
            }
        }

        @Override
        public boolean readyToRestore(PushNotificationManager pushNotificationManager) {
            logger.debug("vibur readyToRestore PushNotificationManager");
            return true;
        }

        @Override
        public void destroy(PushNotificationManager pushNotificationManager) {
            try {
                logger.debug("vibur destroy PushNotificationManager");
                pushNotificationManager.stopConnection();
            } catch (Exception ex) {
                logger.warn("can not destroy PushNotificationManager", ex);
            }
        }
    }

    public void push(PushNotificationProcessor processor) {
        Holder<PushNotificationManager> holder = null;
        boolean valid = true;
        try {
            logger.debug("try take PushNotificationManager from vibur pool");
            holder = pool.take();
            if (holder != null && holder.value() != null) {
                processor.process(holder.value());
            }
        } catch (Exception ex) {
            logger.error("push due to error", ex);
            valid = false;
        } finally {
            if (holder != null) {
                pool.restore(holder, valid);
            }
        }
    }
}
