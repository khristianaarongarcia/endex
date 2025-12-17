---
title: "Developer API"
description: "Build custom addons using The Endex API."
---
# Developer API

Java/Kotlin API for integrating with The Endex.

---

## Maven/Gradle

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.LokiXCZ:TheEndex:1.5.1")
}
```

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.LokiXCZ:TheEndex:1.5.1'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.LokiXCZ</groupId>
        <artifactId>TheEndex</artifactId>
        <version>1.5.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## Getting the API

```kotlin
import org.lokixcz.theendex.api.EndexAPI

val api: EndexAPI = EndexAPI.get()
```

Or from plugin instance:

```kotlin
val endex = Bukkit.getPluginManager().getPlugin("TheEndex") as? Endex
val api = endex?.api
```

---

## Core API Methods

### Market Operations

```kotlin
// Get all tracked items
val items: List<TrackedItem> = api.getItems()

// Get specific item
val diamond: TrackedItem? = api.getItem(Material.DIAMOND)

// Get current price
val price: Double = api.getPrice(Material.DIAMOND)

// Get price change percentage
val change: Double = api.getPriceChange(Material.DIAMOND)

// Check if item is tracked
val tracked: Boolean = api.isTracked(Material.DIAMOND)
```

### Trading

```kotlin
// Buy items for player
val result = api.buyItem(player, Material.DIAMOND, 64)
if (result.success) {
    player.sendMessage("Bought for ${result.totalCost}")
}

// Sell items from player inventory
val sellResult = api.sellItem(player, Material.DIAMOND, 32)

// Sell all of a material
api.sellItem(player, Material.DIAMOND, -1)
```

### Holdings

```kotlin
// Get player holdings
val holdings: List<Holding> = api.getHoldings(player)

// Get specific holding
val holding: Holding? = api.getHolding(player, Material.DIAMOND)

// Add to holdings (bought items)
api.addHolding(player, Material.DIAMOND, 64, 100.0)

// Withdraw from holdings
val withdrawn = api.withdrawHolding(player, Material.DIAMOND, 32)

// Get holdings stats
val stats = api.getHoldingsStats(player)
println("Total value: ${stats.totalValue}")
println("P/L: ${stats.totalPL}")
```

### Price History

```kotlin
// Get price history
val history: List<PricePoint> = api.getPriceHistory(
    Material.DIAMOND,
    limit = 120
)

// With time filter
val recentHistory = api.getPriceHistory(
    Material.DIAMOND,
    sinceTimestamp = System.currentTimeMillis() - 3600000
)
```

---

## Events

Listen to Endex events in your plugin:

```kotlin
import org.lokixcz.theendex.api.events.*

class MyListener : Listener {
    
    @EventHandler
    fun onPriceChange(event: EndexPriceChangeEvent) {
        val material = event.material
        val oldPrice = event.oldPrice
        val newPrice = event.newPrice
        
        if (event.changePercent > 10.0) {
            Bukkit.broadcastMessage("$material spiked ${event.changePercent}%!")
        }
    }
    
    @EventHandler
    fun onTrade(event: EndexTradeEvent) {
        val player = event.player
        val type = event.type  // BUY or SELL
        val material = event.material
        val amount = event.amount
        val price = event.totalPrice
        
        // Cancel the trade
        if (someCondition) {
            event.isCancelled = true
        }
    }
    
    @EventHandler
    fun onMarketEvent(event: EndexMarketEventStartEvent) {
        val eventType = event.marketEvent.type
        val affected = event.marketEvent.affectedItems
        
        println("Market event started: $eventType")
    }
}
```

### Available Events

| Event | Description | Cancellable |
|-------|-------------|-------------|
| `EndexPriceChangeEvent` | Price tick occurred | No |
| `EndexTradeEvent` | Player buying/selling | Yes |
| `EndexHoldingChangeEvent` | Holdings modified | No |
| `EndexMarketEventStartEvent` | Market event begins | Yes |
| `EndexMarketEventEndEvent` | Market event ends | No |

---

## Creating Addons

### Addon Interface

```kotlin
import org.lokixcz.theendex.addon.EndexAddon

class MyAddon : EndexAddon {
    
    override val name: String = "my-addon"
    override val version: String = "1.0.0"
    
