package com.egame.proxy.listener;

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
    // 参见[Java 单例真的写对了么](http://www.race604.com/java-double-checked-singleton/)
    private static volatile NetworkEventBus sInstance;
    private List<INetworkStateListener> listeners;

    public NetworkEventBus() {
        listeners = new ArrayList<>();
    }

    public static NetworkEventBus getDefault() {
        NetworkEventBus bus = sInstance;
        if (sInstance == null) {
            synchronized (NetworkEventBus.class) {
                bus = sInstance;
                if (sInstance == null) {
                    bus = new NetworkEventBus();
                    sInstance = bus;
                }
            }
        }
        return bus;
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
