package com.egame.egamehttpclientsdk.util;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/1 1.00 初始版本
 */

import java.net.InetSocketAddress;

public class ProxyUtil {
//    public static final String PROXY_IP = "192.168.251.57";
//    public static final int PROXY_PORT = 9999;
    public static final String PROXY_IP = "218.94.99.204";
    public static final int PROXY_PORT = 9999;
    public static final InetSocketAddress SOCKET_ADDRESS
            = new InetSocketAddress(PROXY_IP,PROXY_PORT);
}
