package com.egame.proxy.server;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: <文件描述>
 * History:     7/11/16 1.00 初始版本
 */

import com.egame.egamehttpclientsdk.BuildConfig;

import okhttp3.Call;

public interface RestApi {
    String HOST = BuildConfig.HOST;
//  String HOST = "https://open.play.cn/";
    String PROXY_HOST = HOST + "/api/v1/proxy/";
    String DATA_USAGE_URL = PROXY_HOST + "capacity/check.json";
    String IP_POOL_URL = PROXY_HOST + "host/get.json";

    Call getIpPool();
    Call getDataUsage(String appId, String userId, String channelCode);
}
