package com.chinatelecom.myctu.upnsa.service;
/* Copyright © 2010 www.myctu.cn. All rights reserved. */

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.format.DateUtils;
import com.chinatelecom.myctu.upnsa.DependencyFactory;
import com.chinatelecom.myctu.upnsa.core.utils.Logger;
import com.chinatelecom.myctu.upnsa.core.utils.StringUtils;
import com.chinatelecom.myctu.upnsa.manager.MessageRetrieveResult;
import com.chinatelecom.myctu.upnsa.model.Application;
import com.chinatelecom.myctu.upnsa.model.Configuration;
import com.chinatelecom.myctu.upnsa.model.Message;
import com.chinatelecom.myctu.upnsa.protocol.PacketTransfer;
import com.chinatelecom.myctu.upnsa.remote.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * UpnsAgentService
 * <p/>
 * User: snowway
 * Date: 10/2/13
 * Time: 11:34 AM
 */
public class UpnsAgentService extends Service {

    /**
     * UpnsAgentApi Intent
     */
    public static final String INTENT_UPNS_AGENT_API =
            "com.chinatelecom.myctu.upnsa.service.UpnsAgentApi";


    public static final String LOGIN_ACTION = "com.chinatelecom.myctu.mobilebase.upns.login";

    public static final String LOGOUT_ACTION = "com.chinatelecom.myctu.mobilebase.upns.logout";


    /**
     * 未预料异常code
     */
    public static final String ERROR_UNEXPECTED = "UNEXPECTED";

    /**
     * DependencyFactory
     */
    private DependencyFactory factory = DependencyFactory.getInstance();

    /**
     * 所有回调方法
     */
    private List<UpnsAgentListener> upnsAgentListeners = new ArrayList<UpnsAgentListener>();


    /**
     * 是否用户主动断开连接
     */
    private static boolean activeCloseConnection = false;


    /**
     * 获得PacketTransfer
     *
     * @return
     */
    protected PacketTransfer getPacketTransfer() {
        return factory.getPacketTransfer();
    }


    protected ConnectivityManager connectivityManager;

    protected static AlarmManager alarmManager;


    /**
     * UpnsAgentListenerCaller
     */
    private interface UpnsAgentListenerCaller {
        void call(UpnsAgentListener listener) throws RemoteException;
    }

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    /**
     * 通知UpnsAgentListener
     *
     * @param caller
     */
    private void fireUpnsListeners(UpnsAgentListenerCaller caller) {
        for (UpnsAgentListener listener : upnsAgentListeners) {
            if (listener != null) {
                try {
                    caller.call(listener);
                } catch (RemoteException ex) {
                    Logger.error("回调UpnsAgentListener出错," + ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * UpnsAgentApi.Stub,AIDL回调接口
     */
    private UpnsAgentApi.Stub upnsAgentApi = new UpnsAgentApi.Stub() {

        @Override
        public boolean isConnected() throws RemoteException {
            return getPacketTransfer().isConnected();
        }

        @Override
        public void disconnect() throws RemoteException {
            activeCloseConnection = true;
            getPacketTransfer().disconnect();
        }

        @Override
        public void addListener(UpnsAgentListener listener) throws RemoteException {
            if (listener != null) {
                upnsAgentListeners.add(listener);
            }
        }

        @Override
        public void removeListener(UpnsAgentListener listener) throws RemoteException {
            if (listener != null && upnsAgentListeners.contains(listener)) {
                upnsAgentListeners.remove(listener);
            }
        }

        @Override
        public String getCurrentUserId() throws RemoteException {
            return isConnected() ? getPacketTransfer().getCurrentUser() : null;
        }

        @Override
        public boolean registerApplication(UpnsApplication applicationInfo) throws RemoteException {
            Application model = new Application();
            model.setApplicationId(applicationInfo.getApplicationId());
            model.setApplicationName(applicationInfo.getApplicationName());
            model.setNotificationIcon(applicationInfo.getNotificationIcon());
            model.setNotificationIntent(applicationInfo.getNotificationItent());
            factory.getUpnsManager().registerApplication(model);
            return true;
        }

        @Override
        public void userAuthenticated(String userId) throws RemoteException {
            activeCloseConnection = false;
            connect(userId);
        }

        @Override
        public void retrieveMessages(String applicationId, long timestamp, int size, UpnsMessageCallback callback) throws RemoteException {
            MessageRetrieveResult result = factory.getUpnsManager().retrieveMessages(
                    getPacketTransfer().getCurrentUser(),
                    applicationId, timestamp, size);
            List<UpnsMessage> upnsMessages = new ArrayList<UpnsMessage>();
            for (Message message : result.getMessages()) {
                upnsMessages.add(createUpnsMessage(message));
            }
            Logger.info(String.format("返回客户端需要的消息,条数:%d,是否有更多:%s,timestamp:%s", upnsMessages.size(),
                    result.isHasMore(), result.getTimestamp()));
            callback.onMessages(upnsMessages, result.isHasMore(), result.getTimestamp());
        }


        @Override
        public void messageRetrieved(String[] messageIds) throws RemoteException {
            factory.getUpnsManager().removeMessageByIds(messageIds);
        }

        /**
         * 从MessageModel创建UpnsMessage
         *
         * @param model Message Model Packet
         * @return UpnsMessage
         */
        private UpnsMessage createUpnsMessage(Message model) {
            UpnsMessage message = new UpnsMessage();
            message.setMessageId(model.getMessageId());
            message.setApplicationId(model.getApplicationId());
            message.setGroupId(model.getGroupId());
            message.setTitle(model.getTitle());
            message.setContent(model.getContent());
            message.setCreateTime(model.getCreateTime());
            message.setReceiveTime(model.getReceiveTime());
            message.setExtension(model.getExtension());
            return message;
        }
    };


    /**
     * 登录监听器
     */
    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.equals(LOGIN_ACTION, action)) {
                String userId = intent.getStringExtra("userid");
                Logger.info("收到登录广播,用户id为:" + userId);
                connect(userId);
            }
        }
    };

    private BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.equals(LOGOUT_ACTION, action)) {
                Logger.info("收到注销广播,断开连接");
                getPacketTransfer().disconnect();
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        connectivityManager = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (alarmManager == null) {
            alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0,
                    new Intent(UpnsAgentService.INTENT_UPNS_AGENT_API), 0);
            long interval = DateUtils.SECOND_IN_MILLIS * 60;//60s检测
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, interval, pendingIntent);
        }
        registerReceiver(loginReceiver, new IntentFilter(LOGIN_ACTION));
        registerReceiver(logoutReceiver, new IntentFilter(LOGOUT_ACTION));

//        setForeground(true);
//        Notification notification = new Notification();
//        startForeground(1, notification);

