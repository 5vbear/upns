/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository.impl;

import com.google.common.collect.Maps;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.repository.GroupClosure;
import com.sirius.upns.server.node.repository.GroupRepository;
import com.sirius.upns.server.node.repository.MemberClosure;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @project node-server
 * @date 2013-8-30-下午4:21:05
 * @author pippo
 */
public class MemGroupRepository implements GroupRepository {

	private Map<String, Group> _groups = Maps.newConcurrentMap();

	private Map<String, Collection<GroupMember>> _members = Maps.newConcurrentMap();

	@Override
	public Group get(String groupId) {
		return _groups.get(groupId);
	}

	@Override
	public void saveGroup(Group group) {
		if (group.id == null) {
			group.id = UUID.randomUUID().toString();
		}

		group.createTime = System.currentTimeMillis();
		_groups.put(group.id, group);
	}

	@Override
	public void removeGroup(String groupId) {
		_groups.remove(groupId);
	}

	@Override
	public void clearMember(String groupId) {
		_members.remove(groupId);
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
	public void removeMember(String groupId, String... userId) {
		Collection<GroupMember> members = _members.get(groupId);
		if (members == null) {
			return;
		}

		Iterator<GroupMember> iterator = members.iterator();
		while (iterator.hasNext()) {
			GroupMember member = iterator.next();
			if (ArrayUtils.contains(userId, member.userId)) {
				iterator.remove();
			}
		}
	}

	@Override
	public Collection<GroupMember> getGroupMembers(String groupId) {
		return _members.get(groupId);
	}

	@Override
	public Collection<Group> getUserGroups(String userId) {
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
	public void iterateMemberByGroup(String groupId, MemberClosure closure) {
		Iterator<GroupMember> iterator = _members.get(groupId).iterator();
		while (iterator.hasNext()) {
			closure.execute(iterator.next());
		}
	}

	@Override
	public void iterateMemberByUser(String userId, MemberClosure closure) {
		throw new RuntimeException("not impl yet");
	}

	@Override
	public void iterateGroup(String userId, GroupClosure closure) {
		Iterator<Group> iterator = this.getUserGroups(userId).iterator();
		while (iterator.hasNext()) {
			closure.execute(iterator.next());
		}
	}

}
