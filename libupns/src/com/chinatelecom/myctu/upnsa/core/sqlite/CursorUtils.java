/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.sqlite;

import android.database.Cursor;

/**
 * Utility class for Cursor
 *
 * @author snowway
 * @since 3/20/11
 */
public class CursorUtils {

    public static String getString(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getString(cursor.getColumnIndex(colName));
        }
        return "";
    }

    public static int getInt(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(colName));
        }
        return 0;
    }

    public static long getLong(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getLong(cursor.getColumnIndexOrThrow(colName));
        }
        return 0;
    }

    public static boolean getBoolean(Cursor cursor, String colName) {
        return cursor.getColumnIndex(colName) >= 0 && cursor.getInt(cursor.getColumnIndexOrThrow(colName)) > 0;
    }

    public static float getFloat(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getFloat(cursor.getColumnIndexOrThrow(colName));
        }
        return 0;
    }

    public static double getDouble(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getDouble(cursor.getColumnIndexOrThrow(colName));
        }
        return 0;
    }


    public static byte[] getBlob(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getBlob(cursor.getColumnIndexOrThrow(colName));
        }
        return null;
    }
}


