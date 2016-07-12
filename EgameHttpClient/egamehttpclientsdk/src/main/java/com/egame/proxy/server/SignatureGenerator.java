package com.egame.proxy.server;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: <文件描述>
 * History:     7/12/16 1.00 初始版本
 */

/*
步骤1.参数拼接：
app_id（10位）
user_id（10位）
channel_code（8位）

例如
appid = 99 9999 9999
user_id = 99 9999 9999
channel_code= 9999 9999
拼接后参数为：
99 9999 9999 99 9999 9999 9999 9999

步骤2生成随机数
	随机生成8位16进制随机数(例如28aec797)

步骤3.md5计算
	md5（拼接参数+随机数+固定值）
其中，固定值写死在代码中
步骤4.变换后的随机数
	xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx

结果拼接：
版本号,随机数,appId,userId,channelCode,md5的值

结果：
110, 28aec797, 99 9999 9999, 99 9999 9999, 9999 9999, xxxx xxxx xxxx xxxx xxxx xxxx xxxx xxxx
*/

import com.egame.proxy.EgameProxy;
import com.egame.proxy.util.KeyUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureGenerator {
    private String mSignature;

    public SignatureGenerator() {
        generateSignature();
    }

    public String getSignature() {
        return mSignature;
    }
    private void generateSignature() {
        String version = "1.0.0";
        String appId = EgameProxy.get().getAppId();
        if (appId == null) {
            appId = "1234567890"; // test
        }

        String userId = EgameProxy.get().getUserId();
        if (userId == null) {
            userId = "0987654321"; // test
        }

        String channelCode = EgameProxy.get().getChannelCode();
        if (channelCode == null) {
            channelCode = ""; // test
        }

        String nonce = KeyUtil.getNonce();
        String concat = appId + userId + channelCode + version;
        String md5 = "";
        try {
            byte[] bytes = concat.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md5 = new String(md.digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        mSignature = version + ","
                + nonce + ","
                + appId + ","
                + userId + ","
                + channelCode + ","
                + md5;
    }
}
