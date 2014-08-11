package com.chinatelecom.myctu.upnsa.model;

/**
 * 应用模型
 * <p/>
 * User: snowway
 * Date: 10/15/13
 * Time: 11:28 AM
 */
public class Application extends UpnsAgentModel {

    /**
     * 应用Id
     */
    private String applicationId;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 提醒图标
     */
    private byte[] notificationIcon;

    /**
     * 唤醒Intent
     */
    private String notificationIntent;


    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public byte[] getNotificationIcon() {
        return notificationIcon;
    }

    public void setNotificationIcon(byte[] notificationIcon) {
        this.notificationIcon = notificationIcon;
    }

    public String getNotificationIntent() {
        return notificationIntent;
    }

    public void setNotificationIntent(String notificationIntent) {
        this.notificationIntent = notificationIntent;
    }
}
