/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.core.http;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import com.chinatelecom.myctu.upnsa.core.lang.Transformer;
import com.chinatelecom.myctu.upnsa.core.sqlite.CursorTemplate;
import com.chinatelecom.myctu.upnsa.core.sqlite.CursorUtils;
import com.chinatelecom.myctu.upnsa.core.utils.Logger;
import com.chinatelecom.myctu.upnsa.core.utils.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 数据连接工具类
 *
 * @author snowway
 * @since Nov 18, 2010
 */
public class NetworkDetector {

    private static final Uri PREFER_APN = Uri.parse("content://telephony/carriers/preferapn");

    private static final String KEY_USER = "user";

    private static final String CTWAP_USER = "ctwap@mycdma.cn";

    private static final String CTNET_USER = "ctnet@mycdma.cn";


    private Context context;

    private ConnectivityManager connectivityManager;

    public NetworkDetector(Context context) {
        this.context = context;
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    /**
     * 连接类型
     */
    enum Type {
        NONE,
        WIFI,
        CTWAP,
        CTNET,
        OTHER,
    }


    /**
     * @return 是否已经连接
     */
    public boolean isConnected() {
        return getType() != Type.NONE;
    }

    private Type getType() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED);
        if (connected) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return Type.WIFI;
            } else { //2G,3G网络
                return getApnType();
            }
        }
        return Type.NONE;
    }

    /**
     * 获得当前的apn
     *
     * @return Type
     */
    private Type getApnType() {
        return CursorTemplate.one(this.context.getContentResolver().query(PREFER_APN, null, null, null, null),
                new Transformer<Cursor, Type>() {
                    @Override
                    public Type transform(Cursor input) {
                        String user = CursorUtils.getString(input, KEY_USER);
                        if (StringUtils.equals(user, CTWAP_USER)) {
                            return Type.CTWAP;
                        } else if (StringUtils.equals(user, CTNET_USER)) {
                            return Type.CTNET;
                        } else {
                            return Type.OTHER;
                        }
                    }
                });
    }


    private InetAddress getLocalInetAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    @SuppressWarnings("unused")
    private boolean isReachable(Type type) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //ignore
        }
        boolean result = isConnected();
        if (result) {
            try {
                InetAddress address = getLocalInetAddress();
                if (type == Type.CTWAP) {
                    result = address != null && address.isReachable(5000);
                } else if (type == Type.CTNET) {
                    result = address != null && address.isReachable(5000);
                }
            } catch (IOException e) {
                result = false;
            }
        }
        return result;
    }
}
