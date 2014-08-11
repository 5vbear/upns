/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.ui;

import android.app.Activity;
import android.content.Context;
import com.chinatelecom.myctu.upnsa.DependencyFactory;

/**
 * 抽象Activity父类
 * <p/>
 * User: snowway
 * Date: 9/9/13
 * Time: 2:50 PM
 */
public abstract class AbstractUpnsAgentActivity extends Activity {

    /**
     * DependencyFactory
     */
    protected DependencyFactory factory = DependencyFactory.getInstance();

    /**
     * @return Android Context
     */
    protected Context getContext() {
        return this;
    }
}
