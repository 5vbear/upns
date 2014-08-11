/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.service.impl;

import com.google.common.collect.Sets;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.repository.GroupRepository;
import com.sirius.upns.server.node.service.GroupService;
import com.telecom.ctu.platform.framework.engine.service.Exporter;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-12-下午2:12:45
 */
@Service("upns.groupService")
@Exporter(name = "upns.group", serviceInterface = GroupService.class)
public class GroupServiceImpl implements GroupService {

    @Resource
    private GroupRepository groupRepository;

    @Override
    public String create(Group group) {
        Validate.notNull(group, "the group can not be null");
        Validate.isTrue(group.appId > 0, "invalid appId:[%s]", group.appId);

        groupRepository.saveGroup(group);
        return group.id;
    }

    @Override
    public void remove(String groupId) {
        groupRepository.removeGroup(groupId);
    }

    @Override
    public Group get(String groupId) {
        return groupRepository.get(groupId);
    }

    @Override
    public void join(String groupId, List<String> userIds) {
        Validate.notNull(get(groupId), "the group:[%s] not exists", groupId);
        Validate.notNull(userIds, "userIds can not be null");
        Validate.isTrue(userIds.size() <= 100, "the userIds size can not large then 100");

        Set<GroupMember> members = Sets.newHashSet();
        for (String userId : userIds) {
            GroupMember member = new GroupMember();
            member.userId = userId;
            member.groupId = groupId;
            members.add(member);
        }

        groupRepository.addMember(members.toArray(new GroupMember[members.size()]));
    }

    @Override
    public void joinOnCreate(String groupId, int appId, String name, List<String> userIds) {
        Validate.notNull(groupId, "groupId can not be null");
        Validate.isTrue(appId > 0, "invalid appId:[%s]", appId);

        Group group = get(groupId);
        if (group == null) {
            group = new Group();
            group.id = groupId;
            group.appId = appId;
            group.name = name;
            create(group);
        }

        join(groupId, userIds);
    }

    @Override
    public void quit(String groupId, List<String> userIds) {
        Validate.isTrue(userIds.size() <= 100, "the userIds size can not large then 100");
        groupRepository.removeMember(groupId, userIds.toArray(new String[userIds.size()]));
    }

}
