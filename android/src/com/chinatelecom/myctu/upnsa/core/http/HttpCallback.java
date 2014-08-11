/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.http;

import org.apache.http.client.HttpClient;

/**
 * Http回调接口
 *
 * @author snowway
 * @since 4/8/11
 */
public interface HttpCallback<T> {

    /**
     * HTTP回调方法
     *
     * @param client HttpClient
     * @throws Exception Any Exception
     */
    T call(HttpClient client);
}