    private lateinit var api: EndexAPI
    
    override fun onEnable(api: EndexAPI) {
        this.api = api
        // Initialize your addon
    }
    
    override fun onDisable() {
        // Cleanup
    }
    
    override fun getCommands(): Map<String, AddonCommand> {
        return mapOf(
            "hello" to HelloCommand()
        )
    }
    
    override fun getApiRoutes(): List<AddonRoute> {
        return listOf(
            AddonRoute("GET", "/status") { ctx ->
                ctx.json(mapOf("status" to "ok"))
            }
        )
    }
}
```

### Service Registration

Create file `src/main/resources/META-INF/services/org.lokixcz.theendex.addon.EndexAddon`:

```
com.example.myaddon.MyAddon
```

### Addon Commands

```kotlin
class HelloCommand : AddonCommand {
    override val name = "hello"
    override val permission = "myaddon.hello"
    override val usage = "/endex my-addon hello [player]"
    
    override fun execute(sender: CommandSender, args: Array<String>): Boolean {
        sender.sendMessage("Hello from my addon!")
        return true
    }
    
    override fun tabComplete(sender: CommandSender, args: Array<String>): List<String> {
        return emptyList()
    }
}
```

### Addon API Routes

```kotlin
override fun getApiRoutes(): List<AddonRoute> {
    return listOf(
        // GET /api/addon/my-addon/prices
        AddonRoute("GET", "/prices") { ctx ->
            val prices = getMyPrices()
            ctx.json(prices)
        },
        
        // POST /api/addon/my-addon/buy
        AddonRoute("POST", "/buy") { ctx ->
            val body = ctx.bodyAsClass<BuyRequest>()
            val result = processBuy(body)
            ctx.json(result)
        }
    )
}
```

---

## Data Models

### TrackedItem

```kotlin
data class TrackedItem(
    val material: Material,
    val price: Double,
    val basePrice: Double,
    val minPrice: Double,
    val maxPrice: Double,
    val change: Double,
    val volatility: Double,
    val supply: Long,
    val demand: Long
)
```

### Holding

```kotlin
data class Holding(
    val material: Material,
    val quantity: Int,
    val avgCost: Double,
    val currentValue: Double,
    val profitLoss: Double,
    val profitLossPercent: Double
)
```

### TradeResult

```kotlin
data class TradeResult(
    val success: Boolean,
    val message: String,
    val amount: Int,
    val unitPrice: Double,
    val totalCost: Double,
    val newBalance: Double
)
```

---

## Thread Safety

The API is thread-safe for read operations. Write operations (trading, holdings) should be called from the main thread or use:

```kotlin
Bukkit.getScheduler().runTask(plugin) {
    api.buyItem(player, material, amount)
}
```

---

## Best Practices

1. **Check plugin enabled**
   ```kotlin
   if (Bukkit.getPluginManager().isPluginEnabled("TheEndex")) {
       // Safe to use API
   }
   ```

2. **Soft depend**
   ```yaml
   # plugin.yml
   softdepend: [TheEndex]
   ```

3. **Handle missing API**
   ```kotlin
   val api = try {
       EndexAPI.get()
   } catch (e: Exception) {
       logger.warning("TheEndex not found")
       null
   }
   ```

4. **Cache sparingly** — Prices change frequently

---

## Example Plugin

Complete example integration:

```kotlin
class MyPlugin : JavaPlugin(), Listener {
    
    private var endexApi: EndexAPI? = null
    
    override fun onEnable() {
        // Hook into Endex if available
        if (server.pluginManager.isPluginEnabled("TheEndex")) {
            endexApi = EndexAPI.get()
            server.pluginManager.registerEvents(this, this)
            logger.info("Hooked into TheEndex!")
        }
    }
    
    @EventHandler
    fun onTrade(event: EndexTradeEvent) {
        // Log all trades
        logger.info("${event.player.name} ${event.type} ${event.amount}x ${event.material}")
    }
    
    fun getPriceOrDefault(material: Material, default: Double): Double {
        return endexApi?.getPrice(material) ?: default
    }
}
```

---

## Related Pages

- [Addons](addons.md) — Install and manage addons
- [REST API](../web-api/rest-api.md) — HTTP API reference
- [Events](../features/events.md) — Market events
