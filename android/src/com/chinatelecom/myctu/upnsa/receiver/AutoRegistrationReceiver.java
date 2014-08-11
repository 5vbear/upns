package com.chinatelecom.myctu.upnsa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.chinatelecom.myctu.upnsa.DependencyFactory;
import com.chinatelecom.myctu.upnsa.core.utils.Logger;

/**
 * 自动包注册监听器
 * <p/>
 * User: snowway
 * Date: 11/22/13
 * Time: 11:13 AM
 */
public class AutoRegistrationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.debug("收到广播通知:" + intent + ",重新注册应用");
        DependencyFactory.getInstance().getUpnsManager().autoRegisterApplications();
    }
}
