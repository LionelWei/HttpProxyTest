#### Glide接入

- 添加依赖
```
dependencies {
  compile 'com.squareup.okhttp3:okhttp:3.3.1'
  compile 'com.github.bumptech.glide:glide:3.7.0'
  compile 'com.android.support:support-v4:19.1.0'
  compile 'com.github.bumptech.glide:volley-integration:1.4.0@aar'
}
```

- 在AndroidManifest.xml中添加METE-DATA (与Activity同级)
```
<meta-data
    android:name="com.bumptech.glide.integration.okhttp3.OkHttpGlideModule"
    android:value="GlideModule" />
```

- 添加混淆配置
```
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
```

以上为Glide整合OkHttp的配置, 详情请参见[官方文档](https://github.com/bumptech/glide/wiki/Integration-Libraries)


- 初始化
在Application onCreate()添加:
```
EgameProxy.init(context);
EgameProxy.enableGlideProxy();
```

