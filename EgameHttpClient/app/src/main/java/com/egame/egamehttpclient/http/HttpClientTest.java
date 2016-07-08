package com.egame.egamehttpclient.http;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/6/30 1.00 初始版本
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.egame.proxy.util.ProxyUtil;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HttpClientTest extends AbsHttp {
    public HttpClientTest(Context context) {
        super(context);
    }

    public void start() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

//                    Authenticator.setDefault(new MyAuthenticator("username",
//                            "password"));
                    HttpClient client = getHttpClient2();
                    try {
                        HttpGet request = new HttpGet(DOWNLOAD_URL);
                        request.setHeader("Connection", "keep-alive");
/*
                        System.out.println("Executing request " + request
                                + " via SOCKS proxy " + ProxyUtil.SOCKET_ADDRESS);
*/
                        HttpResponse response = client.execute(request);
                        try {
                            System.out
                                    .println("----------------------------------------");
                            System.out.println(response.getStatusLine());

                            int status = response.getStatusLine().getStatusCode();
                            Log.d("MY_PROXY", "httpclient status: " + status);

                            if (status == 200) {

                                File directory = Environment.getExternalStorageDirectory();;
                                File dir1 = new File(directory, "egame/downloader");
                                Log.d("MY_PROXY", "dir1: " + dir1.getAbsolutePath());
                                if(prepare(dir1.getAbsolutePath(), "1.apk", DOWNLOAD_URL)){
                                    System.out.println("创建文件成功");

                                }
                                System.out.println("++++++++++++++");

                                FileOutputStream outputStream = new FileOutputStream(
                                        mDownFile);
                                InputStream inputStream = response.getEntity()
                                        .getContent();
                                byte b[] = new byte[1024];
                                int j = 0;
                                while ((j = inputStream.read(b)) != -1) {
                                    outputStream.write(b, 0, j);
                                }

                                installApk();



                                outputStream.flush();
                                outputStream.close();

                                updateView();
                            }
                        } finally {
                            //                         response.
                        }
                    } finally {
                    }

                } catch (Exception e) {
                    // TODO: handle exception

                    e.printStackTrace();
                }

            }
        }).start();
    }


    @SuppressWarnings("all")
    private HttpClient getHttpClient1() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, 10000);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http",
                new MyConnectionSocketFactory(), 80));
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                params, registry);

        HttpConnectionParams.setConnectionTimeout(params,
                SET_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params,
                SET_SOCKET_TIMEOUT);
        HttpClient client = new DefaultHttpClient(ccm, params);
        return client;
    }

    private HttpClient getHttpClient2() {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        String proxyHost = ProxyUtil.FIDDLER_PROXY_IP_INNER;
        int proxyPort = ProxyUtil.FIDDLER_PROXY_PORT_INNER;
        String userName = "zhangqx";
        String password = "12345";
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(userName, password));
        HttpHost proxy = new HttpHost(proxyHost,proxyPort);

        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
        return httpClient;
    }


}
