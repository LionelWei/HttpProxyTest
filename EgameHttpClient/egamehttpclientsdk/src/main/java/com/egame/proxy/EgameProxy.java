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
import com.egame.proxy.server.HostService;
import com.egame.proxy.support.glide.GlideProxy;
import com.egame.proxy.support.okhttp.EgameOkHttpClient;
import com.egame.proxy.support.volley.EgameRequestQueue;
import com.egame.proxy.util.NetworkUtil;

import java.net.InetSocketAddress;
import java.util.List;

import okhttp3.OkHttpClient;

public class EgameProxy{

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

    public void setProxy() {
        GlideProxy.init(mContext);
    }

    public EgameOkHttpClient setOkHttpProxy(OkHttpClient oldClient) {
        return new EgameOkHttpClient(oldClient);
    }

    public RequestQueue setVolleyProxy() {
        return EgameRequestQueue.requestQueueWithProxy(mContext);
    }

    public void setProxyEnabled(boolean enabled) {
        mIsProxyEnabled = enabled;
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

    public boolean isProxyEnabled() {
        return mIsProxyEnabled;
    }

    public Context getContext() {
        return mContext;
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
                // !!! 仅供测试 实际状态与此相反 TODO
                setProxyEnabled(false);
                break;
        }
    }

    private class ServiceListener implements HostService.IServiceListener {
        @Override
        public void onIpPoolChanged(List<InetSocketAddress> httpSocketAddresses,
                                    List<InetSocketAddress> socksSocketAddresses) {
            EgameProxySelector.get().setHttpProxyPool(httpSocketAddresses);
            EgameProxySelector.get().setSocksProxyPool(socksSocketAddresses);
        }

        @Override
        public void onDataUsageAvailable(boolean isAvailable) {
            DataUsage.setDataUsageAvailable(isAvailable);
        }
    }

}
