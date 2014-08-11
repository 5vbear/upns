package com.chinatelecom.myctu.upnsa.remote;
/**
* UpnsAgentListener状态回调接口
* UpnsAgentListener所有方法均为后台线程调用,如果应用需要更新UI,请使用android提供的Handler机制
*/
interface UpnsAgentListener {


    /**
    * 当连接成功时回调
    * @param userId 当前登录用户Id
    */
    void onConnected(String userId);


    /**
    * 当断开连接时回调
    */
    void onDisconnected();


    /**
    * 当完成历史消息后回调
    * @param applicationId 应用Id
    */
    void onFinishHistory(String applicationId);


    /**
    * 当收到消息后回调
    * @param applicationId 应用Id
    * @param messageId 消息Id
    */
    void onMessage(String applicationId,String messageId);

    /**
     * 发生异常后回调
     *
     * @param errorCode 错误码
     * @param errorMessage 错误描述
     */
    void onFault(String errorCode,String errorMessage);
}