/*
 * Copyright (c) 2010 my.company. All rights reserved.
 */

package com.sirius.upns.server.console.controller;

import com.sirius.upns.server.console.domain.dto.GroupSearcher;
import com.sirius.upns.server.console.repository.GroupSearcherProcessor;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.repository.GroupRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * 群组管理控制器
 * <p/>
 * User: snowway Date: 9/12/13 Time: 10:52 AM
 */
@Controller
@RequestMapping("/group")
public class GroupController extends BaseController {

    @Resource(name = "ctu.upns.groupRepository")
    private GroupRepository groupRepository;

    @Resource
    private GroupSearcherProcessor groupSearcherProcessor;

    /**
     * 群组列表
     */
    @RequestMapping(value = "/list")
    public void list(@ModelAttribute GroupSearcher searcher, ModelMap map) {
        map.addAttribute("searcher", searcher);
        map.addAttribute("pagination", groupSearcherProcessor.process(searcher));
        /* 如果不定义返回页面,根据约定调整页面地址为${action/method}, 跳转方式为forward */
    }

    /**
     * 群组编辑
     */
    @RequestMapping(value = "/edit.dlg")
    public void edit() {

    }

    /**
     * 群组编辑
     */
    @RequestMapping(value = "/edit/{groupId}.dlg")
    public String edit(@PathVariable("groupId") String groupId, ModelMap map) {
        map.addAttribute("group", groupRepository.get(groupId));
        return "group/edit";
    }

    /**
     * 群组编辑,保存
     */
    @RequestMapping(value = "/edit")
    public String edit(@ModelAttribute Group group) {
        groupRepository.saveGroup(group);
        return "closecolorbox";
    }

    /**
     * 群组删除
     */
    @RequestMapping(value = "/remove/{groupId}", method = RequestMethod.GET)
    public String remove(@PathVariable("groupId") String groupId, @ModelAttribute GroupSearcher searcher) {
        groupRepository.removeGroup(groupId);
        groupRepository.clearMember(groupId);
        return String.format("redirect:/%s/group/list?%s", PLUGIN_NAME, searcher.getURLAppender());
    }

}
