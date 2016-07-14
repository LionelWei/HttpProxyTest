package com.egame.proxy.event;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/8 1.00 初始版本
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NetworkEventBus {
    private static NetworkEventBus sInstance = new NetworkEventBus();
    private List<INetworkStateListener> listeners;

    public NetworkEventBus() {
        listeners = new ArrayList<>();
    }

    public static NetworkEventBus getDefault() {
        return sInstance;
    }

    public void register(INetworkStateListener listener) {
        listeners.add(listener);
    }

    public void unRegister(INetworkStateListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void post(int state) {
        Iterator<INetworkStateListener> i = listeners.iterator();
        while (i.hasNext()) {
            INetworkStateListener listener = i.next();
            listener.onNetworkStateChanged(state);
            i.remove();
        }
    }
}
