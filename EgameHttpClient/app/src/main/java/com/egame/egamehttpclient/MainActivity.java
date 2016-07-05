package com.egame.egamehttpclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.egame.egamehttpclient.http.HttpFactory;

public class MainActivity extends AppCompatActivity {
    protected Context mContext;

    public TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text1);
        mContext = this;
        start();
    }

    public void start() {
        HttpFactory.requestByClient(this, HttpFactory.CLIENT_OK_HTTP);
//        HttpFactory.requestByClient(this, HttpFactory.CLIENT_ION);
    }
}
