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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class HttpUrlConnectorTest extends AbsHttp {
    public HttpUrlConnectorTest(Context context) {
        super(context);
    }

    public void start() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(DOWNLOAD_URL);
                    if (true) {
                        // url connection的代理不通 android只能接受resolved address
//                        Proxy proxy = new Proxy(Proxy.Type.SOCKS, ProxyUtil.SOCKET_ADDRESS);
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, ProxyUtil.HTTP_SOCKET_ADDRESS);
                        connection = (HttpURLConnection) url.openConnection(proxy);
                    } else {
                        connection = (HttpURLConnection) url.openConnection();
                    }
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);

                    connection.connect();
                    int code = connection.getResponseCode();
                    Log.d("MY_URLCONNECT", "code: " + code);
                    if (code == 200) {
                        System.out.println("创建文件成功");

                        File directory = Environment.getExternalStorageDirectory();
                        ;
                        File dir1 = new File(directory, "egame/downloader");
                        Log.d("MY_PROXY", "dir1: " + dir1.getAbsolutePath());
                        if (prepare(dir1.getAbsolutePath(), "1.apk", DOWNLOAD_URL)) {
                            System.out.println("创建文件成功");
                        }
                        System.out.println("++++++++++++++");

                        FileOutputStream outputStream = new FileOutputStream(
                                mDownFile);
                        InputStream inputStream = connection.getInputStream();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
