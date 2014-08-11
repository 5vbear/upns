/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.server.console.controller;

import com.myctu.platform.gateway.agent.invoker.ServiceInvokerException;
import com.myctu.platform.protocol.transform.json.JacksonSupport;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.repository.GroupRepository;
import com.sirius.upns.server.node.repository.MessageRepository;
import com.telecom.ctu.platform.framework.engine.service.Invoker;
import com.telecom.ctu.platform.framework.engine.service.SmartServiceInovker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author pippo
 * @project upns-console
 * @date 2013-9-23-下午4:54:49
 */
@Controller
@RequestMapping("/deliverBox")
public class DeliverBoxController extends GroupController {

    @Resource(name = "ctu.upns.groupRepository")
    private GroupRepository groupRepository;

    @Resource(name = "ctu.upns.messageRepository")
    private MessageRepository messageRepository;

    @Invoker(provider = 9, service = "upns.deliverbox.broadcast", returnType = Void.class)
    private SmartServiceInovker broadcast;

    @Invoker(provider = 9, service = "upns.deliverbox.publish", returnType = Void.class)
    private SmartServiceInovker publish;

    @Invoker(provider = 9, service = "upns.deliverbox.send", returnType = Void.class)
    private SmartServiceInovker send;

    @RequestMapping(value = "/publish/{groupId}.dlg")
    public String publish(@PathVariable("groupId") String groupId, ModelMap map) {
        map.addAttribute("group", groupRepository.get(groupId));
        return "deliverBox/publish";
    }

    @RequestMapping(value = "/publish/{groupId}/{messageId}.dlg")
    public String publish(@PathVariable("groupId") String groupId,
            @PathVariable("messageId") String messageId,
            ModelMap map) {
        map.addAttribute("group", groupRepository.get(groupId));
        map.addAttribute("message", messageRepository.getMessage(messageId));
        return "deliverBox/publish";
    }

    @RequestMapping(value = "/publish")
    public String publish(@ModelAttribute Message message, HttpServletRequest request) throws ServiceInvokerException {
        String extensionStr = request.getParameter("e");
        if (StringUtils.isNotBlank(extensionStr)) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, String> e = JacksonSupport.objectMapper
                        .readValue(extensionStr,
                                   Map.class);
                message.extension = e;
            } catch (Exception e) {
            }
        }

        publish.call(message);
        return "closecolorbox";
    }

    @RequestMapping(value = "/broadcast.dlg")
    public String broadcast() {
        return "deliverBox/broadcast";
    }

    @RequestMapping(value = "/broadcast")
    public String broadcast(@ModelAttribute Message message) throws ServiceInvokerException {
        broadcast.call(message);
        return "closecolorbox";
    }

    @RequestMapping(value = "/send/{userId}.dlg")
    public String send(@PathVariable("userId") String userId, HttpServletRequest request) {
        return "deliverBox/send";
    }

    @RequestMapping(value = "/send/{userId}")
    public String send(@ModelAttribute Message message, @PathVariable("userId") String userId)
            throws ServiceInvokerException {
        send.call(message, userId);
        return "closecolorbox";
    }

}
