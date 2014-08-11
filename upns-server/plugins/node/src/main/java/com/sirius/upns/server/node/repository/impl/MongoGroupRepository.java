/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository.impl;

import com.google.common.collect.Lists;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.repository.GroupClosure;
import com.sirius.upns.server.node.repository.GroupRepository;
import com.sirius.upns.server.node.repository.JongoConstants;
import com.sirius.upns.server.node.repository.MemberClosure;
import org.apache.commons.lang3.StringUtils;
import org.jongo.MongoCollection;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @project node-server
 * @date 2013-9-9-上午8:33:15
 * @author pippo
 */
public class MongoGroupRepository extends MongoMemberRepository implements GroupRepository, JongoConstants {

	@Resource(name = "ctu.upns.mongo.db.groups")
	protected MongoCollection groups;

	@Override
	public void saveGroup(Group _group) {
		if (StringUtils.isBlank(_group.id)) {
			_group.id = UUID.randomUUID().toString();
		}

		groups.update(QUERY_BY_ID, _group.id).upsert().merge(_group.toMap());
	}

	@Override
	public void removeGroup(String groupId) {
		groups.remove(QUERY_BY_ID, groupId);
	}

	@Cacheable("method.cache")
	@Override
	public Group get(String groupId) {
		if (StringUtils.isBlank(groupId)) {
			return null;
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> in = groups.findOne("{_id:#}", groupId).as(Map.class);
		if (in == null) {
			return null;
		}

		Group group = new Group();
		group.fromMap(in);
		return group;
	}

	@Override
	public void iterateGroup(String userId, final GroupClosure closure) {
		iterateMemberByUser(userId, new MemberClosure() {

			@Override
			public void execute(GroupMember member) {
				Group group = get(member.groupId);
				if (group != null) {
					closure.execute(group);
				}
			}
		});
	}

	@Override
	public Collection<Group> getUserGroups(String userId) {
		final Collection<Group> groups = Lists.newArrayList();
		iterateGroup(userId, new GroupClosure() {

			@Override
			public void execute(Group group) {
				groups.add(group);
			}
		});

		return groups;
	}

}
