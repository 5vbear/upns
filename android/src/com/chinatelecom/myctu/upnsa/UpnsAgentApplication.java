/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa;

import android.app.Application;

/**
 * UpnsAgentApplication
 * <p/>
 * User: snowway
 * Date: 12/30/11
 * Time: 5:19 PM
 */
public class UpnsAgentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DependencyFactory.getInstance().initialize(this);
    }

    @Override
    public void onTerminate() {
        DependencyFactory.getInstance().destroy();
    }

    @Override
    public void onLowMemory() {
    }
}
