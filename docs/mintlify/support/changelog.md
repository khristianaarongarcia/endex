---
title: "Changelog"
description: "Version history and release notes for The Endex."
---

Full release notes live in the repository's `CHANGELOG.md`.

<Tip>
Before upgrading, back up `plugins/TheEndex/` and skim the release notes for breaking changes.
</Tip>

## Where to look

- `CHANGELOG.md` (root of the repo) — full history
- GitHub Releases — packaged builds and notes

---

## Version 1.5.4 — December 19, 2025

### bStats Metrics Integration

Plugin analytics for tracking usage patterns:

- **Plugin ID:** 28421
- **Custom Charts:**
  - Storage mode (yaml/sqlite)
  - Shop mode (DEFAULT/CUSTOM)
  - Web UI status (enabled/disabled)
  - Holdings status (enabled/disabled)
  - Tracked items count (1-10, 11-25, 26-50, 51-100, 100+)

### Custom Shop GUI Enhancements

- **Holdings Button** — View virtual holdings directly from category pages (slot 45)
- **Sort Button** — Cycle through sorting options (Name, Price, Change) in category pages (slot 53)
- **Filter-Based Auto-Population** — Categories now use `filter` property to automatically populate items matching criteria

### Default Market GUI Improvements

- Removed Amount button for cleaner interface
- Sort button moved to slot 49 for better layout
- Sort order fixed: Price and Change now sort descending (highest first)

### Bug Fixes

- **Hotbar Clicks** — Fixed issue where players couldn't interact with their hotbar while GUI was open
- **Sort Order** — Price and Change sorting now correctly shows highest values first
- **Holdings Button** — Holdings icon in Custom Shop GUI now properly opens the holdings panel
- **`/ex market` Command** — Now correctly opens DEFAULT or CUSTOM shop based on `shop.mode` config

### Technical

- bStats dependency with Shadow relocation (`org.bstats` → `org.lokixcz.theendex.bstats`)
- `MarketGUI.openHoldings()` made public for cross-GUI access

---

## Version 1.6.0 — December 2025

### Custom Shop System

EconomyShopGUI-style category-based shop interface:

- **Category Navigation** — Main menu with category icons → Category pages with items
- **Custom Layouts** — Full control over slot positions, decorations, and borders
- **Market Integration** — Items use dynamic market prices with buy/sell spread
- **Fixed Prices** — Override specific items with static prices
- **Multiple Shops** — Create different shops for different purposes (main, VIP, seasonal)
- **Pagination** — Automatic page navigation for large categories
- **Admin Editing** — Shift+click items to edit (with permission)
- **Sound Effects** — Customizable sounds for all interactions
- **Permission Items** — Restrict items to specific player groups

#### Enabling Custom Shop Mode

```yaml
shop:
  mode: CUSTOM        # DEFAULT or CUSTOM
  main-shop: main     # Shop config to use
  command: shop       # Custom command alias
```

#### Shop Configuration

Shops are defined in `plugins/TheEndex/shops/` as YAML files with categories and items.

See the [Custom Shop Guide](../features/custom-shop) for full documentation.

#### New Commands

| Command | Description |
|---------|-------------|
| `/market shop [id]` | Open a specific custom shop |
| `/market stock` | Open default market (bypasses mode) |

---

## Version 1.5.3 — December 18, 2025

### PlaceholderAPI Integration

Full support for PlaceholderAPI with 30+ placeholders:

- **Market Data:** `%endex_price_<MATERIAL>%`, `%endex_trend_<MATERIAL>%`, `%endex_change_<MATERIAL>%`
- **Top Items:** `%endex_top_price_<1-10>%`, `%endex_bottom_price_<1-10>%`
- **Gainers/Losers:** `%endex_top_gainer_<1-10>%`, `%endex_top_loser_<1-10>%`
- **Holdings:** `%endex_holdings_total%`, `%endex_holdings_count%`
- **Leaderboard:** `%endex_top_holdings_<1-10>%`
- **Statistics:** `%endex_total_items%`, `%endex_total_volume%`, `%endex_active_events%`

See the full [PlaceholderAPI Reference](../features/placeholderapi) for all placeholders.

### Update Checker

Automatic update notifications:

- Checks Spigot and Modrinth APIs on startup
- Console banner when updates available
- OP players notified on join

```yaml
update-checker:
  enabled: true
  notify-ops: true
```

### GUI Customization

Full GUI layout customization via config files in `guis/` folder:

- `market.yml` — Main market interface
- `details.yml` — Item details panel
- `holdings.yml` — Virtual holdings panel
- `deliveries.yml` — Delivery queue panel

Customize titles, sizes, slot positions, categories, and more.

### Command Aliases

Create custom command shortcuts via `commands.yml`:

```yaml
aliases:
  shop: "market"
  stock: "market holdings"
  prices: "market top"
```

---

## Version 1.5.2 — December 17, 2025

### Optimized World Scanner

Complete rewrite with intelligent chunk caching:

- Chunk-level caching with configurable expiry
- Event-driven dirty tracking
- Disk persistence across restarts
- 80-90% reduction in redundant scanning

### GUI Fix (MC 1.21+)

Fixed critical bug where market items could be taken and clicks weren't working on Minecraft 1.21+ servers.

- UUID-based GUI state tracking
- Replaces unreliable title matching

---

## Version 1.5.1 — December 17, 2025

### World Storage Scanner

Prices now react to ALL items stored on your server:

- Scans chests, barrels, shulker boxes, and more
- Global item tracking for true server-wide economics
- Anti-manipulation protection with per-chunk caps
- TPS-aware throttling

---

## Version 1.5.0 — December 17, 2025

### Virtual Holdings System

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

- [Troubleshooting](troubleshooting) — Common issues
- [Configuration](../reference/configuration) — Config reference
- [Installation](../getting-started/installation) — Setup guide
