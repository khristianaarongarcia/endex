---
title: "配置"
description: "config.yml 中的关键设置和常见调优技巧。"
---

主配置文件是：

```text
plugins/TheEndex/config.yml
```

更改后，运行：

```text
/endex reload
```

## 核心定价

```yaml
update-interval-seconds: 60
price-sensitivity: 0.05
history-length: 5
autosave-minutes: 5
save-on-each-update: true
```

## 存储模式

```yaml
storage:
  sqlite: false
```

启用 SQLite 时，重启服务器以便插件在需要时迁移数据。

## 更新检查器

```yaml
update-checker:
  enabled: true      # 启动时检查更新
  notify-ops: true   # 加入时通知 OP 玩家
```

## 投资

```yaml
investments:
  enabled: true
  apr-percent: 5.0
```

## 事件

```yaml
events:
  multiplier-cap: 10.0
  stacking:
    mode: multiplicative
    default-weight: 1.0
    max-active: 0
```

参见：[事件](../features/events)

## 背包感知定价（可选）

```yaml
price-inventory:
  enabled: false
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

## 网页设置（高级）

```yaml
web:
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
  api:
    tokens: []
    token-hashes: []
```

## 自定义网页 UI

要提供您自己的仪表盘包：

```yaml
web:
  custom:
    enabled: false
    root: webui
    reload: false
    export-default: true
```

## GUI 自定义

GUI 布局在 `guis/` 下的单独文件中配置：

- `guis/market.yml` — 主市场界面
- `guis/details.yml` — 物品详情面板
- `guis/holdings.yml` — 虚拟持仓面板
- `guis/deliveries.yml` — 配送队列面板

每个文件支持：
- 带颜色代码的自定义标题
- 背包大小（行 × 9）
- 按钮和物品的槽位位置
- 带自定义材料的分类定义

## 命令别名

自定义命令别名在 `commands.yml` 中配置：

```yaml
aliases:
  shop: "market"           # /shop → /market
  stock: "market holdings" # /stock → /market holdings
  prices: "market top"     # /prices → /market top
```

有关完整的深入配置指南，请参阅仓库中的 `docs/CONFIG.md`。
