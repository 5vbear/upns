package com.chinatelecom.myctu.upnsa.model;
/* Copyright © 2010 www.myctu.cn. All rights reserved. */

import com.chinatelecom.myctu.upnsa.core.utils.Logger;
import com.chinatelecom.myctu.upnsa.core.utils.SerializationUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 消息
 * <p/>
 * User: snowway
 * Date: 10/8/13
 * Time: 11:18 AM
 */
public class Message extends UpnsAgentModel {


    /**
     * 消息在服务器上的创建时间
     */
    private long createTime;

    /**
     * 接收在客户端的接收日期
     */
    private long receiveTime;

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
     * 哪个用户收取了该消息
     */
    private String userId;


    /**
     * 消息扩展字段
     */
    private Map extension;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map getExtension() {
        return extension;
    }

    public void setExtension(Map extension) {
        this.extension = extension;
    }

    public void setExtension(byte[] extensionArray) {
        try {
            setExtension((Map) SerializationUtils.deserialize(extensionArray));
        } catch (Exception ex) {
            Logger.error("不能反序列化消息的extension字段," + ex.getMessage(), ex);
        }
    }

    public byte[] getExtensionAsByteArray() {
        try {
            return SerializationUtils.serialize(getExtension());
        } catch (IOException ex) {
            Logger.error("不能序列化消息的extension字段," + ex.getMessage(), ex);
            return null;
        }
    }
}
