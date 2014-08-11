package com.chinatelecom.myctu.upnsa.model;
/* Copyright © 2010 www.myctu.cn. All rights reserved. */

import java.io.Serializable;

/**
 * 路由信息
 * <p/>
 * User: snowway
 * Date: 10/8/13
 * Time: 11:24 AM
 */
public class Routing implements Serializable {

    /**
     * 路由Id
     */
    private String id;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * deviceToken
     */
    private String deviceToken;

    /**
     * 设备类型,0:android
     */
    private int deviceType;

    /**
     * 连接的ip
     */
    private String host;

    /**
     * 连接的端口
     */
    private int port;

    /**
     * 过期时间
     */
    private long expireTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
