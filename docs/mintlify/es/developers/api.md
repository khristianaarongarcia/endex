---
title: "API para Desarrolladores"
description: "La API de Java de The Endex para integración de desarrolladores."
---

Si eres un desarrollador de plugins Java/Kotlin que quiere integrarse con The Endex, esta página describe los conceptos básicos de la API.

## Maven / Gradle

The Endex se publica en un repositorio público:

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

## Obtener Instancia de la API

```java
import tech.kagsystems.theendex.api.TheEndexAPI;

TheEndexAPI api = TheEndexAPI.getInstance();
```

## Leer Precios

```java
double price = api.getPrice(Material.DIAMOND);
double buyPrice = api.getBuyPrice(Material.DIAMOND);
double sellPrice = api.getSellPrice(Material.DIAMOND);
```

## Trading Programático

```java
api.buy(player, Material.DIAMOND, 10);
api.sell(player, Material.DIAMOND, 5);
```

## Tenencias

```java
Holdings holdings = api.getHoldings(player);
int diamonds = holdings.getAmount(Material.DIAMOND);
double totalValue = holdings.getTotalValue();
```

## Eventos

Escucha eventos del mercado:

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
    TradeType type = event.getType(); // BUY o SELL
}
```

## Proveedor de Servicios

También puedes usar el administrador de servicios de Bukkit:

```java
RegisteredServiceProvider<TheEndexAPI> provider = 
    Bukkit.getServicesManager().getRegistration(TheEndexAPI.class);
    
if (provider != null) {
    TheEndexAPI api = provider.getProvider();
}
```

## Dependencia Blanda

Asegúrate de que tu `plugin.yml` incluya:

```yaml
softdepend: [TheEndex]
```

Para una guía detallada de la API, consulta `docs/API.md` en el repositorio.
