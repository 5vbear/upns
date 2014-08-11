package com.chinatelecom.myctu.upnsa.remote;
/* Copyright © 2010 www.myctu.cn. All rights reserved. */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.*;
import java.util.Map;

/**
 * User: snowway
 * Date: 10/8/13
 * Time: 11:45 AM
 */
public class UpnsMessage implements Parcelable {

    public static final String LOG_TAG = "upns:UpnsMessage";

    /**
     * 唯一ID
     */
    private String messageId;

    /**
     * 应用Id
     */
    private String applicationId;

    /**
     * 群组Id
     */
    private String groupId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;


    /**
     * 消息创建日期
     */
    private long createTime;

    /**
     * 消息实际接收日期
     */
    private long receiveTime;

    /**
     * 扩展字段
     */
    private Map extension;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Map getExtension() {
        return extension;
    }

    public void setExtension(Map extension) {
        this.extension = extension;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    private byte[] serialize(Object target) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(target);
        return b.toByteArray();
    }

    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.getMessageId());
        out.writeString(this.getApplicationId());
        out.writeString(this.getGroupId());
        out.writeLong(this.getCreateTime());
        out.writeString(this.getTitle());
        out.writeString(this.getContent());
        out.writeLong(this.getReceiveTime());
        if (this.getExtension() != null) {
            try {
                byte[] bytes = serialize(this.getExtension());
                out.writeInt(bytes.length);
                out.writeByteArray(bytes);
            } catch (IOException ex) {
                Log.e(LOG_TAG, "不能写入消息的extension属性," + ex.getMessage(), ex);
            }
        } else {
            out.writeInt(0);//没有扩展字段
        }
    }

    public UpnsMessage() {
    }

    private UpnsMessage(Parcel in) {
        this.setMessageId(in.readString());
        this.setApplicationId(in.readString());
        this.setGroupId(in.readString());
        this.setCreateTime(in.readLong());
        this.setTitle(in.readString());
        this.setContent(in.readString());
        this.setReceiveTime(in.readLong());
        //读取extension字段
        int extensionLength = in.readInt();
        if (extensionLength != 0) {// 确实有扩展字段
            byte[] content = new byte[extensionLength];
            in.readByteArray(content);
            try {
                this.setExtension((Map) deserialize(content));
            } catch (Exception ex) {
                Log.e(LOG_TAG, "不能读取消息的extension属性," + ex.getMessage(), ex);
            }
        }
    }

    public static final Creator<UpnsMessage> CREATOR
            = new Creator<UpnsMessage>() {
        public UpnsMessage createFromParcel(Parcel in) {
            return new UpnsMessage(in);
        }

        public UpnsMessage[] newArray(int size) {
            return new UpnsMessage[size];
        }
    };
}