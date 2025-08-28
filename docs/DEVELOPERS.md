# Developers Guide — The Endex API and Addons

This guide shows how to integrate with The Endex as a developer, either as:
- a regular Bukkit/Paper plugin that depends on The Endex’s public API, or
- a lightweight “Addon” jar dropped into `plugins/TheEndex/addons/` that The Endex loads at runtime.

Works on Java 17+ and Paper/Spigot servers that can run The Endex.

---

## 1) Using the public API from your plugin

The Endex registers a Bukkit service for the interface `org.lokixcz.theendex.api.EndexAPI`.

Obtain it via the Bukkit ServicesManager.

Kotlin:
```kotlin
val api = server.servicesManager.getRegistration(org.lokixcz.theendex.api.EndexAPI::class.java)?.provider
if (api == null) {
    logger.warning("EndexAPI not found; is The Endex installed?")
} else {
    val diamond = org.bukkit.Material.DIAMOND
    val price = api.getEffectivePrice(diamond)
    logger.info("Diamond effective price: $price")
}
```

Java:
```java
var reg = getServer().getServicesManager().getRegistration(org.lokixcz.theendex.api.EndexAPI.class);
if (reg == null) {
    getLogger().warning("EndexAPI not found; is The Endex installed?");
} else {
    var api = reg.getProvider();
    var price = api.getEffectivePrice(org.bukkit.Material.DIAMOND);
    getLogger().info("Diamond effective price: " + price);
}
```

### 1.1 API surface (overview)
Key methods (see source for full signatures):
- listMaterials(): Set<Material>
- getBasePrice(material): Double
- getCurrentPrice(material): Double
- getEffectivePrice(material): Double — includes active event multipliers
- getPriceHistory(material, limit): List<PriceSample>
- addDemand(material, amount): Unit — bumps demand, influences next tick
- addSupply(material, amount): Unit — bumps supply, influences next tick
- isBlacklisted(material): Boolean
- multiplierFor(material): Double — combined active multiplier
- getActiveEvents(): List<ActiveMarketEvent>
- getTrackedTotals(): Map<Material, Long> — snapshot of gathered resources this session

Data models (Kotlin data classes):
- PriceSample(time: Instant, price: Double)
- ActiveMarketEvent(name: String, material: Material?, multiplier: Double, endsAt: Instant?)

Note on resource totals:
- The Endex can optionally track gathered resources (block breaks, mob drops, fishing) and periodically persist them to `tracking.yml`.
- Integrations can read a snapshot via `getTrackedTotals()` without touching disk.

### 1.2 Bukkit events you can listen to
- PriceUpdateEvent (Cancellable)
  - Fired before a price is updated per tick.
  - You can cancel or modify the new price.
- PreBuyEvent (Cancellable)
  - Fired before a buy executes.
  - You can cancel or modify the amount and unitPrice.
- PreSellEvent (Cancellable)
  - Fired before a sell executes.
  - You can cancel or modify the amount and unitPrice.

Kotlin example listener:
```kotlin
class MyListener : org.bukkit.event.Listener {
    @org.bukkit.event.EventHandler
    fun onPreBuy(e: org.lokixcz.theendex.api.events.PreBuyEvent) {
        // 10% discount on gold ingots
        if (e.material == org.bukkit.Material.GOLD_INGOT) {
            e.unitPrice = e.unitPrice * 0.9
        }
    }
}
```
Register it from your plugin’s onEnable.

### 1.3 Plugin metadata
In your plugin.yml, declare a soft dependency to ensure The Endex loads first:
```yaml
softdepend:
  - TheEndex
```

### 1.4 Building against The Endex
We don’t publish to a Maven repository yet. Recommended options:
- Drop `TheEndex-<version>.jar` into a `libs/` folder in your plugin project and use compileOnly.

Gradle (Kotlin DSL):
```kotlin
dependencies {
    compileOnly(files("libs/TheEndex-1.0.0.jar"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
```

---

## 2) Writing an Addon (drop-in jar)

Addons are small jars placed in `plugins/TheEndex/addons/`. They aren’t Bukkit plugins (no plugin.yml). The Endex loads them using Java ServiceLoader and calls their lifecycle methods.

### 2.1 Contract
Implement `org.lokixcz.theendex.addon.EndexAddon`:
```kotlin
package com.example.myaddon

import org.lokixcz.theendex.Endex
import org.lokixcz.theendex.addon.EndexAddon
import org.lokixcz.theendex.api.events.PreSellEvent
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MyAddon : EndexAddon, Listener {
    override fun id() = "my-addon"
    override fun name() = "My Addon"
    override fun version() = "1.0.0"

    override fun init(plugin: Endex) {
        plugin.logger.info("[MyAddon] init")
        // Register a listener via the host plugin instance
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun onDisable() {
        // cleanup if needed
    }

    @EventHandler
    fun onPreSell(e: PreSellEvent) {
        if (e.material == Material.DIAMOND) {
            // Bonus: +5% unit price on diamond sells
            e.unitPrice = e.unitPrice * 1.05
        }
    }
}
```

### 2.2 Service descriptor
Add a text file inside your jar at:
- `META-INF/services/org.lokixcz.theendex.addon.EndexAddon`

Contents:
```
com.example.myaddon.MyAddon
```

When the server starts, The Endex will:
- create `plugins/TheEndex/addons/` (if missing),
- scan all `*.jar` in that folder,
- use ServiceLoader to locate `EndexAddon` implementations,
- call `init(plugin)` on each.

On shutdown/reload, your `onDisable()` is called and the classloader is closed.

### 2.3 Accessing the public API from an Addon
Inside `init(plugin)`, you can fetch the API like any other plugin:
```kotlin
val api = plugin.server.servicesManager
    .getRegistration(org.lokixcz.theendex.api.EndexAPI::class.java)
    ?.provider ?: return
val price = api.getEffectivePrice(Material.EMERALD)
plugin.logger.info("Emerald effective price: $price")
```

### 2.4 Building an Addon
Because an addon isn’t a Bukkit plugin, you only need a plain jar with the service file.

Gradle (Kotlin DSL) example:
```kotlin
plugins { kotlin("jvm") version "1.9.24" }
repositories { mavenCentral() }
dependencies {
    compileOnly(files("libs/TheEndex-1.0.0.jar"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}
java { toolchain.languageVersion.set(JavaLanguageVersion.of(17)) }

// Ensure service file is included in the jar’s resources.
sourceSets.main {
    resources.srcDir("src/main/resources")
}
```
Project layout sketch:
```
my-addon/
  src/main/kotlin/com/example/myaddon/MyAddon.kt
  src/main/resources/META-INF/services/org.lokixcz.theendex.addon.EndexAddon
  libs/TheEndex-1.0.0.jar   # copy from your server’s release/
```

### 2.5 Capabilities vs. limitations
- You can: register listeners, use the Endex API, schedule tasks using the provided plugin instance.
- You can’t: declare Bukkit commands via plugin.yml (since you’re not a JavaPlugin). If you need commands or broader server integration, prefer a full Bukkit plugin that depends on The Endex.

---

## 3) Versioning and compatibility tips
- Target Java 17 to match modern Paper environments and The Endex build.
- Use `softdepend: [TheEndex]` if you build a full plugin.
- Expect minor API additions over time; we avoid breaking changes whenever possible. Track The Endex version via `/endex version` in-game or reading its plugin description at runtime.
- For stability-critical integrations, guard calls with null checks and feature flags.

---

## 4) Support
- See `README.md` and `docs/ADDONS.md` for more details.
- Open an issue/PR if you need additional API hooks.
