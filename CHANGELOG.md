# Changelog

All notable changes to this project will be documented in this file.

The format is inspired by Keep a Changelog and follows Semantic Versioning (MAJOR.MINOR.PATCH) where possible.

## [1.5.0] - 2024-12-17
### Added
- **Virtual Holdings System:** Complete redesign of the holdings architecture. Items purchased now go directly into virtual holdings instead of the player's inventory.
  - Players can hold up to 10,000 total items (configurable via `holdings.max-per-player`).
  - Maximum 100 different material types per player (configurable via `holdings.max-materials-per-player`).
  - Average cost basis tracking for accurate P/L calculations.
  - FIFO (First-In-First-Out) withdrawal system.

- **Holdings GUI Panel:** New in-game GUI for managing virtual holdings.
  - View all holdings with quantity, average cost, current price, and P/L per material.
  - Left-click: Withdraw all of a material (up to inventory capacity).
  - Right-click: Withdraw one stack (64 or material's max stack size).
  - "Withdraw All" button to claim everything that fits.
  - Portfolio stats showing total items, value, cost basis, and overall P/L.

- **Holdings Commands:**
  - `/market holdings` - View your virtual holdings in chat.
  - `/market withdraw <item> [amount]` - Withdraw items from holdings to inventory.
  - `/market withdraw all` - Withdraw all holdings to inventory.

- **Web UI Withdraw Support:**
  - Individual withdraw buttons (📤) for each holding row.
  - "📦 Withdraw All" button at top of holdings panel.
  - Real-time feedback with success/warning notifications.
  - Holdings badge now shows "Hold" instead of "Invest" for virtual holdings.

- **REST API Endpoints:**
  - `POST /api/holdings/withdraw` - Withdraw items (body: `{material, amount}` or `{material: "ALL"}`).
  - `GET /api/holdings/stats` - Get holdings statistics and limits.

- **New Permissions:**
  - `theendex.holdings` - Access to holdings system (default: true).
  - `theendex.withdraw` - Permission to withdraw from holdings (default: true).

- **Configuration Options:**
  ```yaml
  holdings:
    enabled: true
    max-per-player: 10000
    max-materials-per-player: 100
    mode: VIRTUAL  # VIRTUAL or LEGACY
  ```

### Changed
- **Minecraft 1.21 Support:** Updated `api-version` to `'1.21'` for full compatibility with Minecraft 1.21.x.
- **Buy Flow Redesign:** Purchases now add items to virtual holdings instead of directly to inventory. Players must manually withdraw items when ready.
- **Holdings Panel Rename:** GUI button changed from "Pending Deliveries" to "My Holdings".
- **Combined Holdings API:** `/api/holdings/combined` now returns virtual holdings data with proper badges.

### Technical
- New database methods: `addToHoldings()`, `removeFromHoldings()`, `getHoldingsTotal()`, `listHoldings()`, `getHolding()`.
- `migrateDeliveriesToHoldings()` helper for upgrading from delivery system.
- Inventory capacity calculation shared between GUI, commands, and web tier.
- CSS styling for withdraw buttons with hover effects and visual feedback.

### Migration Notes
- **Breaking Change:** The buy flow now uses virtual holdings by default. Set `holdings.mode: LEGACY` to restore previous behavior.
- **Database:** Holdings data stored in existing SQLite database; no new database files created.
- **Delivery System:** Existing pending deliveries remain accessible. Consider using `migrateDeliveriesToHoldings()` to convert.

## [1.4.0] - 2025-10-30
### Added
- **Virtual Delivery System:** When purchasing items that exceed inventory capacity, overflow items are automatically sent to a pending delivery queue instead of being lost.
  - SQLite-based delivery storage (`deliveries.db`) with transaction safety to prevent race conditions and duplication exploits.
  - FIFO (First-In-First-Out) claiming with per-material tracking.
  - Automatic database-first claiming approach prevents item duplication on logout/crash.
  - Configurable settings: `delivery.enabled`, `delivery.auto-claim-on-login`, `delivery.max-pending-per-player`.

- **Market GUI Integration:** Access deliveries directly from the market interface.
  - Ender Chest button in slot 51 displays pending item count badge.
  - Full deliveries panel with material breakdown and claim buttons.
  - Left-click to claim all of a material, right-click to claim one stack.
  - "Claim All" button (slot 49) for bulk claiming with progress feedback.

- **Delivery Commands:** Console and command-block support for delivery system.
  - `/market delivery list` - View pending deliveries with formatted output.
  - `/market delivery claim <material> [amount]` - Claim specific items.
  - `/market delivery claim-all` - Claim all pending deliveries.
  - `/market delivery gui` - Open market GUI directly to deliveries panel.

- **Web API Endpoints:** Full delivery system integration for web dashboard.
  - `GET /api/deliveries` - List pending deliveries (returns `{enabled, deliveries, total}`).
  - `POST /api/deliveries/claim` - Claim deliveries (body: `{material?, amount?}` for specific or all).
  - Updated `/docs` endpoint with delivery API documentation.

- **Enhanced Configuration Documentation:** Comprehensive inline comments in `config.yml` explaining delivery system settings with pros/cons and recommendations (200+ lines of documentation).

### Fixed
- **Critical:** Fixed buy loop bug where purchasing 64 items would only deliver 1 on 1.20.1 servers. Buy logic now properly calculates overflow and sends all excess items to delivery in a single operation.

### Security
- **Race Condition Protection:** Atomic transactions in `addPending()` with `conn.autoCommit=false` prevent concurrent purchases from exceeding `max-pending-per-player` limit.
- **Duplication Prevention:** Database-first approach in `claimMaterial()` removes items from database before giving to player, preventing logout duplication exploits.
- **Transaction Safety:** All delivery database operations wrapped in explicit try-catch with rollback to prevent data corruption on errors.

### Technical
- `DeliveryManager` class (426 lines) handles all delivery operations with SQLite backend.
- Per-player delivery limits with atomic transaction checks.
- Inventory capacity calculation accounts for partial stacks and empty slots.
- Delivery system fully integrated into existing buy/sell flow in `MarketCommand` and `WebServer`.

### Upgrade Notes
- **Database:** New `deliveries.db` created automatically on first launch with delivery system enabled.
- **Config:** Three new keys added with defaults (see `config.yml` for full documentation):
  - `delivery.enabled: true` - Enable/disable delivery system.
  - `delivery.auto-claim-on-login: false` - Automatically attempt to claim deliveries on player join.
  - `delivery.max-pending-per-player: 100000` - Maximum items that can be pending per player.
- **No Breaking Changes:** Existing functionality unchanged. Delivery system activates only when inventory is full during purchase.
- **Migration:** Update from 1.3.1 is seamless. No data migration required.

### Acknowledgments
- Continued thanks to ChunkCraft server for detailed bug reporting and testing feedback throughout v1.4.0 development! 🙏

## [1.3.1] - 2025-10-30
### Fixed
- **Critical:** Purchases now check inventory capacity BEFORE deducting money. Previously, buying more items than inventory could hold (e.g., 5000 diamonds) would charge the full amount but only deliver what fit (~2304 for diamonds), with remainder dropped on the ground and potentially lost.
- Purchase is now automatically capped to available inventory space (accounting for partial stacks and empty slots).
- Players receive clear feedback when orders are capped: "[TheEndex] Purchase capped to X MATERIAL due to inventory space (requested: Y). Tip: Empty your inventory or use Ender Chest for larger orders."
- Applies to both `/market buy` command and web dashboard buy API.

### Technical
- Added `calculateInventoryCapacity()` helper in `MarketCommand` and `WebServer` to compute exact available space.
- Calculation accounts for existing partial stacks of the same material + empty slots × max stack size.

### Upgrade Notes
- No config changes required.
- Existing behavior improved: no more accidental item loss on oversized purchases.
- This is a **patch release** focusing on this critical bug fix identified by community feedback.

### Acknowledgments
- Thank you to the ChunkCraft server admin for detailed bug report and testing feedback! 🙏

## [1.3.0] - 2025-09-26
### Security / Hardening
- Removed all reflective private field access (replaced with explicit getters and helper methods) to improve transparency and reduce maintenance risk.
- Web session auth now migrates `?session=` token into `Authorization: Bearer <session>` header; SPA strips token from the URL (prevents accidental leakage via history/referrer).
- Added hashed API token support (`web.api.token-hashes`) using SHA-256 lowercase hex digests with constant-time comparison.
- Plain `web.api.tokens` still accepted for backward compatibility; one-time deprecation warning when hashes also configured.

### Added
- `web.api.token-hashes` config key (secure alternative to storing plaintext tokens).
- CHANGELOG.md (top-level) and updated SECURITY.md marking mitigated items.

### Changed
- Token validation logic centralized with constant-time equality; improved auth code clarity.

### Upgrade Notes
1. Existing session links continue to work. After first load, the UI removes the token from the URL automatically.
2. To migrate API tokens: compute SHA-256 of the token and place digest under `web.api.token-hashes`; then remove the clear token from `web.api.tokens`.
3. No breaking config changes; all keys retained. Migration is optional but recommended.

### Next (Planned, Not Included)
- Resource pack download hardening (size/time limits, traversal checks, HTTPS default).
- Rate limiter map cleanup task.
- Dependency upgrades (stable Kotlin, Jackson 2.17.x, latest Javalin 5.x).

## [1.2.0] - 2025-09-25
See `docs/changelogs.md` for full 1.2.0 release notes (custom Web UI override, unified market view, filter improvements).

## [1.1.0] - 2025-08-29
See `docs/changelogs.md` for full 1.1.0 release notes (resource pack icons, addons tab, HTTP API documentation).

## [1.0.0] - 2025-08-28
Initial release. See `docs/changelogs.md` for full feature list.

---