        doStartService();
    }

    @Override
    public void onDestroy() {
//        unregisterReceiver(loginReceiver);
//        unregisterReceiver(logoutReceiver);
//        getPacketTransfer().disconnect();
//        super.onDestroy();
        Intent localIntent = new Intent();
        localIntent.setClass(this, UpnsAgentService.class); //销毁时重新启动Service
        this.startService(localIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.debug("UpnsAgentService绑定了" + intent);
        if (StringUtils.equals(INTENT_UPNS_AGENT_API, intent.getAction())) {
            return upnsAgentApi;
        }
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;//START_STICKY是service被kill掉后自动重写创建
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        doStartService();
    }

    /**
     * 启动服务
     */
    private void doStartService() {
        if (!activeCloseConnection && !getPacketTransfer().isConnected()) {
            Configuration model = factory.getUpnsManager().getConfiguration(null);
            String userId = model != null ? model.getUserId() : null;
            if (StringUtils.isBlank(userId)) {
                Logger.warn("未获取到登录用户,拒绝尝试连接");
            } else {
                activeCloseConnection = false;
                connect(userId);
            }
        }
    }

    private void connect(String userId) {
        Logger.debug("使用用户:" + userId + "异步连接服务器");
        executor.submit(new ConnectionRunnable(userId));
    }


    /**
     * @return 是否已经连接
     */
    public boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED);
    }

    private class ConnectionRunnable implements Runnable {

        private String userId;

        private ConnectionRunnable(String userId) {
            this.userId = userId;
        }

        @Override
        public void run() {
            getPacketTransfer().connect(userId, eventListener);
        }
    }


    /**
     * PacketTransfer.EventListener
     */
    private PacketTransfer.EventListener eventListener = new PacketTransfer.EventListener() {
        @Override
        public void on(final PacketTransfer.EventType type, final Object... params) {
            fireUpnsListeners(new UpnsAgentListenerCaller() {
                @Override
                public void call(UpnsAgentListener listener) throws RemoteException {
                    if (type == PacketTransfer.EventType.CONNECTED) {
                        listener.onConnected(getPacketTransfer().getCurrentUser());
                    } else if (type == PacketTransfer.EventType.DISCONNECTED) {
                        listener.onDisconnected();
                    } else if (type == PacketTransfer.EventType.MESSAGE) {
                        listener.onMessage((String) params[0], (String) params[1]);
                    } else if (type == PacketTransfer.EventType.FINISH_HISTORY) {
                        listener.onFinishHistory((String) params[0]);
                    } else if (type == PacketTransfer.EventType.FAULT) {
                        listener.onFault((String) params[0], (String) params[1]);
                    }
                }
            });
        }
    };
}