/*
 * FileName:	JJ.java
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		yaokai
 * Description:	<文件描述>
 * History:		2016年3月23日 1.00 初始版本
 */
package egame.tv.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.egame.tv.download.DownProgressListener;
import com.egame.tv.download.DownTask;
import com.egame.tv.download.SimpleDown;
import com.example.webviewdemo.R;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * <功能简述> <Br>
 * <功能详细描述> <Br>
 * 
 * @author  yaokai
 */
public class StartAppJavaScriptInterface {

    private Activity mActivity;

    /**
     * 下载的临时文件
     */
    File mTempFile;
    int count = 0;// 存储进度条当前值，初始为 0

    DownTask mDowntask;
    private static final String TAG = "StartAppJavaScript";

    /**
     * 更新进度条flag
     */
    public final int PROGRESS_FLAG = 2;
    /**
     * 更新进度条
     */
    public final int HINT_PROGRESS = 0;
    /**
     * 控制对话框flag
     */
    public final int DIALOG_FLAG = 1;

    private Handler myHandler;

    public StartAppJavaScriptInterface(Activity mainActivity) {
        this.mActivity = mainActivity;
    }

    // androidStartapp方法与web界面对应 
    @JavascriptInterface
    public void startAppByName(String packageName, String param) {
        PackageManager packageManager = mActivity.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        mActivity.startActivity(intent);
    }

    @JavascriptInterface
    public boolean isAppInstalled(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = mActivity.getPackageManager().getPackageInfo(
                    packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
    
    @JavascriptInterface
   public void closeWindow() {
//       L.d("关闭");
        mActivity.finish();
   }

    @JavascriptInterface
    public void downloadApkFile(String downloadUrl) {
        
        
        
//        Properties prop = System.getProperties();
//        //SOCKS的代理设置
//        prop.setProperty("socksProxyHost", "61.191.45.70");
//        prop.setProperty("socksProxyPort", "8087");
        
        
        
        
        
//        System.setProperty("http.proxyHost", "192.168.251.57");
//        System.setProperty("http.proxyPort", "" + 9999);

        
//      System.setProperty("http.proxyHost", "61.191.45.70");
//      System.setProperty("http.proxyPort", "" + 8087);
        
//        System.setProperty("socksProxyHost", "61.191.45.70");
//        System.setProperty("socksProxyPort", "" + 8087);
//        Authenticator.setDefault(new BasicAuthenticator("anhuitv", "dhqZZXgH"));  
      
      
      // socks代理服务器的地址与端口
//        System.setProperty("socksProxyHost", "192.168.251.57");
//        System.setProperty("socksProxyPort", "9999");
      // 设置登陆到代理服务器的用户名和密码
//      Authenticator.setDefault(new BasicAuthenticator("userName", "Password"));

        
        final ProgressDialog myDialog = new ProgressDialog(mActivity); // 得到一个对象
        myDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置为矩形进度条
        myDialog.setTitle("提示");
        myDialog.setMessage("爱游戏正在下载中，请稍后...");
        myDialog.setIcon(R.drawable.ic_launcher);
        myDialog.setIndeterminate(false); // 设置进度条是否为不明确
        myDialog.setCancelable(true);
        myDialog.setMax(100); // 设置进度条的最大值
        myDialog.setProgress(0); // 设置当前默认进度为 0
        myDialog.setSecondaryProgress(1000); // 设置第二条进度值为100
        myDialog.show(); // 显示进度条

        myHandler = new Handler() {

            public void handleMessage(Message msg) {
                switch (msg.what) {
                case HINT_PROGRESS:
                    myDialog.cancel();
//                    if(mTempFile == null){
//                        return;
//                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(mTempFile),
                            "application/vnd.android.package-archive");
                    mActivity.startActivity(intent);
                    break;
                case PROGRESS_FLAG:
                    Bundle bundle = msg.getData();
                    int progress = bundle.getInt("c_progress");
                    myDialog.setProgress(progress);
                    break;
                case DIALOG_FLAG:
                    new AlertDialog.Builder(mActivity)
                            .setTitle("下载出错")
                            .setMessage("下载更新出错,请检查网络并重新更新")
                            .setPositiveButton(
                                    "确定",
                                    new android.content.DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            myDialog.cancel();
                                        }
                                    }).show();

                    break;
                }
                super.handleMessage(msg);
            }
        };

        File directory = Environment.getExternalStorageDirectory();;
        File dir1 = new File(directory, "egame/downloader");
        Log.d("MY_PROXY", "downloadApkFile dir1: " + dir1.getAbsolutePath());

        mDowntask = SimpleDown.create(downloadUrl, "",
                new DownProgressListener() {

                    @Override
                    public void onStart(File downFile, long totalSize) {
                        Log.d("downloadApkFile", "下载开始");
                        mTempFile = downFile;
                    }

                    @Override
                    public void onProgress(long downSize, int progress) {
                        Log.d(TAG, " 下载进行中 " + downSize + " pro =" + progress);
                        Message message = new Message();
                        message.what = PROGRESS_FLAG;
                        Bundle bundle = new Bundle();
                        bundle.putInt("c_progress", progress);
                        message.setData(bundle);
                        myHandler.sendMessage(message);
                    }

                    @Override
                    public void onCompleted(Result result) {
                        
                        if (result != null) {
                            if (result.resultCode == DownProgressListener.RESULT_CODE_SUCCESS) {
                                Log.d(TAG, "下载完成");

                                try {
                                    Process p = Runtime.getRuntime().exec(
                                            "chmod 755 " + mTempFile);
                                    int status = p.waitFor();
                                    if (status == 0) {
                                        //                          Toast.makeText(context, "权限修改成功", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "权限修改成功");
                                    } else {
                                        Log.d(TAG, "权限修改失败");
                                        //                          Toast.makeText(context, "权限修改失败", Toast.LENGTH_SHORT).show();
//                                        return;
                                    }
                                    
                                    
                                    Message message1 = new Message();
                                    message1.what = HINT_PROGRESS;
                                    myHandler.sendMessage(message1);
                                    
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    return;
                                }

                            } else if (result.resultCode == DownProgressListener.RESULT_CODE_FAILED) {
                                Log.d(TAG, "下载失败");
                                Message message2 = new Message();
                                message2.what = DIALOG_FLAG;
                                myHandler.sendMessage(message2);

                            }
                            
                            
                        }
                    }
                },dir1.getAbsolutePath());

//        mDowntask.start();

    }
    
    public class BasicAuthenticator extends Authenticator {
        String userName;
        String password;

        public BasicAuthenticator(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        /**
         * Called when password authorization is needed.  Subclasses should
         * override the default implementation, which returns null.
         *
         * @return The PasswordAuthentication collected from the
         *         user, or null if none is provided.
         */
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password.toCharArray());
        }
    }

}