/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.business.msg;

import com.sirius.upns.protocol.business.BusinessPacket;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pippo
 * @project upns-protocol
 * @date 2013-10-13-下午7:05:46
 */
public class History implements BusinessPacket {

    private static final long serialVersionUID = 381536854891006143L;

    public static final String APP_ID = "a";

    public static final String USER_ID = "u";

    public static final String LIMIT = "l";

    public static final String TIMESTAMP = "t";

    public static final String UNREAD = "r";

    public static final String ACK = "k";

    public static final String MESSAGES = "m";

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = new LinkedHashMap<String, Object>();
        if (appId != null) {
            out.put(APP_ID, appId);
        }

        if (userId != null) {
            out.put(USER_ID, userId);
        }

        if (limit != null) {
            out.put(LIMIT, limit);
        }

        if (timestamp != null) {
            out.put(TIMESTAMP, timestamp);
        }

        if (unread != null) {
            out.put(UNREAD, unread);
        }

        if (ack != null) {
            out.put(ACK, ack);
        }

        if (messages != null && !messages.isEmpty()) {
            List<Map<String, Object>> m = new ArrayList<Map<String, Object>>();
            for (Custom c : messages) {
                m.add(c.toMap());
            }
            out.put(MESSAGES, m);
        }

        return out;
    }

    @Override
    public void fromMap(Map<String, Object> in) {
        appId = (Integer) in.get(APP_ID);
        userId = (String) in.get(USER_ID);
        limit = (Integer) in.get(LIMIT);
        timestamp = (Long) in.get(TIMESTAMP);
        unread = (Boolean) in.get(UNREAD);
        ack = (Boolean) in.get(ACK);

        if (in.containsKey(MESSAGES)) {
            @SuppressWarnings("unchecked") List<Map<String, Object>> mm = (List<Map<String, Object>>) in.get(MESSAGES);
            messages = new ArrayList<Custom>(mm.size());
            for (Map<String, Object> m : mm) {
                Custom custom = new Custom();
                custom.fromMap(m);
                messages.add(custom);
            }
        }
    }

    /* 不传取用户所有app下的消息 */
    public Integer appId;

    /* 用户标识 */
    public String userId;

    /* 不传默认为10 */
    public Integer limit;

    /* 不传默认为服务端当前时间 */
    public Long timestamp;

    /* 不传=取所有历史记录,true=取未读消息,false=取已读消息 */
    public Boolean unread;

    /* true将取到的消息标记为已读,false或null不做任何操作 */
    public Boolean ack;

    public List<Custom> messages;

    @Override
    public String toString() {
        return String.format("History [appId=%s, userId=%s, limit=%s, timestamp=%s, unread=%s, ack=%s, messages=%s]",
                appId,
                userId,
                limit,
                timestamp,
                unread,
                ack,
                messages);
    }
}
