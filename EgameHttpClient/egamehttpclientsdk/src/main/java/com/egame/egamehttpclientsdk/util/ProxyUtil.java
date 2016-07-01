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
/*
    public static final String PROXY_IP_INNER = "192.168.251.57";
    public static final int PROXY_PORT_INNER = 9999;
*/
    public static final String PROXY_IP_INNER = "192.168.0.102";
    public static final int PROXY_PORT_INNER = 8888;
    public static final String PROXY_IP_OUTER = "218.94.99.204";
    public static final int PROXY_PORT_OUTER = 9999;
    public static final InetSocketAddress SOCKET_ADDRESS
            = InetSocketAddress.createUnresolved(PROXY_IP_INNER,PROXY_PORT_INNER);
}
