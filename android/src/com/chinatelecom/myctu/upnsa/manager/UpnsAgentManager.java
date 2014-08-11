/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.manager;

import android.database.Cursor;
import com.chinatelecom.myctu.upnsa.model.Application;
import com.chinatelecom.myctu.upnsa.model.Configuration;
import com.chinatelecom.myctu.upnsa.model.Message;
import com.chinatelecom.myctu.upnsa.model.Routing;

import java.util.List;

/**
 * Configuration管理接口
 * <p/>
 * User: snowway
 * Date: 9/9/13
 * Time: 3:50 PM
 */
public interface UpnsAgentManager {


    /**
     * 自动注册应用
     */
    void autoRegisterApplications();
    /**
     * 切换用户
     *
     * @param userId 用户Id
     */
    void userAuthenticated(String userId);

    /**
     * @return Configuration对象
     */
    Configuration getConfiguration();


    /**
     * 获取路由信息
     *
     * @param userId 用户Id
     * @return Routing
     */
    Routing getRouting(String userId);


    /**
     * 从sqlite cursor创建Message
     *
     * @param cursor sqlite cursor
     * @return Message
     */
    Message createMessage(Cursor cursor);

    /**
     * 收到消息包
     *
     * @param message Message
     */
    void receiveMessage(Message message);


    /**
     * 批量收到消息包
     *
     * @param messages Message list
     */
    void receiveMessages(List<Message> messages);


    /**
     * 获取消息游标
     *
     * @return Cursor
     */
    Cursor findMessages();

    /**
     * 清除所有本地消息
     */
    void clearMessages();

    /**
     * 注册应用
     *
     * @param model 应用模型
     */
    void registerApplication(Application model);

    /**
     * 获取应用所属的消息完毕
     *
     * @param userId        用户Id
     * @param applicationId 应用Id
     */
    void finishReceiveMessages(String userId, String applicationId);

    /**
     * 获取消息
     *
     * @param userId        用户Id
     * @param applicationId 应用Id
     * @param timestamp     时间戳
     * @param size          获取多少条
     * @return 消息列表
     */
    MessageRetrieveResult retrieveMessages(
            String userId, String applicationId, long timestamp, int size);

    /**
     * 删除指定数组内的消息Id
     * @param messageIds 消息Id
     */
    void removeMessageByIds(String[] messageIds);

    /**
     * @return 所有已注册的应用
     */
    Cursor findRegisteredApplications();

    /**
     * 根据Cursor创建Application
     * @param cursor Cursor
     * @return Application
     */
    Application createApplication(Cursor cursor);
}
