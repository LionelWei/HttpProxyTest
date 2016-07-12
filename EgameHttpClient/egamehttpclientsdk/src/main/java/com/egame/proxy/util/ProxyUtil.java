package com.egame.proxy.util;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/1 1.00 初始版本
 */

import java.net.InetSocketAddress;
import java.util.List;

public class ProxyUtil {
    public static final String TAG = "EgameProxy";
    public static final String TEST_USER_NAME = "zhangqx";
    public static final String TEST_PASSWORD = "12345678";

    // socks代理
    public static final String PROXY_IP_INNER = "192.168.251.57";
    public static final int PROXY_PORT_INNER = 9999;

    // http代理
    public static final String HTTP_PROXY_IP_INNER = "192.168.251.57";
    public static final int HTTP_PROXY_PORT_INNER = 9998;

    // Fiddler代理
    public static final String FIDDLER_PROXY_IP_INNER = "192.168.0.100";
    public static final int FIDDLER_PROXY_PORT_INNER = 8888;

    // socks外网代理
    public static final String PROXY_IP_OUTER = "218.94.99.204";
    public static final int PROXY_PORT_OUTER = 9999;
    public static final InetSocketAddress SOCKET_ADDRESS
            = InetSocketAddress.createUnresolved(PROXY_IP_INNER,PROXY_PORT_INNER);
    public static final InetSocketAddress HTTP_SOCKET_ADDRESS
            = InetSocketAddress.createUnresolved(HTTP_PROXY_IP_INNER,HTTP_PROXY_PORT_INNER);

}
