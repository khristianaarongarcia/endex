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

## Version 1.5.7-dec1038 — December 31, 2025

### Polish Language Support

Complete Polish (Polski) translation:

- **Plugin Language File** — New `lang/pl.yml` with full Polish translations
- **Config Translation** — New `config_translations/config_pl.yml` with Polish comments
- **Documentation** — 18 fully translated pages in `docs/mintlify/pl/`
- **Navigation** — Polish language option in documentation site

### Config Translations Auto-Extraction

Translated config files now auto-extract on first plugin run:

- **10 Languages Available** — English, Chinese, Spanish, French, German, Japanese, Korean, Portuguese, Russian, Polish
- **Location** — Extracted to `plugins/TheEndex/config_translations/`
- **Usage** — Copy your preferred language config to `config.yml`

### Optimized Default Configuration

Config now ships with **minimal resource usage** settings by default:

- **World Storage Scanning** — DISABLED (biggest CPU saver)
- **Inventory-Based Pricing** — DISABLED  
- **Save-on-Each-Update** — DISABLED (reduced disk I/O)
- **Update Interval** — Increased to 120 seconds (was 60)
- **Optimization Guide** — Prominent header in config.yml explaining all settings

<Info>
To enable full features (dynamic scarcity pricing, inventory influence), see the config.yml header for guidance on which settings to change.
</Info>

---

## Version 1.5.7-dec1022 — December 30, 2025

### Web Dashboard Translation

Google Translate integration for the web dashboard:

- **26+ Languages** — English, Chinese, Spanish, French, German, Japanese, Korean, Portuguese, Russian, Italian, Thai, Vietnamese, Indonesian, Turkish, Polish, Dutch, Swedish, Danish, Finnish, Czech, Romanian, Ukrainian, Hindi, Bengali, Tagalog, Arabic
- **Language Selector** — Dropdown in header between player name and theme toggle
- **Dark Theme Styled** — Translation widget matches dashboard theme

### Config Performance Indicators

Added `[PERF: LOW/MEDIUM/HIGH]` tags to config.yml options:

- Helps server owners understand performance impact of each feature
- See immediately which options are expensive

### Translated Config Files

Pre-translated config files in 9 languages available in `resources/translations/`:

- Chinese (Simplified), Spanish, French, German, Japanese, Korean, Portuguese, Russian, Arabic

### Bug Fixes

- **GUI ArrayIndexOutOfBoundsException** — Fixed "Index 33 out of bounds for length 27" error in market details panel
- Inventory size increased from 27 to 36 slots to accommodate all buttons

---

## Version 1.5.7 — December 29, 2025

### Sell from Holdings

Players can sell items directly from virtual holdings:

- **Market GUI** — "Sell 1 Holdings" and "Sell All Holdings" buttons in details panel
- **Custom Shop** — Vanilla items can be sold from holdings when inventory is empty
- **Web API** — New `POST /api/sell-holdings` endpoint
- **Command** — New `/market sellholdings <material> <amount>`

### Arclight/Hybrid Server Compatibility

Full support for Arclight, Mohist, and other hybrid servers:

- Fixed plugin recognition issues
- Added SLF4J simple logger
- Merged service files in Spigot JAR
- Fixed clickable chat links
- Fixed PlaceholderAPI expansion registration

---

## Version 1.5.6-DEC0759 — December 28, 2025

### 1.20.1 Compatibility

- Lowered `api-version` from `'1.21'` to `'1.20'` for 1.20.x server support

---

## Version 1.5.6-DEC0338 — December 22, 2025

### Data Folder Fix

- Reverted plugin folder back to `plugins/TheEndex/` (was accidentally `plugins/Endex/`)

---

## Version 1.5.6-DEC0252 — December 22, 2025

### Command & GUI Fixes

- **`/shop` Command** — Fixed command registration
- **GUI Conflict** — The Endex no longer intercepts other plugins' GUI clicks
- **Command Aliases** — Added `/shop`, `/m`, `/trade`, `/exchange`, `/bazaar`

---

## Version 1.5.6 — December 21, 2025

### In-Game Layout Editor

Complete visual GUI editor for custom shop layouts:

- **5 Editor Modes** — Place Category, Place Decoration, Place Button, Edit Slot, Remove Slot
- **Custom Items** — Add any material as decoration with custom names/lores
- **Live Preview** — See changes in real-time
- **Right-Click Edit** — Customize via chat input
- **One-Click Save** — Writes directly to shop config

#### Opening the Editor

```
/endex shop editor <shop-id>
```

### Custom Category Icons

Categories support fully customized display names and lores.

---

## Version 1.5.5 — December 20, 2025

### Economy Plugin Compatibility

Fixed "Economy unavailable" error:

- **Delayed Retry** — 2-second retry for late-loading economy plugins
- **Expanded Dependencies** — Common economy plugins added to `softdepend`

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
