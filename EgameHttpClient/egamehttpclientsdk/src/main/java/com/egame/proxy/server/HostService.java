package com.egame.proxy.server;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: <文件描述>
 * History:     7/11/16 1.00 初始版本
 */

import com.egame.proxy.EgameProxy;
import com.egame.proxy.model.DataUsageModel;
import com.egame.proxy.model.IpPoolModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HostService {
    private IServiceListener mListener;
    private RestApi mRestApi;

    public HostService(IServiceListener listener) {
        this.mListener = listener;
        mRestApi = new RestApiImpl();
    }

    public interface IServiceListener {
        void onIpPoolChanged(List<String> ipPool);
        void onDataUsageAvailable(boolean isAvailable);
    }

    public void initService() {
        checkIpPool();
        checkDataUsage();
    }

    private void checkIpPool() {
        Call call = mRestApi.getIpPool();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                IpPoolModel model = gson.fromJson(result, IpPoolModel.class);
                String[] ipArray = model.ext.http.split(",");
                List<String> ipList = Arrays.asList(ipArray);
                mListener.onIpPoolChanged(ipList);
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });

    }

    private void checkDataUsage() {
        String appId = EgameProxy.get().getAppId();
        String userId = EgameProxy.get().getUserId();
        String channelCode = EgameProxy.get().getChannelCode();
        Call call = mRestApi.getDataUsage(appId, userId, channelCode);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                DataUsageModel model = gson.fromJson(result, DataUsageModel.class);
                boolean isAvailable = model.ext.flag;
                mListener.onDataUsageAvailable(isAvailable);
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }
}
