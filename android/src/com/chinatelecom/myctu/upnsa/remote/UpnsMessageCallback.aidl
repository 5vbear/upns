package com.chinatelecom.myctu.upnsa.remote;
import com.chinatelecom.myctu.upnsa.remote.UpnsMessage;

/**
*  收到消息后的回调接口
*/
interface UpnsMessageCallback {

    /**
    * 收到消息后回调
    * @param messages 获取的消息对象
    * @param hasMore 是否还有更多的消息未获取
    * @param timestamp 本次获取后最早的一条消息的timestamp
    */
    void onMessages(inout List<UpnsMessage> messages, boolean hasMore, long timestamp);
}