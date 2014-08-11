/*
 * Copyright (c) 2010 my.company. All rights reserved.
 */

package com.sirius.upns.server.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * IndexController User: snowway Date: 9/12/13 Time: 10:51 AM
 */
@Controller
public class IndexController extends BaseController {

	/**
	 * 首页
	 */
	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String index() {
		return "index";
	}
}
