package com.egame.proxy.server;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: <文件描述>
 * History:     7/11/16 1.00 初始版本
 */

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RestApiImpl implements RestApi{

    private static OkHttpClient sInstance = new OkHttpClient();

    public Call getIpPool() {
        Request request = new Request
                .Builder()
                .url(RestApi.IP_POOL_URL)
                .build();
        return sInstance.newCall(request);
    }
    public Call getDataUsage(String appId, String userId, String channelCode) {
        Request request = new Request
                .Builder()
                .url(RestApi.DATA_USAGE_URL)
                .addHeader("app_id", (appId == null) ? "" : appId)
                .addHeader("user_id", (userId == null) ? "": userId)
                .addHeader("channel_code", (channelCode == null) ? "" : channelCode)
                .build();
        return sInstance.newCall(request);
    }

}
