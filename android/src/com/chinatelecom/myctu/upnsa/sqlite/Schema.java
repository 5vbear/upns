package com.chinatelecom.myctu.upnsa.sqlite;
/* Copyright © 2010 www.myctu.cn. All rights reserved. */

/**
 * UPNS Agent 数据字典
 *
 * @author snowway
 * @since 2010-6-24
 */
public interface Schema {

    /**
     * 配置信息
     */
    interface IConfiguration {
        String TABLE_NAME = "configuration";
        String ID = "_id";
        String KEY = "key";
        String VALUE = "value";

        interface Sql {
            String FIND_CONFIGURATION = "select _id, * from configuration";
        }
    }

    /**
     * 消息表
     */
    interface IMessage {
        String TABLE_NAME = "message";
        String ID = "_id";
        String CREATE_TIME = "create_time";
        String MESSAGE_ID = "message_id";
        String APPLICATION_ID = "application_id";
        String GROUPID = "group_id";
        String USER_ID = "user_id";
        String TITLE = "title";
        String CONTENT = "content";
        String RECEIVE_TIME = "receive_time";
        String EXTENSION = "extension";

        interface Sql {
            String FIND_ALL_MESSAGES = "select _id,* from message order by create_time desc";
            String FIND_LAST_MESSAGE_BY_APPLCATION_AND_USER =
                    "select _id,* from message where user_id = ? and application_id = ? " +
                            "order by create_time desc limit 1";
            String COUNT_BY_MESSAGE_ID = "select count(_id) from message where message_id = ?";
            String RETRIEVE_MESSAGE =
                    "select _id,* from message where user_id = ? and application_id = ? " +
                            "and create_time > ? order by create_time asc limit ?";
            String FIND_FIRST_MESSAGE_AFTER = "select _id, * from message where user_id  =? " +
                    "and application_id = ? and create_time > ? order by create_time asc limit 1";
        }
    }


    /**
     * 注册的应用表
     */
    interface IApplication {
        String TABLE_NAME = "application";
        String ID = "_id";
        String APPLICATION_ID = "application_id";
        String APPLICATION_NAME = "application_name";
        String NOTIFICATION_ICON = "notification_icon";
        String NOTIFICATION_INTENT = "notification_intent";

        interface Sql {
            String FIND_APPLICATION_BY_APPLICATION_ID = "select _id,* from application where application_id = ?";
            String FIND_APPLICATIONS = "select _id,* from application order by _id";
        }
    }

}