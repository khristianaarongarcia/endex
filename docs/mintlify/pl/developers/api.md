---
title: "API Deweloperskie"
description: "Integruj się z The Endex używając API serwisu Bukkit i wydarzeń."
---

The Endex udostępnia API serwisu Bukkit oraz zestaw wydarzeń, których możesz nasłuchiwać.

## Pobieranie API (Kotlin)

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

## Pobieranie API (Java)

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

## Powierzchnia API (przegląd)

Typowe metody (zobacz źródło dla pełnych sygnatur):

- `listMaterials()`
- `getBasePrice(material)`
- `getCurrentPrice(material)`
- `getEffectivePrice(material)` (zawiera aktywne mnożniki wydarzeń)
- `getPriceHistory(material, limit)`
- `getActiveEvents()`
- `getTrackedTotals()`

## Wydarzenia

Możesz nasłuchiwać tych wydarzeń Bukkit:

- `PriceUpdateEvent` (anulowalne)
- `PreBuyEvent` (anulowalne)
- `PreSellEvent` (anulowalne)

Przykładowy listener:

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

## Bezpieczeństwo wątków

Wywołania tylko do odczytu są bezpieczne. Handel i mutacje powinny działać na głównym wątku serwera.

## Zależność od The Endex

Nie ma jeszcze publicznego repozytorium Maven. Typowe podejście to:

- Umieść `TheEndex-<version>.jar` w folderze `libs/` swojego pluginu
- Dodaj go jako zależność `compileOnly`

```kotlin
dependencies {
    compileOnly(files("libs/TheEndex-1.5.2.jar"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}
```

W `plugin.yml`:

```yaml
softdepend:
  - TheEndex
```
