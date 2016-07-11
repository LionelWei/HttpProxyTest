package com.egame.proxy.model;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: 查询流量是否超限
 * History:     7/11/16 1.00 初始版本
 */

public class DataUsageModel {

    /**
     * text : success
     * attach : null
     * ext : {"flag":true}
     * code : 0
     */

    public String text;
    public Object attach;
    /**
     * flag : true
     */

    public ExtBean ext;
    public int code;

    public static class ExtBean {
        public boolean flag;
    }
}
