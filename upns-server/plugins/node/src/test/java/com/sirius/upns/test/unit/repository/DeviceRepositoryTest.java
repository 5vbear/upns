/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit.repository;

import com.myctu.platform.spring.ext.BeanLocator;
import com.sirius.upns.protocol.business.DeviceType;
import com.sirius.upns.server.node.domain.model.Device;
import com.sirius.upns.server.node.repository.DeviceRepository;
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
import java.util.List;
import java.util.Random;

/**
 * @project node-server
 * @date 2013-9-9-上午1:13:10
 * @author pippo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:profile/old/ctu.upns.node-server.context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceRepositoryTest {

	@Resource
	private ApplicationContext context;

	@Before
	public void init() {
		BeanLocator.setApplicationContext((ConfigurableApplicationContext) context);
	}

	@Resource
	private DeviceRepository deviceService;

	@Test
	public void a_regist() {
		Device device = deviceService.regist("user123", "token123", DeviceType.android);
		System.out.println(device);
		Assert.assertNotNull(device);
	}

	@Test
	public void b_registApp() {
		Device device = deviceService.registApp("user123", "token123", Arrays.asList(1, 2, new Random().nextInt(100)));
		System.out.println(device);
		Assert.assertTrue(device.installedApp.contains(Integer.valueOf(1)));
	}

	@Test
	public void c_deregistApp() {
		deviceService.deregistApp("user123", "token123", Arrays.asList(1, 2, new Random().nextInt(100)));
		Device device = deviceService.getByToken("user123", "token123");
		System.out.println(device);
		Assert.assertTrue(!device.installedApp.contains(1));
	}

	@Test
	public void d_getByUser() {
		List<Device> devices = deviceService.getByUser("user123");
		System.out.println(Arrays.toString(devices.toArray()));
		Assert.assertTrue(devices.size() > 0);
	}

	@Test
	public void e_deregist() {
		deviceService.deregist("user123", "token123");
		Assert.assertNull(deviceService.getByToken("user123", "token123"));
	}
}
