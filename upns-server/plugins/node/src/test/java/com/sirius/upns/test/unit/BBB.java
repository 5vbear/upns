/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.test.unit;

import com.myctu.platform.HttpHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @project node-server
 * @date 2013-11-3-下午5:25:54
 * @author pippo
 */
public class BBB {

	public static void main(String[] args) {
		ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);

		for (int i = 0; i < 1000; i++) {
			service.execute(new Runnable() {

				@Override
				public void run() {
					HttpHelper.get().doGet("http://127.0.0.1:1337");
				}
			});
		}
	}

}
