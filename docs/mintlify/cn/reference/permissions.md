---
title: "权限"
description: "The Endex 的权限节点。"
---

此页面列出了与 The Endex 相关的权限节点。

在您的权限插件（例如 LuckPerms）中分配这些权限。

## 核心权限

| 权限 | 描述 |
|------------|-------------|
| `theendex.use` | 访问市场命令 |
| `theendex.admin` | 管理命令（重载、事件） |
| `theendex.invest` | 允许创建投资 |
| `theendex.bypass.cooldown` | 跳过交易冷却时间 |

## 网页权限

| 权限 | 描述 |
|------------|-------------|
| `theendex.web.trade` | 通过仪表盘交易 |
| `theendex.web.admin` | 管理仪表盘功能 |
| `theendex.web.link` | 允许使用 /endex link |

## 配送权限

| 权限 | 描述 |
|------------|-------------|
| `theendex.delivery.use` | 访问配送系统 |
| `theendex.delivery.bypass` | 跳过配送延迟 |

## PlaceholderAPI 权限

| 权限 | 描述 |
|------------|-------------|
| `theendex.placeholder.use` | 访问 PAPI 占位符 |

## 插件扩展权限

每个扩展通常在其自己的命名空间下定义权限：

```text
theendex.<addon>.*
theendex.<addon>.use
theendex.<addon>.admin
```

示例（加密货币扩展）：

```text
theendex.crypto.use
theendex.crypto.admin
theendex.crypto.mine
```

有关完整的深入权限指南，请参阅仓库中的 `docs/PERMISSIONS.md`。
