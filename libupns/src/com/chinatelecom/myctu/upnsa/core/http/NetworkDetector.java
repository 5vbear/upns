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

    private Context context;

    private ConnectivityManager connectivityManager;

    public NetworkDetector(Context context) {
        this.context = context;
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    /**
     * @return 是否已经连接
     */
    public boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED);
    }
}
