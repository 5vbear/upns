package com.sirius.upns.server.widget.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sirius.upns.protocol.business.msg.APPUnread;
import com.sirius.upns.protocol.business.msg.History;
import com.sirius.upns.server.node.service.TimelineService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pippo on 14-3-25.
 */
@Controller
@RequestMapping("/msg")
public class MessageController {

    @Resource(name = "upns.timelineService")
    private TimelineService timelineService;


    /**
     * 标记未读为已读
     */
    @RequestMapping("/ack/{userId}")
    @ResponseBody
    public JSONPObject ack(@PathVariable("userId") String userId,
                           @RequestParam(value = "jsonp") String function) {
//        timelineService.ackAll(userId,System.currentTimeMillis());
        Map map = ArrayUtils.toMap(new Object[][]{
                {"successful", true}
        });
        return new JSONPObject(function, map);
    }

    /**
     * 按应用获取未读消息对象
     */
    @RequestMapping("/unread/{userId}")
    @ResponseBody
    public JSONPObject unread(@PathVariable("userId") String userId,
                              @RequestParam(value = "jsonp") String function) {
        Map<String, Object> result = new LinkedHashMap<>();
        int totalUnread = getTotalUnread(userId);
        result.put("totalUnread", totalUnread);
        if (totalUnread > 0) {
            History example = new History();
            example.userId = userId;
            example.unread = true;
            example.ack = false;
            example.limit = totalUnread;
            History history = timelineService.find(example);
            result.put("messages", history.messages);
        }
        return new JSONPObject(function, result);
    }


    private int getTotalUnread(String userId) {
        List<APPUnread> unreads = timelineService.unread(userId);
        int total = 0;
        for (APPUnread unread : unreads) {
            total += unread.unread;
        }
        return total;
    }

}
