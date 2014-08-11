package com.chinatelecom.myctu.upnsa.sqlite;
/* Copyright © 2010 www.myctu.cn. All rights reserved. */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * User: snowway
 * Date: 9/27/13
 * Time: 4:31 PM
 */
public class UpnsAgentSqliteHelper extends SQLiteOpenHelper implements Schema {

    public static final String DATABASE_NAME = "upns_agent.db";

    public static final int DATABASE_VERSION = 2013110201;

    public static final String CREATE_MESSAGE_DDL =
            "CREATE TABLE IF NOT EXISTS  " + IMessage.TABLE_NAME +
                    "(" + IMessage.ID + " INTEGER PRIMARY KEY," +
                    IMessage.MESSAGE_ID + " TEXT," +
                    IMessage.CREATE_TIME + " INTEGER," +
                    IMessage.RECEIVE_TIME + " INTEGER," +
                    IMessage.APPLICATION_ID + " TEXT," +
                    IMessage.USER_ID + " TEXT," +
                    IMessage.GROUPID + " TEXT," +
                    IMessage.TITLE + " TEXT," +
                    IMessage.CONTENT + " TEXT, " +
                    IMessage.EXTENSION + " BLOB)";

    public static final String CREATE_CONFIGURATION_DDL =
            "CREATE TABLE IF NOT EXISTS  " + IConfiguration.TABLE_NAME +
                    "(" + IConfiguration.ID + " INTEGER PRIMARY KEY," +
                    IConfiguration.KEY + " TEXT," +
                    IConfiguration.VALUE + " TEXT)";

    public static final String CREATE_APPLICATION_DDL =
            "CREATE TABLE IF NOT EXISTS  " + IApplication.TABLE_NAME +
                    "(" + IApplication.ID + " INTEGER PRIMARY KEY," +
                    IApplication.APPLICATION_ID + " TEXT," +
                    IApplication.APPLICATION_NAME + " TEXT," +
                    IApplication.NOTIFICATION_ICON + " BLOG," +
                    IApplication.NOTIFICATION_INTENT + " TEXT)";


    public static final String DROP_MESSAGE_DDL = "DROP TABLE IF EXISTS " + IMessage.TABLE_NAME;

    public static final String DROP_CONFIGURATION_DDL = "DROP TABLE IF EXISTS " + IConfiguration.TABLE_NAME;

    public static final String DROP_APPLICATION_DDL = "DROP TABLE IF EXISTS " + IApplication.TABLE_NAME;


    public UpnsAgentSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_CONFIGURATION_DDL);
        database.execSQL(CREATE_APPLICATION_DDL);
        database.execSQL(CREATE_MESSAGE_DDL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            database.execSQL(DROP_CONFIGURATION_DDL);
            database.execSQL(DROP_APPLICATION_DDL);
            database.execSQL(DROP_MESSAGE_DDL);
            onCreate(database);
        }
    }
}
