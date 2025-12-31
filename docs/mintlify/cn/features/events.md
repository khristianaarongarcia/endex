---
title: "市场事件"
description: "定时事件，为特定类别或整个市场应用价格乘数。"
---

市场事件是管理员触发的价格乘数，创造临时的买卖机会。

事件在 `plugins/TheEndex/events.yml` 中定义。

## 命令

```text
/market event list
/market event <name>
/market event end <name>
/market event clear
```

<Tip>
定期事件（每周矿石热潮、季节性收获等）是保持交易活跃的好方法。
</Tip>

## 示例配置

```yaml
events:
  ore_rush:
    display-name: "&6⛏ 矿石热潮！"
    description: "采矿材料需求旺盛！"
    duration: 3600
    broadcast: true
    multipliers:
      DIAMOND: 1.5
      EMERALD: 1.5
      GOLD_INGOT: 1.3
```

## 事件如何应用

有效价格是基础价格乘以组合事件乘数。

<Info>
有关完整架构和叠加规则，请参阅仓库中的 `docs/EVENTS.md`。
</Info>

## 计划事件

虽然 The Endex 没有内置调度功能，但您可以使用：

### 外部调度器

使用 cron 作业或服务器调度器触发事件：

```bash
# 每天下午 6 点
0 18 * * * screen -S minecraft -X stuff "/market event ore_rush^M"
```

### 其他插件

**DeluxeScheduler** 或 **ScheduledCommands** 等插件可以触发：
```
/market event <name>
```

---

## 权限

| 权限 | 描述 |
|------------|-------------|
| `theendex.admin` | 管理事件 |

---

## 玩家策略

<Tip>
**事件前买入**  
如果您知道事件即将来临，提前买入受影响的物品。
</Tip>

<Info>
**关注公告**  
事件会在聊天中广播。快速反应以获利！
</Info>

<Warning>
**崩盘事件**  
在市场崩盘期间，持有通常比低价卖出更好。
</Warning>

---

## 管理员技巧

<Tip>
**定期事件**  
安排事件以保持经济活力。每周事件效果很好。
</Tip>

<Info>
**季节性主题**  
将事件与现实世界或服务器季节相匹配以增加沉浸感。
</Info>

<Warning>
**平衡乘数**  
极端乘数（大于 2.0 或小于 0.5）可能会破坏经济稳定。谨慎使用。
</Warning>

---

## 相关页面

- [动态定价](pricing) — 事件如何影响价格
- [配置](../reference/configuration) — 完整 events.yml 参考
- [命令](../reference/commands) — 事件命令
