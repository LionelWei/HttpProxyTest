## SDK Guide

#### 1. 导入jar包egamehttpclient_20160701_release.jar (版本号以实际为准)
#### 2. OkHttp代理
- 添加dependency
```
dependencies {
    compile 'com.squareup.okhttp3:okhttp:3.3.1'
}
```

- API使用
  爱游戏的代理时在创建OkHttpClient时添加的, 请参考以下示例代码
```
// 原OkHttpClient
OkHttpClient client = new OkHttpClient
                          .Builder()
                          .build();
// 添加代理后的OkHttpClient
client = EgameProxyManager.enableProxy(client);
```
