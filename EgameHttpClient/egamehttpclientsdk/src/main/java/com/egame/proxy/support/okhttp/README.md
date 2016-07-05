#### OkHttp接入

- 添加dependency
```
dependencies {
    compile 'com.squareup.okhttp3:okhttp:3.3.1'
}
```

- 初始化
```
EgameProxy.init(context);
OkHttpClient client = new OkHttpClient.Builder().build();
client = EgameProxy.enableProxy(client);
```