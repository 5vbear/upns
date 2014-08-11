/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node.engine.apns;

import javapns.notification.PushNotificationManager;

/**
 * User: pippo
 * Date: 13-11-23-18:09
 */
public interface PushNotificationProcessor {

    void process(PushNotificationManager manager) throws Exception;

}
