/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.exception;

/**
 * Android Upns根异常
 * <p/>
 * User: snowway Date: 9/9/13 Time: 5:20 PM
 */
public class UpnsAgentException extends RuntimeException {

    private static final long serialVersionUID = -1024260578848341703L;

    public UpnsAgentException() {
    }

    public UpnsAgentException(String detailMessage) {
        super(detailMessage);
    }

    public UpnsAgentException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UpnsAgentException(Throwable throwable) {
        super(throwable);
    }
}
