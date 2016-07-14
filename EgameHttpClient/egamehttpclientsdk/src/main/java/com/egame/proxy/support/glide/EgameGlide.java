package com.egame.proxy.support.glide;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/5 1.00 初始版本
 */

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.egame.proxy.EgameProxy;
import com.egame.proxy.EgameProxyInternal;
import com.egame.proxy.support.ProxySocketFactory;
import com.egame.proxy.util.ProxyUtil;

import java.io.InputStream;

import okhttp3.OkHttpClient;

public class EgameGlide {
    public static void init(Context context) {
        try {
            Class.forName("okhttp3.OkHttpClient");
            Class.forName("com.bumptech.glide.Glide");

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (EgameProxyInternal.get().isProxyAvailable()) {
                builder = builder.socketFactory(new ProxySocketFactory());
            }
            OkHttpClient client = builder.build();
            Glide.get(context)
                    .register(GlideUrl.class, InputStream.class,
                            new OkHttpUrlLoader.Factory(client));
        } catch (ClassNotFoundException e) {
            Log.d(ProxyUtil.TAG, "Glide or OkHttp is invalid");
        }
    }
}
