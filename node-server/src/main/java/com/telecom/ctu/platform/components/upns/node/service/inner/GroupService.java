/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.node.service.inner;

import java.util.Collection;

import com.telecom.ctu.platform.components.upns.node.domain.model.Group;
import com.telecom.ctu.platform.components.upns.node.domain.model.GroupMember;

/**
 * @project node-server
 * @date 2013-8-14-下午4:33:18
 * @author pippo
 */
public interface GroupService {

	Group get(String groupId);

	Collection<String> getUserIdsByGroup(String groupId);

	Collection<Group> getGroupByUser(String userId);

	String addGroup(Group group);

	void removeGroup(String groupId);

	void addMember(GroupMember... member);

	void removeMember(GroupMember... member);

}
