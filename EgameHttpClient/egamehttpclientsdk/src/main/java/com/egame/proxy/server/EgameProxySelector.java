package com.egame.proxy.server;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: <文件描述>
 * History:     7/12/16 1.00 初始版本
 */

import java.net.InetSocketAddress;
import java.util.List;

public class EgameProxySelector {
    private static EgameProxySelector sInstance = new EgameProxySelector();

    private List<InetSocketAddress> mHttpProxyAddresses;
    private List<InetSocketAddress> mSocksProxyAddresses;

    public static EgameProxySelector get() {
        return sInstance;
    }

    public void setHttpProxyPool(List<InetSocketAddress> pool) {
        mHttpProxyAddresses = pool;
    }

    public void setSocksProxyPool(List<InetSocketAddress> pool) {
        mSocksProxyAddresses = pool;
    }

    public List<InetSocketAddress> getHttpProxyPool() {
        return mHttpProxyAddresses;
    }
    public List<InetSocketAddress> getSocksProxyPool() {
        return mSocksProxyAddresses;
    }


    public InetSocketAddress getOptimalHttpProxy() {
        // 目前只选取第一个
        if (mHttpProxyAddresses != null
                && mHttpProxyAddresses.size() > 0) {
            return mHttpProxyAddresses.get(0);
        }
        return null;
    }

    public InetSocketAddress getOptimalSocksProxy() {
        // 目前只选取第一个
        if (mSocksProxyAddresses != null
                && mSocksProxyAddresses.size() > 0) {
            return mSocksProxyAddresses.get(0);
        }
        return null;
    }
}
