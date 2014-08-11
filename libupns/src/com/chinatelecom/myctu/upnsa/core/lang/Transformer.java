/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.lang;

/**
 * Transformer
 *
 * @author snowway
 * @since 2/24/11
 */
public interface Transformer<F, T> {

    /**
     * 对象转换
     *
     * @param input
     * @return
     */
    T transform(F input);
}
