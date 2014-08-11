/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol.test.unit;

import com.sirius.upns.protocol.business.msg.Custom;
import com.sirius.upns.protocol.transfer.MSGPackMarshaller;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @project protocol
 * @date 2013-9-14-下午3:04:12
 * @author pippo
 */
public class MSGPackPerformanceTest {

	static AtomicLong cost = new AtomicLong(0);

	static int count = 100000;

	static Collection<Callable<Long>> tasks = new HashSet<Callable<Long>>();

	static int currency = Runtime.getRuntime().availableProcessors() * 4;

	public static void main(String[] args) throws InterruptedException {

		cost.set(0);
		for (int i = 0; i < 10; i++) {
			pack();
		}
		System.out.println("pack:" + cost.get() / count / 10 / 1000);
	}

	private static MSGPackMarshaller msgPackMarshaller = new MSGPackMarshaller();

	public static void pack() throws InterruptedException {
		tasks.clear();

		for (int i = 0; i < count; i++) {
			tasks.add(new Callable<Long>() {

				@Override
				public Long call() {
					long start = System.nanoTime();
					Custom msg = new Custom();
					msg.appId = 1;
					msg.groupId = UUID.randomUUID().toString();
					msg.title = UUID.randomUUID().toString();
					msg.content = UUID.randomUUID().toString();

					try {

						byte[] bb = msgPackMarshaller.marshal(msg);
						msg = msgPackMarshaller.unmarshal(bb, Custom.class);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cost.addAndGet((System.nanoTime() - start));
					return null;
				}
			});
		}

		ExecutorService service = Executors.newFixedThreadPool(currency);
		service.invokeAll(tasks);
		service.shutdown();

	}

}
