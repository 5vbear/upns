/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.router;

import com.telecom.ctu.platform.framework.engine.module.Plugin;
import org.apache.commons.lang3.StringUtils;

/**
 * User: pippo
 * Date: 13-12-4-12:06
 */
public interface RouterPluginConstants {

    String PLUGIN_NAME = "router";

    String PLUGIN_PATH =
            StringUtils.isBlank(Plugin.PLUGIN_WEB_PREFIX)
            ? PLUGIN_NAME
            : String.format("%s/%s", Plugin.PLUGIN_WEB_PREFIX, PLUGIN_NAME);

    String JSON_CONTENTTYPE = "application/json;charset=UTF-8";


}
