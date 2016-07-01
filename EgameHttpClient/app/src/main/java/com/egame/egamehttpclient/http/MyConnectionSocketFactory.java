package com.egame.egamehttpclient.http;



import com.egame.egamehttpclientsdk.util.ProxyUtil;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyConnectionSocketFactory implements SocketFactory {

	

	@Override
	public Socket createSocket() {
		 Proxy proxy = new Proxy(Proxy.Type.SOCKS, ProxyUtil.SOCKET_ADDRESS);
	     System.out.println("-------createSocket-----");
		 return new Socket(proxy);
	     
	}

	
	@Override
	public Socket connectSocket(Socket socket, String remoteHost, int remotePort, InetAddress localAddress, int localPort, HttpParams params) throws IOException,ConnectTimeoutException {
		System.out.println("remoteHost = " + remoteHost + ", remotePort="+remotePort+",localAddress="+localAddress+",localPort="+localPort) ;
	    
	    Socket sock;
         if (socket != null) {
             sock = socket;
         } else {
             sock = createSocket();
         }
         try {
             sock.connect(new InetSocketAddress(remoteHost,remotePort), 10000);
         } catch (SocketTimeoutException ex) {
             throw ex;
         }
         return sock;
	}


    /** {@inheritDoc} */
     
    @Override
    public boolean isSecure(Socket arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }

}
