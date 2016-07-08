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

import com.egame.proxy.EgameProxy;
import com.egame.proxy.exception.EgameProxyException;
import com.egame.proxy.support.okhttp.EgameOkHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpTest extends AbsHttp{
    public OkHttpTest(Context context) {
        super(context);
    }

    public void start() {
        // 利用反射检查OkHttpClient类是否存在
        try {
            Class.forName("okhttp3.OkHttpClient");
            doStart();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void doStart() {
        OkHttpClient original = new OkHttpClient
                .Builder()
                .build();

        EgameOkHttpClient client = EgameProxy.setOkHttpProxy(original);

        final Request request = new Request
                .Builder()
                .url(DOWNLOAD_URL)
                .build();

        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int code = response.code();
                    Log.d("MY_PROXY", "okhttp status: " + code);
                    if (code == 200) {
                        ResponseBody responseBody = response.body();
                        InputStream in = responseBody.byteStream();

                        File directory = Environment.getExternalStorageDirectory();;
                        File dir1 = new File(directory, "egame/downloader");
                        Log.d("MY_PROXY", "dir1: " + dir1.getAbsolutePath());
                        if(prepare(dir1.getAbsolutePath(), "1.apk", DOWNLOAD_URL)){
                            System.out.println("创建文件成功");

                        }
                        System.out.println("++++++++++++++");

                        FileOutputStream outputStream = new FileOutputStream(
                                mDownFile);
                        byte b[] = new byte[1024];
                        int j = 0;
                        try {
                            while ((j = in.read(b)) != -1) {
                                outputStream.write(b, 0, j);
                            }
                        } catch (SocketException e) {
                            Log.e("MY_PROXY", "SocketException");
                            e.printStackTrace();
                        }

                        installApk();

                        outputStream.flush();
                        outputStream.close();
                        responseBody.close();

                        updateView();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("MY_OKHTTP", "failed");
                    e.printStackTrace();
                }
            });
        } catch (EgameProxyException e) {
            e.printStackTrace();
        }
    }

}

