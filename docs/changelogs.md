# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]
### Added
- Addon command router with dynamic addon subcommands, aliases, and tab completion integration under `/endex`.
- Resource tracking subsystem (block breaks, mob drops, fishing) with periodic persistence to `tracking.yml` and admin `/endex track dump`.
- API: `getTrackedTotals()` to fetch a snapshot of gathered resources.
- Crypto Addon: configurable via YAML, commands (`help, info, balance, buy, sell, transfer, shop, admin`).
- Crypto Addon Shop: YAML-driven (`shop.yml`) with per-item permissions and command actions (console/player).
- Crypto pricing modes: `fixed` or `market` with sensitivity and mean reversion; `/endex crypto info` shows details.
- Crypto price history logging to CSV once per minute (market mode) for external graphing.

### Changed
- Spigot/Modrinth descriptions updated to highlight addons, Crypto Addon, and resource tracking.
- Documentation expanded: Developers API, Addons guide, Commands, and Config guide.

### Compatibility
- Tested on Paper 1.20.1 API; expected to run on 1.21.x.

## [1.0.0] - 2025-08-28
Initial release

### Added
- Demand-driven dynamic pricing with clamping and rolling history
- Full-featured market GUI (pagination, sorting, category filters, text search, details view)
- Vault economy integration with configurable transaction tax
- Market events system (stackable multipliers with cap, broadcasts, persistence across restarts)
- Investments (buy, list, redeem-all)
- YAML storage (default) and optional SQLite store with automatic seeding from YAML
- Player preference persistence (amount, sort, category, search, last page)
- ASCII sparkline trends and CSV history export
- Config versioning and automated migration on startup/reload
- Admin commands, safe reload, item blacklist, and CSV export command
- /endex version command to report plugin version and storage mode

### Compatibility
- Server: Paper/Spigot 1.20.1 to latest (built against API 1.20.1)
- Java 17 runtime
- Economy: Vault (soft dependency)

### Notes
- On Paper, the SQLite driver is fetched automatically via `plugin.yml` libraries (smaller plugin JAR). On plain Spigot, use YAML storage by default or install the SQLite driver manually if enabling SQLite.
