package com.egame.proxy;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/1 1.00 初始版本
 */

import android.content.Context;

import com.egame.proxy.support.ProxySocketFactory;
import com.egame.proxy.support.glide.GlideProxy;

import okhttp3.OkHttpClient;

public class EgameProxy {

    private static Context mContext;
    private static boolean mIsVerified = false;

    public static void init(Context context) {
        mContext = context;
        lookupSubscription();
    }

    public static OkHttpClient enableOkHttpProxy(OkHttpClient oldClient) {
        // 对于socks代理 new Proxy(SOCKS, ...) 不起作用
        // 得用socketFactory 可能是aosp的bug
        return oldClient
                .newBuilder()
//                .proxy(new Proxy(Proxy.Type.SOCKS, ProxyUtil.SOCKET_ADDRESS))
                .socketFactory(new ProxySocketFactory())
                .build();
    }

    public static void enableProxy(Context context) {
        GlideProxy.init(context);
    }

    private static void lookupSubscription() {
        mIsVerified = true;
    }

}
