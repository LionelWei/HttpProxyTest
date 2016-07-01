/*
 * FileName:	DownTask.java
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		Hein
 * Description:	<文件描述>
 * History:		2013-12-9 1.00 初始版本
 */
package com.egame.tv.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.egame.tv.download.DownProgressListener.Result;

/**
 * <功能简述> </Br> <功能详细描述> </Br>
 * 
 * @author Hein
 */
public class DownRunnable implements Runnable {

    private static HttpParams sHttpParams = new BasicHttpParams();

    private String mUrl = null;
    private String mFileName = null;
    private File mDownFile = null;
    private DownProgressListener mListener = null;
    private long mStartTime;

    private int mLastProgress = -1;

    private boolean isCanceled = false;

    /** stream缓冲大小 1024 * 16 */
    public static final int BUFFER_SIZE = 1024 * 50;
    /** 读取图片每次读取缓冲大小 1024 * 8 */
    public static final int ARRAY_SIZE = 1024 * 8;
    /** 下载数据是缓冲大小 1024 * 64 */
    public static final int DATA_BUFFER_SIZE = 1024 * 64;

    private final static String SDCARD_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath();
    String dir;
    
    
    static {
        HttpConnectionParams.setStaleCheckingEnabled(sHttpParams, false);
        HttpConnectionParams.setConnectionTimeout(sHttpParams, 20 * 1000);
        HttpConnectionParams.setSoTimeout(sHttpParams, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(sHttpParams,
                DATA_BUFFER_SIZE);
    }

    public DownRunnable(String url, String name,
            DownProgressListener listener,String dir) {
        this.dir = dir;
        mUrl = url;
        mFileName = name;

        if (listener == null) {
            listener = new DownProgressListener() {

                @Override
                public void onStart(File downFile, long totalSize) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgress(long downSize, int progress) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onCompleted(Result result) {
                    // TODO Auto-generated method stub

                }
            };
        }

        mListener = listener;
    }

    /** {@inheritDoc} */

    @Override
    public void run() {
        // TODO Auto-generated method stub
        mStartTime = System.currentTimeMillis();
        Result result = new Result();
        result.resultCode = DownProgressListener.RESULT_CODE_FAILED;

        if (prepare()) {
            try {
                if (httpGet(mUrl)) {
                    result.resultCode = DownProgressListener.RESULT_CODE_SUCCESS;
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.w("DownEngine", e.getMessage());
                }
            }
        }

        if (result.resultCode == DownProgressListener.RESULT_CODE_FAILED) {
            if (mDownFile != null && mDownFile.exists()) {
                mDownFile.delete();
            }
        }

        result.totalTime = System.currentTimeMillis() - mStartTime;
        mListener.onCompleted(result);
    }

    private boolean prepare() {

      
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

        
        try{
            mDownFile = new File(downDir, mFileName);

        }catch(Exception e){
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

    private boolean httpGet(String url) throws Exception {

        DefaultHttpClient client = new DefaultHttpClient(sHttpParams);

        HttpGet mHttpGet = new HttpGet(url);

        HttpResponse hr = client.execute(mHttpGet);

        StatusLine mStatusLine = hr.getStatusLine();

        int statusCode = mStatusLine.getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {

            HttpEntity entity = hr.getEntity();
            if (entity != null) {
                long fileSize = entity.getContentLength();
                mListener.onStart(mDownFile, fileSize);
                postProgress(fileSize, 0);

                InputStream is = entity.getContent();
                FileOutputStream fos = new FileOutputStream(mDownFile);
                Header contentEncoding = entity.getContentEncoding();
                InputStream nis = null;
                byte[] buf = null;
                int count = 0;
                int totalCount = 0;

                if (contentEncoding != null
                        && contentEncoding.getValue().equals("gzip")) {
                    nis = new GZIPInputStream(is);
                } else {
                    nis = is;
                }

                /*
                 * accumulate enough data to make it worth pushing it up the
                 * stack
                 */
                buf = new byte[DATA_BUFFER_SIZE];
                int len = 0;
                int lowWater = buf.length / 2;

                while (!isCanceled && len != -1) {

                    len = nis.read(buf, count, buf.length - count);

                    if (len != -1) {
                        count += len;
                        totalCount += len;
                    }
                    if (len == -1 || count >= lowWater) {
                        fos.write(buf, 0, count);
                        postProgress(fileSize, totalCount);
                        count = 0;
                    }
                }

                mHttpGet.abort();
                client.getConnectionManager().shutdown();

                is.close();
                nis.close();
                fos.close();
                return !isCanceled;
            }
        }

        throw new Exception(
                "Sth is wrong with Network. The SC is " + statusCode + ".");
    }

    private void postProgress(long totalSize, long downSize) {

        int progress = (int) (downSize * 100 / totalSize);

        if (mLastProgress == progress) {
            return;
        } else {
            mLastProgress = progress;
        }

        if (progress > 100) {
            progress = 100;
        }

        mListener.onProgress(downSize, progress);
    }

    public void cancel() {
        isCanceled = true;
    }
}
