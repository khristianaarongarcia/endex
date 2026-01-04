# Changelog

All notable changes to this project will be documented in this file.

The format is inspired by Keep a Changelog and follows Semantic Versioning (MAJOR.MINOR.PATCH) where possible.

## [Unreleased]
*No unreleased changes at this time.*

## [1.5.8] - 2026-01-04
### Added
- **Inflation System:** New configurable market inflation/deflation system that adjusts prices over time.
  - `inflation.enabled` - Toggle the inflation system on/off
  - `inflation.base-rate` - Base inflation rate per price update cycle (positive = inflation, negative = deflation)
  - `inflation.variance` - Random variance for realistic market simulation
  - `inflation.apply-to-base` - Option to permanently adjust base prices
  - `inflation.category-rates` - Per-category inflation rate overrides (e.g., ORES, FOOD, BUILDING)
- **Market Items Manager:** New "Market Items" button in `/market editor` showing all items from items.yml with pagination.
  - View all vanilla and custom market items in one place
  - Left-click to edit prices, Middle-click to toggle enabled/disabled, Shift+Right-click to remove
  - Full persistence to both items.yml and market.db

### Fixed
- **Market Item Price Editor:** Fixed editor not saving prices for vanilla market items.
  - Created separate `MARKET_ITEM_PRICE` state with proper slot layout (11, 13, 15 for base/min/max)
  - Added dedicated input handlers for market item prices
  - Now properly syncs to both `items.yml` and `market.db`
- **/market remove:** Now properly removes items from both `items.yml` AND `market.db` (previously only removed from items.yml)
- **/market disable:** Now properly syncs disabled state to market manager (no longer requires restart)
- **/market setbase/setmin/setmax:** Now properly persists price changes to SQLite database

### Technical
- Added `EditorState.MARKET_ITEM_PRICE` for vanilla market item editing
- Added `InputType.MARKET_BASE_PRICE`, `MARKET_MIN_PRICE`, `MARKET_MAX_PRICE`
- Added `currentMarketMaterial` to EditorSession for tracking edited material
- Added `handleMarketItemPriceClick()` and related input handlers
- Added `getInflationRateForMaterial()` helper in MarketManager
- Inflation delta integrated into existing price update cycle

## [1.5.7-dec1038] - 2025-12-31
### Added
- **Polish Language Support:** Complete Polish (Polski) translation for both plugin and documentation.
  - New `lang/pl.yml` language file with full Polish translations
  - New `config_translations/config_pl.yml` with Polish config comments
  - Complete Polish documentation in `docs/mintlify/pl/` (18 translated pages)
  - Documentation navigation updated with Polish language option
- **Config Translations Auto-Extraction:** Translated config files are now automatically extracted to `plugins/TheEndex/config_translations/` on first run
  - 10 language configs available: English, Chinese, Spanish, French, German, Japanese, Korean, Portuguese, Russian, Polish
  - Users can copy their preferred language config to `config.yml`

### Changed
- **Optimized Default Configuration:** Config now ships with minimal resource usage settings by default.
  - World storage scanning **DISABLED** by default (biggest CPU saver)
  - Inventory-based pricing **DISABLED** by default
  - Save-on-each-update **DISABLED** by default (reduced disk I/O)
  - Update interval increased to **120 seconds** (was 60)
  - Added prominent optimization guide at top of config.yml
  - All performance-intensive features clearly marked and documented

### Configuration
New default values (optimized for minimal resource usage):
```yaml
update-interval-seconds: 120        # Was 60
save-on-each-update: false          # Was true
price-inventory.enabled: false      # Was true
price-world-storage.enabled: false  # Was true
```

To enable full features, set these back to their previous values. See config.yml header for guidance.

