package com.egame.proxy;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: <文件描述>
 * History:     7/14/16 1.00 初始版本
 */

import android.content.Context;

import com.egame.proxy.server.DataUsage;
import com.egame.proxy.server.EgameProxySelector;
import com.egame.proxy.server.HostService;
import com.egame.proxy.support.glide.EgameGlide;
import com.egame.proxy.support.ion.EgameIon;
import com.egame.proxy.util.NetworkUtil;

import java.net.InetSocketAddress;
import java.util.List;

public class EgameProxyInternal {
    private Context mContext;
    // (初始值)禁用代理
    private boolean mIsProxyEnabled = true;
    private HostService mService;
    private String mAppId;
    private String mUserId;
    private String mChannelCode;

    private static class LazyHolder {
        private static final EgameProxyInternal sInstance = new EgameProxyInternal();
    }

    public static EgameProxyInternal get() {
        return LazyHolder.sInstance;
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

    public boolean isProxyAvailable() {
        return mIsProxyEnabled && DataUsage.isDataUsageAvailable();
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

    public void waitForIpAndData() {
        if (isProxyEnabled()) {
            mService.waitForIpAndData();
        }
    }

    public void initProxy() {
        EgameGlide.init(mContext);
        EgameIon.init(mContext);
    }

    private EgameProxyInternal() {
        mContext = EgameProxy.get().getContext();
        mService = new HostService(new ServiceListener());
        mService.initService();
        // 检查网络状态
        handleNetwork();
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
            if (isIpPoolInited && isDataUsageInited && !isProxyConfigUpdated) {
                isProxyConfigUpdated = true;
                initProxy();
            }

        }
    }

}
