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

import com.egame.egamehttpclientsdk.EgameProxyManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        OkHttpClient client = new OkHttpClient
                .Builder()
                .build();

        client = EgameProxyManager.enableProxy(client);

        final Request request = new Request
                .Builder()
                .url(DOWNLOAD_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
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
                    while ((j = in.read(b)) != -1) {
                        outputStream.write(b, 0, j);
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
    }

}

