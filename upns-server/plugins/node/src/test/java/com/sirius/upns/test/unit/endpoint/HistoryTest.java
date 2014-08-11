/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.endpoint;

import com.myctu.platform.spring.ext.BeanLocator;
import com.sirius.upns.protocol.business.BusinessPacket;
import com.sirius.upns.protocol.business.msg.History;
import com.sirius.upns.protocol.business.sub.ConnectRQ;
import com.sirius.upns.protocol.transfer.MarshallerType;
import com.sirius.upns.server.node.domain.model.Group;
import com.sirius.upns.server.node.domain.model.GroupMember;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.repository.GroupRepository;
import com.sirius.upns.server.node.service.DeliverBox;
import com.sirius.upns.test.unit.endpoint.TCPClient.PacketCallback;
import com.telecom.ctu.platform.framework.engine.ApplicationEngine;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * @author pippo
 * @project node-server
 * @date 2013-10-21-下午10:31:34
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HistoryTest {

    private static int group_count = 10;

    private static int pub_count = 10;

    private static GroupRepository groupRepository;

    private static GroupMember member_offine;

    @BeforeClass
    public static void init() throws Exception {
        ApplicationEngine.main(null);
        groupRepository = BeanLocator.getBean(GroupRepository.class);

		/* 创建10个组 */
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < group_count; i++) {
            Group group = new Group();
            group.appId = new Random().nextInt(999);
            group.name = "test_group_" + RandomStringUtils.randomNumeric(5);
            groupRepository.saveGroup(group);
            groups.add(group);
        }

        String userId = "pippo__offine_" + RandomStringUtils.randomNumeric(5);
        /* 让userId={userId}加入新创建的组 */
        for (Group group : groups) {
            member_offine = new GroupMember();
            member_offine.userId = userId;
            member_offine.groupId = group.id;
            groupRepository.addMember(member_offine);
        }

		/* 为每个组发布{pub_count}条消息 */
        DeliverBox deliveryBox = BeanLocator.getBean(DeliverBox.class);
        for (Group group : groups) {
            for (int i = 0; i < pub_count; i++) {
                Message msg = new Message();
                msg.appId = group.appId;
                msg.groupId = group.id;
                msg.title = "offline_msg_" + group.id + "_" + i;
                deliveryBox.publish(msg);
            }
        }
    }

    @Test
    public void history() throws Exception {
		/* 在发布消息之后为启动一个subscriber */
        SubscriberClient subscriber = new SubscriberClient(member_offine);
        subscriber.start(MarshallerType.MSGPack);
        subscriber.client.write(new ConnectRQ(subscriber.apply.id));

		/* 按照时间戳获取历史消息 */
        final Semaphore block = new Semaphore(0);
        subscriber.client.setCallback(new PacketCallback() {

            @Override
            public void onPackage(BusinessPacket _package) {

                if (_package instanceof History) {
                    System.out.println(_package);
                    block.release();
                }
            }
        });

        History example = new History();
		/* userid可以不传,server会取当前socket所bind的userid */
        example.appId = null;
        example.limit = 10;
        example.timestamp = System.currentTimeMillis();
        subscriber.client.write(example);

        Thread.sleep(1000 * 60);
    }
}
