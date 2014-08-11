/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.utils;

/**
 * Utility class for String
 * <p/>
 * User: snowway
 * Date: 9/9/13
 * Time: 5:09 PM
 */
public class StringUtils {

    public static final String NUMBERS = "0123456789";

    public static final String EMPTY = "";

    public static final int INDEX_NOT_FOUND = -1;


    /**
     * trim字符串
     *
     * @param value
     * @return
     */
    public static String trim(String value) {
        if (isNotBlank(value)) {
            return value.trim();
        } else {
            return "";
        }
    }

    /**
     * 判断字符串为空
     *
     * @param value
     * @return
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }


    /**
     * 判断字符串不为空
     *
     * @param value
     * @return
     */
    public static boolean isNotBlank(String value) {
        return value != null && value.trim().length() > 0;
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param str1
     * @param str2
     * @return equals?
     */
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        if (isBlank(value)) {
            return false;
        }
        char[] values = value.toCharArray();
        for (char current : values) {
            if (!NUMBERS.contains(String.valueOf(current))) {
                return false;
            }
        }
        return true;
    }

    public static int indexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        // JDK1.2/JDK1.3 have a bug, when startPos > str.length for "", hence
        if (searchStr.length() == 0 && startPos >= str.length()) {
            return str.length();
        }
        return str.indexOf(searchStr, startPos);
    }


    public static String defaultString(String str) {
        return str == null ? EMPTY : str;
    }

    /**
     * Abbreviates a string nicely.
     * <p/>
     * This method searches for the first space after the lower limit and abbreviates
     * the String there. It will also append any String passed as a parameter
     * to the end of the String. The upper limit can be specified to forcibly
     * abbreviate a String.
     *
     * @param str         the string to be abbreviated. If null is passed, null is returned.
     *                    If the empty String is passed, the empty string is returned.
     * @param lower       the lower limit.
     * @param upper       the upper limit; specify -1 if no limit is desired.
     *                    If the upper limit is lower than the lower limit, it will be
     *                    adjusted to be the same as the lower limit.
     * @param appendToEnd String to be appended to the end of the abbreviated string.
     *                    This is appended ONLY if the string was indeed abbreviated.
     *                    The append does not count towards the lower or upper limits.
     * @return the abbreviated String.
     * @since 2.4
     */
    public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
        // initial parameter checks
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return StringUtils.EMPTY;
        }

        // if the lower value is greater than the length of the string,
        // set to the length of the string
        if (lower > str.length()) {
            lower = str.length();
        }
        // if the upper value is -1 (i.e. no limit) or is greater
        // than the length of the string, set to the length of the string
        if (upper == -1 || upper > str.length()) {
            upper = str.length();
        }
        // if upper is less than lower, raise it to lower
        if (upper < lower) {
            upper = lower;
        }

        StringBuffer result = new StringBuffer();
        int index = StringUtils.indexOf(str, " ", lower);
        if (index == -1) {
            result.append(str.substring(0, upper));
            // only if abbreviation has occured do we append the appendToEnd value
            if (upper != str.length()) {
                result.append(StringUtils.defaultString(appendToEnd));
            }
        } else if (index > upper) {
            result.append(str.substring(0, upper));
            result.append(StringUtils.defaultString(appendToEnd));
        } else {
            result.append(str.substring(0, index));
            result.append(StringUtils.defaultString(appendToEnd));
        }
        return result.toString();
    }
}
