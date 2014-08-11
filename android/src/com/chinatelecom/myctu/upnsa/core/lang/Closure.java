/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.lang;

/**
 * Closure
 *
 * @author snowway
 * @since 2/24/11
 */
public interface Closure<T> {

    public static class VetoException extends RuntimeException {
        private static final long serialVersionUID = 7887659760614542342L;
    }

    public static class Execution {
        public static void veto() {
            throw new VetoException();
        }
    }

    void execute(T input);
}
