/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.node.domain.model;

import com.myctu.platform.utils.MapUtils;
import com.sirius.upns.protocol.business.DeviceType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author pippo
 * @project protocol
 * @date 2013-8-14-下午2:53:09
 */
public class Device extends AbstractEntity {

    public static final String USER_ID = "u";

    public static final String TOKEN = "t";

    public static final String TYPE = "y";

    public static final String LAST_ACTIVE_TIME = "l";

    public static final String APPS = "a";

    private static final long serialVersionUID = 4785267761123763917L;

    public String userId;

    /* 设备编号,android设备是IMEI,ios设备是apns token */
    public String token;

    public DeviceType type;

    /* 最后活跃时间 */
    public long lastActiveTime;

    /* 设备上安装的应用,仅对android设备有效 */
    /* 客户端service记录app列表 */
	/* app启动主动向service注册 */
	/* service针听卸载事件,更新本地列表,如有网络同时上传列表 */
	/* service每次连接到服务器端需要上传app列表 */
    public List<Integer> installedApp;

    public Device(String userId, String token, DeviceType type) {
        this();

        this.userId = userId;
        this.token = token;
        this.type = type;
    }

    public Device() {
        super();
        this.lastActiveTime = this.createTime;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = super.toMap();
        MapUtils.addIfNotNull(out, USER_ID, userId);
        MapUtils.addIfNotNull(out, TOKEN, token);
        MapUtils.addIfNotNull(out, TYPE, type.code);
        MapUtils.addIfNotNull(out, LAST_ACTIVE_TIME, lastActiveTime);
        MapUtils.addIfNotNull(out, APPS, installedApp);
        return out;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fromMap(Map<String, Object> in) {
        super.fromMap(in);
        userId = (String) in.get(USER_ID);
        token = (String) in.get(TOKEN);
        type = DeviceType.from(in.get(TYPE) != null ? ((Number) in.get(TYPE)).intValue() : 0);
        lastActiveTime = in.get(LAST_ACTIVE_TIME) != null ? ((Number) in.get(LAST_ACTIVE_TIME)).longValue() : 0L;
        installedApp = (List<Integer>) in.get(APPS);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        Device other = (Device) obj;
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        if (userId == null) {
            if (other.userId != null) {
                return false;
            }
        } else if (!userId.equals(other.userId)) {
            return false;
        }
        return true;
    }

    public void addApp(Integer... appId) {

        if (installedApp == null) {
            installedApp = Arrays.asList(appId);
        }

        for (int _id : appId) {
            if (installedApp.contains(_id)) {
                continue;
            }
            installedApp.add(_id);
        }
    }

    public void removeApp(Integer... appId) {
        if (installedApp == null) {
            return;
        }

        for (int _id : appId) {
            installedApp.remove(_id);
        }
    }

    @Override
    public String toString() {
        return String.format("Device [id=%s, createTime=%s, userId=%s, token=%s, type=%s, lastActiveTime=%s, installedApp=%s]",
                id,
                createTime,
                userId,
                token,
                type,
                lastActiveTime,
                installedApp);
    }

}
