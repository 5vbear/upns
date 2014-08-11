package com.chinatelecom.myctu.upnsa.model;
/* Copyright © 2010 www.myctu.cn. All rights reserved. */

import java.io.Serializable;

/**
 * UpnsModel
 * <p/>
 * User: snowway
 * Date: 10/8/13
 * Time: 11:15 AM
 */
public abstract class UpnsAgentModel implements Serializable {


    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
