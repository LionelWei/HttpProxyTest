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
import com.egame.proxy.server.DataUsage;
import com.egame.proxy.server.EgameProxySelector;
import com.egame.proxy.server.HostService;
import com.egame.proxy.support.glide.GlideProxy;
import com.egame.proxy.support.ion.IonProxy;
import com.egame.proxy.support.okhttp.EgameOkHttpClient;
import com.egame.proxy.support.volley.EgameRequestQueue;
import com.egame.proxy.util.NetworkUtil;

import java.net.InetSocketAddress;
import java.util.List;

import okhttp3.OkHttpClient;

public class EgameProxy {

    private Context mContext;
    // (初始值)禁用代理
    private boolean mIsProxyEnabled = true;
    private HostService mService;
    private String mAppId;
    private String mUserId;
    private String mChannelCode;

    private static EgameProxy sInstance = new EgameProxy();

    // 单例
    private EgameProxy() {
        mService = new HostService(new ServiceListener());
    }

    public static EgameProxy get() {
        return sInstance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mService.initService();
        // 检查网络状态
        handleNetwork();
    }

    public Context getContext() {
        return mContext;
    }

    public EgameOkHttpClient getEgameOkHttpClient(OkHttpClient oldClient) {
        return new EgameOkHttpClient(oldClient);
    }

    public RequestQueue getEgameRequestQueue() {
        return EgameRequestQueue.requestQueueWithProxy(mContext);
    }

    public void setProxyEnabled(boolean enabled) {
        if (mIsProxyEnabled != enabled) {
            mIsProxyEnabled = enabled;
            initProxy();
        }
    }

    public boolean isProxyEnabled() {
        return mIsProxyEnabled;
    }

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String appId) {
        this.mAppId = appId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getChannelCode() {
        return mChannelCode;
    }

    public void setChannelCode(String channelCode) {
        this.mChannelCode = channelCode;
    }

    public boolean isProxyAvailable() {
        return mIsProxyEnabled && DataUsage.isDataUsageAvailable();
    }

    /*package*/ void initProxy() {
        GlideProxy.init(mContext);
        IonProxy.init(mContext);
    }

    private void handleNetwork() {
        int state = NetworkUtil.checkState(mContext);
        // 移动网络时 设置代理
        // WIFI时 切断代理直接访问原始服务器
        switch (state) {
            case NetworkUtil.NETWORK_WIFI:
                // !!! 仅供测试 实际状态与此相反 TODO
                setProxyEnabled(true);
                break;
            case NetworkUtil.NETWORK_4G:
            case NetworkUtil.NETWORK_3G2G:
            case NetworkUtil.NETWORK_WIFI_DISCONNECTED:
                // !!! 仅供测试 实际状态与此相反 TODO
                setProxyEnabled(false);
                break;
        }
    }

    private class ServiceListener implements HostService.IServiceListener {
        private boolean isIpPoolInited = false;
        private boolean isDataUsageInited = false;
        private boolean isProxyConfigUpdated = false;
        @Override
        public void onIpPoolChanged(List<InetSocketAddress> httpSocketAddresses,
                                    List<InetSocketAddress> socksSocketAddresses) {
            EgameProxySelector.get().setHttpProxyPool(httpSocketAddresses);
            EgameProxySelector.get().setSocksProxyPool(socksSocketAddresses);
            isIpPoolInited = true;
            updateProxyConfig();
        }

        @Override
        public void onDataUsageAvailable(boolean isAvailable) {
            DataUsage.setDataUsageAvailable(isAvailable);
            isDataUsageInited = true;
            updateProxyConfig();
        }

        private void updateProxyConfig() {
            if (isIpPoolInited && isDataUsageInited) {
                if (!isProxyConfigUpdated) {
                    isProxyConfigUpdated = true;
                    EgameProxy.get().initProxy();
                }
            }

        }
    }

}
