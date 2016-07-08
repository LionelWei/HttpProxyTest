package com.egame.proxy;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/1 1.00 初始版本
 */

import android.content.Context;

import com.android.volley.RequestQueue;
import com.egame.proxy.support.glide.GlideProxy;
import com.egame.proxy.support.okhttp.EgameOkHttpClient;
import com.egame.proxy.support.volley.EgameRequestQueue;
import com.egame.proxy.util.NetworkUtil;

import okhttp3.OkHttpClient;

public class EgameProxy {

    private static Context mContext;
    private static boolean mIsVerified = false;
    private static boolean mIsProxyEnabled = true;

    public static void init(Context context) {
        mContext = context;
        // 检查网络状态
        handleNetwork();
        // 查询订阅情况
        lookupSubscription();
    }

    public static void setProxy() {
        GlideProxy.init(mContext);
    }

    public static EgameOkHttpClient setOkHttpProxy(OkHttpClient oldClient) {
        return new EgameOkHttpClient(oldClient);
    }

    public static RequestQueue setVolleyProxy() {
        return EgameRequestQueue.requestQueueWithProxy(mContext);
    }

    public static void setProxyEnabled(boolean enabled) {
        mIsProxyEnabled = enabled;
    }

    public static boolean isProxyEnabled() {
        return mIsProxyEnabled;
    }

    public static Context getContext() {
        return mContext;
    }

    private static void lookupSubscription() {
        mIsVerified = true;
    }

    private static void handleNetwork() {
        int state = NetworkUtil.checkState(mContext);
        // 移动网络时 设置代理
        // WIFI时 切断代理直接访问原始服务器
        switch (state) {
            case NetworkUtil.NETWORK_WIFI:
                // !!! 仅供测试 实际状态与此相反 TODO
                EgameProxy.setProxyEnabled(true);
                break;
            case NetworkUtil.NETWORK_4G:
            case NetworkUtil.NETWORK_3G2G:
                // !!! 仅供测试 实际状态与此相反 TODO
                EgameProxy.setProxyEnabled(false);
                break;
        }
    }

}