## [1.5.7-dec1022] - 2025-12-30
### Added
- **Web Dashboard Translation:** Google Translate integration for 26+ languages in the web dashboard
  - Language selector dropdown in header (between player name and theme toggle)
  - Supports: English, Chinese, Spanish, French, German, Japanese, Korean, Portuguese, Russian, Italian, Thai, Vietnamese, Indonesian, Turkish, Polish, Dutch, Swedish, Danish, Finnish, Czech, Romanian, Ukrainian, Hindi, Bengali, Tagalog, Arabic
  - Styled to match dark theme with visible text
- **Config Performance Indicators:** Added `[PERF: LOW/MEDIUM/HIGH]` tags to config.yml options to help server owners understand performance impact
- **Translated Config Files:** Pre-translated config files in 9 languages available in `resources/translations/`
  - Chinese (Simplified), Spanish, French, German, Japanese, Korean, Portuguese, Russian, Arabic

### Fixed
- **Market GUI ArrayIndexOutOfBoundsException:** Fixed "Index 33 out of bounds for length 27" error when middle-clicking items in the market details panel
  - Root cause: `openDetails()` created a 27-slot inventory but placed buttons at slots 33 and 35
  - Solution: Increased inventory size from 27 to 36 slots to accommodate all buttons

### Changed
- **Web Dashboard:** Replaced broken AutoTranslate.js library (ES6 module incompatibility) with reliable Google Translate widget

### Technical
- MarketGUI `openDetails()` now uses 36-slot inventory (was 27)
- Holdings sell buttons remain at slots 33 (Sell 1) and 35 (Sell All)
- Google Translate widget initialization via `googleTranslateElementInit()` callback
- CSS styling for `.goog-te-combo` dropdown to match dark theme

## [1.5.7] - 2025-12-29
### Added
- **Sell from Holdings:** Players can now sell items directly from their virtual holdings without withdrawing to inventory first.
  - **Market GUI:** Item details panel now displays "Inventory: X | Holdings: Y" and includes new "Sell 1 Holdings" and "Sell All Holdings" buttons.
  - **Custom Shop GUI:** Vanilla items can now be sold from holdings when inventory is empty or insufficient.
  - **Web API:** New `POST /api/sell-holdings` endpoint for selling holdings via web dashboard.
  - **Command:** New `/market sellholdings <material> <amount>` command for direct holdings sales.

### Fixed
- **Arclight/Hybrid Server Compatibility:** Multiple fixes for running on Arclight, Mohist, and other hybrid servers:
  - Fixed plugin recognition issues with proper `plugin.yml` configuration
  - Added SLF4J simple logger to eliminate Javalin logging warnings
  - Merged service files in Spigot JAR for proper ServiceLoader compatibility
  - Fixed clickable chat links using proper Spigot/Adventure API detection
  - Resolved PlaceholderAPI expansion registration on hybrid servers

### Technical
- Holdings sell buttons use slots 33 (Sell 1) and 35 (Sell All) in details panel
- `performSellFromHoldings()` in WebServer returns `Triple<Boolean, String, Int>` (success, error, amountSold)
- Custom Shop `sellCustomItem()` now reports breakdown: "Nx from inventory, Mx from holdings"

## [1.5.6-DEC0759] - 2025-12-28
### Fixed
- **Minecraft 1.20.1 Compatibility:** Lowered `api-version` from `'1.21'` to `'1.20'` to restore support for 1.20.1+ servers
  - This was preventing the plugin from loading on Arclight and other 1.20.x servers

## [1.5.6-DEC0338] - 2025-12-22
### Fixed
- **Plugin Data Folder:** Reverted plugin name back to `TheEndex` to keep data folder as `plugins/TheEndex/`
  - Previous snapshot accidentally changed folder to `plugins/Endex/`, breaking existing configs

## [1.5.6-DEC0252] - 2025-12-22
### Fixed
- **`/shop` Command:** Fixed `/shop` not working — command now properly registered as alias for `/market`
- **GUI Conflict with Other Plugins:** Fixed issue where The Endex would intercept inventory clicks in other plugins' GUIs
  - Added title verification to click/drag handlers to prevent intercepting non-Endex inventories
  - Improved inventory close handler to properly clean up tracking when another plugin opens a GUI
