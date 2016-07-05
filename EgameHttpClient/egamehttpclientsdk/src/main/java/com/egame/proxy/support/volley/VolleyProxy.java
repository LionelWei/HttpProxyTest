package com.egame.proxy.support.volley;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/5 1.00 初始版本
 */

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyProxy {
    public static RequestQueue requestQueueWithProxy(Context context) {
        return Volley.newRequestQueue(context, new OkHttpStack());
    }
}
