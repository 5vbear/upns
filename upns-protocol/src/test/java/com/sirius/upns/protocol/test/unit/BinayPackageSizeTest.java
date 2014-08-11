/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.test.unit;

import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.protocol.business.sub.Ping;
import com.sirius.upns.protocol.transfer.MarshallerType;
import com.sirius.upns.protocol.transfer.Payload;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;

/**
 * @project protocol
 * @date 2013-8-31-下午4:49:53
 * @author pippo
 */
public class BinayPackageSizeTest {

	private Ping ping = new Ping(System.currentTimeMillis());

	private Custom custom = new Custom();

	@Before
	public void init() {
		custom.appId = -1;
		custom.appId = -1;
		custom.groupId = UUID.randomUUID().toString();
		custom.title = "adbc";
		custom.content = "afasdfasdfasdfasdfasdfasdfasdfasdfasdfad";
		custom.extension = new HashMap<String, String>();
		custom.extension.put("a", "1");
		custom.extension.put("b", "2");
		custom.extension.put("c", "dsdfsdf");
	}

	@Test
	public void msgPack() throws Exception {
		System.out.println("msgPack@ping" + new Payload(MarshallerType.MSGPack, ping).getBytes().length);
		System.out.println("msgPack@custom" + new Payload(MarshallerType.MSGPack, custom).getBytes().length);
	}

	@Test
	public void json() throws Exception {
		System.out.println("json@ping" + new Payload(MarshallerType.JSON, ping).getBytes().length);
		System.out.println("json@custom" + new Payload(MarshallerType.JSON, custom).getBytes().length);
	}

}
