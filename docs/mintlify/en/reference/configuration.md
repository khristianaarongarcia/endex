---
title: "Configuration"
description: "Key settings in config.yml and common tuning tips."
---

The main configuration file is:

```text
plugins/TheEndex/config.yml
```

After changes, run:

```text
/endex reload
```

## Core pricing

```yaml
update-interval-seconds: 60
price-sensitivity: 0.05
history-length: 5
autosave-minutes: 5
save-on-each-update: true
```

## Storage mode

```yaml
storage:
  sqlite: false
```

When enabling SQLite, restart the server so the plugin can migrate data if needed.

## Update Checker

```yaml
update-checker:
  enabled: true      # Check for updates on startup
  notify-ops: true   # Notify OP players on join
```

## Investments

```yaml
investments:
  enabled: true
  apr-percent: 5.0
```

## Events

```yaml
events:
  multiplier-cap: 10.0
  stacking:
    mode: multiplicative
    default-weight: 1.0
    max-active: 0
```

See: [Events](../features/events)

## Inventory-aware pricing (optional)

```yaml
price-inventory:
  enabled: false
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

## Web settings (high-level)

```yaml
web:
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
  api:
    tokens: []
    token-hashes: []
```

## Custom web UI

To serve your own dashboard bundle:

```yaml
web:
  custom:
    enabled: false
    root: webui
    reload: false
    export-default: true
```

## Inflation System

Control gradual price changes over time:

```yaml
inflation:
  enabled: false
  base-rate: 0.001          # 0.1% per cycle (positive = inflation, negative = deflation)
  variance: 0.0005          # Random variance ±0.05%
  apply-to-base: false      # Permanently adjust base prices
  category-rates:           # Per-category overrides
    ORES: 0.002             # Ore prices inflate faster
    FOOD: -0.0005           # Food prices deflate slightly
    BUILDING: 0.0           # Building materials stable
```

<Info>
Inflation is applied during each price update cycle. Set `apply-to-base: true` to make changes permanent, or leave it `false` for temporary price drift that resets on restart.
</Info>

## GUI Customization

GUI layouts are configured in separate files under `guis/`:

- `guis/market.yml` — Main market interface
- `guis/details.yml` — Item details panel
- `guis/holdings.yml` — Virtual holdings panel
- `guis/deliveries.yml` — Delivery queue panel

Each file supports:
- Custom titles with color codes
- Inventory size (rows × 9)
- Slot positions for buttons and items
- Category definitions with custom materials

## Command Aliases

Custom command aliases are configured in `commands.yml`:

```yaml
aliases:
  shop: "market"           # /shop → /market
  stock: "market holdings" # /stock → /market holdings
  prices: "market top"     # /prices → /market top
```

For the full deep-dive config guide, see `docs/CONFIG.md` in the repo.
