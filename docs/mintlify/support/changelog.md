---
title: "Changelog"
description: "Version history and release notes for The Endex."
---

# Changelog

Full release notes live in the repository's `CHANGELOG.md`.

<Tip>
Before upgrading, back up `plugins/TheEndex/` and skim the release notes for breaking changes.
</Tip>

## Where to look

- `CHANGELOG.md` (root of the repo) — full history
- GitHub Releases — packaged builds and notes

#### Virtual Holdings System

Complete redesign of the holdings architecture:

- **Direct to Holdings** — Purchases go to virtual holdings, not inventory
- **Large Capacity** — Hold up to 10,000 items per player
- **100 Materials** — Track up to 100 different item types
- **Cost Tracking** — Average cost basis for accurate P/L
- **FIFO Withdrawals** — First-In-First-Out system

#### Holdings GUI Panel

New in-game interface:

- View all holdings with quantity, cost, and P/L
- Left-click: Withdraw all of a material
- Right-click: Withdraw one stack
- "Withdraw All" button
- Portfolio stats display

#### Holdings Commands

```
/market holdings          # View holdings
/market withdraw <item>   # Withdraw specific item
/market withdraw all      # Withdraw everything
```

#### Web UI Support

- Individual withdraw buttons (📤)
- "Withdraw All" button
- Real-time feedback
- Updated badges

#### New Permissions

- `theendex.holdings` — Access holdings
- `theendex.withdraw` — Withdraw from holdings

### Changes

- **Minecraft 1.21 Support** — Updated API version
- **Buy Flow Redesign** — Purchases to holdings first
- **GUI Rename** — "Pending Deliveries" → "My Holdings"

### Migration Notes

<Warning>
**Breaking Change:** Buy flow uses virtual holdings by default.
Set `holdings.mode: LEGACY` to restore previous behavior.
</Warning>

---

## Version 1.4.0 — October 30, 2025

### Virtual Delivery System

When inventory is full, purchases go to delivery queue:

- SQLite-based storage
- FIFO claiming
- Transaction safety
- Duplication prevention

### GUI Integration

- Ender Chest button shows pending count
- Full deliveries panel
- Claim buttons (all/stack)
- Bulk claiming

### Delivery Commands

```
/market delivery list
/market delivery claim <material> [amount]
/market delivery claim-all
/market delivery gui
```

### API Endpoints

- `GET /api/deliveries`
- `POST /api/deliveries/claim`

### Bug Fixes

- **Critical:** Fixed buy loop bug (64 items → 1 delivered)

### Security

- Race condition protection
- Duplication prevention
- Transaction safety

---

## Version 1.3.1 — October 30, 2025

### Critical Fix

- **Inventory check before payment** — Previously, buying excess items would charge full amount but only deliver what fit
- Purchase now capped to available space
- Clear feedback when orders are capped

---

## Version 1.3.0 — September 26, 2025

### Security Improvements

- Removed reflective private field access
- Session token migrates to Authorization header
- URL token stripped automatically
- Hashed API token support (`token-hashes`)
- Constant-time comparison

### Configuration

New `web.api.token-hashes` for secure token storage.

---

## Version 1.2.0 — September 25, 2025

### Features

- Custom Web UI override
- Unified market view
- Filter improvements
- Enhanced configuration

---

## Version 1.1.0 — August 29, 2025

### Features

- Resource pack icons
- Addons tab
- HTTP API documentation
- Performance improvements

---

## Version 1.0.0 — August 28, 2025

### Initial Release

- Dynamic pricing engine
- Market GUI
- Web dashboard
- REST API
- Event system
- Investment tracking
- Addon support

---

## Upgrade Guide

### From 1.4.x to 1.5.x

1. Backup your `config.yml` and database
2. Replace plugin JAR
3. Restart server
4. Review new `holdings` config section
5. Consider migrating deliveries to holdings

### From 1.3.x to 1.4.x

1. Replace plugin JAR
2. Restart server
3. New `deliveries.db` created automatically
4. Configure delivery settings in `config.yml`

### General Upgrades

1. Always backup before upgrading
2. Read release notes for breaking changes
3. Check config for new options
4. Test on staging server first

---

## Related Pages

- [Troubleshooting](troubleshooting.md) — Common issues
- [Configuration](../reference/configuration.md) — Config reference
- [Installation](../getting-started/installation.md) — Setup guide
