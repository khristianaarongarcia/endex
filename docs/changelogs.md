# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]
### Added
- Inventory-aware pricing (optional): price update formula now supports gentle pressure from online players' inventories with per-player baseline, sensitivity, and max per-cycle impact caps. Config: `price-inventory.*`.
- Inventory Snapshot Service: online-only scans with TTL cache; aggregates per-material totals; optional ender chest inclusion.
- Web Combined Holdings: new `/api/holdings/combined` endpoint and UI badges to show Invest (database) and Inv (live inventory) quantities together.
- Admin holdings view: `/api/holdings/{uuid}` (requires `web.roles.admin-view-permission`).
- Inventory totals API: `/api/inventory-totals` exposes online per-material totals when enabled.
- `GET /api/session` now reports `invHoldingsEnabled` so the web UI can switch to combined mode.
- Config: `web.roles.*` (trader/admin) and `web.holdings.inventory.*` (enable, include-enderchest, cache-seconds).

### Changed
- Pricing model blends trade demand/supply delta with inventory pressure; retains EMA smoothing and per-item clamps.
- Web holdings view renders combined quantities with [Inv]/[Invest] badges and clarifies that PnL is based on invested quantity.
- Documentation updated: Spigot and Modrinth descriptions aligned; config and API docs expanded.

### Fixed
- Javalin path parameter syntax updated to `{uuid}` (was `:uuid`) in web routes.
- Kotlin nullability around permission strings in web layer.

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

## [1.1.0] - 2025-08-29
### Added
- Web UI item icons served from resource packs with `/icon/{material}` endpoint (public, ETag caching)
- Resource pack extraction/usage via `web.icons.*` with support for both `item/items` and `block/blocks` folders
- Aliases for tricky materials (e.g., PUMPKIN, CACTUS) to reduce 404s across pack variations
- Addons tab in the UI and `/api/addons` endpoint (auth required)
- Developer HTTP API documentation (`docs/API.md`) and reverse proxy guide (`docs/REVERSE_PROXY.md`)

### Changed
- Improved client error fallback for missing icons (initials badge)
- More informative server logs (actual paths tried for missing icons when `logging.verbose=true`)

### Fixed
- Kotlin string interpolation in logs and headers (no longer prints `$vars` literally)
- Javalin route syntax for path params (now uses `/icon/{material}`)

### Compatibility
- Built against Paper API 1.20.1; expected to work on newer Paper versions

### Highlights
- HTTP API reference: see `docs/API.md`
- Reverse proxy examples (HTTPS): `docs/REVERSE_PROXY.md`
- Config keys for icons and addons: `docs/CONFIG.md`
