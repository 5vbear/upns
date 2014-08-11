/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.model;

import java.util.Map;

/**
 * @author pippo
 */
public class GroupMember extends AbstractEntity {

    private static final long serialVersionUID = -3851297922822564228L;

    public static final String GROUP_ID = "g";

    public static final String USER_ID = "u";

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = super.toMap();
        out.put(GROUP_ID, groupId);
        out.put(USER_ID, userId);
        return out;
    }

    @Override
    public void fromMap(Map<String, Object> in) {
        super.fromMap(in);
        this.groupId = (String) in.get(GROUP_ID);
        this.userId = (String) in.get(USER_ID);
    }

    public String groupId;

    public String userId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        GroupMember that = (GroupMember) o;

        if (!groupId.equals(that.groupId)) {
            return false;
        }
        if (!userId.equals(that.userId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + groupId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("GroupMember [groupId=%s, userId=%s]",
                groupId,
                userId);
    }
}
