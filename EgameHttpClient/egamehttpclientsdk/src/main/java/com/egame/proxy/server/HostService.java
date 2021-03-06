package com.egame.proxy.server;

/*
 * FileName:    
 * Copyright:   炫彩互动网络科技有限公司
 * Author:      weilai
 * Description: <文件描述>
 * History:     7/11/16 1.00 初始版本
 */

import android.util.Log;

import com.egame.proxy.EgameProxyInternal;
import com.egame.proxy.model.DataUsageModel;
import com.egame.proxy.model.IpPoolModel;
import com.egame.proxy.util.ProxyUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HostService {
    public boolean isIpAcquired = false;
    public boolean isDataAcquired = false;
    public boolean notified = false;
    private IServiceListener mListener;
    private RestApi mRestApi;

    public HostService(IServiceListener listener) {
        this.mListener = listener;
        mRestApi = new RestApiImpl();
    }

    public interface IServiceListener {
        void onIpPoolChanged(List<InetSocketAddress> httpSocketAddresses,
                             List<InetSocketAddress> socksSocketAddresses);

        void onDataUsageAvailable(boolean isAvailable);
    }

    public void initService() {
        checkIpPool();
        checkDataUsage();
    }

    // 同步获取ip池和数据流量
    public synchronized void waitForIpAndData() {
        while (!isIpAcquired || !isDataAcquired) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkIpPool() {
        Call call = mRestApi.getIpPool();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                IpPoolModel model = gson.fromJson(result, IpPoolModel.class);
                Log.d(ProxyUtil.TAG, model.toString());
                String[] httpProxyArray = model.ext.http.split(",");
                String[] socksProxyArray = model.ext.socks.split(",");
                List<InetSocketAddress> httpProxyList = new ArrayList<InetSocketAddress>();
                for (String s : httpProxyArray) {
                    String[] pair = s.split(":");
                    if (pair.length == 2) {
                        String ip = pair[0];
                        int port = Integer.valueOf(pair[1]);
                        InetSocketAddress socketAddress =
                                InetSocketAddress.createUnresolved(ip, port);
                        httpProxyList.add(socketAddress);
                    }
                }
                List<InetSocketAddress> socksProxyList = new ArrayList<InetSocketAddress>();
                for (String s : socksProxyArray) {
                    String[] pair = s.split(":");
                    if (pair.length == 2) {
                        String ip = pair[0];
                        int port = Integer.valueOf(pair[1]);
                        InetSocketAddress socketAddress =
                                InetSocketAddress.createUnresolved(ip, port);
                        socksProxyList.add(socketAddress);
                    }
                }

                mListener.onIpPoolChanged(httpProxyList, socksProxyList);
                acquiredIpPool();
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });

    }

    private synchronized void acquiredIpPool() {
        isIpAcquired = true;
        if (isDataAcquired && !notified) {
            notifyAll();
        }
    }

    private synchronized void acquiredData() {
        isDataAcquired = true;
        if (isIpAcquired && !notified) {
            notified = true;
            notifyAll();
        }
    }

    private void checkDataUsage() {
        String appId = EgameProxyInternal.get().getAppId();
        String userId = EgameProxyInternal.get().getUserId();
        Call call = mRestApi.getDataUsage(appId, userId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                DataUsageModel model = gson.fromJson(result, DataUsageModel.class);
                Log.d(ProxyUtil.TAG, model.toString());
                boolean isAvailable = model.ext.flag;
                mListener.onDataUsageAvailable(isAvailable);
                acquiredData();
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }
}
