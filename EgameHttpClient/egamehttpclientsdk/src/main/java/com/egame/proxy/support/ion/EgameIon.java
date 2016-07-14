package com.egame.proxy.support.ion;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/5 1.00 初始版本
 */

import android.content.Context;
import android.util.Log;

import com.egame.proxy.EgameProxy;
import com.egame.proxy.EgameProxyInternal;
import com.egame.proxy.server.EgameProxySelector;
import com.egame.proxy.util.ProxyUtil;
import com.koushikdutta.ion.Ion;

import java.net.InetSocketAddress;

public class EgameIon {
    public static void init(Context context) {
        try {
            Class.forName("com.koushikdutta.ion.Ion");
            doInit(context);
        } catch (ClassNotFoundException e) {
            Log.d(ProxyUtil.TAG, "Ion is invalid");
        }
    }

    private static void doInit(Context context) {
        boolean enableProxy = false;
        if (EgameProxyInternal.get().isProxyAvailable()) {
            InetSocketAddress socketAddress = EgameProxySelector.get().getOptimalHttpProxy();
            if (socketAddress != null) {
                enableProxy = true;
                String ipAddress = socketAddress.getHostName();
                int port = socketAddress.getPort();
                Ion.getDefault(context)
                        .configure()
                        .proxy(ipAddress, port);

            }
        }
        if (!enableProxy) {
            Ion.getDefault(context)
                    .configure()
                    .disableProxy();
        }
    }
}
