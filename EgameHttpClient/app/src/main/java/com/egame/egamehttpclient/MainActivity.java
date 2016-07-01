package com.egame.egamehttpclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class MainActivity extends AppCompatActivity {

    public static final String DOWNLOAD_URL = "http://open.play.cn/f/pkg/gm/000/001/766/ba4cf61eh1af4369/WSSGS.apk";
    protected File mDownFile;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        start();
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

    protected boolean prepare(String dir,String mFileName,String mUrl) {
        File downDir = new File(dir);

        if (downDir.exists()) {
            if (!downDir.isDirectory()) {
                downDir.delete();
                downDir.mkdirs();
            }
        } else {
            downDir.mkdirs();
        }

        if (TextUtils.isEmpty(mFileName)) {
            mFileName = Uri.parse(mUrl).getLastPathSegment();
        }

        try {
            mDownFile = new File(downDir, mFileName);

        } catch (Exception e) {
            return false;
        }

        if (mDownFile.exists()) {
            mDownFile.delete();
        }

        try {
            if (mDownFile.createNewFile()) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }

    protected void installApk(){
        try {
            Process p = Runtime.getRuntime()
                    .exec("chmod 755 " + mDownFile);
            int status = p.waitFor();
            if (status == 0) {
                //                          Toast.makeText(context, "权限修改成功", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "权限修改成功");
            } else {
                Log.d("MainActivity", "权限修改失败");
                //                          Toast.makeText(context, "权限修改失败", Toast.LENGTH_SHORT).show();
//                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mDownFile),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    protected void updateView() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("MY_DOWN", "download finished");
            }
        });
    }

}
