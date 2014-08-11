package com.chinatelecom.myctu.upnsa.receiver;
/* Copyright © 2010 www.myctu.cn. All rights reserved. */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.chinatelecom.myctu.upnsa.DependencyFactory;
import com.chinatelecom.myctu.upnsa.core.http.NetworkDetector;
import com.chinatelecom.myctu.upnsa.core.utils.Logger;
import com.chinatelecom.myctu.upnsa.service.UpnsAgentService;

/**
 * UpnsAgentReceiver广播接收
 * <p/>
 * User: snowway
 * Date: 10/8/13
 * Time: 10:26 PM
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private NetworkDetector detector = DependencyFactory.getInstance().getNetworkDetector();


    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.debug("收到广播通知:" + intent + ",准备启动UpnsAgentService");
        if (detector.isConnected()) {
            Logger.debug("当前已成功连接至网络,启动UpnsAgentService");
            context.startService(new Intent(UpnsAgentService.INTENT_UPNS_AGENT_API));
        }
    }
}
