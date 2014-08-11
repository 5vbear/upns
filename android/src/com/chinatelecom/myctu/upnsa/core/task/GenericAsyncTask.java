/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.task;

import android.os.AsyncTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * User: snowway
 * Date: 1/4/12
 * Time: 9:51 PM
 */
public abstract class GenericAsyncTask<P, R> extends AsyncTask<Void, P, AsyncResult<R>> {

    private Throwable lastError;

    @Override
    protected final void onPreExecute() {
        try {
            onPrepare();
        } catch (Throwable ex) {
            lastError = ex;
        }
    }

    protected void pause(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            //ignore
        }
    }

    @Override
    protected final AsyncResult<R> doInBackground(Void... voids) {
        if (lastError == null) {
            try {
                return new AsyncResult<R>(runInBackground());
            } catch (Exception ex) {
                Throwable reason = ex;
                while (reason instanceof UndeclaredThrowableException ||
                        reason instanceof InvocationTargetException) {
                    reason = reason.getCause();
                }
                return new AsyncResult<R>(reason);
            }
        } else {
            return new AsyncResult<R>(lastError);
        }
    }

    @Override
    protected final void onPostExecute(AsyncResult<R> result) {
        onFinally();
        if (result.getError() != null) {
            onException(result.getError());
        } else {
            onComplete(result.getTarget());
        }
    }

    @Override
    protected final void onProgressUpdate(P... values) {
        onProgress(values[0]);
    }

    public final void start() {
        this.execute((Void) null);
    }

    protected void onProgress(P value) {
    }


    protected void onPrepare() {
    }


    protected abstract R runInBackground() throws Exception;


    protected void onComplete(R result) {
    }


    protected void onException(Throwable error) {
    }


    protected void onFinally() {
    }
}
