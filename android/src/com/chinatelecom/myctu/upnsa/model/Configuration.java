package com.chinatelecom.myctu.upnsa.model;
/* Copyright © 2010 www.myctu.cn. All rights reserved. */

/**
 * 配置信息
 * <p/>
 * User: snowway
 * Date: 10/8/13
 * Time: 11:25 AM
 */
public class Configuration extends UpnsAgentModel {

    /**
     * KEY_REGISTER_URL
     */
    public static final String KEY_REGISTER_URL = "registerUrl";

    /**
     * KEY_USER_ID
     */
    public static final String KEY_USER_ID = "userId";

    /**
     * 登录用户Id
     */
    private String userId;


    /**
     * 注册的URL地址
     */
    private String registerUrl;


    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
