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
import com.egame.proxy.support.okhttp.OkHttpProxy;
import com.egame.proxy.support.volley.VolleyProxy;

import okhttp3.OkHttpClient;

public class EgameProxy {

    private static Context mContext;
    private static boolean mIsVerified = false;

    public static void init(Context context) {
        mContext = context;
        lookupSubscription();
    }

    public static void enableProxy() {
        GlideProxy.init(mContext);
    }

    public static OkHttpClient enableOkHttpProxy(OkHttpClient oldClient) {
        return OkHttpProxy.clientWithProxy(oldClient);
    }

    public static RequestQueue enableVolleyProxy() {
        return VolleyProxy.requestQueueWithProxy(mContext);
    }


    private static void lookupSubscription() {
        mIsVerified = true;
    }

}
