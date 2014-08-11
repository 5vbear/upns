/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository;

import com.sirius.upns.server.node.domain.model.GroupMember;

import java.util.Collection;

/**
 * @project node-server
 * @date 2013-9-24-下午12:44:15
 * @author pippo
 */
public interface MemberRepository {

	void addMember(GroupMember... _members);

	void removeMember(String groupId, String... userId);

	void clearMember(String groupId);

	void iterateMemberByGroup(String groupId, MemberClosure closure);

	void iterateMemberByUser(String userId, MemberClosure closure);

	Collection<GroupMember> getGroupMembers(String groupId);

}