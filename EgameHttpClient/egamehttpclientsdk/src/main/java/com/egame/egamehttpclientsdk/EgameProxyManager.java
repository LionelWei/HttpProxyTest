package com.egame.egamehttpclientsdk;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/1 1.00 初始版本
 */

import com.egame.egamehttpclientsdk.okhttp.ProxySocketFactory;

import okhttp3.OkHttpClient;

public class EgameProxyManager {
    public static OkHttpClient enableProxy(OkHttpClient oldClient) {
        // 对于socks代理 new Proxy(SOCKS, ...) 不起作用
        // 得用socketFactory 可能是aosp的bug
        return  oldClient
                .newBuilder()
//                .proxy(new Proxy(Proxy.Type.SOCKS, ProxyUtil.SOCKET_ADDRESS))
                .socketFactory(new ProxySocketFactory())
                .build();
    }


}
