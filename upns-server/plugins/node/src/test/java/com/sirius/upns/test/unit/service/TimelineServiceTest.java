/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.service;

import com.myctu.platform.spring.ext.BeanLocator;
import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.protocol.business.msg.History;
import com.sirius.upns.server.node.domain.model.Message;
import com.sirius.upns.server.node.repository.impl.MongoInitTool;
import com.sirius.upns.server.node.service.DeliverBox;
import com.sirius.upns.server.node.service.TimelineService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author pippo
 * @project node-server
 * @date 2013-10-21-下午8:29:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:ctu.application.test.context.xml",
		"classpath*:META-INF/sirius.upns.node.context.xml",
		//		"classpath*:META-INF/sirius.upns.node.engine.apns.context.xml",
		//		"classpath*:META-INF/sirius.upns.node.engine.endpoint.context.xml",
		//		"classpath*:META-INF/sirius.upns.node.module.pubsub.context.xml",
		"classpath*:META-INF/sirius.upns.node.module.queue.context.xml",
		"classpath*:META-INF/sirius.upns.node.support.mongo.context.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimelineServiceTest {

	private static boolean init;
	private static String userId = "pippo";
	private static String groupId = "timeline_pippo";
	private static Integer[] appIds = new Integer[] { 60001, 60002 };

	private static Integer msgCount = 0;

	@Before
	public void init() throws InterruptedException {
		BeanLocator.setApplicationContext((ConfigurableApplicationContext) context);

		if (init) {
			return;
		}

		/* 要神勇,会清空所有数据 */
		mongoInitTool.clearMessage();
		mongoInitTool.clearACK();
		mongoInitTool.clearTimeline();

		/* 每个应用各自初始化10条用户消息 */
		for (Integer appId : appIds) {
			for (int i = 0; i < 10; i++) {
				Message message = new Message();
				message.appId = appId;
				message.groupId = groupId;
				message.title = "t_" + System.currentTimeMillis();
				deliverBox.publish2user(message, Arrays.asList(userId));
				msgCount++;
			}
		}


		/* 每个应用各自初始初始化10条广播消息 */
		//		for (Integer appId : appIds) {
		//			for (int i = 0; i < 10; i++) {
		//				Message message = new Message();
		//				message.appId = appId;
		//				message.groupId = KeyHelper.GROUP_ID_BROADCOST;
		//				message.title = "t_" + System.currentTimeMillis();
		//				deliverBox.broadcast(message);
		//				msgCount++;
		//			}
		//		}

		Thread.sleep(1000 * 10);
		System.out.println("###########");
		init = true;
	}

	@Resource
	private ApplicationContext context;

	@Resource
	private MongoInitTool mongoInitTool;

	@Resource(name = "upns.deliverBox")
	private DeliverBox deliverBox;

	@Resource(name = "upns.timelineService")
	private TimelineService timelineService;

	@Test
	public void history_60001() {
		History example = new History();
		example.userId = userId;
		example.appId = 60001;
		example.limit = 50;
		example.unread = true;
		example.timestamp = System.currentTimeMillis();

		example = timelineService.find(example);
		System.out.println(example);
		System.out.println(example.messages.size());

		Assert.assertTrue(!example.messages.isEmpty());

		/*每个应用上有用户消息10,广播10*/
		Assert.assertTrue(example.messages.size() == msgCount / appIds.length);
	}

	@Test
	public void history_all() {
		History example = new History();
		example.userId = userId;
		/* 应用不传则为查所有应用的历史消息 */
		example.appId = null;
		example.limit = 50;
		example.timestamp = System.currentTimeMillis();

		example = timelineService.find(example);
		System.out.println(example);

		Assert.assertTrue(!example.messages.isEmpty());
		/*用户消息20,广播20*/
		Assert.assertTrue(example.messages.size() == msgCount);
	}

	@Test
	public void history_ack_1_one() throws InterruptedException {
		History example = new History();
		example.userId = userId;
		/* 应用不传则为查所有应用的历史消息 */
		example.appId = null;
		example.limit = 1;
		example.timestamp = System.currentTimeMillis();
		example.unread = true;
		example.ack = false;

		/* 读取第一条并ack */
		example = timelineService.find(example);
		System.out.println(example);
		Assert.assertTrue(example.messages.size() == 1);
		Custom custom = example.messages.get(0);
		timelineService.ack(userId, custom.id);
		Thread.sleep(10 * 1000);

		/* 每个应用上有用户消息10,广播10 */
		/* 共两个应用 */
		/* 再次读取unread为true的应该有39条 */
		example.limit = 50;
		example = timelineService.find(example);
		System.out.println(example);
		System.out.println(example.messages.size());
		Assert.assertTrue(example.messages.size() == msgCount - 1);
	}

	@Test
	public void history_ack_2_all() {
		History example = new History();
		example.userId = userId;
		/* 应用不传则为查所有应用的历史消息 */
		example.appId = null;
		example.limit = 50;
		example.timestamp = System.currentTimeMillis();
		example.unread = true;
		example.ack = true;

		/* 读取消息并更新ack */
		example = timelineService.find(example);
		System.out.println(example);
		Assert.assertTrue(!example.messages.isEmpty());

		/* 再次读取unread为true的应该有0条 */
		example = timelineService.find(example);
		System.out.println(example);
		Assert.assertTrue(example.messages.isEmpty());
	}
}
