# The Endex — Configuration Guide

Location: plugins/TheEndex/config.yml

After editing, run `/endex reload` or restart the server.

## Top-level keys

- config-version: 1
  - Used to detect breaking config changes between versions. If it doesn’t match what the plugin expects, a warning is logged. You can back up and let the plugin regenerate a fresh config.

- update-interval-seconds: 60
  - How often dynamic prices update.

- price-sensitivity: 0.05
  - Tuning factor in: new = old * (1 + (demand - supply) * sensitivity)

- history-length: 5
  - How many price points to retain per item.

- autosave-minutes: 5
  - Interval for periodic backups.

- save-on-each-update: true
  - If using YAML storage, write market.yml after each update. Set false to reduce IO.

### storage
- sqlite: false
  - When true, items and history are stored in SQLite (`plugins/TheEndex/market.db`). On first run with SQLite, existing YAML is migrated into the database.

### seed-all-materials
- false by default. If true, on first run seeds the market with all Bukkit Materials except AIR/LEGACY, respecting the blacklist.

### include-default-important-items
- true by default. Adds a curated, reasonable set of items on first run (ignored if seed-all-materials is true).

### transaction-tax-percent
- Percentage tax applied to each buy/sell.

### blacklist-items
- Item names (Bukkit Material) that must never be included in the market.

### gui
- details-chart: true
  - Show an ASCII history sparkline in the item details view.

### investments
- enabled: true
- apr-percent: 5.0
  - Annual percentage rate for passive investment certificates. Use `/market invest [buy|list|redeem-all]`.

### history-export
- enabled: true
- folder: history
  - CSV history files per item written on backup under `plugins/TheEndex/history`.

### events
- multiplier-cap: 10.0
  - Cap on stacked event multipliers (effective price will not exceed this value due to events).

For full event configuration, stacking modes, and admin commands, see `docs/EVENTS.md`.

### price-inventory
- enabled: false
  - When true, the price update formula also considers online players' inventories.
- sensitivity: 0.02
  - Scales the pressure from inventory surplus/deficit vs the baseline.
- per-player-baseline: 64
  - Average per online player amount considered "neutral"; above pushes down, below nudges up.
- max-impact-percent: 10.0
  - Caps the per-cycle percent change attributable to inventory pressure.

### web.roles
- default: TRADER
  - Default web role for authenticated sessions.
- trader-permission: endex.web.trade
  - Bukkit permission required for trading via web UI.
- admin-view-permission: endex.web.admin
  - Bukkit permission required to view another player's holdings via `/api/holdings/{uuid}`.

### web.holdings.inventory
- enabled: true
  - Enables live inventory snapshots for the web Combined Holdings view and optional `/api/inventory-totals`.
- include-enderchest: false
  - When true, scans online players' ender chests in addition to their main inventories.
- cache-seconds: 15
  - TTL for per-player inventory snapshots to keep scans lightweight.

### web.custom
- enabled: false
  - When true, the plugin will attempt to serve an external `index.html` from `<dataFolder>/<root>/` instead of the embedded UI.
- root: webui
  - Folder (relative to plugin data folder) containing `index.html` and any static assets (css/js/images).
- reload: false
  - If true, disables caching and re-reads `index.html` on every request (development mode). Set false in production.
- export-default: true
  - On startup, if `index.html` is missing and this is true, the embedded UI is exported as a starting point.

See `docs/CUSTOM_WEBUI.md` for detailed customization workflow, commands, and safety notes.

## Tips
- To switch to SQLite later, set `storage.sqlite: true` and restart. The plugin will migrate data from YAML if the DB is empty.
- Use `/market price <material>` to inspect live prices and current event multipliers.
- Use `/endex reload` after changing config, `events.yml`, or editing `market.yml`.

---

## Addon configuration (Crypto)

If you install the Crypto Addon, its configuration lives under:
- `plugins/TheEndex/addons/settings/crypto/config.yml`

Key settings (excerpt):
- `enabled`: true|false
- `name`, `symbol`, `decimals`
- `price.mode`: `fixed` or `market`
- `price.fixed`, `price.base`, `price.min`, `price.max`, `price.sensitivity`
- `fees.buyPercent`, `fees.sellPercent`, `fees.transferPercent`
- `limits.dailyBuyCap`, `limits.dailySellCap`, `limits.tradeCooldownSeconds`
- `aliases`: list of alias commands (e.g., `cc`, `crypto`)

Shop GUI (YAML) for the addon:
- `plugins/TheEndex/addons/settings/crypto/shop.yml`
- Define inventory `title`, `size`, and `items` with per-item `slot`, `material`, `name`, `lore`, `price`, `commands`, `run-as`, and optional `permission`.
