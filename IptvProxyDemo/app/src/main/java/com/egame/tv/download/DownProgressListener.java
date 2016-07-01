/*
 * FileName:	DownProgressListener.java
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		Hein
 * Description:	<文件描述>
 * History:		2013-12-9 1.00 初始版本
 */
package com.egame.tv.download;

import java.io.File;

/**
 * <功能简述> </Br>
 * <功能详细描述> </Br>
 * 
 * @author  Hein
 */
public interface DownProgressListener {

    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_FAILED = -1;
    
    public void onStart(File downFile, long totalSize);
    
    public void onProgress(long downSize, int progress);
    
    public void onCompleted(Result result);
    
    public static class Result {
        
        public long totalTime;
        public int resultCode;
    }
}
