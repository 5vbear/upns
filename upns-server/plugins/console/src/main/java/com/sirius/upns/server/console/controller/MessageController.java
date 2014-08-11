/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.controller;

import com.sirius.upns.server.console.domain.dto.MessageACKSearcher;
import com.sirius.upns.server.console.domain.dto.MessageSearcher;
import com.sirius.upns.server.console.repository.ACKSearcherProcessor;
import com.sirius.upns.server.console.repository.MessageSearcherProcessor;
import com.sirius.upns.server.node.domain.dto.Pagination;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.repository.GroupRepository;
import com.sirius.upns.server.node.repository.MessageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pippo
 * @project upns-console
 * @date 2013-9-23-下午7:42:32
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Resource(name = "ctu.upns.groupRepository")
    private GroupRepository groupRepository;

    @Resource(name = "ctu.upns.messageRepository")
    private MessageRepository messageRepository;

    @Resource
    private MessageSearcherProcessor messageSearcherProcessor;

    @Resource
    private ACKSearcherProcessor ackSearcherProcessor;

    @RequestMapping(value = "/list")
    public String list(@ModelAttribute MessageSearcher searcher, ModelMap map) {

        map.addAttribute("searcher", searcher);
        map.addAttribute("group", groupRepository.get(searcher.getExample().getGroupId()));

        Pagination<Message> pagination = messageSearcherProcessor.process(searcher);
        map.addAttribute("pagination", pagination);

        List<Group> groups = new ArrayList<Group>(pagination.items.size());
        for (Message message : pagination.items) {
            groups.add(groupRepository.get(message.groupId));
        }
        map.addAttribute("groups", groups);

        return "message/list";
    }

    @RequestMapping(value = "/ack")
    public String ack(@ModelAttribute MessageACKSearcher searcher, ModelMap map) {
        map.addAttribute("searcher", searcher);
        map.addAttribute("message", messageRepository.getMessage(searcher.getExample().getMessageId()));
        map.addAttribute("pagination", ackSearcherProcessor.process(searcher));
        return "message/ack";
    }
}
