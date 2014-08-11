/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.exception;

/**
 * Upns HTTP服务异常
 * <p/>
 * User: snowway Date: 9/9/13 Time: 5:32 PM
 */
public class HttpInvokerException extends UpnsAgentException {

    private static final long serialVersionUID = -6094342900898435624L;

    private int responseCode;

    public HttpInvokerException(int responseCode) {
        super("HTTP服务异常,响应码:" + responseCode);
        this.responseCode = responseCode;
    }

    public HttpInvokerException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public int getResponseCode() {
        return responseCode;
    }
}
