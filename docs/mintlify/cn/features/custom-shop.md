---
title: "自定义商店系统"
description: "创建类似 EconomyShopGUI 的基于分类的商店，具有自定义布局"
---

# 自定义商店系统

<Info>
自 v1.5.0 起可用。v1.5.4 增强了基于过滤器的分类和持仓/排序按钮。布局编辑器在 v1.5.6 中修复。
</Info>

## 概述

自定义商店系统提供了默认可滚动市场界面的替代方案。玩家看到的不是所有物品的单一列表，而是带有分类图标的**主菜单**，指向专用的**分类页面**。

### 功能

- **基于分类的导航** - 将物品组织成逻辑分类（方块、矿石、食物等）
- **基于过滤器的自动填充** - 分类自动包含匹配的物品 (v1.5.4+)
- **持仓按钮** - 从任何分类页面访问虚拟持仓 (v1.5.4+)
- **排序按钮** - 在分类内按名称、价格或变化排序 (v1.5.4+)
- **自定义布局** - 完全控制槽位位置、装饰和边框
- **游戏内布局编辑器** - 商店布局的可视化 GUI 编辑器 (v1.5.6 修复)
- **市场价格集成** - 物品自动使用动态市场价格
- **固定价格** - 用静态买卖价格覆盖特定物品
- **多个商店** - 为不同目的创建不同的商店
- **分页** - 大型分类的自动页面导航
- **管理员编辑** - Shift+点击物品进行编辑（需要权限）
- **音效** - 可自定义的交互音效
- **基于权限的物品** - 限制特定组访问物品

## 启用自定义商店模式

编辑您的 `config.yml`：

```yaml
shop:
  # 从 DEFAULT 改为 CUSTOM
  mode: CUSTOM
  
  # 使用哪个商店配置作为主商店
  main-shop: main
  
  # 自定义命令（可选）
  command: shop
```

更改模式后，运行 `/endex reload` 或重启服务器。

## 商店命令

| 命令 | 描述 |
|---------|-------------|
| `/market` | 打开自定义商店（当 mode=CUSTOM 时） |
| `/market shop [id]` | 打开特定商店 |
| `/market stock` | 打开默认市场（绕过模式） |

## 创建商店

商店配置存储在 `plugins/TheEndex/shops/` 中作为 YAML 文件。默认商店是 `main.yml`。

### 基本结构

```yaml
# 唯一标识符
id: main
enabled: true
title: "&5&l服务器商店"

# 主菜单设置
menu:
  title: "&8服务器商店"
  size: 54  # 必须是 9 的倍数
  layout:
    # 定义每个槽位的内容
    19:
      type: CATEGORY
      category: blocks
    21:
      type: CATEGORY
      category: ores

# 装饰
decoration:
  fill-empty: false
  empty-material: GRAY_STAINED_GLASS_PANE

# 带物品的分类
categories:
  blocks:
    name: "方块"
    icon: BRICKS
    items:
      COBBLESTONE: true
      STONE: true
```

## 菜单布局类型

`menu.layout` 部分定义每个槽位显示的内容：

| 类型 | 描述 |
|------|-------------|
| `CATEGORY` | 链接到分类页面 |
| `DECORATION` | 静态装饰物品 |
| `EMPTY` | 空槽位（空气） |
| `CLOSE` | 关闭按钮 |
| `INFO` | 信息显示（显示余额等） |

### 分类槽位

```yaml
layout:
  19:
    type: CATEGORY
    category: blocks  # 引用 categories.blocks
```

### 装饰槽位

```yaml
layout:
  0:
    type: DECORATION
    material: BLACK_STAINED_GLASS_PANE
    name: " "
```

### 信息槽位

```yaml
layout:
  4:
    type: INFO
    material: GOLD_INGOT
    name: "&6您的余额：&f%balance%"
    lore:
      - "&7浏览下面的商店！"
```

## 分类配置

每个分类都有自己的物品页面：

```yaml
categories:
  ores:
    # 显示设置
    name: "矿石与矿物"
    icon: DIAMOND
    icon-name: "&b矿石与矿物"
    icon-lore:
      - "&7珍贵的矿石和矿物"
      - "&e点击浏览！"
    
    # 页面设置
    page-title: "&8矿石与矿物"
    page-size: 54
    
    # 物品槽位范围（用于自动排列）
    item-slots:
      start: 0
      end: 44
    
    # 填充空槽位
    fill-empty: false
    empty-material: GRAY_STAINED_GLASS_PANE
    
    # 此分类中的物品
    items:
      DIAMOND: true
      EMERALD: true
      GOLD_INGOT: true
```

## 基于过滤器的分类 (v1.5.4+)

<Info>
v1.5.4 新功能：分类可以根据材料名称过滤器自动填充物品！
</Info>

使用 `filter` 属性自动填充分类，而不是手动列出每个物品：

