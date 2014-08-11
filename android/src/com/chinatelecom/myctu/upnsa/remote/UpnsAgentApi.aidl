package com.chinatelecom.myctu.upnsa.remote;
import com.chinatelecom.myctu.upnsa.remote.UpnsApplication;
import com.chinatelecom.myctu.upnsa.remote.UpnsMessageCallback;
import com.chinatelecom.myctu.upnsa.remote.UpnsAgentListener;


/**
* UpnsAgent对外接口
*/
interface UpnsAgentApi {

    /**
    *  UPNS Service是否连接上服务器
    */
    boolean isConnected();


    /**
    * 断开UPNS Service的连接
    */
    void disconnect();

    /**
    * 添加一个Listener
    */
    void addListener(UpnsAgentListener listener);


    /**
    * 删除指定的Listener
    */
    void removeListener(UpnsAgentListener listener);

    /**
    * 获得当前登录的用户,只在成功用户成功连接服务器后返回当前用户id,否则返回null
    */
    String getCurrentUserId();

    /**
    * 应用注册,应用首次需要注册后才可以收到消息,可重复注册,会覆盖之前的注册信息
    *
    * @param application 应用信息类
    * @return 注册是否成功
    */
    boolean registerApplication(in UpnsApplication application);

    /**
    * 用户已认证
    */
    void userAuthenticated(String userId);


    /**
    * 获取未读消息列表
    * @param applicationId 应用id
    * @param timestamp 取某个时间戳之后的未读消息,首次获取的时候参数为0
    * @param size 最多取多少条 可以传Integer.MAX_VALUE获取最大值
    * @param callback 收到消息后的回调接口
    */
    void retrieveMessages(String applicationId,long timestamp,int size,
        in UpnsMessageCallback callback);


    /**
     * 告知UpnsAgent,哪些消息已经被客户端成功获取并处理
     */
    void messageRetrieved(inout String[] messageIds);
}