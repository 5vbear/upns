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
import com.myctu.platform.spring.ext.ApplicationContextLauncher;
import com.telecom.ctu.platform.framework.engine.ApplicationEngine;
import org.apache.commons.lang3.RandomStringUtils;
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
public class DeviceServiceTest implements GatewaySupport {

    private static InvokerFactory factory = Utils.getInvokerFactory("http://127.0.0.1:8080/" + SERVICE_PATH,
                                                                    9,
                                                                    TransportMode.route.code);

    @BeforeClass
    public static void init() throws Exception {
        ApplicationEngine.main(null);
    }

    @AfterClass
    public static void destory() throws Exception {
        ApplicationContextLauncher.stop();
    }

    @Test
    public void regist() throws ServiceInvokerException {
        HttpServiceInvoker invoker = factory.createInvoker(-1, "upns.device.regist");

        ServiceMessageReplyBuffer reply = invoker.invoke(new Object[]{"pippo", RandomStringUtils.randomNumeric(64), 2});
        System.out.println(reply.asText());

        Assert.assertTrue(reply.getHeader().getMessage_code() == 10000);
    }

}
