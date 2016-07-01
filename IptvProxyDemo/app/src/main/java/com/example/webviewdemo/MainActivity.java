package com.example.webviewdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.webviewdemo.http.HttpFactory;

import egame.tv.webview.StartAppJavaScriptInterface;

public class MainActivity extends Activity {

    private static Activity mActivity;
    private WebView webView;
    public TextView textView;

    //    private String cur_url = "http://125.88.99.222:8082/ACS/vas/verifyuser?SPID=2&UserID=088800000103&ReturnURL=218.94.99.204:8084/api/v1/charge/tv/iptv/call_back/guangdong/userToken&Action=UserTokenRequest";
    //    private String cur_url = "http://125.88.99.222:8082/ACS/vas/verifyuser?SPID=2&UserID=088800000103&ReturnURL=218.94.99.204:8084/api/v1/charge/tv/iptv/call_back/guangdong/userToken&Action=UserTokenRequest";

    //    private String cur_url = "http://14.29.1.43:8080/testDes.jsp?current_time="+System.currentTimeMillis();
    //    private String cur_url = "http://172.41.8.127:8080/testDes.jsp?current_time="+System.currentTimeMillis();
    //    private String cur_url = "http://183.60.28.26/IPTV_Advance/monthPackOrder/order.jsp?user=088820140904273&productId=99881259&isMonthPackOrder=1&gameID=1496&paramurl=http://open.play.cn/api/v1/charge/tv/iptv/call_back/guangdong?correlator=201605301114359456134328&orderNo=201605301114359456134328&userToken=0D1EC5CA7161769E1C";
//    private String cur_url = "file:///android_asset/index.html";
    
        private String cur_url = "http://192.168.199.245:8080/test/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text1);
        onInit();

        HttpFactory.requestByClient(this, HttpFactory.CLIENT_OK_HTTP);

        
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                Authenticator.setDefault(new MyAuthenticator("username", "password"));//
//                try {
//                    Test.TestSocket();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    private void onInit() {
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view,
                    SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        //设置webView参数与 JavascriptInterface，名字"jsCall"需与web界面对应
        webView.getSettings().setNeedInitialFocus(true);
        webView.getSettings().setLightTouchEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.addJavascriptInterface(new StartAppJavaScriptInterface(this),
                "STBAppManager");
        webView.loadUrl(cur_url);

    }

    //实现内部类DemoJavaScriptInterface并传入activity参数

    final class DemoJavaScriptInterface {

        public DemoJavaScriptInterface(MainActivity mainActivity) {
            mActivity = mainActivity;
        }

        // androidStartapp方法与web界面对应 
        @JavascriptInterface
        public void startAppByName(String packageName, String param) {
            PackageManager packageManager = mActivity.getPackageManager();
            Intent intent = packageManager
                    .getLaunchIntentForPackage(packageName);
            intent.putExtra("param", param);
            mActivity.startActivity(intent);
        }

        @JavascriptInterface
        public boolean isAppInstalled() {
            return true;
        }

        /**
         * 
         * close(这里用一句话描述这个方法的作用)
         * 
         * @Title: close
         * @param
         * @return void 返回类型
         * @throws
         */
        public void closeWindow() {
            //            L.d("关闭");
            mActivity.finish();
        }
    }

}
