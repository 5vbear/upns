package com.chinatelecom.myctu.upnsa.remote;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 第三方应用信息
 * <p/>
 * User: snowway
 * Date: 10/15/13
 * Time: 9:44 AM
 */
public class UpnsApplication implements Parcelable {

    /**
     * 应用Id
     */
    private String applicationId;


    /**
     * 应用名称,显示在任务栏上
     */
    private String applicationName;

    /**
     * 应用显示在任务栏上的图标
     */
    private byte[] notificationIcon;

    /**
     * 当点击任务栏图标时发送的Intent
     */
    private String notificationItent;

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

    public String getNotificationItent() {
        return notificationItent;
    }

    public void setNotificationItent(String notificationItent) {
        this.notificationItent = notificationItent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpnsApplication> CREATOR
            = new Creator<UpnsApplication>() {
        public UpnsApplication createFromParcel(Parcel in) {
            return new UpnsApplication(in);
        }

        public UpnsApplication[] newArray(int size) {
            return new UpnsApplication[size];
        }
    };

    public UpnsApplication() {
    }

    private UpnsApplication(Parcel in) {
        this.setApplicationId(in.readString());
        this.setApplicationName(in.readString());
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readByteArray(content);
        this.setNotificationIcon(content);
        this.setNotificationItent(in.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.applicationId);
        parcel.writeString(this.applicationName);
        parcel.writeInt(this.notificationIcon.length);
        parcel.writeByteArray(this.notificationIcon);
        parcel.writeString(this.notificationItent);
    }
}
