---
title: "动态定价"
description: "The Endex 如何根据供需和可选信号计算价格。"
---

The Endex 价格随时间变化。每个更新周期，每种材料都会根据市场压力获得新价格。

## 核心理念

- **需求**在玩家购买时增加。
- **供应**在玩家出售时增加。
- 调节值（**价格敏感度**）控制经济的波动程度。

## 更新间隔

定价更新按 plugins/TheEndex/config.yml 中配置的固定时间表运行：

```yaml
pricing:
  update-interval-seconds: 5
```

## 价格影响因素

### 1. 交易活动（主要）

核心定价驱动因素。每次买卖都会影响价格：

```yaml
# 价格对交易的敏感程度 (0.0 - 1.0)
sensitivity: 0.1
```

### 2. 背包感知定价

价格可以对玩家持有的物品做出反应：

```yaml
price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

### 3. 世界存储扫描器

价格对服务器上存储的所有物品做出反应：

```yaml
price-world-storage:
  enabled: true
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  max-impact-percent: 5.0
  chunks-per-tick: 50
```

### 4. 市场事件

管理员触发的乘数，影响特定物品：

```yaml
events:
  ore_rush:
    multipliers:
      DIAMOND: 1.5
      EMERALD: 1.5
```

参见：[事件](events)

## 最低/最高价格限制

每个物品都有可配置的价格限制：

```yaml
items:
  DIAMOND:
    base-price: 100.0
    min-price: 10.0
    max-price: 1000.0
```

## 平滑处理

插件应用 EMA（指数移动平均）平滑：

```yaml
smoothing:
  enabled: true
  factor: 0.3
```

这创建了自然的价格曲线，而不是锯齿状的尖峰。

## 理解价格变动

### 示例场景

起始价格：钻石 **100 硬币**

1. **玩家 A 购买 64 个钻石** - 需求增加，价格上涨至约 105 硬币
2. **玩家 B 购买 128 个钻石** - 需求更多，价格上涨至约 115 硬币
3. **玩家 C 出售 256 个钻石** - 供应涌入市场，价格下跌至约 95 硬币
4. **10 分钟无交易** - 价格缓慢稳定在约 98 硬币

### 价格历史

在游戏内查看价格历史：

```text
/market price diamond
```

或通过网页仪表盘的价格图表。

## 服务器管理员技巧

- **保守开始** - 从低敏感度（0.05-0.1）开始，如果市场感觉太稳定则增加。
- **世界存储扫描器** - 在成熟服务器上启用。新服务器可能由于物品数量少而价格波动。
- **测试事件** - 使用事件创造令人兴奋的市场时刻。玩家喜欢价格飙升！

## 相关页面

- [市场事件](events) - 价格乘数
- [配置](../reference/configuration) - 完整配置参考
- [REST API](../web-api/rest-api) - 价格历史端点
