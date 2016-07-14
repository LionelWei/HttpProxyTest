package com.egame.proxy.support.okhttp;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/8 1.00 初始版本
 */

import com.egame.proxy.EgameProxy;
import com.egame.proxy.EgameProxyInternal;
import com.egame.proxy.exception.EgameProxyException;
import com.egame.proxy.event.INetworkStateListener;
import com.egame.proxy.event.NetworkEventBus;
import com.egame.proxy.server.HostService;
import com.egame.proxy.util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EgameCall implements INetworkStateListener{
    private Call mDelegate;
    private EgameOkHttpClient mClient;
    private Request mRequest;

    // 只有异步调用才需要队列
    // 同步的直接在调用时处理
    private static List<EgameCall> sAsyncCallQueue = new ArrayList<>();

    public EgameCall(Call call, Request request, EgameOkHttpClient client) {
        this.mDelegate = call;
        mRequest = request;
        mClient = client;
    }

    public Request request() {
        return mDelegate.request();
    }

    public Response execute() throws EgameProxyException, IOException {
        checkProxy();
        while(HostService.isPoolLocked || HostService.isDataLocked) {
            // wait
        }
        OkHttpClient newClient = mClient.clientWithProxy(mClient.delegate());
        mDelegate = newClient.newCall(mRequest);
        return mDelegate.execute();
    }

    public boolean isExecuted() {
        return mDelegate.isExecuted();
    }

    public void enqueue(final Callback responseCallback) throws EgameProxyException {
        cancelCallWhenMobileWithoutProxy();
        checkProxy();
        // 由于要等待notify, 所以另起一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(HostService.isPoolLocked || HostService.isDataLocked) {
//                    Log.d(ProxyUtil.TAG, "waiting...");
                    // wait
                }
                OkHttpClient newClient = mClient.clientWithProxy(mClient.delegate());
                mDelegate = newClient.newCall(mRequest);
                sAsyncCallQueue.add(EgameCall.this);
                mDelegate.enqueue(responseCallback);
            }
        }).start();
    }

    public boolean isCanceled() {
        return mDelegate.isCanceled();
    }

    public void cancel() {
        mDelegate.cancel();
    }

    @Override
    public void onNetworkStateChanged(int state) {
        if (state != NetworkUtil.NETWORK_WIFI) {
            cancelCallWhenMobileWithoutProxy();
        }
    }

    private void checkProxy() throws EgameProxyException {
        int netState = NetworkUtil.checkState(EgameProxy.get().getContext());
        if (netState != NetworkUtil.NETWORK_WIFI
                && !EgameProxyInternal.get().isProxyAvailable()) {
            throw new EgameProxyException();
        }
    }

    private void cancelCallWhenMobileWithoutProxy() {
        NetworkEventBus.getDefault().register(this);
        int netState = NetworkUtil.checkState(EgameProxy.get().getContext());
        Iterator<EgameCall> i = sAsyncCallQueue.iterator();
        while (i.hasNext()) {
            EgameCall call = i.next();
            if (call == null) {
                i.remove();
                continue;
            }

            if (call.isExecuted() || call.isCanceled()
                    || (netState != NetworkUtil.NETWORK_WIFI
                        && !EgameProxyInternal.get().isProxyAvailable())) {
                i.remove();
                // 由于普通类没有生命周期, 只能在这里手动注销
                NetworkEventBus.getDefault().unRegister(this);
                continue;
            }

            if (netState != NetworkUtil.NETWORK_WIFI
                && !EgameProxyInternal.get().isProxyAvailable()) {
                // 当处于非WiFi网络且没有启动代理时, 取消当前请求
                call.cancel();
                i.remove();
                // 由于普通类没有生命周期, 只能在这里手动注销
                NetworkEventBus.getDefault().unRegister(this);
            }
        }
    }
}
