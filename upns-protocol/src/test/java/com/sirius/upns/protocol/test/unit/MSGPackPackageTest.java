/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.test.unit;

import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.protocol.transfer.MarshallerType;
import com.sirius.upns.protocol.transfer.Payload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;

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
		custom.extension = new HashMap<String, String>();
		custom.extension.put("a", "1");
		custom.extension.put("b", "2");
		custom.extension.put("c", "dsdfsdf");
	}

	@Test
	public void test() throws Exception {
		Payload payload = new Payload(MarshallerType.MSGPack, custom);
		String p_str1 = payload.toString();

		System.out.println(p_str1);
		byte[] bb = payload.getBytes();

		payload = new Payload(bb);
		String p_str2 = payload.toString();
		System.out.println(p_str2);

		Assert.assertTrue(p_str1.equals(p_str2));
	}

}
