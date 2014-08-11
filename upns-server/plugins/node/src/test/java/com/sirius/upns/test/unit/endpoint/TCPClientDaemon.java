/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.endpoint;

import com.myctu.platform.HttpHelper;
import com.myctu.platform.protocol.transform.json.JacksonSupport;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.protocol.business.route.SubApply;
import com.sirius.upns.protocol.business.sub.ConnectRQ;
import com.sirius.upns.protocol.business.sub.ConnectRS;
import com.sirius.upns.protocol.transfer.MarshallerType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-15-下午3:05:44
 */
public class TCPClientDaemon {

	//	private static String router_url = "http://127.0.0.1:8080/upns/subscriber/apply/%s/%s/%s";
	//private static String router_url = "http://180.168.60.15:8010/upns/subscriber/apply/%s/%s/%s";
	private static String router_url = "http://upns.myctu.cn/subscriber/apply/%s/%s/%s";

	private static String userId = "pippo";
	private static String deviceToken = "unitTest";

	@Test
	public void jsonClient() throws Exception {
		TCPClient client = new TCPClient(MarshallerType.JSON);

		/* 真正的集成环境客,户端创建session应该通过web service接口进行 */
		String json = HttpHelper.get().doGet(String.format(router_url,
				userId,
				deviceToken,
				DeviceType.android.code));

		Assert.assertNotNull(json);
		System.out.println(json);

		@SuppressWarnings("unchecked")
		Map<String, Object> m = JacksonSupport.objectMapper.readValue(json, Map.class);
		SubApply apply = new SubApply();
		apply.fromMap(m);

		/* 启动client */
		TCPClient.host = apply.host;
		TCPClient.port = apply.port;
		TCPClient.userId = userId;
		TCPClient.deviceToken = deviceToken;
		client.start();

		BlockCallback callback = new BlockCallback();
		client.setCallback(callback);
		client.write(new ConnectRQ(apply.id));
		callback.block();

		ConnectRS rs = (ConnectRS) callback.packet;
		Assert.assertTrue(rs.success);

		Thread.sleep(Long.MAX_VALUE);
	}

	@Test
	public void msgPackClient() throws Exception {
		//		TCPClient client = new TCPClient(MarshallerType.MSGPack);
		//		client.start();
	}

}
