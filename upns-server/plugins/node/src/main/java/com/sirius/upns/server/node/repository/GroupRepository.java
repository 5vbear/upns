/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository;

import com.sirius.upns.server.node.domain.model.Group;

import java.util.Collection;

/**
 * @project node-server
 * @date 2013-8-14-下午4:33:18
 * @author pippo
 */
public interface GroupRepository extends MemberRepository {

	void saveGroup(Group group);

	void removeGroup(String groupId);

	Group get(String groupId);

	void iterateGroup(String userId, GroupClosure closure);

	Collection<Group> getUserGroups(String userId);

}
