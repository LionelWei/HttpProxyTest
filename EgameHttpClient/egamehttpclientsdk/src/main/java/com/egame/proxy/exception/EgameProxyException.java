package com.egame.proxy.exception;

/*
 * FileName:	
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		weilai
 * Description:	<文件描述>
 * History:		2016/7/8 1.00 初始版本
 */

import java.io.IOException;

public class EgameProxyException extends IOException {
    public EgameProxyException() {
    }

    public EgameProxyException(String detailMessage) {
        super(detailMessage);
    }

    public EgameProxyException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public EgameProxyException(Throwable throwable) {
        super(throwable);
    }
}
