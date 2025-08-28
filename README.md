# The Endex

Dynamic, demand-driven market for Minecraft (Paper). Supports 1.20.1+ with Vault economy. Browse, buy, sell, invest, and run time-limited market events.

## Features
- Dynamic prices: respond to player buy/sell demand with configurable sensitivity
- GUI market with search, categories, sorting, and item details (ASCII history sparkline)
- Rich commands: buy/sell/price/top, admin reload, events management
- Events system: item/category multipliers with stack cap and expiry
- Storage: YAML by default or SQLite with auto-migration from existing YAML
- Investments: passive APR-based investment certificates
- Backups and CSV history export for analysis

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
- See `docs/CONFIG.md` for all options (update interval, sensitivity, storage = sqlite, events, investments, history export, blacklist, etc.)

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
- Event admin: `/market event [list|<name>|end <name>|clear]`

## Storage: YAML vs SQLite
- Default: YAML files in `plugins/TheEndex`.
- To enable SQLite, set `storage.sqlite: true` and restart. On first run with SQLite, existing YAML items/history are migrated into `plugins/TheEndex/market.db`.

## Development
- Kotlin, Paper API 1.20.1 target, Java 17 toolchain
- Shadow jar is produced (no classifier) and copied to `MCTestServer/plugins`
- Run a local test server via the run task: `./gradlew.bat runServer`

## License
This project is provided as-is for your server. External dependencies (Paper API, Vault API, sqlite-jdbc) retain their own licenses.
# The Endex (Minecraft Market Plugin)

Foundation-stage Paper plugin (Kotlin) targeting 1.20.1+ servers.

## Build options

You need either the Gradle Wrapper (gradlew/gradlew.bat) or a local Gradle install.

### 1) Using IntelliJ IDEA (no system Gradle required)
- Open the project in IntelliJ IDEA.
- Open the Gradle tool window.
- Run the `wrapper` task (Gradle group: build setup). This generates `gradlew`, `gradlew.bat`, and `gradle/wrapper/*`.
- Then run:
  - `build` task to produce the jar at `build/libs`.
  - Optionally `runServer` to start a Paper test server (downloads server jar automatically).

### 2) Using a local Gradle install (Windows PowerShell)
If you have Gradle installed:
```powershell
# Generate wrapper scripts (creates gradlew.bat and wrapper jar)
gradle wrapper

# Build the plugin
gradle clean build
```
After generating the wrapper once, you can use:
```powershell
# Using the wrapper
./gradlew.bat clean build
```

If you don't have Gradle installed, you can install it with winget:
```powershell
winget install Gradle.Gradle
```

## Install to a server
- Copy the built jar from `build/libs` into your Paper server's `plugins/` folder.
- Start the server; the plugin will create `plugins/TheEndex/` with `config.yml` and `market.yml`.

## Configuration
See `src/main/resources/config.yml` for:
- `update-interval-seconds`, `price-sensitivity`, `history-length`, `autosave-minutes`.
- `seed-items` list for initial tracked materials.
- `include-default-important-items`: when true, adds a curated set of common commodities on first run.
- `blacklist-items`: materials that must never be tracked (overrides seeds/curated).
- `transaction-tax-percent`: percent fee applied to buy/sell.

## Commands
- `/market` – shows usage
- `/market price <MATERIAL>` – shows current price (Phase 1)
- Other subcommands (buy/sell/top) are placeholders until Phase 2.

## Notes
If `gradlew.bat` is missing, it means the wrapper scripts and jar were not generated or committed. Run the `wrapper` task once (see above) to generate them.
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