- **Command Alias Registration:** Fixed `CommandAliasManager` not reading the `shortcuts` section from `commands.yml`
  - Now properly parses nested shortcut config (target, permission, description)

### Changed
- **Command Aliases:** Added built-in aliases in plugin.yml for better Bukkit registration
  - `/market` aliases: `shop`, `m`, `trade`, `exchange`, `bazaar`
  - `/endex` aliases: `ex`

## [1.5.6] - 2025-12-21
### Added
- **In-Game Layout Editor:** Complete visual GUI editor for custom shop layouts — no config editing required!
  - Open with `/endex shop editor <shop-id>` (e.g., `/endex shop editor main`)
  - **5 Editor Modes:** Place Category, Place Decoration, Place Button, Edit Slot, Remove Slot
  - **Custom Items Support:** Add any material as decoration or custom items with personalized names and lores
  - **Visual Preview:** See changes in real-time before saving
  - **Right-Click Editing:** Right-click any placed item to customize its display name and lore
  - **Chained Prompts:** Edit name then lore in sequence via chat input
  - **State Preservation:** Navigate between editor dialogs without losing unsaved changes
  - **One-Click Save:** Save button writes layout directly to shop config file

- **Custom Category Icons:** Categories can now have fully customized display names and lores
  - Set via layout editor or directly in shop config
  - Custom names/lores display correctly in `/market` player view

### Technical
- `TITLE_PREFIX = "§4§l⚙ "` distinguishes editor GUIs from player shop GUIs
- `reopenLayoutEditor()` preserves `tempLayout` and `unsavedChanges` state across dialogs
- Mode priority system ensures Edit/Remove modes work on existing items
- Chained text input via `pendingInputType` for name → lore flow

## [1.5.5] - 2025-12-20
### Fixed
- **Economy Plugin Compatibility:** Fixed "Economy unavailable" error with late-loading economy plugins (SimpleEconomy, etc.)
  - Added delayed retry (2 seconds) for economy provider detection
  - Economy plugins that register with Vault after TheEndex loads are now properly detected
  - Added common economy plugins to `softdepend` list to improve load order

### Technical
- Expanded `softdepend` in plugin.yml to include: Essentials, EssentialsX, CMI, SimpleEconomy, Economy, iConomy, Fe, XConomy, PlayerPoints, TokenManager, CoinsEngine, GemsEconomy, RoyaleEconomy, TheNewEconomy
- Scheduled delayed economy check runs 40 ticks (2 seconds) after server finishes loading

## [1.5.4] - 2025-12-19
### Added
- **bStats Metrics Integration:** Plugin analytics for tracking usage patterns.
  - Plugin ID: 28421
  - Custom charts: storage mode, shop mode, web UI status, holdings status, tracked items count
  - Fully anonymized data collection via bStats platform

- **Custom Shop GUI Enhancements:**
  - **Holdings Button:** View your virtual holdings directly from category pages (slot 45)
  - **Sort Button:** Cycle through sorting options (Name, Price, Change) in category pages (slot 53)
  - **Filter-Based Auto-Population:** Categories now use `filter` property to automatically populate items
  - Items matching category filters are dynamically included without manual listing

- **Default Market GUI Improvements:**
  - Removed Amount button from main market view for cleaner interface
  - Sort button moved to slot 49 for better layout
  - Sort order fixed: Price and Change now sort descending (highest first)

### Fixed
- **Hotbar Click Blocking:** Fixed issue where players couldn't interact with their hotbar while market GUI was open
- **Sort Order:** Price and Change sorting now correctly shows highest values first (descending order)
- **Holdings Button:** Holdings icon in Custom Shop GUI now properly opens the holdings panel
- **`/ex market` Command:** Now correctly opens DEFAULT or CUSTOM shop based on `shop.mode` config setting

### Technical
- bStats dependency added with proper Shadow relocation (`org.bstats` → `org.lokixcz.theendex.bstats`)
- `MarketGUI.openHoldings()` made public for cross-GUI access
- Improved click event handling with proper cancellation placement

