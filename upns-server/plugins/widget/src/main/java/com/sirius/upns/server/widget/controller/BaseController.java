/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.widget.controller;

import com.sirius.upns.server.widget.WidgetPluginConstants;
import com.telecom.ctu.platform.framework.support.plugin.CTUBaseController;

/**
 * User: pippo
 * Date: 13-12-4-16:41
 */
public class BaseController extends CTUBaseController implements WidgetPluginConstants {

    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }

    @Override
    protected String getPluginPath() {
        return PLUGIN_PATH;
    }

    @Override
    protected String getHomePath() {
        return null;
    }

}
