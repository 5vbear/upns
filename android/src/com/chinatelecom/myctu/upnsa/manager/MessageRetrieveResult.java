package com.chinatelecom.myctu.upnsa.manager;

import com.chinatelecom.myctu.upnsa.model.Message;

import java.util.List;

/**
 * 消息获取结果集
 * <p/>
 * User: snowway
 * Date: 10/24/13
 * Time: 4:36 PM
 */
public class MessageRetrieveResult {

    private boolean hasMore;

    private long timestamp;

    private List<Message> messages;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
