package com.egame.egamehttpclientsdk.okhttp;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/1 1.00 初始版本
 */

import com.egame.egamehttpclientsdk.util.ProxyUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

public class ProxySocketFactory extends SocketFactory {
    public Socket createSocket() {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, ProxyUtil.SOCKET_ADDRESS);
        return new Socket(proxy);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return createSocket();
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return createSocket();
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return createSocket();
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return createSocket();
    }
}
