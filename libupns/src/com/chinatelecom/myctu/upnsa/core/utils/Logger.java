/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.utils;

import android.util.Log;

/**
 * Logger Utility class
 * <p/>
 * User: snowway
 * Date: 9/8/13
 * Time: 12:02 PM
 */
public class Logger {

    public static final String LOG_TAG = "upns";


    private static String getThread() {
        return Thread.currentThread().getName() + ":" + String.valueOf(Thread.currentThread().getId());
    }

    /**
     * 错误日志
     *
     * @param message 信息
     */
    public static void error(String message) {
        Log.e(LOG_TAG + "-[" + getThread() + "]", message);
    }

    /**
     * 信息日志
     *
     * @param message 信息
     */
    public static void info(String message) {
        Log.i(LOG_TAG + "-[" + getThread() + "]", message);
    }


    /**
     * 警告日志
     *
     * @param message 信息
     */
    public static void warn(String message) {
        Log.w(LOG_TAG + "-[" + getThread() + "]", message);
    }


    /**
     * 调试日志
     *
     * @param message 信息
     */
    public static void debug(String message) {
        Log.d(LOG_TAG + "-[" + getThread() + "]", message);
    }

    /**
     * 错误日志
     *
     * @param message 信息
     * @param cause   原因
     */
    public static void error(String message, Throwable cause) {
        Log.e(LOG_TAG + "-[" + getThread() + "]", message, cause);
    }
}
