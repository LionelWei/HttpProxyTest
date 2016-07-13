package com.egame.proxy.util;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/7 1.00 初始版本
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkUtil {
    public static final int NETWORK_WIFI = 0x1;
    public static final int NETWORK_4G = 0x2;
    public static final int NETWORK_3G2G = 0x3;
    public static final int NETWORK_WIFI_DISCONNECTED = 0x4;
    public static int checkState(Context context) {
        int state = -1;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = cm.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState();
        NetworkInfo.State mobile = cm.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState();
        // 4g
        int subType = 0;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            subType = netInfo.getSubtype();
        }
        Log.d("EGAME_PROXY", "网络的subType=" + subType);
        if (subType == TelephonyManager.NETWORK_TYPE_LTE) {
            Log.d("EGAME_PROXY", "网络状态: 4G");
            state = NETWORK_4G;
        } else if (wifi != null && wifi == NetworkInfo.State.CONNECTED) {
            Log.d("EGAME_PROXY", "网络状态: wifi");
            state = NETWORK_WIFI;
        } else if (mobile != null && mobile == NetworkInfo.State.CONNECTED) {
            Log.d("EGAME_PROXY", "网络状态: mobile");
            state = NETWORK_3G2G;
        } else {
            Log.d("EGAME_PROXY", "网络状态: null");
            state = NETWORK_WIFI_DISCONNECTED;
        }
        return state;
    }

}
