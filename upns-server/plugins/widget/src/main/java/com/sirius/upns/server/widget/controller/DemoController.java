package com.sirius.upns.server.widget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by pippo on 14-3-25.
 */
@Controller
public class DemoController extends BaseController {

    @RequestMapping("/demo")
    public void index(ModelMap context, HttpServletRequest request) {
        context.put("websocket_url", request.getRequestURL().toString().replace("http://", "ws://").replace("/widget/demo", ""));
    }

}
