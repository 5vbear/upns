/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.telecom.ctu.platform.components.upns.protocol.test.unit;

import java.util.HashMap;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telecom.ctu.platform.components.upns.protocol.binary.MSGPackPackage;
import com.telecom.ctu.platform.components.upns.protocol.sub.msg.Custom;

/**
 * @project protocol
 * @date 2013-8-31-下午5:07:07
 * @author pippo
 */
public class MSGPackPackageTest {

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
	public void test() throws Exception {
		byte[] bb = new MSGPackPackage(custom).toByteArray();

		MSGPackPackage _package = new MSGPackPackage(bb);
		Assert.assertTrue(_package.payload instanceof Custom);

		Custom _custom = (Custom) _package.payload;
		Assert.assertTrue(_custom.content.equals(custom.content));
	}

}
