---
title: "Developer API"
description: "Integrate with The Endex using the Bukkit service API and events."
---

# Developer API

The Endex exposes a Bukkit service API plus a set of events you can listen to.

## Getting the API (Kotlin)

```kotlin
val api = server.servicesManager
    .getRegistration(org.lokixcz.theendex.api.EndexAPI::class.java)
    ?.provider

if (api == null) {
    logger.warning("EndexAPI not found; is The Endex installed?")
} else {
    val price = api.getEffectivePrice(org.bukkit.Material.DIAMOND)
    logger.info("Diamond effective price: $price")
}
```

## Getting the API (Java)

```java
var reg = getServer().getServicesManager()
    .getRegistration(org.lokixcz.theendex.api.EndexAPI.class);

if (reg == null) {
    getLogger().warning("EndexAPI not found; is The Endex installed?");
} else {
    var api = reg.getProvider();
    var price = api.getEffectivePrice(org.bukkit.Material.DIAMOND);
    getLogger().info("Diamond effective price: " + price);
}
```

## API surface (overview)

Common methods (see source for full signatures):

- `listMaterials()`
- `getBasePrice(material)`
- `getCurrentPrice(material)`
- `getEffectivePrice(material)` (includes active event multipliers)
- `getPriceHistory(material, limit)`
- `getActiveEvents()`
- `getTrackedTotals()`

## Events

You can listen to these Bukkit events:

- `PriceUpdateEvent` (cancellable)
- `PreBuyEvent` (cancellable)
- `PreSellEvent` (cancellable)

Example listener:

```kotlin
class MyListener : org.bukkit.event.Listener {
    @org.bukkit.event.EventHandler
    fun onPreBuy(e: org.lokixcz.theendex.api.events.PreBuyEvent) {
        if (e.material == org.bukkit.Material.GOLD_INGOT) {
            e.unitPrice *= 0.9
        }
    }
}
```

## Thread safety

Read-only calls are safe. Trading and mutation should run on the main server thread.

## Depending on The Endex

There isn’t a public Maven repository yet. A common approach is:

- Put `TheEndex-<version>.jar` into your plugin’s `libs/`
- Add it as a `compileOnly` dependency

```kotlin
dependencies {
    compileOnly(files("libs/TheEndex-1.5.2.jar"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}
```

In `plugin.yml`:

```yaml
softdepend:
  - TheEndex
```
