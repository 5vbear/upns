/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.endpoint;

import com.myctu.platform.HttpHelper;
import com.myctu.platform.protocol.transform.json.JacksonSupport;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.protocol.business.route.SubApply;
import com.sirius.upns.protocol.transfer.MarshallerType;
import com.sirius.upns.server.node.domain.model.GroupMember;
import org.junit.Assert;

import java.util.Map;

/**
 * @project node-server
 * @date 2013-9-26-上午7:16:01
 * @author pippo
 */
public class SubscriberClient {

	public static String deviceToken = "unitTest";

	public SubscriberClient(GroupMember member) {
		this.member = member;
	}

	public void start(MarshallerType marshallerType) throws Exception {
		/* 真正的集成环境客,户端创建session应该通过web service接口进行 */
		String json = HttpHelper.get().doGet(String.format("http://127.0.0.1:8080/subscriber/apply/%s/%s/%s",
			member.userId,
			deviceToken,
			DeviceType.android.code));

		Assert.assertNotNull(json);
		System.out.println(json);

		@SuppressWarnings("unchecked") Map<String, Object> m = JacksonSupport.objectMapper.readValue(json, Map.class);
		apply = new SubApply();
		apply.fromMap(m);

		/* 启动client */
		TCPClient.host = apply.host;
		TCPClient.port = apply.port;
		client = new TCPClient(marshallerType);
		client.start();
	}

	public void stop() throws Exception {
		client.stop();
	}

	public GroupMember member;

	public SubApply apply;

	public TCPClient client;

}
