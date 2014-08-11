/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.exception;

/**
 * Socket服务异常
 * <p/>
 * User: snowway Date: 9/9/13 Time: 5:32 PM
 */
public class SocketException extends UpnsAgentException {


    public SocketException() {
    }

    public SocketException(String detailMessage) {
        super(detailMessage);
    }

    public SocketException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SocketException(Throwable throwable) {
        super(throwable);
    }
}
