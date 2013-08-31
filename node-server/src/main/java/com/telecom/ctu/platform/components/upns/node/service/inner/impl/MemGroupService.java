/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.inner.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.telecom.ctu.platform.components.upns.node.domain.model.Group;
import com.telecom.ctu.platform.components.upns.node.domain.model.GroupMember;
import com.telecom.ctu.platform.components.upns.node.service.inner.GroupService;

/**
 * @project node-server
 * @date 2013-8-30-下午4:21:05
 * @author pippo
 */
public class MemGroupService implements GroupService {

	private Map<String, Group> _groups = Maps.newConcurrentMap();

	private Map<String, Collection<GroupMember>> _members = Maps.newConcurrentMap();

	@Override
	public Group get(String groupId) {
		return _groups.get(groupId);
	}

	@Override
	public Collection<String> getUserIdsByGroup(String groupId) {
		Collection<GroupMember> members = _members.get(groupId);
		if (members == null) {
			return Collections.emptyList();
		}

		Set<String> ids = new HashSet<>();
		for (GroupMember member : members) {
			ids.add(member.userId);
		}

		return ids;
	}

	@Override
	public Collection<Group> getGroupByUser(String userId) {
		Set<Group> groups = new HashSet<>();

		for (String groupId : _members.keySet()) {
			Collection<GroupMember> members = _members.get(groupId);
			if (members == null) {
				continue;
			}

			for (GroupMember member : members) {
				if (member.userId.equals(userId)) {
					groups.add(this.get(groupId));
				}
			}
		}
		return groups;
	}

	@Override
	public String addGroup(Group group) {
		if (group.id == null) {
			group.id = UUID.randomUUID().toString();
		}

		group.createTime = System.currentTimeMillis();
		_groups.put(group.id, group);
		return group.id;
	}

	@Override
	public void removeGroup(String groupId) {
		_groups.remove(groupId);
	}

	@Override
	public void addMember(GroupMember... member) {

		for (GroupMember _member : member) {
			String groupId = _member.groupId;
			Collection<GroupMember> members = _members.get(groupId);

			if (members == null) {
				members = new HashSet<>();
				_members.put(_member.groupId, members);
			}

			members.add(_member);
		}
	}

	@Override
	public void removeMember(GroupMember... member) {
		for (GroupMember _member : member) {
			String groupId = _member.groupId;
			Collection<GroupMember> members = _members.get(groupId);
			if (members == null) {
				continue;
			}

			members.remove(_member);
		}
	}

}
