/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.repository.impl;

import com.google.common.collect.Lists;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.repository.JongoConstants;
import com.sirius.upns.server.node.repository.MemberClosure;
import com.sirius.upns.server.node.repository.MemberRepository;
import org.jongo.MongoCollection;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-24-下午12:49:28
 */
public class MongoMemberRepository implements MemberRepository, JongoConstants {

	private static final String queryByUser = String.format("{%s:#}", GroupMember.USER_ID);

	private static final String queryByGroup = String.format("{%s:#}", GroupMember.GROUP_ID);

	@Resource(name = "ctu.upns.mongo.db.members")
	protected MongoCollection members;

	@Override
	public void addMember(GroupMember... _members) {
		String check = String.format("{%s:#, %s:#}", GroupMember.GROUP_ID, GroupMember.USER_ID);
		for (GroupMember _member : _members) {
			/* 只添加不存在的成员 */
			if (members.findOne(check, _member.groupId, _member.userId).as(Map.class) == null) {
				members.insert(_member.toMap());
			}
		}
	}

	@Override
	public void removeMember(String groupId, String... userId) {
		for (String _id : userId) {
			this.members.remove(String.format("{%s:#, %s:#}", GroupMember.GROUP_ID, GroupMember.USER_ID), groupId, _id);
		}
	}

	@Override
	public void clearMember(String groupId) {
		this.members.remove(queryByGroup, groupId);
	}

	@Override
	public void iterateMemberByGroup(String groupId, MemberClosure closure) {
		@SuppressWarnings("rawtypes") Iterable<Map> iterable = members.find(queryByGroup, groupId).as(Map.class);
		for (Map<String, Object> _in : iterable) {
			GroupMember member = new GroupMember();
			member.fromMap(_in);
			closure.execute(member);
		}
	}

	@Override
	public void iterateMemberByUser(String userId, MemberClosure closure) {
		@SuppressWarnings("rawtypes")
		Iterable<Map> iterable = members.find(queryByUser, userId).as(Map.class);
		for (Map<String, Object> _in : iterable) {
			GroupMember member = new GroupMember();
			member.fromMap(_in);
			closure.execute(member);
		}
	}

	@Override
	public Collection<GroupMember> getGroupMembers(String groupId) {
		final Collection<GroupMember> members = Lists.newArrayList();

		iterateMemberByGroup(groupId, new MemberClosure() {

			@Override
			public void execute(GroupMember member) {
				members.add(member);
			}
		});

		return members;
	}

}
