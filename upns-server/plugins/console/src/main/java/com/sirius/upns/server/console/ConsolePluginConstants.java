/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.console;

import com.telecom.ctu.platform.framework.engine.module.Plugin;
import org.apache.commons.lang3.StringUtils;

/**
 * User: pippo
 * Date: 13-12-4-12:06
 */
public interface ConsolePluginConstants {

    String PLUGIN_NAME = "console";

    String PLUGIN_PATH =
            StringUtils.isBlank(Plugin.PLUGIN_WEB_PREFIX)
            ? PLUGIN_NAME
            : String.format("%s/%s", Plugin.PLUGIN_WEB_PREFIX, PLUGIN_NAME);

}
