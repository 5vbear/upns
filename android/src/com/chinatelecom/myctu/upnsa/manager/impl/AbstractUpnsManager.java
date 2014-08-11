/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.manager.impl;

import com.chinatelecom.myctu.upnsa.core.http.DefaultHttpCallback;
import com.chinatelecom.myctu.upnsa.core.utils.Logger;
import com.chinatelecom.myctu.upnsa.exception.HttpInvokerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * AbstractUpnsManager
 * <p/>
 * User: snowway
 * Date: 9/9/13
 * Time: 5:27 PM
 */
public abstract class AbstractUpnsManager {

    /**
     * 封装Http请求
     *
     * @param <T>
     */
    protected abstract class UpnsHttpCallback<T> extends DefaultHttpCallback<T> {

        @Override
        public final T call(HttpClient client) {
            try {
                HttpRequestBase method = getHttpMethod();
                HttpResponse response = client.execute(method);
                if (isStatusOK(response)) {
                    return onSuccessfulResponse(response);
                } else {
                    throw new HttpInvokerException(response.getStatusLine().getStatusCode());
                }
            } catch (Exception ex) {
                throw new HttpInvokerException(ex.getMessage(), ex);
            }
        }

        /**
         * 当成功响应时回调
         *
         * @param response
         * @return
         */
        protected abstract T onSuccessfulResponse(HttpResponse response) throws Exception;

        /**
         * 组装Http方法,HttpGet,HttpPost...
         */
        protected abstract HttpRequestBase getHttpMethod();
    }

}
