# 沉浸式状态栏插件
在android设备上实现沉浸式状态栏

# Methods
`ImmersePlugin.setDarkMode`(参数为bool值，设置状态栏文字为黑色，支持Miui、Flyme、Android M+)

# Ionic1
见 https://github.com/squallliu/whcyit-immerse

# Ionic2
修改app.module.ts
为android添加statusbarPadding属性
```
IonicModule.forRoot(MyApp, {
  platforms: {
    android: {
      statusbarPadding: true
    }
  }
})
```

# Supported Platforms
- Android 4.4+
