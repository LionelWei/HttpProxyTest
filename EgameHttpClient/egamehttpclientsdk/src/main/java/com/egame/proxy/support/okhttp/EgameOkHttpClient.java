package com.egame.proxy.support.okhttp;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/7 1.00 初始版本
 */

import android.util.Log;

import com.egame.proxy.EgameProxy;
import com.egame.proxy.util.ProxyUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.CookieJar;
import okhttp3.Credentials;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class EgameOkHttpClient {
    private static OkHttpClient sInstance = new OkHttpClient();

    private OkHttpClient mDelegate;

    public EgameOkHttpClient() {
        mDelegate = sInstance;
        mDelegate = clientWithProxy(mDelegate);
    }

    public EgameOkHttpClient(OkHttpClient originClient) {
        mDelegate = originClient;
        mDelegate = clientWithProxy(mDelegate);
    }

    /**
     * 对原生Call的代理
     * @param request OkHttp 网络请求
     * @return EgameCall 经SDK处理过的Call, 兼容原生版本
     */
    public EgameCall newCall(Request request) {
        Call call = mDelegate.newCall(request);
        return new EgameCall(call);
    }

    // OkHttpClient原始函数
    public int connectTimeoutMillis() {
        return mDelegate.connectTimeoutMillis();
    }

    public CookieJar cookieJar() {
        return mDelegate.cookieJar();
    }

    public List<Protocol> protocols() {
        return mDelegate.protocols();
    }

    public boolean retryOnConnectionFailure() {
        return mDelegate.retryOnConnectionFailure();
    }

    public Dispatcher dispatcher() {
        return mDelegate.dispatcher();
    }

    public Authenticator authenticator() {
        return mDelegate.authenticator();
    }

    public Cache cache() {
        return mDelegate.cache();
    }

    public boolean followSslRedirects() {
        return mDelegate.followSslRedirects();
    }

    public boolean followRedirects() {
        return mDelegate.followRedirects();
    }

    public List<Interceptor> interceptors() {
        return mDelegate.interceptors();
    }

    public List<Interceptor> networkInterceptors() {
        return mDelegate.networkInterceptors();
    }

    public Proxy proxy() {
        return mDelegate.proxy();
    }

    public int readTimeoutMillis() {
        return mDelegate.readTimeoutMillis();
    }

    public Authenticator proxyAuthenticator() {
        return mDelegate.proxyAuthenticator();
    }

    public SSLSocketFactory sslSocketFactory() {
        return mDelegate.sslSocketFactory();
    }

    public OkHttpClient.Builder newBuilder() {
        return mDelegate.newBuilder();
    }

    public int writeTimeoutMillis() {
        return mDelegate.writeTimeoutMillis();
    }

    public CertificatePinner certificatePinner() {
        return mDelegate.certificatePinner();
    }

    public ProxySelector proxySelector() {
        return mDelegate.proxySelector();
    }

    public Dns dns() {
        return mDelegate.dns();
    }

    public List<ConnectionSpec> connectionSpecs() {
        return mDelegate.connectionSpecs();
    }

    public HostnameVerifier hostnameVerifier() {
        return mDelegate.hostnameVerifier();
    }

    public SocketFactory socketFactory() {
        return mDelegate.socketFactory();
    }

    public ConnectionPool connectionPool() {
        return mDelegate.connectionPool();
    }


    private OkHttpClient clientWithProxy(OkHttpClient oldClient) {
        OkHttpClient.Builder builder = oldClient.newBuilder();
        if (EgameProxy.get().isProxyEnabled()) {
            // 对于socks代理 new Proxy(SOCKS, ...) 不起作用
            // 得用socketFactory 可能是aosp的bug
            Proxy httpProxy = new Proxy(Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved(
                            ProxyUtil.FIDDLER_PROXY_IP_INNER,
                            ProxyUtil.FIDDLER_PROXY_PORT_INNER));
            builder = builder
                    .proxy(httpProxy)
                    .proxyAuthenticator(new OkProxyAuthenticator());
        }
        return builder
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    public static class OkProxyAuthenticator implements Authenticator {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            String credential = Credentials.basic(
                    ProxyUtil.TEST_USER_NAME,
                    ProxyUtil.TEST_PASSWORD);
            // 原始服务器字段: Authorization
            // 代理服务器字段: Proxy-Authorization
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credential)
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
