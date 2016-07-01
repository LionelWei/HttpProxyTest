package com.scoket;

/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * The default class for creating plain (unencrypted) sockets.
 * <p>
 * The following parameters can be used to customize the behavior of this
 * class:
 * <ul>
 *  <li>{@link org.apache.http.params.CoreConnectionPNames#CONNECTION_TIMEOUT}</li>
 *  <li>{@link org.apache.http.params.CoreConnectionPNames#}</li>
 * </ul>
 *
 * @since 4.0
 */
@SuppressWarnings("deprecation")
public class MySocketFactory implements SocketFactory {


    public static MySocketFactory getSocketFactory() {
        return new MySocketFactory();
    }

    public MySocketFactory() {
        super();
    }

    public Socket createSocket(final HttpParams params) {
    	 Proxy proxy = new Proxy(Proxy.Type.SOCKS, Utils.socksaddr);
	     return new Socket(proxy);
    }

    public Socket createSocket() {
    	 Proxy proxy = new Proxy(Proxy.Type.SOCKS, Utils.socksaddr);
	     return new Socket(proxy);
    }

    /**
     * @since 4.1
     */
    public Socket connectSocket(
            final Socket socket,
            final InetSocketAddress remoteAddress,
            final InetSocketAddress localAddress,
            final HttpParams params) throws IOException, ConnectTimeoutException {
        if (remoteAddress == null) {
            throw new IllegalArgumentException("Remote address may not be null");
        }
        Socket sock = socket;
        if (sock == null) {
            sock = createSocket();
        }
        if (localAddress != null) {
            sock.bind(localAddress);
        }
//        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
//        int soTimeout = HttpConnectionParams.getSoTimeout(params);

        try {
//            sock.setSoTimeout(soTimeout);
            sock.connect(remoteAddress, Utils.connectTimeout);
        } catch (SocketTimeoutException ex) {
            throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
        }
        return sock;
    }

    /**
     * Checks whether a socket connection is secure.
     * This factory creates plain socket connections
     * which are not considered secure.
     *
     * @param sock      the connected socket
     *
     * @return  <code>false</code>
     *
     * @throws IllegalArgumentException if the argument is invalid
     */
    public final boolean isSecure(Socket sock)
        throws IllegalArgumentException {

        if (sock == null) {
            throw new IllegalArgumentException("Socket may not be null.");
        }
        // This check is performed last since it calls a method implemented
        // by the argument object. getClass() is final in java.lang.Object.
        if (sock.isClosed()) {
            throw new IllegalArgumentException("Socket is closed.");
        }
        return false;
    }

	@Override
	   public Socket connectSocket(
	            final Socket socket,
	            final String host, int port,
	            final InetAddress localAddress, int localPort,
	            final HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
	        InetSocketAddress local = null;
	        if (localAddress != null || localPort > 0) {
	            // we need to bind explicitly
	            if (localPort < 0) {
	                localPort = 0; // indicates "any"
	            }
	            local = new InetSocketAddress(localAddress, localPort);
	        }
	        InetAddress remoteAddress;
	        remoteAddress = InetAddress.getByName(host);
	        InetSocketAddress remote = new InetSocketAddress(remoteAddress, port);
	        return connectSocket(socket, remote, local, params);
	    }



}
