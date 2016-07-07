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
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.File;

public class IonTest extends AbsHttp{
    public IonTest(Context context) {
        super(context);
    }

    public void start() {
/*
        Ion.getDefault(mContext)
                .configure()
                .proxy(ProxyUtil.PROXY_IP_INNER, ProxyUtil.PROXY_PORT_INNER);
*/

        Log.d("MY_PROXY", "ION start");
        File directory = Environment.getExternalStorageDirectory();;
        File dir1 = new File(directory, "egame/downloader");
        Log.d("MY_PROXY", "dir1: " + dir1.getAbsolutePath());
        if(prepare(dir1.getAbsolutePath(), "1.apk", DOWNLOAD_URL)){
            Log.d("MY_PROXY","创建文件成功");
        }
        Log.d("MY_PROXY","++++++++++++++");

        Ion.with(mContext)
                .load(DOWNLOAD_URL)
//                .proxy(ProxyUtil.PROXY_IP_OUTER, ProxyUtil.PROXY_PORT_OUTER)
                .proxy(ProxyUtil.HTTP_PROXY_IP_INNER, ProxyUtil.HTTP_PROXY_PORT_INNER)
                .setLogging("MyLogs", Log.VERBOSE)
                .write(mDownFile)
                .withResponse()
                .setCallback(new FutureCallback<Response<File>>() {
                    @Override
                    public void onCompleted(Exception e, Response<File> result) {
                        Log.d("MY_PROXY", "code: " + result.getHeaders().code());
                        mDownFile = result.getResult();
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            installApk();
                            updateView();
                        }
                    }
                });
    }
}
