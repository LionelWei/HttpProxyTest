#### Volley接入

- 添加依赖
```
dependencies {
    compile 'com.squareup.okhttp3:okhttp:3.3.1'
    compile 'eu.the4thfloor.volley:com.android.volley:2015.05.28'
    compile 'com.squareup.okhttp3:okhttp-urlconnection:3.4.0-RC1'
}
```

- 初始化
```
EgameProxy.init(context);
EgameProxy.enableProxy(context);
RequestQueue queue = EgameProxy.enableVolleyProxy(context);
```