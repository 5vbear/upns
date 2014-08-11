/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * template for Sqlite database
 *
 * @author snowway
 * @since 2/24/11
 */
public final class SqliteTemplate {

    public static final String LOG_TAG = SqliteTemplate.class.getName();

    private static final Object _lock = new Object();

    private SQLiteDatabase database;


    public SqliteTemplate(SQLiteDatabase database) {
        this.database = database;
        this.database.setLockingEnabled(true);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * 查询数据库返回游标
     *
     * @param sql  sql
     * @param args args
     * @return Cursor
     */
    public Cursor query(final String sql, final String[] args) {
        return execute(new SqliteCallback<Cursor>() {
            @Override
            public Cursor doInSqlite(SQLiteDatabase database) {
                Cursor cursor = database.rawQuery(sql, args);
                cursor.moveToFirst();
                return cursor;
            }
        });
    }

    /**
     * insert 数据库
     *
     * @param table          表名
     * @param values         插入的值
     * @param nullColumnHack null字段值
     * @return 插入后产生的id
     */
    public long insert(final String table, final ContentValues values, final String nullColumnHack) {
        return execute(new SqliteCallback<Long>() {
            @Override
            public Long doInSqlite(SQLiteDatabase database) {
                return database.insert(table, nullColumnHack, values);
            }
        });
    }


    /**
     * 更新数据库
     *
     * @param table  表名
     * @param values 字段值
     * @param where  where条件
     * @param args   where参数
     * @return 更新条数
     */
    public int update(final String table, final ContentValues values, final String where, final String[] args) {
        return execute(new SqliteCallback<Integer>() {
            @Override
            public Integer doInSqlite(SQLiteDatabase database) {
                return database.update(table, values, where, args);
            }
        });
    }


    /**
     * 删除数据库数据
     *
     * @param table 表名
     * @param where where条件
     * @param args  where参数
     * @return 删除条数
     */
    public int delete(final String table, final String where, final String[] args) {
        return execute(new SqliteCallback<Integer>() {
            @Override
            public Integer doInSqlite(SQLiteDatabase database) {
                return database.delete(table, where, args);
            }
        });
    }


    /**
     * 执行数据库操作
     *
     * @param callback SqliteCallback
     * @param <T>      返回类型
     * @return 执行结果
     */
    public <T> T execute(SqliteCallback<T> callback) {
        try {
            synchronized (_lock) {
                if (database.isOpen()) {
                    database.acquireReference();
                    try {
                        return callback.doInSqlite(database);
                    } catch (Exception ex) {
                        Log.e(LOG_TAG, "error execute database", ex);
                        throw new SqliteException(ex.getMessage(), ex);
                    }
                } else {
                    throw new IllegalStateException("database closed");
                }
            }
        } finally {
            synchronized (_lock) {
                if (database != null) {
                    database.releaseReference();
                }
            }
        }
    }

    public boolean isOpen() {
        return database != null && database.isOpen();
    }


    public void close() {
        if (database != null && isOpen()) {
            database.close();
            database = null;
        }
    }
}
