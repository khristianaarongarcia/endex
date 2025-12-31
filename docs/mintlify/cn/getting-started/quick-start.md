---
title: "快速入门"
description: "打开市场，买入、卖出、提取，几分钟内完成。"
---

## 1) 打开市场

```text
/market
```

## 2) 购买物品

在 GUI 中，点击物品并选择数量。

或使用命令（如果服务器启用）：

```text
/market buy diamond 10
```

根据配置，购买的物品会进入**持仓**（推荐）或直接进入背包（旧模式）。

## 3) 查看您的持仓

```text
/market holdings
```

## 4) 提取到背包

```text
/market withdraw diamond
/market withdraw diamond 64
/market withdraw all
```

<Tip>
仅在真正需要物品时才提取。将物品保留在持仓中可以使交易更顺畅并减少物品丢失。
</Tip>

## 5) 出售物品

```text
/market sell diamond 5
```

## 下一步

- 了解算法：[动态定价](../features/pricing)
- 了解存储：[虚拟持仓](../features/holdings)
- 不丢失溢出物品：[配送系统](../features/delivery)

## 管理员指南

### 启动事件

触发影响价格的市场事件：

```
/market event list           # 查看可用事件
/market event ore_rush       # 启动事件
/market event end ore_rush   # 提前结束
/market event clear          # 结束所有事件
```

### 检查状态

```
/endex version    # 插件版本和状态
/endex reload     # 重新加载配置
```

### 管理网页仪表盘

```
/endex web              # 获取会话链接
/endex webui export     # 导出 UI 进行自定义
/endex webui reload     # 重新加载自定义 UI
```

---

## 了解经济系统

### 价格如何运作

The Endex 使用真实的供需：

- **玩家购买** → 需求增加 → **价格上涨** 📈
- **玩家出售** → 供应增加 → **价格下跌** 📉
- **无人交易** → 价格缓慢回归基准

### 价格影响因素

价格可能受以下因素影响：

1. **交易活动** — 买卖交易
2. **玩家背包** — 玩家持有的物品
3. **世界存储** — 服务器各处箱子中的物品
4. **市场事件** — 管理员触发的乘数

### 持仓系统

虚拟持仓系统：

```
购买物品 → 虚拟持仓 → 提取到背包
                ↓
        跟踪盈亏
```

优点：
- 背包已满也不会丢失物品
- 跟踪平均成本基础
- 查看实时盈亏
- 与网页仪表盘配合使用

---

## 常见工作流程

### 日内交易

1. 使用 `/market price <item>` 观察价格
2. 低价买入：`/market buy diamond 64`
3. 等待价格上涨
4. 提取：`/market withdraw diamond`
5. 高价卖出：`/market sell diamond 64`

### 批量交易（网页仪表盘）

1. 获取会话链接：`/endex web`
2. 在浏览器中打开
3. 快速交易多个物品
4. 监控实时价格图表

### 事件套利

1. 关注事件：`/market event list`
2. 在事件前买入受影响物品
3. 等待事件期间价格飙升
4. 在峰值时卖出

---

## 成功技巧

<Tip>
**低买高卖**  
观察价格趋势，在价格下跌时买入。在需求飙升时卖出。
</Tip>

<Info>
**使用持仓系统**  
在需要物品之前不要提取。持仓会自动跟踪您的盈亏。
</Info>

<Warning>
**关注事件**  
市场事件可能会大幅影响价格。定期查看 `/market event list`。
</Warning>

<Tip>
**网页仪表盘**  
对于认真的交易，使用网页仪表盘。它更快并显示图表。
</Tip>

---

## 下一步

- [动态定价](../features/pricing) — 了解算法
- [虚拟持仓](../features/holdings) — 掌握持仓系统
- [网页仪表盘](../web-api/dashboard) — 通过浏览器交易
- [命令参考](../reference/commands) — 所有命令详解
