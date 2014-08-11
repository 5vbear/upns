/*
 * Copyright (c) 2010 my.company. All rights reserved.
 */

package com.sirius.upns.server.console.controller;

import com.sirius.upns.server.console.domain.dto.MemberSearcher;
import com.sirius.upns.server.console.repository.MemberSearcherProcessor;
import com.sirius.upns.server.node.domain.dto.Pagination;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.repository.GroupRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 成员管理Controller
 * User: snowway Date: 9/12/13 Time: 11:48 AM
 */
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

	@Resource(name = "ctu.upns.groupRepository")
	private GroupRepository groupRepository;

	@Resource
	private MemberSearcherProcessor memberSearcherProcessor;

	@RequestMapping(value = "/list")
	public String list(@ModelAttribute MemberSearcher searcher, ModelMap map) {
		map.addAttribute("searcher", searcher);
		map.addAttribute("group", groupRepository.get(searcher.getExample().getGroupId()));

		Pagination<GroupMember> pagination = memberSearcherProcessor.process(searcher);
		map.addAttribute("pagination", pagination);

		Map<String, Group> groups = new HashMap<>();
		for (GroupMember member : pagination.getItems()) {
			Group group = groupRepository.get(member.groupId);
			groups.put(group.id, group);
		}
		map.addAttribute("groups", groups);

		return "member/list";
	}

	@RequestMapping(value = "/join/{groupId}.dlg")
	public String memberJoin(@PathVariable("groupId") String groupId, ModelMap map) {
		map.addAttribute("group", groupRepository.get(groupId));
		return "member/join";
	}

	@RequestMapping(value = "/join")
	public String memberJoin(@ModelAttribute GroupMember member) {
		groupRepository.addMember(member);
		return "closecolorbox";
	}

	@RequestMapping(value = "/remove/{groupId}/{userId}")
	public String remove(@PathVariable("groupId") String groupId,
			@PathVariable("userId") String userId,
			@ModelAttribute MemberSearcher searcher) {
		groupRepository.removeMember(groupId, userId);
		return String.format("redirect:/%s/member/list?%s", PLUGIN_NAME, searcher.getURLAppender());
	}
}
