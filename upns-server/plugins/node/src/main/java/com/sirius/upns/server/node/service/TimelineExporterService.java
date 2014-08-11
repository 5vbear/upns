/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node.service;

import com.sirius.upns.protocol.business.msg.History;

/**
 * User: pippo
 * Date: 13-11-24-19:18
 */
public interface TimelineExporterService {

    History find(History example);

}
