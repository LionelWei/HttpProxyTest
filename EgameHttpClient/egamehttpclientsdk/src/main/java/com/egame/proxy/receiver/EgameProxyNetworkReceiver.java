package com.egame.proxy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.egame.proxy.EgameProxy;
import com.egame.proxy.listener.NetworkEventBus;
import com.egame.proxy.util.NetworkUtil;

public class EgameProxyNetworkReceiver extends BroadcastReceiver {
    public EgameProxyNetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action;
        if (intent != null ) {
            action = intent.getAction();
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                handleNetworkStatusChange(context);
            }
        }
    }

    private void handleNetworkStatusChange(Context context) {
        int state = NetworkUtil.checkState(context);
        // 移动网络时 设置代理
        // WIFI时 切断代理直接访问原始服务器
        switch (state) {
            case NetworkUtil.NETWORK_WIFI:
                EgameProxy.setProxyEnabled(false);
                break;
            case NetworkUtil.NETWORK_4G:
            case NetworkUtil.NETWORK_3G2G:
                EgameProxy.setProxyEnabled(true);
                break;
        }
        NetworkEventBus.getDefault().post(state);
    }
}
