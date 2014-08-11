/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.router.controller;

import com.myctu.platform.protocol.transform.json.JacksonSupport;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.protocol.business.route.SubApply;
import com.sirius.upns.server.router.RouterPluginConstants;
import com.sirius.upns.server.router.service.SubscriberRouterService;
import com.telecom.ctu.platform.framework.engine.ApplicationEngine;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pippo
 * @project router
 * @date 2013-9-8-下午4:45:56
 */
@WebServlet(urlPatterns = "/subscriber/apply/*")
public class SubApplyServlet extends HttpServlet implements RouterPluginConstants {

    private static final long serialVersionUID = -4119362570106627924L;

    private static final Logger logger = LoggerFactory.getLogger(SubApplyServlet.class);

    /* /apply/${user_id}/${deviceToken}/${deviceType} */
    private static Pattern path_pattern = Pattern.compile("^/subscriber/apply/([\\w[\\-]]+)/(.*)/(\\d)");

    @Override
    public void init() throws ServletException {
        subscriberRouterService = ApplicationEngine.getPluginBean(PLUGIN_NAME, SubscriberRouterService.class);
    }

    private SubscriberRouterService subscriberRouterService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().replaceFirst(req.getContextPath(), "");
        logger.debug("the request path is:[{}],the query is:[{}]", path, req.getQueryString());

        Matcher matcher = path_pattern.matcher(path);
        Validate.isTrue(matcher.find(),
                        String.format(
                                "invalid sub apply path:[%s], expect:[[/subscriber/apply/${user_id}/${deviceToken}/${deviceType}]",
                                path)
                       );

        String userId = matcher.group(1);
        String deviceToken = matcher.group(2);
        DeviceType deviceType = DeviceType.from(Integer.valueOf(matcher.group(3)));

        SubApply apply = subscriberRouterService.apply(userId, deviceToken, deviceType);
        logger.debug("the created apply is:[{}]", apply);

        JacksonSupport.objectMapper.writeValue(resp.getOutputStream(), apply.toMap());
    }

}
