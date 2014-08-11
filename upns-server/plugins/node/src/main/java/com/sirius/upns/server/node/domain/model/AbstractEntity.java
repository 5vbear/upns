/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.model;

import com.myctu.platform.protocol.MapSerializable;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-8-下午8:46:39
 */
public abstract class AbstractEntity implements MapSerializable {

    private static final long serialVersionUID = -2952600451373751602L;

    public static final String ID = "_id";

    public static final String CREATE_TIME = "ct";

    public Date getCreateDate() {
        return new Date(createTime);
    }

    public AbstractEntity() {
        this.id = UUID.randomUUID().toString();
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put(ID, id);
        out.put(CREATE_TIME, createTime);
        return out;
    }

    @Override
    public void fromMap(Map<String, Object> in) {
        this.id = (String) in.get(ID);
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }

        this.createTime = (Long) in.get(CREATE_TIME);
        if (this.createTime == null) {
            this.createTime = System.currentTimeMillis();
        }
    }

    /**
     * 主键
     */
    public String id;

    /**
     * 创建日期
     */
    public Long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
