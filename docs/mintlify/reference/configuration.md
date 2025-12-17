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

See: [Events](../features/events.md)

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

For the full deep-dive config guide, see `docs/CONFIG.md` in the repo.
