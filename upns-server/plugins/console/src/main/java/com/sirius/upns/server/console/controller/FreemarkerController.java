/*
 * Copyright (c) 2010 my.company. All rights reserved.
 */

package com.sirius.upns.server.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Freemarker Sitemesh模板支持类
 * <p/>
 * User: snowway Date: 11/21/12
 */
@Controller
public class FreemarkerController extends BaseController {

    private static final String TAGLIBs_PATH = FreemarkerController.class.getResource("/taglibs").getFile();

    @RequestMapping("/layout/{name:\\w+}.ftl")
    public String decorate(ModelMap context, @PathVariable("name") String name) {
        context.addAttribute("taglibs", TAGLIBs_PATH);
        return "layout/" + name;
    }
}
