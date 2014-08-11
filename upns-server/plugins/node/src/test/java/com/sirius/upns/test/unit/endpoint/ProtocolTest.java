/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.endpoint;

import com.google.common.collect.ImmutableList;
import com.myctu.platform.spring.ext.BeanLocator;
import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.msg.Receipt;
import com.sirius.upns.protocol.business.msg.Simple;
import com.sirius.upns.protocol.business.sub.Close;
import com.sirius.upns.protocol.business.sub.ConnectRQ;
import com.sirius.upns.protocol.business.sub.ConnectRS;
import com.sirius.upns.protocol.business.sub.Ping;
import com.sirius.upns.protocol.business.sub.RegistRQ;
import com.sirius.upns.protocol.business.sub.RegistRS;
import com.sirius.upns.protocol.transfer.MarshallerType;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.repository.GroupRepository;
import com.sirius.upns.server.node.service.DeliverBox;
import com.sirius.upns.test.unit.endpoint.TCPClient.PacketCallback;
import com.telecom.ctu.platform.framework.engine.ApplicationEngine;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author pippo
 * @project node-server
 * @date 2013-8-26-下午3:43:38
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProtocolTest {

    private static ApplicationEngine engine = new ApplicationEngine();


    private static GroupRepository groupRepository;

    private static Group group;

    private static SubscriberClient subscriber;

    private static int pub_count = 10;

    @BeforeClass
    public static void init() throws Exception {
        engine.start();

        groupRepository = BeanLocator.getBean(GroupRepository.class);
        /* 添加group */
        group = new Group();
        group.appId = new Random().nextInt(999);
        group.name = "test_group_" + RandomStringUtils.randomNumeric(5);
        groupRepository.saveGroup(group);

		/* 添加成员 */
        GroupMember member_active = new GroupMember();
        member_active.userId = "pippo_" + RandomStringUtils.randomNumeric(5);
        member_active.groupId = group.id;
        groupRepository.addMember(member_active);

		/* 创建subscriber client */
        subscriber = new SubscriberClient(member_active);

		/* 启动subscriber1,用于测试在线消息推送 */
        subscriber.start(MarshallerType.MSGPack);
    }

    @AfterClass
    public static void destory() throws Exception {
        engine.stop();
    }

    @Test
    public void b_connect() throws Exception {
        BlockCallback callback = new BlockCallback();
        subscriber.client.setCallback(callback);
        subscriber.client.write(new ConnectRQ(subscriber.apply.id));
        callback.block();

        ConnectRS rs = (ConnectRS) callback.packet;
        Assert.assertTrue(rs.success);
    }

    @Test
    public void c_registApp() throws Exception {
        BlockCallback callback = new BlockCallback();
        subscriber.client.setCallback(callback);
        subscriber.client.write(new RegistRQ(ImmutableList.<Integer>builder().add(group.appId).add(1).add(2).build()));
        callback.block();

        Assert.assertNotNull(callback.packet);
        System.out.println(callback.packet);

        RegistRS rs = (RegistRS) callback.packet;
        Assert.assertTrue(rs.success);
    }

    @Test
    public void d_sub() throws Exception {
        CountDownCallback callback = new CountDownCallback(subscriber, pub_count);
        subscriber.client.setCallback(callback);

        DeliverBox deliveryBox = BeanLocator.getBean(DeliverBox.class);
        for (int i = 0; i < pub_count; i++) {
            Message msg = new Message();
            msg.appId = group.appId;
            msg.groupId = group.id;
            msg.title = "msg" + i;
            deliveryBox.publish(msg);
        }

        callback.block();
    }

    @Test
    public void y_ping() throws Exception {
        BlockCallback callback = new BlockCallback();
        subscriber.client.setCallback(callback);
        subscriber.client.write(new Ping(System.currentTimeMillis()));
        callback.block();

        Assert.assertNotNull(callback.packet);
        System.out.println(callback.packet);
    }

    @Test
    public void z_close() throws Exception {
        BlockCallback callback = new BlockCallback();
        subscriber.client.setCallback(callback);
        subscriber.client.write(new Close());
        subscriber.client.stop();

        callback.block();
    }


    private static class CountDownCallback implements PacketCallback {

        public CountDownCallback(SubscriberClient subscriber, int count) {
            this.subscriber = subscriber;
            this.countDownLatch = new CountDownLatch(count);
        }

        private SubscriberClient subscriber;

        private CountDownLatch countDownLatch;

        public void block() throws InterruptedException {
            countDownLatch.await(300, TimeUnit.SECONDS);
        }

        @Override
        public void onPackage(BusinessPacket _package) {
            System.out.println("###" + _package);

            Simple simple = (Simple) _package;
            subscriber.client.write(new Receipt(simple.id, SubscriberClient.deviceToken));

            countDownLatch.countDown();
        }
    }
}
