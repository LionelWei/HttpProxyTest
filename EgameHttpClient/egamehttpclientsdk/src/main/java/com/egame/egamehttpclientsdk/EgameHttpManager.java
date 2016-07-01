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

public class EgameHttpManager {
    public static OkHttpClient enableProxy(OkHttpClient oldClient) {
        return  oldClient
                .newBuilder()
                .socketFactory(new ProxySocketFactory())
                .build();
    }
}
