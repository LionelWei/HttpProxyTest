package com.egame.proxy.model;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: <文件描述>
 * History:     7/11/16 1.00 初始版本
 */

public class IpPoolModel {

    /**
     * text : success
     * attach : null
     * ext : {"http":"192.168.251.57:9998","socks":"192.168.251.57:9999"}
     * code : 0
     */

    public String text;
    public Object attach;
    /**
     * http : 192.168.251.57:9998
     * socks : 192.168.251.57:9999
     */

    public ExtBean ext;
    public int code;

    public static class ExtBean {
        public String http;
        public String socks;

        @Override
        public String toString() {
            return "ExtBean{" +
                    "http='" + http + '\'' +
                    ", socks='" + socks + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "IpPoolModel{" +
                "text='" + text + '\'' +
                ", attach=" + attach +
                ", ext=" + ext +
                ", code=" + code +
                '}';
    }
}