## [1.5.3] - 2025-12-18
### Added
- **PlaceholderAPI Integration:** Full support for PlaceholderAPI with 30+ placeholders for scoreboards, holograms, and other plugins.
  - Market data: `%endex_price_<MATERIAL>%`, `%endex_trend_<MATERIAL>%`, `%endex_supply_<MATERIAL>%`, `%endex_demand_<MATERIAL>%`
  - Top items: `%endex_top_price_<1-10>%`, `%endex_bottom_price_<1-10>%`, `%endex_top_gainer_<1-10>%`, `%endex_top_loser_<1-10>%`
  - Holdings: `%endex_holdings_total%`, `%endex_holdings_count%`, `%endex_top_holdings_<1-10>%`
  - Statistics: `%endex_total_items%`, `%endex_total_volume%`, `%endex_average_price%`, `%endex_active_events%`

- **Update Checker:** Automatic update notifications for server administrators.
  - Checks Spigot and Modrinth for new versions on startup.
  - Console banner when updates are available.
  - OP players notified on join when updates available.
  - Configurable via `update-checker.enabled` and `update-checker.notify-ops`.

- **GUI Customization System:** Full GUI layout and appearance customization.
  - Per-GUI config files in `guis/` folder: `market.yml`, `details.yml`, `holdings.yml`, `deliveries.yml`.
  - Customizable titles, sizes, and slot positions.
  - Category definitions with custom icons and materials.
  - Background filler and navigation button customization.

- **Command Aliases System:** Custom command alias registration.
  - Configure aliases in `commands.yml` for any market subcommand.
  - Example: `/shop` → `/market`, `/stock` → `/market holdings`.
  - Supports tab completion inheritance.

### Configuration
New config files added:
```yaml
# config.yml additions
update-checker:
  enabled: true
  notify-ops: true

# guis/market.yml, details.yml, holdings.yml, deliveries.yml
# commands.yml for aliases
```

### Dependencies
- PlaceholderAPI added as soft dependency (optional but recommended).

### Technical
- `EndexExpansion` class for PlaceholderAPI expansion.
- `UpdateChecker` class with Spigot API and Modrinth API integration.
- `GuiConfigManager` for loading and managing GUI configurations.
- `CommandAliasManager` for dynamic command alias registration.
- `SqliteStore.getAllPlayersHoldings()` method for leaderboard placeholders.

## [1.5.2] - 2025-12-17
### Added
- **Optimized World Storage Scanner:** Complete rewrite with intelligent chunk caching and dirty tracking.
  - Chunk-level caching with configurable expiry (default: 600 seconds).
  - Event-driven dirty tracking — only re-scans chunks where containers were modified.
  - Disk persistence for cache data across server restarts.
  - Empty inventory skip optimization for faster scanning.
  - Material set-based O(1) container type filtering.

### Fixed
- **GUI Click Handling (MC 1.21+):** Fixed critical bug where market GUI items could be taken and clicks weren't registering on Minecraft 1.21+ servers.
  - Root cause: MC 1.21 changed `InventoryView` from a class to an interface, breaking reflection-based title extraction.
  - Solution: Implemented UUID-based GUI state tracking with `GuiType` enum instead of unreliable title matching.
  - All GUI types (Market, Details, Deliveries, Holdings) now properly track player state.

### Configuration
New cache settings under `price-world-storage`:
```yaml
price-world-storage:
  cache:
    enabled: true
    chunk-expiry-seconds: 600
    full-refresh-cycles: 5
    persist-to-disk: true
```

### Technical
- `GuiType` enum: `MARKET`, `DETAILS`, `DELIVERIES`, `HOLDINGS`, `NONE`
- `openGuis: MutableMap<UUID, GuiType>` tracks which GUI each player has open
- `ChunkScanCache` data class for per-chunk scan results
- Event listeners for `InventoryCloseEvent`, `BlockPlaceEvent`, `BlockBreakEvent`, `ChunkUnloadEvent`
- Cache file: `plugins/TheEndex/scanner-cache.json`

