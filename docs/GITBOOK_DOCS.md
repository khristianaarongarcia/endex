# The Endex Documentation

> A dynamic market economy plugin for Minecraft servers with real-time pricing, virtual holdings, web dashboard, and addon support.

![The Endex Banner](https://i.imgur.com/onDbDSW.png)

---

## Table of Contents

1. [Introduction](#introduction)
2. [Installation](#installation)
3. [Getting Started](#getting-started)
4. [Features](#features)
   - [Dynamic Pricing](#dynamic-pricing)
   - [Market GUI](#market-gui)
   - [Virtual Holdings System](#virtual-holdings-system)
   - [Investments](#investments)
   - [Events & Shocks](#events--shocks)
   - [Delivery System](#delivery-system)
5. [Commands](#commands)
6. [Permissions](#permissions)
7. [Configuration](#configuration)
8. [Web Dashboard](#web-dashboard)
9. [REST API](#rest-api)
10. [Addons](#addons)
11. [Developer API](#developer-api)
12. [Troubleshooting](#troubleshooting)
13. [Changelog](#changelog)

---

## Introduction

**The Endex** brings a living, breathing economy to your Minecraft server. Prices dynamically respond to player activity—buying drives prices up, selling pushes them down. Combined with a beautiful in-game GUI, optional web dashboard, virtual holdings system, and an extensible addon framework, The Endex offers everything you need for a sophisticated server economy.

### Key Highlights

- 📈 **Dynamic Pricing** — Real supply/demand economics
- � **World Storage Scanning** — Prices react to ALL items on the server
- �🎒 **Virtual Holdings** — Store purchased items with P/L tracking
- 🖥️ **Web Dashboard** — Trade from your browser with live updates
- 🎮 **Market GUI** — Beautiful in-game interface
- 📦 **Delivery System** — Never lose overflow items
- 🔌 **Addon Support** — Extend functionality with plugins
- 💹 **Investments** — APR-based investment certificates

### Compatibility

| Requirement | Version |
|-------------|---------|
| Minecraft | 1.20.1 - 1.21.x |
| Java | 17+ |
| Server | Paper/Spigot |
| Economy | Vault (soft dependency) |

---

## Installation

### Step 1: Download

Download the latest release from [Modrinth](https://modrinth.com/plugin/theendex) or [SpigotMC](https://www.spigotmc.org/resources/theendex).

### Step 2: Install

1. Place `TheEndex.jar` in your server's `plugins/` folder
2. Restart your server (or use a plugin manager)
3. The plugin will generate default configuration files

### Step 3: Configure

Edit `plugins/TheEndex/config.yml` to customize:
- Market items and prices
- Update intervals
- Web dashboard settings
- Holdings limits

### Step 4: Set Up Economy

Ensure you have **Vault** and an economy provider (like EssentialsX) installed for transactions to work.

---

## Getting Started

### For Players

1. **Open the Market GUI**
   ```
   /market
   ```

2. **Buy Items** — Items go to your virtual holdings
   ```
   /market buy diamond 10
   ```

3. **Withdraw to Inventory** — Claim items when ready
   ```
   /market withdraw diamond
   ```

4. **Sell Items** — Sell from your inventory
   ```
   /market sell diamond 5
   ```

5. **Check Holdings** — View your virtual portfolio
   ```
   /market holdings
   ```

### For Administrators

1. **Trigger Events**
   ```
   /market event ore_rush
   ```

2. **Check Plugin Status**
   ```
   /endex version
   ```

3. **Reload Configuration**
   ```
   /endex reload
   ```

---

## Features

### Dynamic Pricing

The Endex uses a sophisticated pricing algorithm based on real supply and demand.

#### How It Works

- **Buying** increases demand → prices rise
- **Selling** increases supply → prices fall
- **EMA Smoothing** prevents wild price swings
- **Min/Max Clamps** keep prices reasonable

#### Price Formula

```
newPrice = currentPrice × (1 + sensitivity × (demand - supply) / volume)
```

#### Configuration

```yaml
# How sensitive prices are to trading (0.0 - 1.0)
sensitivity: 0.1

# How many ticks of history to keep
history-length: 100

# Price update interval in seconds
update-interval: 60

# Apply EMA smoothing
smoothing:
  enabled: true
  factor: 0.3
```

#### Inventory-Aware Pricing (Optional)

Prices can react to how many items players are holding:

```yaml
price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

#### World Storage Scanner (NEW in v1.5.1)

Prices can react to ALL items stored across the entire server:

```yaml
price-world-storage:
  enabled: true
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  max-impact-percent: 5.0
  containers:
    chests: true
    barrels: true
    shulker-boxes: true
```

**How It Works:**
- Scans all containers (chests, barrels, shulkers, etc.) in loaded chunks
- Items above `global-baseline` → price pressure DOWN
- Items below `global-baseline` → price pressure UP
- Anti-manipulation: per-chunk caps prevent storage farm exploits
- TPS-aware: skips scanning if server is under load

---

### Market GUI

A beautiful, intuitive interface for all trading operations.

![Market GUI](https://i.imgur.com/2NVDOxj.gif)

#### Features

- **Categories** — Browse by item type
- **Search** — Find items quickly
- **Sorting** — By name, price, or change
- **Quick Amounts** — 1, 5, 10, 64, or custom
- **Price Sparklines** — Visual price history
- **Impact Estimates** — See how your trade affects price

#### Opening the GUI

```
/market
```

#### Navigation

| Slot | Function |
|------|----------|
| Items Area | Click to select, buy, or sell |
| Bottom Left | Previous page |
| Bottom Right | Next page |
| Slot 49 | Category filter |
| Slot 51 | My Holdings |
| Slot 53 | Settings/Search |

---

### Virtual Holdings System

**New in v1.5.0** — A complete redesign of how purchases work.

#### How It Works

1. **Buy Items** → Items go to virtual holdings (not inventory)
2. **Track P/L** → See profit/loss per material
3. **Withdraw** → Claim to inventory when ready

#### Why Virtual Holdings?

- ✅ Never lose items due to full inventory
- ✅ Track average cost basis accurately
- ✅ See real-time profit/loss
- ✅ Global storage limits prevent abuse
- ✅ Works seamlessly with web dashboard

#### Holdings GUI

Access from the market GUI or command:

```
/market holdings
```

**Features:**
- View all holdings with quantity and value
- Average cost and current price per item
- Profit/Loss calculation (green = profit, red = loss)
- Left-click: Withdraw all of material
- Right-click: Withdraw one stack
- "Withdraw All" button for bulk claiming

#### Configuration

```yaml
holdings:
  # Enable/disable the holdings system
  enabled: true
  
  # Maximum total items a player can hold
  max-per-player: 10000
  
  # Maximum different materials per player
  max-materials-per-player: 100
  
  # VIRTUAL = new system, LEGACY = old direct-to-inventory
  mode: VIRTUAL
```

---

### Investments

Create APR-based investment certificates for items.

#### Commands

```bash
# Buy an investment position
/market invest buy diamond 100

# View your investments
/market invest list

# Redeem all matured investments
/market invest redeem-all
```

#### Configuration

```yaml
investments:
  enabled: true
  # Annual Percentage Rate
  apr: 5.0
  # Minimum hold time before redemption (seconds)
  min-hold-time: 3600
```

---

### Events & Shocks

Time-limited events that affect market prices.

![Events](https://i.imgur.com/Qa5Hrhw.gif)

#### Built-in Events

| Event | Effect |
|-------|--------|
| `ore_rush` | Ore prices +50% |
| `harvest_festival` | Crop prices -30% |
| `war_economy` | Weapons +100% |
| `market_crash` | All prices -20% |

#### Commands

```bash
# List available events
/market event list

# Start an event
/market event ore_rush

# End an event early
/market event end ore_rush

# Clear all events
/market event clear
```

#### Custom Events

Define in `events.yml`:

```yaml
events:
  diamond_rush:
    display-name: "Diamond Rush!"
    description: "Diamonds are in high demand!"
    duration: 3600  # 1 hour in seconds
    broadcast: true
    multipliers:
      DIAMOND: 2.0
      DIAMOND_ORE: 1.5
      DIAMOND_BLOCK: 2.5
```

---

### Delivery System

Backup system for overflow when holdings are full.

#### How It Works

1. Buy more items than holdings can store
2. Excess goes to delivery queue
3. Claim via GUI or commands

#### Commands

```bash
# View pending deliveries
/market delivery list

# Claim specific item
/market delivery claim diamond 64

# Claim everything
/market delivery claim-all

# Open delivery GUI
/market delivery gui
```

#### Configuration

```yaml
delivery:
  enabled: true
  auto-claim-on-login: false
  max-pending-per-player: 100000
```

---

## Commands

### Core Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/market` | Open market GUI | `theendex.market` |
| `/market buy <item> <amount>` | Buy items to holdings | `theendex.buy` |
| `/market sell <item> <amount>` | Sell items from inventory | `theendex.sell` |
| `/market price <item>` | Check item price | `theendex.market` |
| `/market top` | View top traded items | `theendex.market` |

### Holdings Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/market holdings` | View your holdings | `theendex.holdings` |
| `/market withdraw <item> [amount]` | Withdraw from holdings | `theendex.withdraw` |
| `/market withdraw all` | Withdraw everything | `theendex.withdraw` |

### Investment Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/market invest buy <item> <amount>` | Create investment | `theendex.invest` |
| `/market invest list` | View investments | `theendex.invest` |
| `/market invest redeem-all` | Redeem investments | `theendex.invest` |

### Delivery Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/market delivery list` | View pending deliveries | `theendex.market` |
| `/market delivery claim <item> [amount]` | Claim delivery | `theendex.market` |
| `/market delivery claim-all` | Claim all deliveries | `theendex.market` |
| `/market delivery gui` | Open delivery GUI | `theendex.market` |

### Event Commands (Admin)

| Command | Description | Permission |
|---------|-------------|------------|
| `/market event list` | List events | `theendex.admin` |
| `/market event <name>` | Start event | `theendex.admin` |
| `/market event end <name>` | End event | `theendex.admin` |
| `/market event clear` | Clear all events | `theendex.admin` |

### Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/endex help` | Show help | `theendex.market` |
| `/endex version` | Show version | `theendex.market` |
| `/endex reload` | Reload config | `theendex.admin` |
| `/endex webui export` | Export web UI | `theendex.admin` |
| `/endex webui reload` | Reload web UI | `theendex.admin` |

---

## Permissions

### Player Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `theendex.market` | `true` | Access market GUI |
| `theendex.buy` | `true` | Buy items |
| `theendex.sell` | `true` | Sell items |
| `theendex.holdings` | `true` | View holdings |
| `theendex.withdraw` | `true` | Withdraw from holdings |
| `theendex.invest` | `true` | Use investments |

### Admin Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `theendex.admin` | `op` | Admin commands |
| `endex.web.trade` | `false` | Web trading access |
| `endex.web.admin` | `op` | View others' holdings |

### Crypto Addon Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `theendex.crypto.info` | `true` | View crypto info |
| `theendex.crypto.balance` | `true` | Check balance |
| `theendex.crypto.buy` | `true` | Buy crypto |
| `theendex.crypto.sell` | `true` | Sell crypto |
| `theendex.crypto.transfer` | `true` | Transfer crypto |
| `theendex.crypto.shop` | `true` | Access crypto shop |
| `theendex.crypto.admin` | `op` | Admin commands |

---

## Configuration

### Main Configuration (`config.yml`)

```yaml
# ===========================================
# THE ENDEX - CONFIGURATION
# ===========================================

# Config version (do not modify)
config-version: 15

# ===========================================
# CORE SETTINGS
# ===========================================

# Price update interval in seconds
update-interval: 60

# Price sensitivity (0.0 - 1.0)
# Higher = more volatile prices
sensitivity: 0.1

# Transaction tax percentage (0.0 - 100.0)
tax: 2.5

# History length for price tracking
history-length: 100

# Autosave interval in minutes
autosave-interval: 5

# ===========================================
# STORAGE
# ===========================================

storage:
  # yaml or sqlite
  type: sqlite
  
  # SQLite database file
  database: market.db

# ===========================================
# HOLDINGS SYSTEM
# ===========================================

holdings:
  # Enable virtual holdings
  enabled: true
  
  # Max items per player
  max-per-player: 10000
  
  # Max different materials
  max-materials-per-player: 100
  
  # VIRTUAL or LEGACY mode
  mode: VIRTUAL

# ===========================================
# DELIVERY SYSTEM
# ===========================================

delivery:
  # Enable delivery queue
  enabled: true
  
  # Auto-claim on login
  auto-claim-on-login: false
  
  # Max pending per player
  max-pending-per-player: 100000

# ===========================================
# PRICE SMOOTHING
# ===========================================

smoothing:
  enabled: true
  factor: 0.3

# ===========================================
# INVENTORY-AWARE PRICING
# ===========================================

price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0

# ===========================================
# WEB DASHBOARD
# ===========================================

web:
  enabled: true
  host: "0.0.0.0"
  port: 8080
  
  # Session settings
  session:
    timeout-minutes: 60
    
  # API tokens (plain or hashed)
  api:
    tokens: []
    token-hashes: []
    
  # Roles
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
    
  # Holdings display
  holdings:
    inventory:
      enabled: true
      include-enderchest: false
      cache-seconds: 15
      
  # Custom UI override
  custom:
    enabled: false
    root-dir: "webui"

# ===========================================
# INVESTMENTS
# ===========================================

investments:
  enabled: true
  apr: 5.0
  min-hold-time: 3600

# ===========================================
# BLACKLIST
# ===========================================

# Items that cannot be traded
blacklist:
  - BEDROCK
  - BARRIER
  - COMMAND_BLOCK
  - STRUCTURE_BLOCK

# ===========================================
# CATEGORIES
# ===========================================

categories:
  ores:
    icon: DIAMOND
    items:
      - DIAMOND
      - EMERALD
      - GOLD_INGOT
      - IRON_INGOT
      - COAL
  crops:
    icon: WHEAT
    items:
      - WHEAT
      - CARROT
      - POTATO
      - BEETROOT
```

### Events Configuration (`events.yml`)

```yaml
# ===========================================
# MARKET EVENTS
# ===========================================

events:
  ore_rush:
    display-name: "&6⛏ Ore Rush!"
    description: "Mining materials in high demand!"
    duration: 3600
    broadcast: true
    multipliers:
      DIAMOND: 1.5
      EMERALD: 1.5
      GOLD_INGOT: 1.3
      IRON_INGOT: 1.2
      COAL: 1.1
      
  harvest_festival:
    display-name: "&a🌾 Harvest Festival"
    description: "Abundant crops flood the market!"
    duration: 7200
    broadcast: true
    multipliers:
      WHEAT: 0.7
      CARROT: 0.7
      POTATO: 0.7
      BEETROOT: 0.7
      
  market_crash:
    display-name: "&c📉 Market Crash!"
    description: "Economic downturn hits all sectors!"
    duration: 1800
    broadcast: true
    multipliers:
      "*": 0.8  # Affects all items
```

---

## Web Dashboard

![Web Dashboard](https://i.imgur.com/2hXIQfx.gif)

### Enabling the Dashboard

```yaml
web:
  enabled: true
  host: "0.0.0.0"
  port: 8080
```

### Accessing the Dashboard

1. Get a session link in-game:
   ```
   /endex web
   ```
2. Open the URL in your browser
3. Session auto-authenticates

### Features

- **Live Prices** — Real-time updates via WebSocket
- **Trading** — Buy/sell from browser
- **Holdings Panel** — View and withdraw holdings
- **Price Charts** — Historical price graphs
- **Item Icons** — Resource pack integration

### Customization

Export and modify the default UI:

```
/endex webui export
```

Edit files in `plugins/TheEndex/webui/`, then:

```
/endex webui reload
```

---

## REST API

### Authentication

Include session token in header:

```
Authorization: Bearer <session-token>
```

Or use API token:

```
Authorization: Bearer <api-token>
```

### Endpoints

#### Market Data

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/market` | List all items |
| `GET` | `/api/market/{item}` | Get item details |
| `GET` | `/api/market/{item}/history` | Price history |

#### Trading

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/buy` | Buy items |
| `POST` | `/api/sell` | Sell items |

**Request body:**
```json
{
  "material": "DIAMOND",
  "amount": 10
}
```

#### Holdings

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/holdings` | Get your holdings |
| `GET` | `/api/holdings/combined` | Holdings + inventory |
| `GET` | `/api/holdings/stats` | Holdings statistics |
| `POST` | `/api/holdings/withdraw` | Withdraw items |

**Withdraw request:**
```json
{
  "material": "DIAMOND",
  "amount": 10
}
```

**Withdraw all:**
```json
{
  "material": "ALL"
}
```

#### Deliveries

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/deliveries` | List pending |
| `POST` | `/api/deliveries/claim` | Claim items |

#### Player

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/balance` | Get balance |
| `GET` | `/api/receipts` | Trade history |

### WebSocket

Connect to `/ws` for real-time updates:

```javascript
const ws = new WebSocket('ws://localhost:8080/ws?session=TOKEN');

ws.onmessage = (event) => {
  const data = JSON.parse(event.data);
  // Handle price updates
};
```

### Server-Sent Events

Alternative to WebSocket:

```javascript
const sse = new EventSource('/sse?session=TOKEN');

sse.onmessage = (event) => {
  const data = JSON.parse(event.data);
  // Handle updates
};
```

---

## Addons

The Endex supports drop-in addons for extended functionality.

### Installing Addons

1. Place addon JAR in `plugins/TheEndex/addons/`
2. Restart server or reload plugin
3. Configure addon in its config file

### Bundled Addons

#### Crypto Addon

Adds cryptocurrency trading to your economy.

**Commands:**
```bash
/crypto info
/crypto balance
/crypto buy <amount>
/crypto sell <amount>
/crypto transfer <player> <amount>
/crypto shop
```

### Creating Addons

See [Developer API](#developer-api) for addon development guide.

---

## Developer API

### Maven/Gradle

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.khristianaarongarcia:theendex:1.5.0")
}
```

### Getting the API

```kotlin
val api = Bukkit.getServicesManager()
    .getRegistration(EndexAPI::class.java)
    ?.provider

api?.let {
    val price = it.getPrice(Material.DIAMOND)
    it.buy(player, Material.DIAMOND, 10)
}
```

### API Methods

```kotlin
interface EndexAPI {
    // Pricing
    fun getPrice(material: Material): Double
    fun getBasePrice(material: Material): Double
    fun getPriceHistory(material: Material): List<Double>
    
    // Trading
    fun buy(player: Player, material: Material, amount: Int): Boolean
    fun sell(player: Player, material: Material, amount: Int): Boolean
    
    // Holdings
    fun getHoldings(player: Player): Map<Material, Int>
    fun addToHoldings(player: Player, material: Material, amount: Int)
    fun removeFromHoldings(player: Player, material: Material, amount: Int)
    
    // Events
    fun getActiveEvents(): List<MarketEvent>
    fun startEvent(name: String)
    fun endEvent(name: String)
}
```

### Creating an Addon

```kotlin
class MyAddon : EndexAddon {
    override fun onEnable(plugin: Endex) {
        // Initialize addon
    }
    
    override fun onDisable() {
        // Cleanup
    }
    
    override fun getCommands(): Map<String, CommandExecutor> {
        return mapOf("myaddon" to MyCommand())
    }
}
```

Register in `META-INF/services/org.lokixcz.theendex.addon.EndexAddon`:
```
com.example.MyAddon
```

---

## Troubleshooting

### Common Issues

#### "Economy not found"

**Cause:** Vault or economy provider not installed.

**Solution:**
1. Install Vault
2. Install an economy provider (EssentialsX, CMI, etc.)
3. Restart server

#### "Holdings full"

**Cause:** Player reached `max-per-player` limit.

**Solution:**
1. Withdraw items: `/market withdraw all`
2. Increase limit in config if needed

#### "Web dashboard not loading"

**Cause:** Port conflict or firewall.

**Solution:**
1. Check port isn't in use
2. Open port in firewall
3. Try different port in config

#### "Prices not updating"

**Cause:** No trading activity.

**Solution:**
- Prices only change when players buy/sell
- Check `update-interval` in config
- Ensure `sensitivity` > 0

### Debug Mode

Enable debug logging:

```yaml
debug: true
```

Check logs in `plugins/TheEndex/logs/`.

### Getting Help

1. Check `/endex version` for version info
2. Review `latest.log` for errors
3. Join [Discord](https://discord.gg/ujFRXksUBE)
4. Open issue on GitHub

---

## Changelog

### Version 1.5.1 (December 2025)

**World Storage Scanner**
- Global item tracking: prices react to ALL items on server
- Scans chests, barrels, shulker boxes across loaded chunks
- Anti-manipulation: per-chunk caps, TPS throttling
- Double chest deduplication
- Nested shulker box content scanning
- Configurable container types and world exclusions

### Version 1.5.0 (December 2024)

**Virtual Holdings System**
- Complete redesign: purchases go to virtual holdings
- Holdings GUI with P/L tracking
- Withdraw commands and web UI buttons
- Minecraft 1.21 support
- Configurable limits (10k items, 100 materials)

### Version 1.4.0 (October 2025)

**Delivery System**
- SQLite-backed delivery queue
- GUI integration with claim buttons
- `/market delivery` commands
- Fixed buy loop bug on 1.20.1

### Version 1.3.x (September 2025)

**Security & Auth**
- Removed reflective access
- Session tokens in Authorization header
- SHA-256 hashed API tokens
- Inventory capacity checks

### Version 1.2.0 (September 2025)

**Custom Web UI**
- Export and customize web interface
- Unified market view
- Performance improvements

### Version 1.1.0 (August 2025)

**Resource Pack Icons**
- Item icons from server resource pack
- Combined holdings badges
- HTTP API documentation

### Version 1.0.0 (August 2025)

**Initial Release**
- Dynamic pricing system
- Market GUI
- Investments
- Events
- Web dashboard
- Addon framework

---

## Support

- **Discord:** [Join Server](https://discord.gg/ujFRXksUBE)
- **GitHub:** [Issues](https://github.com/khristianaarongarcia/theendex/issues)
- **Modrinth:** [Plugin Page](https://modrinth.com/plugin/theendex)
- **SpigotMC:** [Resource Page](https://www.spigotmc.org/resources/theendex)

---

---

*Last updated: December 17, 2025 | Version 1.5.1*

````
