/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.test.unit.endpoint;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.common.collect.ImmutableList;
import com.myctu.platform.spring.ext.BeanLocator;
import com.telecom.ctu.platform.components.upns.node.NodeServerLauncher;
import com.telecom.ctu.platform.components.upns.node.domain.model.Group;
import com.telecom.ctu.platform.components.upns.node.domain.model.GroupMember;
import com.telecom.ctu.platform.components.upns.node.service.inner.GroupService;
import com.telecom.ctu.platform.components.upns.node.service.outer.DeliveryBox;
import com.telecom.ctu.platform.components.upns.node.service.outer.SubscriberRouterTable;
import com.telecom.ctu.platform.components.upns.protocol.BusinessPackage;
import com.telecom.ctu.platform.components.upns.protocol.DeviceType;
import com.telecom.ctu.platform.components.upns.protocol.pub.Simple;
import com.telecom.ctu.platform.components.upns.protocol.sub.Close;
import com.telecom.ctu.platform.components.upns.protocol.sub.ConnectRQ;
import com.telecom.ctu.platform.components.upns.protocol.sub.ConnectRS;
import com.telecom.ctu.platform.components.upns.protocol.sub.Ping;
import com.telecom.ctu.platform.components.upns.protocol.sub.RegistRQ;
import com.telecom.ctu.platform.components.upns.protocol.sub.RegistRS;
import com.telecom.ctu.platform.components.upns.protocol.sub.SubApply;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Receipt;
import com.telecom.ctu.platform.components.upns.test.unit.endpoint.TCPClient.PackageCallback;

/**
 * @project node-server
 * @date 2013-8-26-下午3:43:38
 * @author pippo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProtocolTest {

	private static TCPClient client = new TCPClient();

	private static String userId = "pippo";

	private static String deviceToken = "unitTest";

	private static SubApply apply;

	private static Group group;

	@BeforeClass
	public static void init() throws Exception {
		NodeServerLauncher.main(null);
		/* 添加group */
		GroupService groupService = BeanLocator.getBean(GroupService.class);
		group = new Group();
		group.appId = -1;
		group.name = "test_group";
		groupService.addGroup(group);

		GroupMember member = new GroupMember();
		member.userId = userId;
		member.groupId = group.id;
		groupService.addMember(member);

		/* 真正的集成环境客,户端创建session应该通过web service接口进行 */
		SubscriberRouterTable routerTable = BeanLocator.getBean(SubscriberRouterTable.class);
		apply = routerTable.apply(userId, deviceToken, DeviceType.android);

		/* 启动client */
		client.start();
	}

	@Test
	public void y_ping() throws Exception {
		BlockCallback callback = new BlockCallback();
		client.setCallback(callback);
		client.write(new Ping(System.currentTimeMillis()));
		callback.block();

		Assert.assertNotNull(callback._package);
		System.out.println(callback._package);
	}

	@Test
	public void b_connect() throws Exception {
		BlockCallback callback = new BlockCallback();
		client.setCallback(callback);
		client.write(new ConnectRQ(apply.id));
		callback.block();

		ConnectRS rs = (ConnectRS) callback._package;
		Assert.assertTrue(rs.success);
	}

	@Test
	public void c_registApp() throws Exception {
		BlockCallback callback = new BlockCallback();
		client.setCallback(callback);
		client.write(new RegistRQ(ImmutableList.<Integer> builder().add(1).add(2).build()));
		callback.block();

		Assert.assertNotNull(callback._package);
		System.out.println(callback._package);

		RegistRS rs = (RegistRS) callback._package;
		Assert.assertTrue(rs.success);
	}

	@Test
	public void d_sub() throws Exception {
		int count = 100;
		CountDownCallback callback = new CountDownCallback(count);
		client.setCallback(callback);

		DeliveryBox deliveryBox = BeanLocator.getBean(DeliveryBox.class);

		for (int i = 0; i < count; i++) {
			Simple simple = new Simple();
			simple.appId = -1;
			simple.groupId = group.id;
			simple.title = "msg" + i;
			deliveryBox.publish(simple);
		}

		callback.block();
	}

	@Test
	public void z_close() throws Exception {
		BlockCallback callback = new BlockCallback();
		client.setCallback(callback);
		client.write(new Close());

		client.stop();
	}

	private static class BlockCallback implements PackageCallback {

		Semaphore recieved = new Semaphore(0);

		BusinessPackage _package;

		public void block() throws InterruptedException {
			recieved.tryAcquire(300, TimeUnit.SECONDS);
		}

		@Override
		public void onPackage(BusinessPackage _package) {
			this._package = _package;
			recieved.release();
		}

	};

	private static class CountDownCallback implements PackageCallback {

		public CountDownCallback(int count) {
			countDownLatch = new CountDownLatch(count);
		}

		private CountDownLatch countDownLatch;

		public void block() throws InterruptedException {
			countDownLatch.await(300, TimeUnit.SECONDS);
		}

		@Override
		public void onPackage(BusinessPackage _package) {
			System.out.println(_package);

			Simple simple = (Simple) _package;
			client.write(new Receipt(simple.id, deviceToken));

			countDownLatch.countDown();
		}
	}
}
