/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.test.unit;

import java.util.HashMap;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.telecom.ctu.platform.components.upns.protocol.binary.JSONPackage;
import com.telecom.ctu.platform.components.upns.protocol.binary.MSGPackPackage;
import com.telecom.ctu.platform.components.upns.protocol.sub.Ping;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Custom;

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
		custom.extension = new HashMap<String, Object>();
		custom.extension.put("a", 1L);
		custom.extension.put("b", 2);
		custom.extension.put("c", "dsdfsdf");
	}

	@Test
	public void msgPack() throws Exception {

		//ping可以节省40%
		//msg可节省20%

		System.out.println("msgPack@ping" + new MSGPackPackage(ping).toByteArray().length);
		System.out.println("msgPack@custom" + new MSGPackPackage(custom).toByteArray().length);
	}

	@Test
	public void json() throws Exception {
		System.out.println("json@ping" + new JSONPackage(ping).toByteArray().length);
		System.out.println("json@custom" + new JSONPackage(custom).toByteArray().length);
	}

}
