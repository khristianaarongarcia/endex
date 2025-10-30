## Public API (for developers)

The Endex exposes a lightweight Bukkit API and an HTTP API.

### Bukkit API example

```kotlin
val api = server.servicesManager.load(org.lokixcz.theendex.api.EndexAPI::class.java)
if (api != null) {
  val mats = api.listMaterials()
  val diamond = org.bukkit.Material.DIAMOND
  val cur = api.getCurrentPrice(diamond)
}
```

You can also listen to Bukkit events:

```kotlin
@EventHandler fun onPriceUpdate(e: org.lokixcz.theendex.api.events.PriceUpdateEvent) { /* inspect/modify/cancel */ }
@EventHandler fun onPreBuy(e: org.lokixcz.theendex.api.events.PreBuyEvent) { /* adjust unitPrice or amount */ }
@EventHandler fun onPreSell(e: org.lokixcz.theendex.api.events.PreSellEvent) { /* enforce rules */ }
```

### HTTP API

See `docs/API.md` for the full HTTP API: endpoints, parameters, auth, ETag, SSE/WS, and the `/icon/{material}` icon endpoint.

# The Endex

Dynamic, demand-driven market for Minecraft (Paper). Supports 1.20.1+ with Vault economy. Browse, buy, sell, invest, and run time-limited market events.

## Features
- Dynamic prices: respond to player buy/sell demand with configurable sensitivity
- GUI market with search, categories, sorting, and item details (ASCII history sparkline)
- Delivery system: overflow purchases queue safely with GUI + commands to claim pending items
- Rich commands: buy/sell/price/top, admin reload, events management
- Events system: item/category multipliers with stack cap and expiry
- Storage: YAML by default or SQLite with auto-migration from existing YAML
- Investments: passive APR-based investment certificates
- Backups and CSV history export for analysis
- Addon framework: drop-in jars with subcommands, aliases, and tab completion (see docs/ADDONS.md) — addon content now integrates directly (no separate Addons tab)
- Web UI item icons served from a resource pack (configurable via `web.icons.*`)
- Customizable Web UI override (`web.custom.*`) – export & edit `index.html` and assets
- Resource tracking: observe gathered resources server-wide; admin `/endex track dump`; API `getTrackedTotals()`
- Crypto Addon (optional): `/endex crypto ...`, YAML-driven `shop.yml`, per-item permissions, fixed/market pricing, price history CSV

## Quick start
1. Build the plugin
   - Windows PowerShell
     - `./gradlew.bat clean build -x test --no-daemon`
   - The shaded jar is placed in `MCTestServer/plugins` automatically. You can also copy it into your server `/plugins`.
2. Start your Paper server with Vault installed
3. Join and open the market: `/endex market` or `/market`

## Configuration
- Default config is generated at `plugins/TheEndex/config.yml`
- Edit safely and then run `/endex reload`
- See `docs/CONFIG.md` for all options (update interval, sensitivity, storage = sqlite, events, investments, history export, blacklist, web icons, addons, etc.)

Config versioning:
- This build expects `config-version: 1` at the top of `config.yml`. If it differs, a warning is logged with guidance to back up and regenerate from defaults.

## Commands
See `docs/COMMANDS.md` for full details.

Highlights:
- `/market buy <material> <amount>` and `/market sell <material> <amount>`
- `/market price <material>` with event multiplier insight and ASCII chart (if enabled)
- `/market top` for movers
- `/endex market` to open GUI
- `/endex reload` to reload config, events, and market data
- `/endex version` to show plugin version and storage mode
- `/endex track dump` to dump top gathered resources (if tracking enabled)
- Addon aliases like `/cc` can be registered by addons for convenience
- Event admin: `/market event [list|<name>|end <name>|clear]`

## Storage: YAML vs SQLite
- Default: YAML files in `plugins/TheEndex`.
- To enable SQLite, set `storage.sqlite: true` and restart. On first run with SQLite, existing YAML items/history are migrated into `plugins/TheEndex/market.db`.

## Development
- Kotlin, Paper API 1.20.1 target, Java 17 toolchain
- Shadow jar is produced (no classifier) and copied to `MCTestServer/plugins`
- Run a local test server via the run task: `./gradlew.bat runServer`
- Public API: see `docs/DEVELOPERS.md` (now includes `getTrackedTotals()`)

## License
This project is provided as-is for your server. External dependencies (Paper API, Vault API, sqlite-jdbc) retain their own licenses.
<!-- Top blurb -->
# The Endex

Dynamic, demand-driven market for Minecraft (Paper). Prices react to player behavior and can optionally account for online players' inventories. Includes a rich Market GUI, timed events, an optional web dashboard, and an addon framework.

## Highlights
- Dynamic pricing with sensitivity, smoothing, and per-item clamps
- Inventory-aware pricing (optional) using safe, online-only snapshots
- Market GUI with search, categories, sorting, details panel, and tiny sparkline
- Investments (buy, list, redeem-all) and time-limited events
- Web dashboard (optional) with real item icons and Combined Holdings (Invest + Inv)
- YAML or SQLite storage with migration; CSV history export
- Addon framework + optional Crypto Addon

## Quick start
1. Build
   - Windows PowerShell
     - `./gradlew.bat clean build -x test --no-daemon`
   - The shaded jar is produced and copied to `MCTestServer/plugins` and `release/` automatically.
2. Install
   - Copy the jar from `release/` (or `build/libs`) to your server `plugins/` folder.
3. Run and open the market
   - Start Paper (Java 17) with Vault installed
   - `/market` or `/endex market`

## Docs
- Commands: `docs/COMMANDS.md`
- Config: `docs/CONFIG.md`
- API: `docs/API.md`
- Events guide: `docs/EVENTS.md`
- Addons: `docs/ADDONS.md`, developers: `docs/DEVELOPERS.md`
- Reverse proxy (HTTPS): `docs/REVERSE_PROXY.md`
- Changelog: `docs/changelogs.md`

## Configuration quick bits
- Inventory-aware pricing

```yaml
price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

- Web combined holdings & roles

```yaml
web:
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
  holdings:
    inventory:
      enabled: true
      include-enderchest: false
      cache-seconds: 15
```

## Build options

You need either the Gradle Wrapper (gradlew/gradlew.bat) or a local Gradle install.

### 1) Using IntelliJ IDEA
- Open the project
- Open the Gradle tool window
- Run the `wrapper` task (if needed), then `build`
- Optionally run `runServer` to launch a local Paper test server

### 2) Using a local Gradle install (Windows PowerShell)
```powershell
# Generate wrapper scripts (creates gradlew.bat and wrapper jar)
gradle wrapper

# Build the plugin
gradle clean build
```
Then prefer the wrapper:
```powershell
./gradlew.bat clean build
```

## License
See individual dependency licenses. This plugin is provided as-is.
# The Endex (Minecraft Market Plugin)

Foundation build for a dynamic item market.

- Server: Paper 1.20.1+ (should run on newer Paper versions)
- Language: Kotlin (JDK 17)
- Core features:
  - market.yml persistence (auto-created)
  - periodic price updates based on demand/supply
  - config-driven intervals and sensitivity
  - simple backup to market_backup.yml
  - `/market` command with `price` subcommand (basic)

## Build

This project uses the Gradle Wrapper. If the wrapper scripts are missing or not executable, you can run Gradle from your IDE or install Gradle locally.

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
