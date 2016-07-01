package com.egame.egamehttpclient.http;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/6/30 1.00 初始版本
 */

import android.content.Context;

public class HttpFactory {
    public static final int CLIENT_HTTP_CLIENT = 0x1;
    public static final int CLIENT_HTTP_URL_CONNECTION = 0x2;
    public static final int CLIENT_OK_HTTP = 0x3;
    public static final int CLIENT_ION = 0x4;

    public static void requestByClient(Context context, int type) {
        switch (type) {
            case CLIENT_HTTP_CLIENT:
                new HttpClientTest(context).start();
                break;
            case CLIENT_HTTP_URL_CONNECTION:
                new HttpUrlConnectorTest(context).start();
                break;
            case CLIENT_OK_HTTP:
                new OkHttpTest(context).start();
                break;
            case CLIENT_ION:
                new IonTest(context).start();
                break;
        }

    }
}
