---
title: "欢迎使用 The Endex"
description: "一款适用于 Minecraft 服务器的动态市场经济插件，具有实时定价、持仓、网页仪表盘和插件扩展支持。"
---

**The Endex** 是一款适用于 Minecraft 服务器的动态玩家驱动市场插件。

它提供：

- **实时物品定价**，根据供需变化
- **持仓**（虚拟存储），让玩家无需整理背包即可交易
- **配送队列**，当存储空间已满时购买物品不会消失
- **市场事件**，用于价格飙升、崩盘和服务器范围的活动
- **网页仪表盘 + REST API**，用于快速交易和集成
- **PlaceholderAPI 支持**，用于计分板、全息图和标签列表

## 核心亮点

| 功能 | 描述 |
| --- | --- |
| 动态定价 | 真实的供需经济学 |
| 世界存储扫描 | 价格可以对存储的物品做出反应（可选） |
| 虚拟持仓 | 存储购买的物品并跟踪盈亏 |
| 网页仪表盘 | 通过浏览器实时更新进行交易 |
| 市场 GUI | 游戏内交易界面 |
| 配送系统 | 永不丢失溢出物品 |
| PlaceholderAPI | 30+ 个占位符供外部插件使用 |
| 插件扩展支持 | 通过扩展增强功能 |
| 投资 | 基于年利率的投资证书 |

## 1.5.7-dec1022 新功能

- **网页仪表盘翻译** — 集成 Google 翻译，支持 26+ 种语言
- **GUI 修复** — 修复了市场详情面板中的 ArrayIndexOutOfBoundsException
- **性能指示器** — 配置选项现在显示 `[PERF: LOW/MEDIUM/HIGH]` 标签
- **翻译配置** — 9 种语言的预翻译配置文件
- **从持仓出售** — 直接从虚拟持仓出售物品 (1.5.7)
- **游戏内布局编辑器** — 自定义商店布局的可视化 GUI 编辑器 (1.5.6)
- **Arclight 支持** — 完全兼容混合服务器 (1.5.7)

## 兼容性

| 要求 | 版本 |
| --- | --- |
| Minecraft | 1.20.1 - 1.21.x |
| Java | 17+ |
| 服务器 | Paper/Spigot/Purpur |
| 经济 | Vault（软依赖） |
| 占位符 | PlaceholderAPI（软依赖） |

## 下一步

- 阅读安装指南：[安装](getting-started/installation)
- 快速开始交易：[快速入门](getting-started/quick-start)
- 设置占位符：[PlaceholderAPI](features/placeholderapi)

***

## 快速链接

* [**安装指南**](getting-started/installation) — 几分钟内开始
* [**命令参考**](reference/commands) — 所有可用命令
* [**配置**](reference/configuration) — 自定义一切
* [**PlaceholderAPI**](features/placeholderapi) — 占位符参考
* [**网页仪表盘**](web-api/dashboard) — 通过浏览器交易
* [**更新日志**](support/changelog) — 最新动态

***

## 切换语言

本文档提供 **10 种语言**版本。要切换语言：

1. 在文档顶部/导航栏中找到 **语言选择器**
2. 点击它查看可用语言
3. 选择您喜欢的语言

| 语言 | 代码 |
| --- | --- |
| 🇺🇸 英语 | `en` |
| 🇨🇳 简体中文 | `cn` |
| 🇪🇸 西班牙语 | `es` |
| 🇫🇷 法语 | `fr` |
| 🇩🇪 德语 | `de` |
| 🇯🇵 日语 | `jp` |
| 🇰🇷 韩语 | `ko` |
| 🇧🇷 葡萄牙语（巴西） | `pt-BR` |
| 🇷🇺 俄语 | `ru` |
| 🇵🇱 波兰语 | `pl` |

<Tip>
游戏内插件也支持多种语言！在 `config.yml` 中的 `language: cn` 设置您喜欢的语言（使用上表中的语言代码）。
</Tip>

***

## 支持

* **Discord:** [加入服务器](https://discord.gg/ujFRXksUBE)
* **GitHub:** [问题反馈](https://github.com/khristianaarongarcia/endex/issues)
* **Modrinth:** [插件页面](https://modrinth.com/plugin/theendex)
* **SpigotMC:** [资源页面](https://www.spigotmc.org/resources/the-endex-dynamic-stock-market-for-minecraft-1-20-1-1-21-11-large-update.128382/)

***

_最后更新：2026年1月4日 | 版本 1.5.8_
