/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa;

import android.app.NotificationManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import com.chinatelecom.myctu.upnsa.core.http.NetworkDetector;
import com.chinatelecom.myctu.upnsa.core.injection.DependencyInjectingObjectFactory;
import com.chinatelecom.myctu.upnsa.core.sqlite.DefaultSqliteTemplateFactory;
import com.chinatelecom.myctu.upnsa.core.sqlite.SqliteTemplate;
import com.chinatelecom.myctu.upnsa.manager.UpnsAgentManager;
import com.chinatelecom.myctu.upnsa.manager.impl.UpnsAgentManagerImpl;
import com.chinatelecom.myctu.upnsa.protocol.PacketFactory;
import com.chinatelecom.myctu.upnsa.protocol.PacketListener;
import com.chinatelecom.myctu.upnsa.protocol.PacketTransfer;
import com.chinatelecom.myctu.upnsa.protocol.support.DefaultPacketListener;
import com.chinatelecom.myctu.upnsa.protocol.support.JsonPacketFactory;
import com.chinatelecom.myctu.upnsa.protocol.support.XSocketPacketTransfer;
import com.chinatelecom.myctu.upnsa.sqlite.UpnsAgentSqliteHelper;


/**
 * User: snowway
 * Date: 7/30/13
 * Time: 11:07 AM
 */
public class DependencyFactory {

    private static final DependencyFactory INSTANCE = new DependencyFactory();

    private DependencyInjectingObjectFactory factory;

    public static DependencyFactory getInstance() {
        return INSTANCE;
    }


    public void initialize(Context context) {
        factory = new DependencyInjectingObjectFactory();
        factory.registerImplementationObject(SqliteTemplate.class,
                new DefaultSqliteTemplateFactory(
                        new UpnsAgentSqliteHelper(context)).getSqliteTemplate());
        factory.registerImplementationObject(TelephonyManager.class,
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        factory.registerImplementationObject(NotificationManager.class,
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        factory.registerImplementationObject(NetworkDetector.class, new NetworkDetector(context));
        factory.registerSingletonImplementationClass(PacketFactory.class, JsonPacketFactory.class);
        factory.registerImplementationObject(UpnsAgentManager.class,
                new UpnsAgentManagerImpl(context));
        factory.registerSingletonImplementationClass(PacketListener.class, DefaultPacketListener.class);
        factory.registerSingletonImplementationClass(PacketTransfer.class, XSocketPacketTransfer.class);
        getUpnsManager().autoRegisterApplications();
    }


    public void destroy() {
        getSqliteTemplate().close();
        factory = null;
    }

    public <T> T getObject(Class<T> type) {
        return factory.getObject(type);
    }


    public UpnsAgentManager getUpnsManager() {
        return factory.getObject(UpnsAgentManager.class);
    }

    public SqliteTemplate getSqliteTemplate() {
        return factory.getObject(SqliteTemplate.class);
    }

    public NetworkDetector getNetworkDetector() {
        return factory.getObject(NetworkDetector.class);
    }

    public PacketListener getPacketListener() {
        return factory.getObject(PacketListener.class);
    }

    public PacketTransfer getPacketTransfer() {
        return factory.getObject(PacketTransfer.class);
    }
}
