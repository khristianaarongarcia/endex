---
title: "开发者 API"
description: "The Endex 的 Java API 用于开发者集成。"
---

如果您是 Java/Kotlin 插件开发者，想要与 The Endex 集成，本页面描述了 API 基础知识。

## Maven / Gradle

The Endex 发布到公共仓库：

### Maven

```xml
<repository>
    <id>theendex</id>
    <url>https://repo.kagsystems.tech/releases</url>
</repository>

<dependency>
    <groupId>tech.kagsystems</groupId>
    <artifactId>theendex-api</artifactId>
    <version>1.5.7</version>
    <scope>provided</scope>
</dependency>
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://repo.kagsystems.tech/releases")
}

dependencies {
    compileOnly("tech.kagsystems:theendex-api:1.5.7")
}
```

## 获取 API 实例

```java
import tech.kagsystems.theendex.api.TheEndexAPI;

TheEndexAPI api = TheEndexAPI.getInstance();
```

## 读取价格

```java
double price = api.getPrice(Material.DIAMOND);
double buyPrice = api.getBuyPrice(Material.DIAMOND);
double sellPrice = api.getSellPrice(Material.DIAMOND);
```

## 编程方式交易

```java
api.buy(player, Material.DIAMOND, 10);
api.sell(player, Material.DIAMOND, 5);
```

## 持仓

```java
Holdings holdings = api.getHoldings(player);
int diamonds = holdings.getAmount(Material.DIAMOND);
double totalValue = holdings.getTotalValue();
```

## 事件

监听市场事件：

```java
@EventHandler
public void onPriceChange(MarketPriceChangeEvent event) {
    Material material = event.getMaterial();
    double oldPrice = event.getOldPrice();
    double newPrice = event.getNewPrice();
}

@EventHandler
public void onTrade(MarketTradeEvent event) {
    Player player = event.getPlayer();
    Material material = event.getMaterial();
    int amount = event.getAmount();
    TradeType type = event.getType(); // BUY 或 SELL
}
```

## 服务提供者

您也可以使用 Bukkit 的服务管理器：

```java
RegisteredServiceProvider<TheEndexAPI> provider = 
    Bukkit.getServicesManager().getRegistration(TheEndexAPI.class);
    
if (provider != null) {
    TheEndexAPI api = provider.getProvider();
}
```

## 软依赖

确保您的 `plugin.yml` 包含：

```yaml
softdepend: [TheEndex]
```

有关深入的 API 指南，请参阅仓库中的 `docs/API.md`。
