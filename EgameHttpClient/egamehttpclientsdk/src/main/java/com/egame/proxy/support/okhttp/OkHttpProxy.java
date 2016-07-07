package com.egame.proxy.support.okhttp;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/7 1.00 初始版本
 */

import android.util.Log;

import com.egame.proxy.util.ProxyUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class OkHttpProxy {
    public static OkHttpClient clientWithProxy(OkHttpClient oldClient) {
        // 对于socks代理 new Proxy(SOCKS, ...) 不起作用
        // 得用socketFactory 可能是aosp的bug
        Proxy httpProxy = new Proxy(Proxy.Type.HTTP,
                InetSocketAddress.createUnresolved(
                        ProxyUtil.FIDDLER_PROXY_IP_INNER,
                        ProxyUtil.FIDDLER_PROXY_PORT_INNER));
        return oldClient.newBuilder()
                .proxy(httpProxy)
//                .socketFactory(new ProxySocketFactory())
                .authenticator(new OkAuthenticator())
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    public static class OkAuthenticator implements Authenticator {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            String credential = Credentials.basic(
                    ProxyUtil.TEST_USER_NAME,
                    ProxyUtil.TEST_PASSWORD);
            return response.request().newBuilder()
                    .header("Authorization", credential)
                    .build();
        }
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override public Response intercept(Chain chain) throws IOException {
            long t1 = System.nanoTime();
            Request request = chain.request();
            Log.d("MY_PROXY", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d("MY_PROXY", String.format("Received response for %s in %.1fms%n%s",
                    request.url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }
}
