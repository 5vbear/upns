/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.node;

import com.telecom.ctu.platform.framework.engine.module.Component;
import org.springframework.context.support.CTUApplicationContext;

/**
 * User: pippo
 * Date: 14-1-1-10:42
 */
public class Configuration extends Component {

    public Configuration(CTUApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public String getName() {
        return "classpath*:META-INF/sirius.upns.node.context.xml";
    }
}