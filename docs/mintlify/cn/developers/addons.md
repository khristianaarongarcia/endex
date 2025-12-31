---
title: "创建插件扩展"
description: "为 The Endex 创建自定义插件扩展。"
---

The Endex 支持通过简单的模块系统创建自定义插件扩展。

## 插件扩展结构

一个典型的插件扩展包含：

```
MyAddon/
├── build.gradle.kts
├── src/main/kotlin/
│   └── com/example/myaddon/
│       └── MyAddon.kt
└── src/main/resources/
    └── addon.yml
```

## addon.yml

```yaml
name: MyAddon
version: 1.0.0
main: com.example.myaddon.MyAddon
api-version: 1.5.0
description: 我的自定义插件扩展
author: YourName
```

## 主类

```kotlin
import tech.kagsystems.theendex.addons.Addon
import tech.kagsystems.theendex.addons.AddonInfo

@AddonInfo(
    name = "MyAddon",
    version = "1.0.0",
    description = "我的自定义插件扩展"
)
class MyAddon : Addon() {
    
    override fun onEnable() {
        logger.info("MyAddon 已启用！")
    }
    
    override fun onDisable() {
        logger.info("MyAddon 已禁用！")
    }
}
```

## 使用 API

```kotlin
import tech.kagsystems.theendex.api.TheEndexAPI

class MyAddon : Addon() {
    
    override fun onEnable() {
        val api = TheEndexAPI.getInstance()
        
        // 监听价格变化
        api.onPriceChange { material, oldPrice, newPrice ->
            logger.info("$material: $oldPrice -> $newPrice")
        }
    }
}
```

## 注册命令

```kotlin
override fun onEnable() {
    registerCommand("myaddon") { sender, args ->
        sender.sendMessage("Hello from MyAddon!")
        true
    }
}
```

## 注册配置

```kotlin
override fun onEnable() {
    saveDefaultConfig()
    
    val myValue = config.getString("my-key", "default")
}
```

## 安装

将编译好的 JAR 放入：

```text
plugins/TheEndex/addons/
```

重新加载 The Endex：

```text
/endex reload
```

## 示例插件扩展

查看仓库中的 `addons/sample-addon` 作为完整示例。

**加密货币插件扩展** (`addons/crypto`) 是一个更高级的示例，添加了：

- 虚拟货币系统
- 挖矿机制
- 自定义 GUI
- WebSocket 集成

有关深入的插件扩展开发指南，请参阅仓库中的 `docs/ADDONS.md`。
