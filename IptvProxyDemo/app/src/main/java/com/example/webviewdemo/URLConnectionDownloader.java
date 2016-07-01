package com.example.webviewdemo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public class URLConnectionDownloader {
	// 单纯测试下载
	public static void main(String[] args) {
//		 Properties prop = System.getProperties();
//	        
//	        // socks代理服务器的地址与端口
//	        prop.setProperty("socksProxyHost", "192.168.251.57");
//	        prop.setProperty("socksProxyPort", "9999");
//	        // 设置登陆到代理服务器的用户名和密码
//	        Authenticator.setDefault(new MyAuthenticator("test11", "Password"));
		
		  InetSocketAddress addr = new InetSocketAddress("192.168.251.57", 9999);
	        // Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr); // Socket 代理
	        Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr); // http 代理
	        Authenticator.setDefault(new MyAuthenticator("username", "password"));// 设置代理的用户和密码
		
		download(
				"http://open.play.cn/f/pkg/gm/000/001/766/ba4cf61eh1af4369/WSSGS.apk","E:\\WSSGS.apk",proxy);
		System.out.println("下载完成");
	}

	/**
	 * 下载文件到本地
	 * 
	 * @param urlString
	 *            被下载的文件地址
	 * @param filename
	 *            本地文件名
	 * @param proxy 
	 */
	public static void download(String urlString, String filename, Proxy proxy) {
		// 构造URL
		URL url;
		try {
			url = new URL(urlString);
			// 打开连接
			URLConnection con = url.openConnection(proxy);
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流s
			OutputStream os = new FileOutputStream(filename);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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