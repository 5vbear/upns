package com.chinatelecom.myctu.upnsa.manager.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * 出现在任务栏上的提示信息
 * <p/>
 * User: snowway
 * Date: 10/21/13
 * Time: 11:16 AM
 */
public class NotificationItem {

    /**
     * android notification id
     */
    private int notificationId;

    /**
     * 应用Id
     */
    private String applicationId;

    /**
     * 提示标题
     */
    private String title;

    /**
     * 提示内容
     */
    private String content;

    private HashMap extension;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashMap getExtension() {
        return extension;
    }

    public void setExtension(HashMap extension) {
        this.extension = extension;
    }
}
