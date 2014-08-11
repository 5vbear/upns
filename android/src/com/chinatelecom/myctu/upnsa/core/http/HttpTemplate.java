/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * HttpTemplate方法
 *
 * @author snowway
 * @since 4/8/11
 */
public class HttpTemplate {

    private HttpClient httpClient;

    public HttpTemplate() {
        this.httpClient = new DefaultHttpClient();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 执行HttpCallback回调
     *
     * @param callback
     */
    public <T> T execute(HttpCallback<T> callback) {
        try {
            return callback.call(httpClient);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}
