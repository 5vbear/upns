/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.rpc;

import com.myctu.platform.gateway.agent.GatewaySupport;
import com.myctu.platform.gateway.agent.GatewaySupport.Utils.InvokerFactory;
import com.myctu.platform.gateway.agent.invoker.ServiceInvokerException;
import com.myctu.platform.gateway.agent.invoker.http.HttpServiceInvoker;
import com.myctu.platform.gateway.agent.message.ServiceMessageReplyBuffer;
import com.myctu.platform.protocol.service.config.ServiceConfig.TransportMode;
import com.sirius.upns.server.node.domain.model.Group;
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
public class GroupServiceTest implements GatewaySupport {

    private static InvokerFactory factory = GatewaySupport.Utils
            .getInvokerFactory("http://127.0.0.1:8080/" + SERVICE_PATH,
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
    public void a_create() throws ServiceInvokerException {
        Group group = new Group();
        group.appId = 60001;
        group.name = "group#" + System.currentTimeMillis();

        HttpServiceInvoker invoker = factory.createInvoker(9, "upns.group.create");

        ServiceMessageReplyBuffer reply = invoker.invoke(new Object[]{group});
        System.out.println(reply.asText());

        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);
    }

}
