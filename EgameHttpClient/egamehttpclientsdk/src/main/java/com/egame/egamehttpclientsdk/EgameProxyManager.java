package com.egame.egamehttpclientsdk;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/1 1.00 初始版本
 */

import com.egame.egamehttpclientsdk.util.ProxyUtil;

import java.net.Proxy;

import okhttp3.OkHttpClient;

public class EgameProxyManager {
    public static OkHttpClient enableProxy(OkHttpClient oldClient) {
        return  oldClient
                .newBuilder()
                .proxy(new Proxy(Proxy.Type.HTTP, ProxyUtil.SOCKET_ADDRESS))
//                .socketFactory(new ProxySocketFactory())
                .build();
    }


}
