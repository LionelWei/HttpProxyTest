package com.egame.proxy.support.volley;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/5 1.00 初始版本
 */

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class EgameRequestQueue {
    RequestQueue mDelegate;

    public static RequestQueue requestQueueWithProxy(Context context) {
        return Volley.newRequestQueue(context, new OkHttpStack());
    }

    public void start() {
        mDelegate.start();
    }

    public void cancelAll(RequestQueue.RequestFilter filter) {
        mDelegate.cancelAll(filter);
    }

    public <T> void removeRequestFinishedListener(RequestQueue.RequestFinishedListener<T> listener) {
        mDelegate.removeRequestFinishedListener(listener);
    }

    public void stop() {
        mDelegate.stop();
    }

    public <T> Request<T> add(Request<T> request) {
        return mDelegate.add(request);
    }

    public <T> void addRequestFinishedListener(RequestQueue.RequestFinishedListener<T> listener) {
        mDelegate.addRequestFinishedListener(listener);
    }

    public Cache getCache() {
        return mDelegate.getCache();
    }

    public void cancelAll(Object tag) {
        mDelegate.cancelAll(tag);
    }

    public int getSequenceNumber() {
        return mDelegate.getSequenceNumber();
    }
}
