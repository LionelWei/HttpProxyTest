/*
 * FileName:	DownThread.java
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		Hein
 * Description:	<文件描述>
 * History:		2013-12-9 1.00 初始版本
 */
package com.egame.tv.download;


/**
 * <功能简述> </Br> <功能详细描述> </Br>
 * 
 * @author Hein
 */
public class DownTask extends Thread {

    private DownRunnable mTask = null;

    public DownTask(DownRunnable task) {
        super(task, "Simple:" + task.hashCode());
        mTask = task;
    }

    public void cancel() {
        if (mTask != null) {
            mTask.cancel();
        }
    }
}
