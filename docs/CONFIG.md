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

## Tips
- To switch to SQLite later, set `storage.sqlite: true` and restart. The plugin will migrate data from YAML if the DB is empty.
- Use `/market price <material>` to inspect live prices and current event multipliers.
- Use `/endex reload` after changing config, `events.yml`, or editing `market.yml`.
