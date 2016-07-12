package com.egame.egamehttpclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.egame.egamehttpclient.http.HttpFactory;
import com.egame.proxy.EgameProxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    protected Context mContext;

    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text1);
        mContext = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
    }

//    String spec = "https://u.play.cn/login/v1/login?service=https%3A%2F%2Fu.play.cn%2Fmc%2Fv1%2Findex&client_id=8888008";
    String spec = "http://open.play.cn/f/pkg/gm/000/001/766/ba4cf61eh1af4369/WSSGS.apk";

    public void start() {
//        System.setProperty("http.proxySet", "true");
//        System.setProperty("http.proxyHost", "192.168.251.57");
//        System.setProperty("http.proxyPort", "9998");
//        System.setProperty("socksProxyHost", ProxyUtil.PROXY_IP_INNER);
//        System.setProperty("socksProxyPort", ""+ProxyUtil.PROXY_PORT_INNER);
//        Authenticator.setDefault(new BasicAuthenticator("zhangqx", "12345678"));

//        sendHttpPost(AbsHttp.DOWNLOAD_URL, null);
//        Log.d("MY_PROXY", sendHttpPost(AbsHttp.DOWNLOAD_URL, null));
//        HttpFactory.requestByClient(this, HttpFactory.CLIENT_HTTP_CLIENT);
        EgameProxy.get().init(this);
        try {
            Log.d("Thread", "sleep start");
            Thread.sleep(10000);
            Log.d("Thread", "sleep end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HttpFactory.requestByClient(this, HttpFactory.CLIENT_OK_HTTP);
    }

    private static String sendHttpPost(String spec, String charset) {
        StringBuffer resultBuffer = new StringBuffer();
        try {
            URL url = new URL(spec);
            URLConnection connection = url.openConnection();

            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            int code = httpURLConnection.getResponseCode();
            Log.d("sendHttpPost", "code: " + code);
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBuffer.toString();
    }

    static class BasicAuthenticator extends Authenticator {
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
         * user, or null if none is provided.
         */
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password.toCharArray());
        }
    }

}
