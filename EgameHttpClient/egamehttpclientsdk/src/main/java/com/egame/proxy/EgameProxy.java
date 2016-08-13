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
import com.egame.proxy.support.okhttp.EgameOkHttpClient;
import com.egame.proxy.support.volley.EgameRequestQueue;

import okhttp3.OkHttpClient;

// 暴露给CP的接口
public class EgameProxy {
    private static EgameProxy sInstance = new EgameProxy();
    private Context mContext;
    private EgameProxyInternal mInternal;

    // 单例
    private EgameProxy() {
    }

    public static EgameProxy get() {
        return sInstance;
    }

    /**
     * 初始化sdk
     */
    public void init(Context context) {
        mContext = context.getApplicationContext();
        mInternal = EgameProxyInternal.get();
    }

    /**
     * 获取经sdk封装过的okHttpClient
     */
    public EgameOkHttpClient getEgameOkHttpClient(OkHttpClient oldClient) {
        return new EgameOkHttpClient(oldClient);
    }

    /**
     * 获取封装过的Volley requestQueue
     */
    public RequestQueue getEgameRequestQueue() {
        return EgameRequestQueue.requestQueueWithProxy(mContext);
    }

    /**
     * 设置app id
     */
    public void setAppId(String appId) {
        mInternal.setAppId(appId);
    }

    /**
     * 设置用户名
     */
    public void setUserId(String userId) {
        mInternal.setUserId(userId);
    }

    public Context getContext() {
        return mContext;
    }
}
