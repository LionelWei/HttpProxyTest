package com.egame.proxy;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: <文件描述>
 * History:     7/12/16 1.00 初始版本
 */

public class DataUsage {
    private static boolean mDataUsageAvailable = false;

    public static boolean isDataUsageAvailable() {
        return mDataUsageAvailable;
    }

    public static void setDataUsageAvailable(boolean dataUsageAvailable) {
        DataUsage.mDataUsageAvailable = dataUsageAvailable;
    }
}