```yaml
categories:
  ores:
    name: "矿石与矿物"
    icon: DIAMOND
    filter: "_ORE|RAW_|DIAMOND|EMERALD|LAPIS|COAL|COPPER_INGOT|IRON_INGOT|GOLD_INGOT|NETHERITE"
    
  wood:
    name: "木材与原木"
    icon: OAK_LOG
    filter: "_LOG|_WOOD|_PLANKS|_SLAB|_STAIRS|_FENCE|_DOOR|_BUTTON|_SIGN"
    
  food:
    name: "食物"
    icon: GOLDEN_APPLE
    filter: "APPLE|BREAD|BEEF|PORK|CHICKEN|MUTTON|RABBIT|COD|SALMON|CARROT|POTATO|BEETROOT|MELON|COOKIE|CAKE|PIE|STEW|SOUP"
```

### 过滤器如何工作

- `filter` 属性是与材料名称匹配的**正则表达式模式**
- 使用 `|` 分隔多个模式（或逻辑）
- 只有**在市场中启用**的物品才会被包含
- 匹配过滤器的物品会自动添加到分类中

### 过滤器示例

| 分类 | 过滤器模式 |
|----------|---------------|
| 矿石 | `_ORE\|RAW_\|DIAMOND\|EMERALD` |
| 木材 | `_LOG\|_WOOD\|_PLANKS` |
| 石头 | `STONE\|COBBLE\|GRANITE\|DIORITE\|ANDESITE\|DEEPSLATE` |
| 羊毛 | `_WOOL` |
| 玻璃 | `GLASS` |
| 食物 | `APPLE\|BREAD\|BEEF\|PORK\|CHICKEN` |

### 组合过滤器 + 手动物品

您可以同时使用过滤器和手动物品：

```yaml
categories:
  special:
    name: "特殊物品"
    icon: NETHER_STAR
    filter: "NETHERITE|ELYTRA"
    items:
      # 不匹配过滤器的额外手动物品
      BEACON: true
      TOTEM_OF_UNDYING:
        buy-price: 10000
        use-market-prices: false
```

## 分类页面按钮 (v1.5.4+)

分类页面现在在底部行包含导航按钮：

| 槽位 | 按钮 | 功能 |
|------|--------|----------|
| 45 | 📦 持仓 | 打开您的虚拟持仓面板 |
| 49 | ⬅️ 返回 | 返回主商店菜单 |
| 53 | 🔀 排序 | 循环排序：名称 → 价格 → 变化 |

### 排序选项

排序按钮循环切换：
1. **名称** (A-Z) - 字母顺序
2. **价格** (从高到低) - 最贵的优先
3. **变化** (从高到低) - 涨幅最大的优先

<Tip>
排序偏好按玩家保存，并在会话之间持续！
</Tip>

## 物品配置

### 简单格式（市场价格）

```yaml
items:
  DIAMOND: true
  EMERALD: true
  IRON_INGOT: true
```

物品使用动态市场价格，带有买卖差价。

### 高级格式（固定价格）

```yaml
items:
  DIAMOND:
    material: DIAMOND
    name: "&b✦ 高级钻石"
    lore:
      - "&7一颗闪闪发光的钻石！"
    slot: 0                    # 强制特定槽位
    buy-price: 1000            # 固定买入价格
    sell-price: 800            # 固定卖出价格
    use-market-prices: false   # 禁用市场集成
    quantity: 1                # 每笔交易的物品数量
    permission: vip.diamond    # 所需权限
    commands:
      on-buy:
        - "say %player% 购买了一颗钻石！"
      on-sell:
        - "say %player% 出售了一颗钻石！"
```

### 物品属性

| 属性 | 描述 | 默认值 |
|----------|-------------|---------|
| `material` | Bukkit 材料名称 | 键名 |
| `name` | 自定义显示名称 | 材料名称 |
| `lore` | 额外描述行 | 空 |
| `slot` | 强制特定槽位 | 自动 |
| `buy-price` | 固定买入价格 | 市场价 |
| `sell-price` | 固定卖出价格 | 市场价 |
| `use-market-prices` | 使用动态价格 | true |
| `quantity` | 每次购买的堆叠大小 | 1 |
| `permission` | 所需权限 | 无 |
| `commands.on-buy` | 购买时执行的命令 | 无 |
| `commands.on-sell` | 出售时执行的命令 | 无 |

## 音效配置

```yaml
sounds:
  open-menu: UI_BUTTON_CLICK
  open-category: UI_BUTTON_CLICK
  buy: ENTITY_EXPERIENCE_ORB_PICKUP
  sell: ENTITY_EXPERIENCE_ORB_PICKUP
  error: ENTITY_VILLAGER_NO
  page-change: UI_BUTTON_CLICK
```

使用 [Sound API](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html) 中的 Bukkit 音效名称。

## 价格描述自定义

在 `config.yml` 中，自定义价格在物品描述中的显示方式：

