---
title: "安装"
description: "安装 The Endex，启动服务器并验证是否正常工作。"
---

## 要求

- 兼容 Paper/Spigot 的服务器（推荐 Paper/Purpur）
- Java **17+**
- Minecraft **1.20.1 - 1.21.x**

可选（推荐用于买卖）：

- **Vault**
- 经济提供者（EssentialsX、CMI 等）

## 下载

从以下位置获取最新的 `TheEndex-*.jar`：

- [Modrinth](https://modrinth.com/plugin/theendex)
- [SpigotMC](https://www.spigotmc.org/resources/theendex)
- [GitHub Releases](https://github.com/khristianaarongarcia/endex/releases)

## 安装

1. 停止服务器。
2. 将 jar 文件复制到服务器的 `plugins/` 文件夹。
3. 启动服务器。

## 验证

在游戏内或控制台运行以下命令之一：

```text
/endex
/market
```

如果您看到帮助输出（或市场 GUI 打开），则安装成功。

## 创建的文件

首次启动后，插件会在 `plugins/TheEndex/` 下创建文件：

```text
plugins/
└── TheEndex/
    ├── config.yml
    ├── market.yml
    ├── events.yml
    └── data/
```

<Info>
The Endex 会在版本之间迁移许多配置值，但在更新前您仍应备份 `plugins/TheEndex/`。
</Info>


***

## 第三步：设置经济（可选）

要使用买卖功能，您需要一个经济系统：

### 安装 Vault

1. 下载 [Vault](https://www.spigotmc.org/resources/vault.34315/)
2. 放入 `plugins/` 文件夹
3. 重启服务器

### 安装经济提供者

选择以下流行选项之一：

| 插件                                              | 描述                |
| --------------------------------------------------- | -------------------------- |
| [EssentialsX](https://essentialsx.net/)             | 最流行，功能丰富 |
| [CMI](https://www.spigotmc.org/resources/cmi.3742/) | 一体化管理      |
| [LuckPerms Vault](https://luckperms.net/)           | 轻量级选项         |

***

## 第四步：验证安装

1. 加入服务器
2.  运行版本命令：

    ```
    /endex version
    ```
3. 您应该看到版本信息和状态
4.  打开市场 GUI：

    ```
    /market
    ```

***

## 第五步：初始配置

编辑 `plugins/TheEndex/config.yml` 进行自定义：

```yaml
# 需要查看的基本设置
update-interval: 60        # 价格更新频率（秒）
sensitivity: 0.1           # 价格波动性 (0.0 - 1.0)
tax: 2.5                   # 交易税百分比

# 存储（推荐 sqlite）
storage:
  type: sqlite
  database: market.db

# 启用网页仪表盘
web:
  enabled: true
  port: 8080
```

编辑后，重新加载配置：

```
/endex reload
```

***

## 安装插件扩展

插件扩展可以为 The Endex 添加额外功能。

1. 下载扩展 JAR
2.  放入 `plugins/TheEndex/addons/`：

    ```
    plugins/TheEndex/
    └── addons/
        └── CryptoAddon.jar
    ```
3.  重启服务器或重新加载：

    ```
    /endex reload
    ```

***

## 更新

更新 The Endex：

1. 下载新版本
2. 替换 `plugins/` 中的旧 JAR
3. 重启服务器
4. 在更新日志中检查配置迁移

<Info>
The Endex 会自动在版本之间迁移配置。更新前务必备份！
</Info>

***

## 下一步

* [快速入门指南](quick-start) — 学习基础知识
* [配置](../reference/configuration) — 自定义一切
* [命令](../reference/commands) — 所有可用命令
