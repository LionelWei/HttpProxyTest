package com.egame.egamehttpclient.http;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/6/30 1.00 初始版本
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.egame.egamehttpclient.MainActivity;

import java.io.File;
import java.io.IOException;

public class AbsHttp {
//    public static final String DOWNLOAD_URL = "http://open.play.cn/f/pkg/gm/000/001/766/ba4cf61eh1af4369/WSSGS.apk";
//    public static final String DOWNLOAD_URL = "http://open.play.cn/f/pkg/gm/000/001/766/ba4cf61eh1af4369/WSSGS.apk";
    public static final String DOWNLOAD_URL = "http://open.play.cn/api/v2/egame/host.json";
    public static final int SET_CONNECTION_TIMEOUT = 5 * 1000;
    public static final int SET_SOCKET_TIMEOUT = 20 * 1000;

    protected File mDownFile;
    protected Context mContext;

    public AbsHttp(Context context) {
        mContext = context;
    }


    protected boolean prepare(String dir, String mFileName, String mUrl) {
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
                if (mContext instanceof MainActivity) {
                    TextView textView = ((MainActivity) mContext).textView;
                    textView.setText("Download finished");
                }
            }
        });
    }
}
