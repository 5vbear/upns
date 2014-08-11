/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */
package com.sirius.upns.test.unit.rpc;

import com.myctu.platform.gateway.agent.GatewaySupport;
import com.myctu.platform.gateway.agent.GatewaySupport.Utils.InvokerFactory;
import com.myctu.platform.gateway.agent.invoker.ServiceInvokerException;
import com.myctu.platform.gateway.agent.invoker.http.HttpServiceInvoker;
import com.myctu.platform.gateway.agent.message.ServiceMessageReplyBuffer;
import com.myctu.platform.protocol.service.config.ServiceConfig.TransportMode;
import com.sirius.upns.protocol.business.msg.History;
import com.telecom.ctu.platform.framework.engine.ApplicationEngine;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-12-下午2:50:21
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimelineServiceTest implements GatewaySupport {

    private static InvokerFactory factory = Utils.getInvokerFactory("http://127.0.0.1:8080/" + SERVICE_PATH,
                                                                    -1,
                                                                    TransportMode.route.code);


    @BeforeClass
    public static void init() throws Exception {
        ApplicationEngine.main(null);
    }

    @AfterClass
    public static void destory() throws Exception {
        System.exit(-1);
    }

    @Test
    public void find() throws ServiceInvokerException {
        HttpServiceInvoker invoker = factory.createInvoker(-1, "upns.timeline.find");

        History history = new History();
        history.appId = 123;
        history.userId = "pippo";
        history.timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 30 * 2;
        history.ack = true;

        ServiceMessageReplyBuffer reply = invoker.invoke(new Object[]{history});
        System.out.println(reply.asText());

        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);
    }

}
