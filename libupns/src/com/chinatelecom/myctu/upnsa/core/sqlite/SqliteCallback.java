/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.sqlite;

import android.database.sqlite.SQLiteDatabase;

/**
 * SqliteCallback
 *
 * @author snowway
 * @since 2/24/11
 */
public interface SqliteCallback<T> {

    /**
     * Sqlite回调方法
     *
     * @param database
     * @return
     */
    T doInSqlite(SQLiteDatabase database);
}
