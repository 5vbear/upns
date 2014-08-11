/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.task;

/**
 * 异步结果
 * <p/>
 * User: snowway
 * Date: 1/4/12
 * Time: 9:51 PM
 */
public class AsyncResult<R> {

    private R target;

    private Throwable error;

    public AsyncResult(R target) {
        this.target = target;
    }

    public AsyncResult(Throwable error) {
        this.error = error;
    }

    public R getTarget() {
        return target;
    }

    public Throwable getError() {
        return error;
    }
}
