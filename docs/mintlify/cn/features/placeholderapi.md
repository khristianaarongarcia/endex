---
title: "PlaceholderAPI 集成"
description: "The Endex 的 PlaceholderAPI 占位符完整列表 - 用于计分板、全息图、标签列表等。"
---

The Endex 提供完整的 PlaceholderAPI 集成，包含 30+ 个占位符，用于显示市场数据、玩家持仓、排行榜和统计数据。

<Info>
PlaceholderAPI 是**软依赖** - 插件没有它也能工作，但占位符将不可用。
</Info>

## 安装

1. 在服务器上安装 [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
2. 安装 The Endex (1.5.3+)
3. 重启服务器
4. 扩展自动注册 - 不需要 `/papi ecloud download`！

## 占位符参考

### 市场物品占位符

使用材料名称获取特定物品的数据。

| 占位符 | 描述 | 示例输出 |
|-------------|-------------|----------------|
| `%endex_price_<MATERIAL>%` | 当前价格 | `800.00` |
| `%endex_price_formatted_<MATERIAL>%` | 带货币符号的价格 | `$800.00` |
| `%endex_change_<MATERIAL>%` | 自上次更新以来的价格变化 | `+5.25%` |
| `%endex_trend_<MATERIAL>%` | 趋势箭头 | `↑`、`↓` 或 `→` |
| `%endex_supply_<MATERIAL>%` | 当前供应量 | `1,234` |
| `%endex_demand_<MATERIAL>%` | 当前需求量 | `567` |

**示例：**
```
%endex_price_DIAMOND%           → 800.00
%endex_price_formatted_DIAMOND% → $800.00
%endex_trend_DIAMOND%           → ↑
%endex_change_DIAMOND%          → +5.25%
%endex_supply_IRON_INGOT%       → 1,234
%endex_demand_GOLD_INGOT%       → 567
```

### 按价格排名的物品 (1-10)

显示市场上最贵和最便宜的物品。

| 占位符 | 描述 |
|-------------|-------------|
| `%endex_top_price_<N>%` | 第 N 贵的物品名称 |
| `%endex_top_price_<N>_value%` | 第 N 贵的物品价格 |
| `%endex_bottom_price_<N>%` | 第 N 便宜的物品名称 |
| `%endex_bottom_price_<N>_value%` | 第 N 便宜的物品价格 |

**示例：**
```
%endex_top_price_1%        → 下界合金锭
%endex_top_price_1_value%  → 2,000.00
%endex_top_price_2%        → 钻石
%endex_top_price_2_value%  → 800.00
%endex_bottom_price_1%     → 圆石
%endex_bottom_price_1_value% → 4.00
```

### 涨幅和跌幅最大的物品 (1-10)

显示价格变动最大的物品。

| 占位符 | 描述 |
|-------------|-------------|
| `%endex_top_gainer_<N>%` | 第 N 大涨幅物品名称 |
| `%endex_top_gainer_<N>_change%` | 第 N 涨幅的变化百分比 |
| `%endex_top_loser_<N>%` | 第 N 大跌幅物品名称 |
| `%endex_top_loser_<N>_change%` | 第 N 跌幅的变化百分比 |

**示例：**
```
%endex_top_gainer_1%        → 绿宝石
%endex_top_gainer_1_change% → +15.50%
%endex_top_loser_1%         → 铁锭
%endex_top_loser_1_change%  → -8.20%
```

### 玩家持仓占位符

显示玩家虚拟持仓的信息。

<Note>
这些占位符需要玩家在线，并且与查看玩家相关。
</Note>

| 占位符 | 描述 |
|-------------|-------------|
| `%endex_holdings_total%` | 玩家的总持仓价值 |
| `%endex_holdings_count%` | 持仓中的物品总数 |
| `%endex_holdings_top_<N>%` | 第 N 有价值的持仓物品名称 |
| `%endex_holdings_top_<N>_value%` | 第 N 有价值的持仓价值 |
| `%endex_holdings_top_<N>_amount%` | 第 N 有价值的持仓数量 |

**示例：**
```
%endex_holdings_total%       → 125,430.50
%endex_holdings_count%       → 2,456
%endex_holdings_top_1%       → 钻石
%endex_holdings_top_1_value% → 64,000.00
%endex_holdings_top_1_amount% → 80
```

### 持仓排行榜 (1-10)

按总持仓价值显示最富有的玩家。

| 占位符 | 描述 |
|-------------|-------------|
| `%endex_top_holdings_<N>%` | 按持仓排名第 N 富有的玩家（名称） |
| `%endex_top_holdings_<N>_value%` | 第 N 富有玩家的持仓价值 |

**示例：**
```
%endex_top_holdings_1%       → Steve
%endex_top_holdings_1_value% → 1,250,000.00
%endex_top_holdings_2%       → Alex
%endex_top_holdings_2_value% → 890,500.00
```

### 市场统计

全市场统计数据。

| 占位符 | 描述 | 示例输出 |
|-------------|-------------|----------------|
| `%endex_total_items%` | 市场中的物品总数 | `57` |
| `%endex_total_volume%` | 所有物品价格之和 | `25,430.00` |
| `%endex_average_price%` | 平均物品价格 | `446.14` |
| `%endex_active_events%` | 活跃市场事件数量 | `2` |

## 使用案例

### 计分板示例

使用 [TAB](https://www.spigotmc.org/resources/tab-1-5-x-1-21-1.57806/) 或 [AnimatedScoreboard](https://www.spigotmc.org/resources/animatedscoreboard.20848/) 等插件创建市场计分板：

```yaml
scoreboard:
  title: "&6⚡ 市场"
  lines:
    - "&7您的持仓："
    - "&f%endex_holdings_total%"
    - ""
    - "&7钻石：&f%endex_price_DIAMOND% %endex_trend_DIAMOND%"
    - "&7绿宝石：&f%endex_price_EMERALD% %endex_trend_EMERALD%"
    - "&7铁锭：&f%endex_price_IRON_INGOT% %endex_trend_IRON_INGOT%"
    - ""
    - "&7最大涨幅：&a%endex_top_gainer_1%"
    - "&7  %endex_top_gainer_1_change%"
```

### 全息图示例

使用 [DecentHolograms](https://www.spigotmc.org/resources/decentholograms.96927/) 或 [HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays) 显示市场信息：

```yaml
# /dh create market
# /dh line add market &6&l⚡ THE ENDEX
# /dh line add market &7实时市场
# /dh line add market &8─────────────
# /dh line add market &f钻石：&a$%endex_price_DIAMOND% %endex_trend_DIAMOND%
# /dh line add market &f绿宝石：&a$%endex_price_EMERALD% %endex_trend_EMERALD%
# /dh line add market &f下界合金：&a$%endex_price_NETHERITE_INGOT% %endex_trend_NETHERITE_INGOT%
# /dh line add market &8─────────────
# /dh line add market &7顶级交易者：&e%endex_top_holdings_1%
```

### 标签列表示例

使用 [TAB](https://www.spigotmc.org/resources/tab.57806/) 在标签中显示市场信息：

```yaml
header:
  - "&6⚡ The Endex 市场"
  - "&7您的持仓：&f%endex_holdings_total%"
  - "&7活跃事件：&f%endex_active_events%"
```

### 排行榜牌子

使用 [PlaceholderAPI Sign](https://www.spigotmc.org/resources/placeholderapi-sign.41475/) 创建排行榜牌子：

```
[papi]
%endex_top_holdings_1%
$%endex_top_holdings_1_value%
&a#1 交易者
```

## 故障排除

### 占位符不工作

1. **检查 PlaceholderAPI 是否已安装：**
   ```
   /papi info
   ```

2. **验证 The Endex 扩展已加载：**
   ```
   /papi list
   ```
   在列表中查找 `endex`。

3. **测试占位符：**
   ```
   /papi parse me %endex_total_items%
   ```

4. **检查控制台**启动时关于 PlaceholderAPI 的错误。

### 常见问题

| 问题 | 解决方案 |
|-------|----------|
| 字面返回 `%endex_...%` | PlaceholderAPI 未安装或扩展未注册 |
| 返回 `N/A` | 材料不在市场中或无效的材料名称 |
| 返回 `0` | 无可用数据（例如无持仓、无历史） |
| 玩家占位符空白 | 玩家必须在线才能获取持仓数据 |

## 相关页面

- [持仓系统](/features/holdings) — 了解虚拟持仓
- [配置](/reference/configuration) — 插件设置
- [命令](/reference/commands) — 可用命令
