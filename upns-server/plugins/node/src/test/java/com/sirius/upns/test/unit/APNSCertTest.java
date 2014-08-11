/*
 *  Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.sirius.upns.test.unit;

import java.security.KeyStore;

/**
 * User: pippo
 * Date: 13-11-25-11:25
 */
public class APNSCertTest {

    public static void main(String[] args) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        String password = "123456";
        ks.load(APNSCertTest.class.getResourceAsStream("/tca.upns.p12"), password.toCharArray());
    }

}
