/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.rpc;

import com.myctu.platform.gateway.agent.GatewaySupport;
import com.myctu.platform.gateway.agent.GatewaySupport.Utils.InvokerFactory;
import com.myctu.platform.gateway.agent.invoker.ServiceInvokerException;
import com.myctu.platform.gateway.agent.invoker.http.HttpServiceInvoker;
import com.myctu.platform.gateway.agent.message.ServiceMessageReplyBuffer;
import com.myctu.platform.protocol.service.config.ServiceConfig.TransportMode;
import com.sirius.upns.server.node.domain.model.Message;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.UUID;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-12-下午2:50:21
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeliverBoxTest implements GatewaySupport {

    private static InvokerFactory factory =
            GatewaySupport.Utils.getInvokerFactory("http://127.0.0.1:8080/" +
                                                   SERVICE_PATH,
                                                   9,
                                                   TransportMode.route.code
                                                  );

    private static Message msg;

    @BeforeClass
    public static void init() throws Exception {
        msg = new Message();
        msg.appId = 1;
        msg.title = "msg" + System.currentTimeMillis();
    }

    @AfterClass
    public static void destory() throws Exception {
        Thread.sleep(1000 * 30);
        System.exit(-1);
    }

    @Test
    public void a_broadcast() throws ServiceInvokerException {
        HttpServiceInvoker invoker = factory.createInvoker(-1, "upns.deliverbox.broadcast");
        ServiceMessageReplyBuffer reply = invoker.invoke(new Object[]{msg});
        System.out.println(reply.asText());
        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);
    }

    @Test
    public void b_ack() throws ServiceInvokerException {
        HttpServiceInvoker invoker = factory.createInvoker(-1, "upns.deliverbox.ack");
        ServiceMessageReplyBuffer reply = invoker.invoke(new Object[]{"pippo", msg.id});
        System.out.println(reply.asText());
        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);
    }

    @Test
    public void c_publish2user() throws ServiceInvokerException {
        msg.groupId = "group#" + System.currentTimeMillis();
        HttpServiceInvoker invoker = factory.createInvoker(-1, "upns.deliverbox.publish2user");
        ServiceMessageReplyBuffer reply = invoker.invoke(
                new Object[]{msg, new String[]{UUID.randomUUID().toString(), "pippo"}});
        System.out.println(reply.asText());
        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);
    }

    @Test
    public void send() throws ServiceInvokerException {
        HttpServiceInvoker invoker = factory.createInvoker(-1, "upns.deliverbox.send");
        ServiceMessageReplyBuffer reply = invoker.invoke(new Object[]{msg, "pippo"});
        System.out.println(reply.asText());
        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);
    }

    @Test
    public void batchSend() throws ServiceInvokerException {
        HttpServiceInvoker invoker = factory.createInvoker(-1, "upns.deliverbox.batchsend");
        ServiceMessageReplyBuffer reply = invoker.invoke(new Object[]{msg, new String[]{"pippo", "snowway"}});
        System.out.println(reply.asText());
        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);
    }

    @Test
    public void apnsSend() throws ServiceInvokerException {
        HttpServiceInvoker invoker = factory.createInvoker(-1, "upns.device.regist");
        ServiceMessageReplyBuffer reply = invoker.invoke(new Object[]{"pippo", RandomStringUtils.randomNumeric(64), 2});
        System.out.println(reply.asText());
        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);

        invoker = factory.createInvoker(-1, "upns.deliverbox.send");
        reply = invoker.invoke(new Object[]{msg, "pippo"});
        System.out.println(reply.asText());
        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);
    }
}
