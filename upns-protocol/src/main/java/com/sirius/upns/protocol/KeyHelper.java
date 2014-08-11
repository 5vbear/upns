/* Copyright © 2010 www.myctu.cn. All rights reserved. */
package com.sirius.upns.protocol;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author pippo
 * @project node-server
 * @date 2013-9-8-下午7:45:25
 */
public class KeyHelper {

    public static void main(String[] args) throws InterruptedException {

        System.out.println(apply(UUID.randomUUID().toString()).getBytes().length);

        ExecutorService service = Executors.newFixedThreadPool(3);

        Collection<Callable<Long>> tasks = new HashSet<Callable<Long>>();
        tasks.add(new Callable<Long>() {

            @Override
            public Long call() {
                double start = System.nanoTime();
                for (int i = 0; i < 1000000; i++) {
                    apply(UUID.randomUUID().toString());
                }
                double cost = System.nanoTime() - start;
                System.out.println(String.format("apply cost:[%s]ns", cost / 1000000));
                return null;
            }
        });

        tasks.add(new Callable<Long>() {

            @Override
            public Long call() {
                double start = System.nanoTime();
                for (int i = 0; i < 1000000; i++) {
                    apply1(UUID.randomUUID().toString());
                }
                double cost = System.nanoTime() - start;
                System.out.println(String.format("apply1 cost:[%s]ns", cost / 1000000));
                return null;
            }
        });

        tasks.add(new Callable<Long>() {

            @Override
            public Long call() {
                double start = System.nanoTime();
                for (int i = 0; i < 1000000; i++) {
                    apply2(UUID.randomUUID().toString());
                }
                double cost = System.nanoTime() - start;
                System.out.println(String.format("apply2 cost:[%s]ns", cost / 1000000));
                return null;
            }
        });

        service.invokeAll(tasks);

        service.shutdown();
    }

    private static final String PATTERN = "%s%s%s";

    private static final String APPLY_PREFIX = "apply.";

    public static String apply(String applyId) {
        return MD5Utils.md5Hex(String.format(PATTERN, APPLY_PREFIX, "id.", applyId));
    }

    @Deprecated
    public static String apply1(String applyId) {
        return MD5Utils.md5Hex(new StringBuilder(APPLY_PREFIX).append("id.").append(applyId).toString());
    }

    @Deprecated
    public static String apply2(String applyId) {
        return MD5Utils.md5Hex(APPLY_PREFIX + "id." + applyId);
    }

    private static final String ROUTER_PREFIX = "router.";

    public static String router(String routerId) {
        return MD5Utils.md5Hex(String.format(PATTERN, ROUTER_PREFIX, "id.", routerId));
    }

    public static String userRouters(String userId) {
        return MD5Utils.md5Hex(String.format(PATTERN, ROUTER_PREFIX, "user.", userId));
    }

    private static final String CHANNEL_PREFIX = "channel.";

    public static final String GROUP_ID_BROADCOST = "broadcast";

    public static final String CHANNEL_BROADCAST = channel(GROUP_ID_BROADCOST);

    public static final String GROUP_ID_PRIVATE = "private";

    public static final String CHANNEL_PRIVATE = channel(GROUP_ID_PRIVATE);

    public static String channel(String groupId) {
        return String.format(PATTERN, CHANNEL_PREFIX, "id.", groupId);
        //		return MD5Utils.md5Hex(String.format(PATTERN, CHANNEL_PREFIX, "id.", groupId));
    }

}
