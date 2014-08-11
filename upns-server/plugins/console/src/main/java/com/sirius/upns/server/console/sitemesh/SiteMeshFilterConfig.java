package com.sirius.upns.server.console.sitemesh;

import com.google.common.collect.ImmutableMap;
import com.opensymphony.module.sitemesh.Config;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by pippo on 14-3-23.
 */
public class SiteMeshFilterConfig implements FilterConfig {

    private static final Map<String, String> parameters
            = ImmutableMap.<String, String>builder()
                          .put("configFile",
                               SiteMeshFilterConfig.class.getResource("/META-INF/sitemesh.xml").getFile())
                          .build();

    public SiteMeshFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;


        filterConfig.getServletContext().setAttribute("sitemesh.factory", new ClassPathConfigFactory(new Config(this)));
    }

    private FilterConfig filterConfig;

    @Override
    public String getFilterName() {
        return filterConfig.getFilterName();
    }

    @Override
    public ServletContext getServletContext() {
        return filterConfig.getServletContext();
    }

    @Override
    public String getInitParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

}