### Performance
- Reduced redundant container scanning by 80-90% on stable servers
- Chunk caching eliminates repeated full scans of unchanged areas
- Dirty tracking ensures only modified chunks are re-scanned
- Memory-efficient with automatic cache cleanup on chunk unload

## [1.5.1] - 2025-12-17
### Added
- **World Storage Scanner:** Scans ALL containers (chests, barrels, shulker boxes, etc.) across loaded chunks to provide truly dynamic, server-wide pricing based on global item scarcity/abundance.
- **Global Item Tracking:** Prices now react to total items stored across the entire server, not just player inventories or trades.
- **Anti-Manipulation Protection:** Per-chunk item caps, per-material caps, and suspicious activity logging prevent storage farm exploits.
- **TPS-Aware Throttling:** Scanner automatically skips or aborts if server TPS drops below threshold (default 18.0).
- **Double Chest Deduplication:** Ensures double chests are only counted once, preventing inflation.
- **Configurable Container Types:** Enable/disable scanning for chests, barrels, shulker boxes, hoppers, droppers, dispensers, furnaces, and brewing stands.
- **Nested Shulker Scanning:** Optionally counts items inside shulker boxes stored in other containers.
- **World Exclusion:** Skip creative, minigame, or spawn worlds from scanning.

### Configuration
New `price-world-storage` section in config.yml:
```yaml
price-world-storage:
  enabled: true
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  max-impact-percent: 5.0
  chunks-per-tick: 50
  containers:
    chests: true
    barrels: true
    shulker-boxes: true
    hoppers: false
    droppers: false
    dispensers: false
    furnaces: false
    brewing-stands: false
  scan-shulker-contents: true
  excluded-worlds: []
  anti-manipulation:
    per-chunk-item-cap: 10000
    per-material-chunk-cap: 5000
    min-tps: 18.0
    log-suspicious: true
```

### Security
- Storage farm manipulation mitigated via per-chunk caps
- Coordinated price manipulation reduced through proportional contribution limits
- Admin alerts for suspicious storage patterns (chunks exceeding item caps)

### Technical
- New `WorldStorageScanner` service with async batch processing
- Integrated into `MarketManager.updatePrices()` alongside existing inventory scanning
- Uses daemon thread with configurable scan intervals
- Location-based double chest tracking prevents double-counting

### Upgrade Notes
- World Storage Scanner is **enabled by default** for new installations
- Existing configs will use defaults; add `price-world-storage` section to customize
- Performance impact is minimal due to batched async processing and TPS monitoring

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
### Added
- Custom Web UI override (`web.custom.*`): external `index.html` & static assets, auto-export starter file, optional reload bypass.
- Admin commands: `/endex webui export`, `/endex webui reload` for managing exported UI.
- Unified web market view with advanced filters (category, price bounds, trend, sort fields & direction) and collapsible persistent filter panel.
- Category auto-population & improved watchlist/group toggles.

### Changed
- Removed legacy Addons tab and `/api/addons` usage; addons now integrate as items or custom routes.
- Simplified live update flow (single item stream via WS/SSE/polling).
- Refactored layout (two-row search + filters) for clarity and responsiveness.
- Copy tasks marked untracked to prevent Gradle state errors with transient DB journal files.

### Fixed
- UI tab reversion issue resolved by removing tab abstraction.
- Async misuse (`await` in sync render) eliminated; stabilized frontend rendering.
- Ensured Vault economy deductions occur on main thread; corrected SELL handling logic.

### Removed
- Addons navigation UI and related frontend logic.

### Documentation
- Added `docs/CUSTOM_WEBUI.md` and updated config with `web.custom.*` keys.

### Compatibility
- Built against Paper API 1.20.1; expected to operate on 1.21.x.

### Upgrade Notes
- If you relied on Addons tab navigation, migrate to item-based representation or custom web routes.
- Enable `web.custom.enabled` to customize UI; restart to export default scaffold.

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

---

