/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.server.console;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;
import com.sirius.upns.server.console.sitemesh.ClassPathConfigFactory;
import com.telecom.ctu.platform.framework.engine.module.Plugin;

import javax.servlet.DispatcherType;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * User: pippo Date: 14-1-6-12:16
 */
public class ConsolePlugin extends Plugin implements ConsolePluginConstants {

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public void init() {
        super.init();
        siteMesh();
    }

    private void siteMesh() {
        FilterRegistration.Dynamic siteMeshFilter = getServletContext().addFilter("sitemesh", new SiteMeshFilter() {
            @Override
            public void init(FilterConfig filterConfig) {
                filterConfig.getServletContext().setAttribute("sitemesh.factory", new ClassPathConfigFactory(new Config(filterConfig)));
                super.init(filterConfig);
            }
        });
        siteMeshFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), true, dispatcher.getName());
    }

}
