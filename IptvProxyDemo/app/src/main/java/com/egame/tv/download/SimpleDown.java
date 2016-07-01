/*
 * FileName:	SimpleDown.java
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		Hein
 * Description:	<文件描述>
 * History:		2013-12-9 1.00 初始版本
 */
package com.egame.tv.download;


/**
 * <功能简述> </Br> <功能详细描述> </Br>
 * 
 * @author Hein
 */
public class SimpleDown {

    public static DownTask create(String url, DownProgressListener listener,String dir) {
        return create(url, null, listener,dir);
    }

    public static DownTask create(String url, String name,
            DownProgressListener listener,String dir) {
        return new DownTask(new DownRunnable(url, name, listener,dir));
    }
}
