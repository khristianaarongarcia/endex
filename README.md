# The Endex## Public API (for developers)



<div align="center">The Endex exposes a lightweight Bukkit API and an HTTP API.



**Dynamic, demand-driven market for Minecraft (Paper/Spigot 1.20.1 - 1.21.x)**### Bukkit API example



[![Version](https://img.shields.io/badge/version-1.5.4-blue.svg)](https://github.com/khristianaarongarcia/endex/releases)```kotlin

[![Minecraft](https://img.shields.io/badge/minecraft-1.20.1--1.21.x-brightgreen.svg)](https://www.minecraft.net/)val api = server.servicesManager.load(org.lokixcz.theendex.api.EndexAPI::class.java)

[![Discord](https://img.shields.io/discord/1234567890?color=7289DA&label=Discord)](https://discord.gg/7edgWnfCbY)if (api != null) {

  val mats = api.listMaterials()

[Documentation](https://lokixcz-plugins.kagsystems.tech/) • [Discord](https://discord.gg/7edgWnfCbY) • [Spigot](https://www.spigotmc.org/resources/the-endex-dynamic-stock-market-for-minecraft-1-20-1-1-21-11-large-update.128382/) • [Modrinth](https://modrinth.com/plugin/theendex)  val diamond = org.bukkit.Material.DIAMOND

  val cur = api.getCurrentPrice(diamond)

</div>}

```

---

You can also listen to Bukkit events:

Prices react to player behavior, server-wide storage, and random market events. Includes a rich Market GUI, timed events, an optional web dashboard, virtual holdings system, PlaceholderAPI integration, and an addon framework.

```kotlin

## Features@EventHandler fun onPriceUpdate(e: org.lokixcz.theendex.api.events.PriceUpdateEvent) { /* inspect/modify/cancel */ }

@EventHandler fun onPreBuy(e: org.lokixcz.theendex.api.events.PreBuyEvent) { /* adjust unitPrice or amount */ }

- **Dynamic Pricing** — Prices respond to buy/sell demand with configurable sensitivity, EMA smoothing, and per-item clamps@EventHandler fun onPreSell(e: org.lokixcz.theendex.api.events.PreSellEvent) { /* enforce rules */ }

- **World Storage Scanner** — Prices react to items stored across ALL server containers```

- **Virtual Holdings** — Buy items into virtual storage with P/L tracking and cost basis

- **PlaceholderAPI** — 30+ placeholders for scoreboards, holograms, tab lists### HTTP API

- **Market GUI** — Beautiful interface with search, categories, sorting, and real-time charts

- **Web Dashboard** — REST API with live updates, charts, and browser-based tradingSee `docs/API.md` for the full HTTP API: endpoints, parameters, auth, ETag, SSE/WS, and the `/icon/{material}` icon endpoint.

- **Market Events** — Time-boxed price multipliers with server broadcasts

- **Delivery System** — Overflow protection when storage is full# The Endex

- **Addon Framework** — Extensible API for custom functionality

- **Update Checker** — Automatic notifications for new versionsDynamic, demand-driven market for Minecraft (Paper). Supports 1.20.1+ with Vault economy. Browse, buy, sell, invest, and run time-limited market events.

- **GUI Customization** — Full layout customization via config files

- **Command Aliases** — Create custom command shortcuts## Features

- Dynamic prices: respond to player buy/sell demand with configurable sensitivity

## Quick Start- GUI market with search, categories, sorting, and item details (ASCII history sparkline)

- Delivery system: overflow purchases queue safely with GUI + commands to claim pending items

1. **Install**- Rich commands: buy/sell/price/top, admin reload, events management

   - Download from [Spigot](https://www.spigotmc.org/resources/128382/) or [Modrinth](https://modrinth.com/plugin/theendex)- Events system: item/category multipliers with stack cap and expiry

   - Place in your `plugins/` folder- Storage: YAML by default or SQLite with auto-migration from existing YAML

   - Requires Vault + an economy plugin (EssentialsX, CMI, etc.)- Investments: passive APR-based investment certificates

- Backups and CSV history export for analysis

2. **Start Server**- Addon framework: drop-in jars with subcommands, aliases, and tab completion (see docs/ADDONS.md) — addon content now integrates directly (no separate Addons tab)

   ```- Web UI item icons served from a resource pack (configurable via `web.icons.*`)

   java -jar paper.jar- Customizable Web UI override (`web.custom.*`) – export & edit `index.html` and assets

   ```- Resource tracking: observe gathered resources server-wide; admin `/endex track dump`; API `getTrackedTotals()`

- Crypto Addon (optional): `/endex crypto ...`, YAML-driven `shop.yml`, per-item permissions, fixed/market pricing, price history CSV

3. **Open Market**

   ```## Quick start

   /market1. Build the plugin

   ```   - Windows PowerShell

     - `./gradlew.bat clean build -x test --no-daemon`

## Commands   - The shaded jar is placed in `MCTestServer/plugins` automatically. You can also copy it into your server `/plugins`.

2. Start your Paper server with Vault installed

| Command | Description |3. Join and open the market: `/endex market` or `/market`

|---------|-------------|

| `/market` | Open market GUI |## Configuration

| `/market buy <item> <amount>` | Purchase items |- Default config is generated at `plugins/TheEndex/config.yml`

| `/market sell <item> <amount>` | Sell items |- Edit safely and then run `/endex reload`

| `/market price <item>` | Check current price |- See `docs/CONFIG.md` for all options (update interval, sensitivity, storage = sqlite, events, investments, history export, blacklist, web icons, addons, etc.)

| `/market holdings` | View virtual holdings |

| `/market withdraw <item>` | Withdraw from holdings |Config versioning:

| `/market delivery list` | View pending deliveries |- This build expects `config-version: 1` at the top of `config.yml`. If it differs, a warning is logged with guidance to back up and regenerate from defaults.

| `/endex reload` | Reload configuration |

| `/endex version` | Show plugin version |## Commands

See `docs/COMMANDS.md` for full details.

See [COMMANDS.md](docs/COMMANDS.md) for full command reference.

Highlights:

## Configuration- `/market buy <material> <amount>` and `/market sell <material> <amount>`

- `/market price <material>` with event multiplier insight and ASCII chart (if enabled)

Default config generated at `plugins/TheEndex/config.yml`- `/market top` for movers

- `/endex market` to open GUI

Key settings:- `/endex reload` to reload config, events, and market data

```yaml- `/endex version` to show plugin version and storage mode

# Dynamic pricing- `/endex track dump` to dump top gathered resources (if tracking enabled)

price-sensitivity: 0.05- Addon aliases like `/cc` can be registered by addons for convenience

update-interval-seconds: 60- Event admin: `/market event [list|<name>|end <name>|clear]`



# Virtual holdings## Storage: YAML vs SQLite

holdings:- Default: YAML files in `plugins/TheEndex`.

  enabled: true- To enable SQLite, set `storage.sqlite: true` and restart. On first run with SQLite, existing YAML items/history are migrated into `plugins/TheEndex/market.db`.

  max-per-player: 10000

## Development

# World storage scanner- Kotlin, Paper API 1.20.1 target, Java 17 toolchain

price-world-storage:- Shadow jar is produced (no classifier) and copied to `MCTestServer/plugins`

  enabled: true- Run a local test server via the run task: `./gradlew.bat runServer`

  sensitivity: 0.01- Public API: see `docs/DEVELOPERS.md` (now includes `getTrackedTotals()`)



# Update checker## License

update-checker:This project is provided as-is for your server. External dependencies (Paper API, Vault API, sqlite-jdbc) retain their own licenses.

  enabled: true<!-- Top blurb -->

  notify-ops: true# The Endex

```

Dynamic, demand-driven market for Minecraft (Paper). Prices react to player behavior and can optionally account for online players' inventories. Includes a rich Market GUI, timed events, an optional web dashboard, and an addon framework.

See [CONFIG.md](docs/CONFIG.md) for all options.

## Highlights

## PlaceholderAPI- Dynamic pricing with sensitivity, smoothing, and per-item clamps

- Inventory-aware pricing (optional) using safe, online-only snapshots

The Endex provides 30+ placeholders when PlaceholderAPI is installed:- Market GUI with search, categories, sorting, details panel, and tiny sparkline

- Investments (buy, list, redeem-all) and time-limited events

| Placeholder | Description |- Web dashboard (optional) with real item icons and Combined Holdings (Invest + Inv)

|-------------|-------------|- YAML or SQLite storage with migration; CSV history export

| `%endex_price_DIAMOND%` | Current diamond price |- Addon framework + optional Crypto Addon

| `%endex_trend_DIAMOND%` | Price trend (↑/↓/→) |

| `%endex_holdings_total%` | Player's total holdings value |## Quick start

| `%endex_top_holdings_1%` | Richest player name |1. Build

| `%endex_total_items%` | Total items in market |   - Windows PowerShell

     - `./gradlew.bat clean build -x test --no-daemon`

See full placeholder reference in [documentation](https://lokixcz-plugins.kagsystems.tech/features/placeholderapi).   - The shaded jar is produced and copied to `MCTestServer/plugins` and `release/` automatically.

2. Install

## Storage   - Copy the jar from `release/` (or `build/libs`) to your server `plugins/` folder.

3. Run and open the market

- **YAML** (default) — Files in `plugins/TheEndex/`   - Start Paper (Java 17) with Vault installed

- **SQLite** — Enable with `storage.sqlite: true`   - `/market` or `/endex market`



Automatic migration from YAML to SQLite on first run with SQLite enabled.## Docs

- Commands: `docs/COMMANDS.md`

## Building from Source- Config: `docs/CONFIG.md`

- API: `docs/API.md`

```powershell- Events guide: `docs/EVENTS.md`

# Clone repository- Addons: `docs/ADDONS.md`, developers: `docs/DEVELOPERS.md`

git clone https://github.com/khristianaarongarcia/endex.git- Reverse proxy (HTTPS): `docs/REVERSE_PROXY.md`

cd endex- Changelog: `docs/changelogs.md`



# Build## Configuration quick bits

./gradlew.bat clean build -x test --no-daemon- Inventory-aware pricing



# JAR output: build/libs/```yaml

```price-inventory:

  enabled: true

## Documentation  sensitivity: 0.02

  per-player-baseline: 64

- [Commands Reference](docs/COMMANDS.md)  max-impact-percent: 10.0

- [Configuration Guide](docs/CONFIG.md)```

- [HTTP API](docs/API.md)

- [Events System](docs/EVENTS.md)- Web combined holdings & roles

- [Addon Development](docs/ADDONS.md)

- [Developer API](docs/DEVELOPERS.md)```yaml

- [Full Documentation](https://lokixcz-plugins.kagsystems.tech/)web:

  roles:

## Public API    default: TRADER

    trader-permission: endex.web.trade

### Bukkit API    admin-view-permission: endex.web.admin

  holdings:

```kotlin    inventory:

val api = server.servicesManager.load(org.lokixcz.theendex.api.EndexAPI::class.java)      enabled: true

if (api != null) {      include-enderchest: false

    val price = api.getCurrentPrice(Material.DIAMOND)      cache-seconds: 15

    val materials = api.listMaterials()```

}

```## Build options



### Bukkit EventsYou need either the Gradle Wrapper (gradlew/gradlew.bat) or a local Gradle install.



```kotlin### 1) Using IntelliJ IDEA

@EventHandler- Open the project

fun onPriceUpdate(e: PriceUpdateEvent) {- Open the Gradle tool window

    // Inspect, modify, or cancel price changes- Run the `wrapper` task (if needed), then `build`

}- Optionally run `runServer` to launch a local Paper test server



@EventHandler### 2) Using a local Gradle install (Windows PowerShell)

fun onPreBuy(e: PreBuyEvent) {```powershell

    // Adjust price or amount before purchase# Generate wrapper scripts (creates gradlew.bat and wrapper jar)

}gradle wrapper

```

# Build the plugin

See [DEVELOPERS.md](docs/DEVELOPERS.md) for full API reference.gradle clean build

```

## RequirementsThen prefer the wrapper:

```powershell

- **Server:** Paper/Spigot 1.20.1 - 1.21.x./gradlew.bat clean build

- **Java:** 17+```

- **Economy:** Vault (soft dependency)

- **Placeholders:** PlaceholderAPI (soft dependency, optional)## License

See individual dependency licenses. This plugin is provided as-is.

## Support# The Endex (Minecraft Market Plugin)



- **Discord:** [Join Server](https://discord.gg/7edgWnfCbY)Foundation build for a dynamic item market.

- **GitHub Issues:** [Report Bug](https://github.com/khristianaarongarcia/endex/issues)

- **Documentation:** [Online Docs](https://lokixcz-plugins.kagsystems.tech/)- Server: Paper 1.20.1+ (should run on newer Paper versions)

- Language: Kotlin (JDK 17)

## License- Core features:

  - market.yml persistence (auto-created)

This project is provided as-is for your server. External dependencies (Paper API, Vault API, sqlite-jdbc) retain their own licenses.  - periodic price updates based on demand/supply

  - config-driven intervals and sensitivity

---  - simple backup to market_backup.yml

  - `/market` command with `price` subcommand (basic)

<div align="center">

## Build

**⚠️ Disclaimer:** This plugin was created with the help of AI. While thoroughly tested, please expect potential bugs. Report issues on Discord or GitHub.

This project uses the Gradle Wrapper. If the wrapper scripts are missing or not executable, you can run Gradle from your IDE or install Gradle locally.

</div>

On Windows PowerShell, from the project root:

```powershell
# If gradlew.bat exists
./gradlew.bat clean build

# If wrapper is missing, use your IDE's Gradle tool window to run 'build'
```

Artifacts will be in `build/libs`. Use the `-all`/shadow jar if present.

## Run quickly (dev)

A dev server task is configured. If the wrapper is available:

```powershell
./gradlew.bat runServer
```

This downloads a Paper server and starts it with your plugin.

## Install to your server

- Copy the built jar from `build/libs` into your server's `plugins/` folder.
- Start the server. The plugin creates `plugins/TheEndex/` data with `market.yml`.

## Config

`config.yml` options:

- `update-interval-seconds`: how often price updates run
- `price-sensitivity`: multiplier in the price formula
- `history-length`: history entries per item
- `autosave-minutes`: how often to write a backup file
- `seed-items`: list of `Material` names to start tracking if no market.yml exists

## Commands & Permissions

- `/market price <MATERIAL>` — show current price (foundation)
- Future: `/market buy|sell|top` (stubbed)

Permissions:
- `theendex.market` (default: true)
- `theendex.buy` (default: true)
- `theendex.sell` (default: true)
- `theendex.admin` (default: op)

## Roadmap

See `the_endex_roadmap.md`. Phase 1 (foundation) implemented.
