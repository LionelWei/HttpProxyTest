package com.scoket;



import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Socket;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;


@SuppressWarnings("deprecation")
public class Test {
	
	private static final int SET_CONNECTION_TIMEOUT = 5 * 1000;  
    private static final int SET_SOCKET_TIMEOUT = 20 * 1000;  
    @SuppressWarnings("deprecation")
    private static MySocketFactory socketFactory = new MySocketFactory();
    private static SchemeRegistry registry = new SchemeRegistry();
    static{
    	 registry.register(new Scheme("http",socketFactory , 80));  
    }
   
    
    public static HttpClient initHttpClient(boolean userProxy){
    	  HttpParams params = new BasicHttpParams();  
          HttpConnectionParams.setConnectionTimeout(params, 10000);  
          HttpConnectionParams.setSoTimeout(params, 10000);  
          HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
          HttpProtocolParams.setContentCharset(params, HTTP.UTF_8); 
          HttpConnectionParams.setConnectionTimeout(params, SET_CONNECTION_TIMEOUT);  
          HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);  
          if(userProxy){
        	  ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);  
              return new DefaultHttpClient(ccm, params);  
          }else{
        	  return new DefaultHttpClient(params);  
          }
    }
    
    public static Socket newSocket(String host,int port,boolean userProxy) throws IOException{
    	if(userProxy){
    		InetSocketAddress socksaddr = new InetSocketAddress(host,port);
    		return socketFactory.connectSocket(socketFactory.createSocket(),socksaddr, null, null);
    	}else{
    		Socket	sock = new Socket();
    	    sock.connect(new InetSocketAddress(host,port), Utils.connectTimeout);
    		return sock;
    	}
    	
    }
    
    public static void testHttp() throws IllegalStateException, IOException{
    	  try {
  	        HttpGet request = new HttpGet("http://open.play.cn/f/pkg/gm/000/001/766/ba4cf61eh1af4369/WSSGS.apk");
  	        HttpClient client = initHttpClient(false);
  	        System.out.println("Executing request " + request + " via SOCKS proxy " + Utils.socksaddr);
  	        HttpResponse response = client.execute(request);
  	        try {
  	            System.out.println("----------------------------------------");
  	            System.out.println(response.getStatusLine());
  	            
  	            
  	            if (response.getStatusLine().getStatusCode() == 200) {  
  	                String filePath = "E://WSSGS.apk"; // �ļ�·��  
  	                File file = new File(filePath);  
  	                FileOutputStream outputStream = new FileOutputStream(file);  
  	                InputStream inputStream = response.getEntity().getContent();  
  	                byte b[] = new byte[1024];  
  	                int j = 0;  
  	                while ((j = inputStream.read(b)) != -1) {  
  	                    outputStream.write(b, 0, j);  
  	                }  
  	                outputStream.flush();  
  	                outputStream.close();  
  	            }  
  	        } finally {
  	        }
  	    } finally {
  	    }
    }
    
    
    public static void TestSocket() throws IOException{
    	DataInputStream dataIS = new DataInputStream(System.in);
    	OutputStream os;
    	PrintStream ps;
    	String say = "";
    	boolean flag = true;
    	while (flag) {
    		if (flag)	
    			System.out.println("connection ....");
    		Socket socket = newSocket("192.168.251.58",8009,true);
    		System.out.println("connection ok.");
    		flag = true;
    		os = socket.getOutputStream();
    		ps = new PrintStream(os);
    		new Thread(new ReadThread(socket.getInputStream())).start();
    		while (true) {
    			say = dataIS.readLine();
    			ps.println(say);
    		}
    	}
    }
    
    static class ReadThread implements Runnable{

    	private InputStream input;
    	public ReadThread(InputStream in){
    		this.input = in;
    	}
		@Override
		public void run() {
			DataInputStream din = new DataInputStream(input);
			String line= "";
			try {
				while((line=din.readLine()) != null){
					System.out.print("----->");
					System.out.println(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
	public static void main(String[] args) throws Exception {
	     Authenticator.setDefault(new MyAuthenticator("username", "password"));// 
	     TestSocket();
	}
	
	static class MyAuthenticator extends Authenticator {
        private String user = "";
        private String password = "";
 
        public MyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }
 
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }
}
