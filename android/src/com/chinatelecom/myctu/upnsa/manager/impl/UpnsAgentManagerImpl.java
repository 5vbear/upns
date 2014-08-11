/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.manager.impl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.chinatelecom.myctu.upnsa.R;
import com.chinatelecom.myctu.upnsa.core.http.HttpTemplate;
import com.chinatelecom.myctu.upnsa.core.injection.Dependency;
import com.chinatelecom.myctu.upnsa.core.lang.Closure;
import com.chinatelecom.myctu.upnsa.core.lang.Transformer;
import com.chinatelecom.myctu.upnsa.core.sqlite.CursorTemplate;
import com.chinatelecom.myctu.upnsa.core.sqlite.CursorUtils;
import com.chinatelecom.myctu.upnsa.core.sqlite.SqliteCallback;
import com.chinatelecom.myctu.upnsa.core.sqlite.SqliteTemplate;
import com.chinatelecom.myctu.upnsa.core.utils.BitmapUtils;
import com.chinatelecom.myctu.upnsa.core.utils.Logger;
import com.chinatelecom.myctu.upnsa.core.utils.StringUtils;
import com.chinatelecom.myctu.upnsa.manager.MessageRetrieveResult;
import com.chinatelecom.myctu.upnsa.manager.UpnsAgentManager;
import com.chinatelecom.myctu.upnsa.model.Application;
import com.chinatelecom.myctu.upnsa.model.Configuration;
import com.chinatelecom.myctu.upnsa.model.Message;
import com.chinatelecom.myctu.upnsa.model.Routing;
import com.chinatelecom.myctu.upnsa.sqlite.Schema;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: snowway
 * Date: 9/9/13
 * Time: 4:10 PM
 */
public class UpnsAgentManagerImpl extends AbstractUpnsManager implements UpnsAgentManager, Schema {

    /**
     * android 设备类型
     */
    public static final String DEVICE_TYPE_ANDROID = "1";

    private ApplicationInfo applicationInfo;

    @Dependency
    protected SqliteTemplate sqliteTemplate;

    @Dependency
    protected TelephonyManager telephonyManager;

    @Dependency
    protected NotificationManager notificationManager;

    private Context context;