```yaml
shop:
  custom:
    price-lore:
      - "&7"
      - "&a左键点击：&f购买：&6$%buy_price%"
      - "&c右键点击：&f出售：&6$%sell_price%"
      - "&7"
      - "&8库存：%stock% | 需求：%demand%"
    
    admin-lore: "&e⚡ Shift 点击：编辑物品（管理员）"
```

### 可用占位符

| 占位符 | 描述 |
|-------------|-------------|
| `%buy_price%` | 当前买入价格 |
| `%sell_price%` | 当前卖出价格 |
| `%market_price%` | 基础市场价格 |
| `%stock%` | 供应水平 |
| `%demand%` | 需求水平 |

## 导航设置

```yaml
shop:
  custom:
    items-per-page: 45      # 每个分类页面显示的物品数
    show-back-button: true
    back-button-slot: 49
    show-pagination: true
    prev-page-slot: 48
    next-page-slot: 50
    admin-edit: true        # 允许管理员 Shift+点击编辑
```

## 游戏内布局编辑器 (v1.5.6)

<Info>
布局编辑器在 v1.5.6 中进行了全面改造，修复了编辑、保存和模式切换的所有主要问题。
</Info>

布局编辑器提供了一个可视化 GUI，无需接触配置文件即可创建和编辑商店布局。

### 打开编辑器

```
/endex shop editor <shop-id>
```

示例：`/endex shop editor main`

### 编辑器模式

编辑器有 5 种模式，通过底部工具栏选择：

| 模式 | 描述 |
|------|-------------|
| **放置分类** | 点击空槽位放置分类图标 |
| **放置装饰** | 点击空槽位放置装饰物品 |
| **放置按钮** | 点击空槽位放置导航按钮 |
| **编辑槽位** | 点击任何现有物品修改其名称/描述 |
| **删除槽位** | 点击任何现有物品删除它 |

### 编辑名称和描述

1. 切换到**编辑槽位**模式（铅笔图标）
2. **右键点击**任何现有物品
3. 在聊天中输入新**名称**（或输入"skip"保持当前）
4. 在聊天中输入新**描述**（行之间用 `|` 分隔）
5. 更改立即在预览中显示

<Warning>
关闭编辑器前记得点击**保存**！在编辑器内导航时会保留未保存的更改，但关闭时会丢失。
</Warning>

### 工具栏按钮

| 槽位 | 按钮 | 功能 |
|------|--------|----------|
| 45 | 💾 保存 | 将布局保存到配置文件 |
| 46 | ❌ 取消 | 放弃更改并关闭 |
| 47-51 | 模式按钮 | 切换编辑器模式 |
| 53 | ℹ️ 帮助 | 显示编辑器说明 |

### 技巧

- **左键点击**现有物品以使用其默认操作（取决于模式）
- 在编辑模式下**右键点击**现有物品打开名称/描述提示
- 从子菜单选择分类或按钮时会保留更改
- 编辑器使用独特的红色标题前缀（`§4§l⚙`）以避免与玩家商店冲突

## 多个商店

在 `plugins/TheEndex/shops/` 中创建额外的商店文件：

```
shops/
├── main.yml        # 默认商店
├── vip.yml         # 仅 VIP 商店
└── seasonal.yml    # 活动商店
```

打开特定商店：
```
/market shop vip
/market shop seasonal
```

## 权限

| 权限 | 描述 |
|------------|-------------|
| `endex.shop.admin` | 通过 Shift+点击编辑物品 |
| （物品权限） | 访问特定物品 |

## 示例：VIP 商店

```yaml
# shops/vip.yml
id: vip
enabled: true
title: "&6&lVIP 商店"

menu:
  title: "&8VIP 专属商店"
  size: 27
  layout:
    13:
      type: CATEGORY
      category: exclusive

categories:
  exclusive:
    name: "VIP 物品"
    icon: NETHER_STAR
    icon-name: "&6VIP 专属"
    icon-lore:
      - "&7VIP 会员专属物品！"
    page-title: "&8VIP 物品"
    page-size: 54
    items:
      ENCHANTED_GOLDEN_APPLE:
        buy-price: 5000
        sell-price: 2500
        use-market-prices: false
        permission: group.vip
      NETHERITE_BLOCK:
        buy-price: 50000
        sell-price: 25000
        use-market-prices: false
        permission: group.vip
```

## 对比：DEFAULT vs CUSTOM 模式

| 功能 | DEFAULT | CUSTOM |
|---------|---------|--------|
| 界面 | 可滚动列表 | 基于分类 |
| 导航 | 排序与过滤 | 主菜单 → 分类 |
| 布局 | 固定 | 完全可自定义 |
| 物品排列 | 自动 | 手动或自动 |
| 多个商店 | 否 | 是 |
| 价格显示 | 描述 | 可自定义描述 |
| 最适合 | 股票市场感觉 | 传统商店感觉 |

<Tip>
即使启用自定义模式，您也可以随时使用 `/market stock` 访问默认市场！
</Tip>
