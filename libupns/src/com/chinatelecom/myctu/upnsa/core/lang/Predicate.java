/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.lang;

/**
 * Predicate
 *
 * @author snowway
 * @since 2/24/11
 */
public interface Predicate<T> {

    @SuppressWarnings("rawtypes")
    public static final Predicate TRUE = new Predicate() {
        @Override
        public boolean eval(Object input) {
            return true;
        }
    };

    boolean eval(T input);
}