    public UpnsAgentManagerImpl(Context context) {
        this.context = context;
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            applicationInfo =
                    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException ex) {
            Logger.error("不能获取META信息:" + ex.getMessage(), ex);
            throw new IllegalArgumentException("必须在AndroidManafest.xml中设置<application>元素的<meta-data>子元素");
        }
    }

    /**
     * 自动注册应用
     */
    @Override
    public void autoRegisterApplications() {
        String targetPackage = applicationInfo.metaData.getString("targetPackage");
        //首先清空已经注册的应用数据
        sqliteTemplate.delete(IApplication.TABLE_NAME, null, null);
        Logger.debug("已清空所有已经注册的应用");
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applications =
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo application : applications) {
            String packageName = application.packageName;
            if (packageName.toLowerCase().startsWith(targetPackage.toLowerCase())) {
                Logger.debug("查找到设备安装了应用:" + packageName);
                Drawable drawable = application.loadIcon(packageManager);
                Application model = new Application();
                Object applicationIdValue = application.metaData != null ?
                        application.metaData.get("applicationId") : null;
                if (applicationIdValue != null && StringUtils.isNotBlank(applicationIdValue.toString())) {
                    model.setApplicationId(applicationIdValue.toString());
                    model.setApplicationName(application.loadLabel(packageManager).toString());
                    model.setNotificationIntent(application.metaData.getString("notificationIntent"));
                    model.setNotificationIcon(BitmapUtils.toByteArray(BitmapUtils.drawableToBitmap(drawable)));
                    registerApplication(model);
                } else{
                    Logger.warn("忽略应用:" + application.loadLabel(packageManager)+"注册,原因是未配置meta的applicationId属性");
                }
            }
        }
    }

    /**
     * 管理设置项
     *
     * @param key   key
     * @param value value
     */
    private void settingConfiguration(String key, String value) {
        sqliteTemplate.delete(IConfiguration.TABLE_NAME, IConfiguration.KEY + "=?", new String[]{key});
        ContentValues values = new ContentValues();
        values.put(IConfiguration.KEY, key);
        values.put(IConfiguration.VALUE, value);
        sqliteTemplate.insert(IConfiguration.TABLE_NAME, values, null);
    }

    @Override
    public void userAuthenticated(String userId) {
        String registerUrl = String.format(applicationInfo.metaData.getString("registerUrl"),
                userId, telephonyManager.getDeviceId(), DEVICE_TYPE_ANDROID);
        settingConfiguration(Configuration.KEY_REGISTER_URL, registerUrl);
        settingConfiguration(Configuration.KEY_USER_ID, userId);
    }

    @Override
    public Configuration getConfiguration() {
        Cursor cursor = sqliteTemplate.query(IConfiguration.Sql.FIND_CONFIGURATION, null);
        final Configuration model = new Configuration();
        CursorTemplate.each(cursor, new Closure<Cursor>() {
            @Override
            public void execute(Cursor input) {
                String key = CursorUtils.getString(input, IConfiguration.KEY);
                String value = CursorUtils.getString(input, IConfiguration.VALUE);
                if (StringUtils.equals(Configuration.KEY_REGISTER_URL, key)) {
                    model.setRegisterUrl(value);
                } else if (StringUtils.equals(Configuration.KEY_USER_ID, key)) {
                    model.setUserId(value);
                }
            }
        });
        return model;
    }

    /**
     * @return HttpTemplate
     */
    protected HttpTemplate getHttpTemplate() {
        return new HttpTemplate();
    }


    @Override
    public Routing getRouting(final String userId) {
        return getHttpTemplate().execute(new UpnsHttpCallback<Routing>() {
            @Override
            protected Routing onSuccessfulResponse(HttpResponse response) throws Exception {
                String content = getResponseAsString(response);
                JSONObject json = new JSONObject(content);
                    /*
                    {"_id":"53abfbc0-85bf-4818-b65c-76616b4bdd41","u":"snowway",
                    "o":"A0000044C84CC5","t":0,"h":"127.0.0.1","p":8156,"e":1378716653595}
                    */
                Routing routing = new Routing();
                routing.setId(json.getString("_id"));
                routing.setUserId(json.getString("u"));
                routing.setDeviceToken(json.getString("o"));
                routing.setDeviceType(json.getInt("t"));
                routing.setHost(json.getString("h"));
                routing.setPort(json.getInt("p"));
                routing.setExpireTime(json.getLong("e"));
                return routing;
            }

            @Override
            protected HttpRequestBase getHttpMethod() {
                return new HttpGet(getConfiguration().getRegisterUrl());
            }
        });
    }


    /**
     * 保存MessageModel对象
     *
     * @param message
     * @return 会话id
     */
    private void persistMessage(Message message) {
        if (isMessageExists(message.getMessageId())) {
            Logger.warn(String.format("id为:%s的消息已存在,本次忽略保存", message.getMessageId()));
        } else {
            ContentValues values = new ContentValues();
            values.put(IMessage.MESSAGE_ID, message.getMessageId());
            values.put(IMessage.CREATE_TIME, message.getCreateTime());
            values.put(IMessage.RECEIVE_TIME, System.currentTimeMillis());
            values.put(IMessage.APPLICATION_ID, message.getApplicationId());
            values.put(IMessage.GROUPID, message.getGroupId());
            values.put(IMessage.TITLE, message.getTitle());
            values.put(IMessage.CONTENT, message.getContent());
            values.put(IMessage.USER_ID, message.getUserId());
            values.put(IMessage.EXTENSION, message.getExtensionAsByteArray());
            long id = sqliteTemplate.insert(IMessage.TABLE_NAME, values, null);
            message.setId(id);
            Logger.debug(String.format("成功保存id为:%s的消息", message.getMessageId()));
        }
    }


    @Override
    public Message createMessage(Cursor cursor) {
        Message message = new Message();
        message.setId(CursorUtils.getLong(cursor, IMessage.ID));
        message.setMessageId(CursorUtils.getString(cursor, IMessage.MESSAGE_ID));
        message.setApplicationId(CursorUtils.getString(cursor, IMessage.APPLICATION_ID));
        message.setCreateTime(CursorUtils.getLong(cursor, IMessage.CREATE_TIME));
        message.setReceiveTime(CursorUtils.getLong(cursor, IMessage.RECEIVE_TIME));
        message.setGroupId(CursorUtils.getString(cursor, IMessage.GROUPID));
        message.setTitle(CursorUtils.getString(cursor, IMessage.TITLE));
        message.setContent(CursorUtils.getString(cursor, IMessage.CONTENT));
        message.setUserId(CursorUtils.getString(cursor, IMessage.USER_ID));
        message.setExtension(CursorUtils.getBlob(cursor, IMessage.EXTENSION));
        return message;
    }


    /**
     * 指定的消息id是否存在
     */
    private boolean isMessageExists(String messageId) {
        Cursor cursor = sqliteTemplate.query(IMessage.Sql.COUNT_BY_MESSAGE_ID, new String[]{messageId});
        int count = CursorTemplate.one(cursor, new Transformer<Cursor, Integer>() {
            @Override
            public Integer transform(Cursor input) {
                return input.getInt(0);
            }
        });
        return count > 0;
    }

    @Override
    public void receiveMessage(Message message) {
        persistMessage(message);
        createNotification(message.getUserId(), message.getApplicationId());
    }

    @Override
    public void receiveMessages(final List<Message> messages) {
        sqliteTemplate.execute(new SqliteCallback<Object>() {
            @Override
            public Object doInSqlite(SQLiteDatabase database) {
                database.beginTransaction();
                for (Message message : messages) {
                    persistMessage(message);
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                return null;
            }
        });
    }


    /**
     * 获取某用户某应用下最新的消息
     *
     * @param userId        用户Id
     * @param applicationId 应用Id
     * @return 消息对象
     */
    private Message findLastMessage(String userId, String applicationId) {
        Cursor cursor = sqliteTemplate.query(IMessage.Sql.FIND_LAST_MESSAGE_BY_APPLCATION_AND_USER,
                new String[]{userId, applicationId});
        return CursorTemplate.one(cursor, new Transformer<Cursor, Message>() {
            @Override
            public Message transform(Cursor input) {
                return createMessage(input);
            }
        });
    }

    /**
     * 创建NotificationItem
     *
     * @return NotificationItem
     */
    private NotificationItem createNotificationItem(String userId, String applicationId) {
        NotificationItem item = new NotificationItem();
        item.setApplicationId(applicationId);
        item.setNotificationId(applicationId.hashCode());
        item.setTitle("您有新的未读消息");
        Message lastMessage = findLastMessage(userId, applicationId);
        item.setContent(lastMessage.getTitle());
        item.setExtension(lastMessage.getExtension() != null ?
                new HashMap(lastMessage.getExtension()) : new HashMap());
        return item;
    }



    /**
     * 创建默认任务栏提醒
     */
    private void createNotification(String userId, String applicationId){
        Application application = findApplication(applicationId);
        if (application == null) {
            Logger.info(String.format("未能找到applicationId:%s的应用注册信息," +
                    "本次不弹出任务栏提示", applicationId));
            return;
        }
        NotificationItem item = createNotificationItem(userId, applicationId);
        Notification notification = new Notification(R.drawable.message_notification,"您有新的未读消息:" +
                item.getContent(),System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_ALL;

        RemoteViews remoteView = new RemoteViews(applicationInfo.packageName,R.layout.notification);
        remoteView.setImageViewBitmap(R.id.applicationView, BitmapUtils.fromByteArray(application.getNotificationIcon()));
        remoteView.setTextViewText(R.id.titleView, item.getTitle());
        remoteView.setTextViewText(R.id.contentView,item.getContent());
        remoteView.setTextViewText(R.id.receiveTimeView,TIME_FORMAT.format(new Date(System.currentTimeMillis())));
        notification.contentView = remoteView;

        Intent targetIntent = new Intent(application.getNotificationIntent());
        targetIntent.putExtra("extension", item.getExtension());
        PendingIntent intent =
                PendingIntent.getActivity(context, item.getNotificationId(),
                        targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = intent;
        notificationManager.notify(item.getNotificationId(), notification);
    }


    public final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    /**
     * 创建默认任务栏提醒
     */
    private void _createNotification(String userId, String applicationId) {
        Application application = findApplication(applicationId);
        if (application == null) {
            Logger.info(String.format("未能找到applicationId:%s的应用注册信息," +
                    "本次不弹出任务栏提示", applicationId));
            return;
        }
        NotificationItem item = createNotificationItem(userId, applicationId);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.message_notification)
                        .setLargeIcon(BitmapUtils.fromByteArray(application.getNotificationIcon()))
                        .setContentTitle(item.getTitle())
                        .setContentText(item.getContent());
        Intent targetIntent = new Intent(application.getNotificationIntent());
        targetIntent.putExtra("extension", item.getExtension());
        PendingIntent intent =
                PendingIntent.getActivity(context, item.getNotificationId(),
                        targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;
        notificationManager.notify(item.getNotificationId(), notification);
    }


    /**
     * 从Sqlite获取ApplicationModel
     */
    private Application findApplication(String applicationId) {
        Cursor cursor = sqliteTemplate.query(IApplication.Sql.FIND_APPLICATION_BY_APPLICATION_ID,
                new String[]{applicationId});
        return CursorTemplate.one(cursor, new Transformer<Cursor, Application>() {
            @Override
            public Application transform(Cursor input) {
                Application model = new Application();
                model.setId(CursorUtils.getLong(input, IApplication.ID));
                model.setApplicationId(CursorUtils.getString(input, IApplication.APPLICATION_ID));
                model.setApplicationName(CursorUtils.getString(input, IApplication.APPLICATION_NAME));
                model.setNotificationIcon(CursorUtils.getBlob(input, IApplication.NOTIFICATION_ICON));
                model.setNotificationIntent(CursorUtils.getString(input, IApplication.NOTIFICATION_INTENT));
                return model;
            }
        });
    }

    @Override
    public Cursor findMessages() {
        return sqliteTemplate.query(IMessage.Sql.FIND_ALL_MESSAGES, null);
    }

    @Override
    public void clearMessages() {
        sqliteTemplate.delete(IMessage.TABLE_NAME, null, null);
    }

    @Override
    public void registerApplication(Application model) {
        sqliteTemplate.delete(IApplication.TABLE_NAME, IApplication.APPLICATION_ID + "=?",
                new String[]{model.getApplicationId()});
        ContentValues values = new ContentValues();
        values.put(IApplication.APPLICATION_ID, model.getApplicationId());
        values.put(IApplication.APPLICATION_NAME, model.getApplicationName());
        values.put(IApplication.NOTIFICATION_ICON, model.getNotificationIcon());
        values.put(IApplication.NOTIFICATION_INTENT, model.getNotificationIntent());
        sqliteTemplate.insert(IApplication.TABLE_NAME, values, null);
        Logger.debug("成功注册应用:" + model.getApplicationName());
    }

    @Override
    public void finishReceiveMessages(String userId, String applicationId) {
        createNotification(userId, applicationId);
    }

    @Override
    public MessageRetrieveResult retrieveMessages(String userId, String applicationId, long timestamp, int size) {
        MessageRetrieveResult result = new MessageRetrieveResult();
        Cursor cursor = sqliteTemplate.query(IMessage.Sql.RETRIEVE_MESSAGE, new String[]{
                userId, applicationId, String.valueOf(timestamp), String.valueOf(size)//多获取一条消息
        });
        final List<Message> messages = new ArrayList<Message>();
        CursorTemplate.each(cursor, new Closure<Cursor>() {
            @Override
            public void execute(Cursor input) {
                messages.add(createMessage(input));
            }
        });
        long firstTimestamp = messages.isEmpty() ? timestamp : messages.get(messages.size() - 1).getCreateTime();
        Message firstMessage = findFirstMessageAfter(userId, applicationId, firstTimestamp);
        result.setMessages(messages);
        result.setHasMore(messages.size() == size && firstMessage != null);
        result.setTimestamp(firstTimestamp);
        return result;
    }

    /**
     * 查找某用户某应用下位于某时间下的首条消息
     */
    private Message findFirstMessageAfter(String userId, String applicationId, long timestamp) {
        Cursor cursor = sqliteTemplate.query(IMessage.Sql.FIND_FIRST_MESSAGE_AFTER,
                new String[]{userId, applicationId, String.valueOf(timestamp)});
        return CursorTemplate.one(cursor, new Transformer<Cursor, Message>() {
            @Override
            public Message transform(Cursor input) {
                return createMessage(input);
            }
        });
    }

    @Override
    public void removeMessageByIds(final String[] messageIds) {
        if (messageIds == null || messageIds.length == 0) {
            return;
        }
        String[] array = new String[messageIds.length];
        Arrays.fill(array, "?");
        sqliteTemplate.delete(IMessage.TABLE_NAME, IMessage.MESSAGE_ID + " in (" + TextUtils.join(",", array) + ")", messageIds);
        Logger.debug("成功删除消息Id为:" + Arrays.asList(messageIds) + "的消息");
    }

    @Override
    public Cursor findRegisteredApplications() {
        return sqliteTemplate.query(IApplication.Sql.FIND_APPLICATIONS, null);
    }

    @Override
    public Application createApplication(Cursor cursor) {
        Application application = new Application();
        application.setId(CursorUtils.getLong(cursor, IApplication.ID));
        application.setApplicationName(CursorUtils.getString(cursor, IApplication.APPLICATION_NAME));
        application.setApplicationId(CursorUtils.getString(cursor, IApplication.APPLICATION_ID));
        application.setNotificationIcon(CursorUtils.getBlob(cursor, IApplication.NOTIFICATION_ICON));
        application.setNotificationIntent(CursorUtils.getString(cursor, IApplication.NOTIFICATION_INTENT));
        return application;
    }
}
