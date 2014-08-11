/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Utility class for IO
 * <p/>
 * User: snowway
 * Date: 9/9/13
 * Time: 4:52 PM
 */
public class IOUtils {

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    /**
     * 从InputStream读取UTF-8 String
     *
     * @param stream 输入流
     * @return String
     */
    public static String toString(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, CHARSET_UTF8));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
}
