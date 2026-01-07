[CENTER][B][SIZE=6]The Endex 1.5.8-JAN80235 | Dynamic Market & Addons [1.20.1 - 1.21.x][/SIZE][/B][/CENTER]

[CENTER][IMG]https://i.imgur.com/onDbDSW.png[/IMG][/CENTER]

[CENTER][I]Bring a living economy to your server. Prices move with player demand and supply, with a slick Market GUI, timed events, optional web dashboard, virtual holdings system, and an addon framework.[/I][/CENTER]

[CENTER]
[URL='https://discord.gg/ujFRXksUBE'][IMG]https://imgur.com/csuMkZs.png[/IMG][/URL] [URL='https://lokixcz-plugins.kagsystems.tech/introduction'][IMG]https://imgur.com/MTeHKfU.png[/IMG][/URL]
[/CENTER]



[CENTER][IMG]https://i.imgur.com/QK38If6.png[/IMG][/CENTER]

The Endex is a dynamic economy plugin that brings realistic market mechanics to your Minecraft server. Prices fluctuate based on player trading activity, server-wide item storage, and random market events.

[B] Version 1.5.8-JAN80235 Highlights:[/B]
• [B][COLOR=#FF0000]🛡️ Anti-Exploit Protection[/COLOR][/B] — 4-layer defense system blocks infinite money exploits!
• [B][COLOR=#00AA00]6% Buy/Sell Spread[/COLOR][/B] — Creates friction cost that prevents profitable arbitrage loops!
• [B][COLOR=#00AA00]60-Second Sell Cooldown[/COLOR][/B] — Blocks instant buy→sell flipping after purchases!
• [B][COLOR=#00AA00]Price Mean Reversion[/COLOR][/B] — Pulls manipulated prices back to base during idle periods!
• [B][COLOR=#00AA00]Config Auto-Update[/COLOR][/B] — Automatically migrates old configs while preserving your settings!
• [B][COLOR=#0088FF]Zero Performance Impact[/COLOR][/B] — All protections use <0.01% CPU overhead!

[B]What's Protected:[/B]
✅ [B]Buy-Sell Arbitrage[/B] — Can't buy low and sell high for instant profit
✅ [B]Price Manipulation[/B] — Prices drift back to normal when manipulation stops
✅ [B]Instant Flipping[/B] — Must wait 60s after buying before selling
✅ [B]Your Economy[/B] — Fair, balanced gameplay for all players

[B]Previous Version Features (1.5.8-JAN70109):[/B]
• [B]Shop Editor Fixes[/B] — Delete Category and Save Changes buttons now work properly!
• [B]Language Files[/B] — Added missing shop-editor translations to Korean, Russian, Portuguese, and Chinese!
• [B]Permissions Fix[/B] — Added missing permission nodes: endex.shop.editor, endex.shop.admin, endex.web.trade, endex.web.admin

[B]Earlier 1.5.8 Features:[/B]
• [B]Category Creation Fix[/B] — Fixed "Create Category" dialog not responding to clicks!
• [B]Inflation System[/B] — New configurable inflation/deflation system with per-category rates!
• [B]Market Items Manager[/B] — New editor button to view/edit all market items in one place!
• [B]Price Editor Fix[/B] — Fixed vanilla item price editing in the market editor!
• [B]Admin Commands Fixed[/B] — /market remove, disable, setbase, setmin, setmax now persist properly!



[CENTER][IMG]https://i.imgur.com/5OYklKS.png[/IMG][/CENTER]

[B] Dynamic Pricing[/B]
Prices react to demand/supply with configurable sensitivity, EMA smoothing, and per-item min/max caps.

[B] World Storage Scanner[/B]
Prices adapt to global item quantities across ALL server storage. Abundant items drop in price; scarce items rise.

[B] Virtual Holdings[/B]
Buy items into virtual storage with average cost tracking and profit/loss display. Withdraw when ready.

[B]️ Market GUI[/B]
Beautiful interface with categories, search, sorting, quick buy/sell buttons, and real-time price charts.

[IMG]https://i.imgur.com/2NVDOxj.gif[/IMG]

[SPOILER="Screenshots"]
[IMG]https://i.imgur.com/SY0ZO4F.png[/IMG]
[IMG]https://i.imgur.com/fdWGBho.png[/IMG]
[/SPOILER]

[B]⚡ Market Events[/B]
Time-boxed price multipliers (Ore Rush, Market Crash, etc.) with server broadcasts.

[IMG]https://i.imgur.com/Qa5Hrhw.gif[/IMG]

[B] Web Dashboard[/B]
Optional REST API with live updates, charts, item icons, and trading from your browser.

[IMG]https://i.imgur.com/2hXIQfx.gif[/IMG]

[B] Addon Framework[/B]
Extensible API for custom addons with command routing and web integration.

[B] Delivery System[/B]
Overflow protection when inventory or holdings are full. Claim items anytime.

[B] PlaceholderAPI Support[/B]
30+ placeholders for scoreboards, holograms, tab lists, and more. Display prices, trends, holdings, leaderboards, and market stats anywhere.

[B] bStats Metrics[/B]
Anonymous usage statistics to help improve the plugin.

[CENTER][IMG]https://bstats.org/signatures/bukkit/The%20Endex.svg[/IMG][/CENTER]



[CENTER][IMG]https://i.imgur.com/Up3T3eB.png[/IMG][/CENTER]

[CODE]
/endex help              - Show help menu
/endex version           - Display plugin version

/market                  - Open market GUI
/shop                    - Alias for /market
/m, /trade, /exchange    - Additional market aliases

/market buy <item> <amt> - Purchase items
/market sell <item> <amt>- Sell items
/market price <item>     - Check current price
/market top              - View top traded items

/market holdings         - View your virtual holdings
/market withdraw <item>  - Withdraw items to inventory
/market withdraw all     - Withdraw everything
/market sellholdings <item> <amt> - Sell directly from holdings

/market delivery list    - View pending deliveries
/market delivery claim   - Claim delivered items
/market delivery gui     - Open delivery GUI

/market invest buy <item> <amount>
/market invest list
/market invest redeem-all

/market event list       - View active events
/market event <name>     - Start an event (admin)
/market event end <name> - End an event (admin)
/market event clear      - Clear all events (admin)

# Namespaced commands also work:
/endex:shop, /endex:market, /endex:m
[/CODE]



[CENTER][IMG]https://imgur.com/jNDfH1h.png[/IMG][/CENTER]

[CODE]
theendex.market      - Access market GUI (default: true)
theendex.buy         - Purchase items (default: true)
theendex.sell        - Sell items (default: true)
theendex.holdings    - Access holdings system (default: true)
theendex.withdraw    - Withdraw from holdings (default: true)
theendex.invest      - Use investments (default: true)
theendex.admin       - Admin commands (default: op)
endex.shop.editor    - Shop editor GUI access (default: op)
endex.shop.admin     - Admin actions in custom shops (default: op)
endex.web.trade      - Web dashboard trading (default: true)
endex.web.admin      - View other players' holdings (default: op)
[/CODE]



[B] Requirements[/B]
• Server: Paper/Spigot 1.20.1 - 1.21.x
• Java: 17 or higher
• Economy: Vault + Economy plugin (EssentialsX, CMI, etc.)

[B] Storage Options[/B]
• YAML (default) or SQLite database
• Automatic backups and safe reloads
• CSV price history export



[B]Configuration Highlights[/B]

[B]Virtual Holdings:[/B]
[CODE]
holdings:
  enabled: true
  max-per-player: 10000
  max-materials-per-player: 100
  mode: VIRTUAL
[/CODE]

[B]World Storage Scanner:[/B]
[CODE]
price-world-storage:
  enabled: true
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  containers:
    chests: true
    barrels: true
    shulker-boxes: true
  anti-manipulation:
    per-chunk-item-cap: 10000
    per-material-chunk-cap: 5000
    min-tps: 18.0
[/CODE]

[B]Inventory-aware pricing:[/B]
[CODE]
price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
[/CODE]

[B]Web combined holdings:[/B]
[CODE]
web:
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
  holdings:
    inventory:
      enabled: true
      include-enderchest: false
      cache-seconds: 15
[/CODE]



[B]Compatibility[/B]
• Server: Paper/Spigot 1.20.1 to 1.21.x
• Java 17 runtime
• Economy: Vault (soft dependency)

[B]Data & Reliability[/B]
• YAML storage in the plugin folder by default; optional SQLite database
• Periodic backups and orderly saves; safe reload reschedules tasks cleanly
• Atomic CSV export to plugins/TheEndex/history
• Holdings operations use atomic transactions to prevent duplication exploits

[B]API & Addons[/B]
• Public API service for other plugins: org.lokixcz.theendex.api.EndexAPI
• Addon loader with command routing and aliasing
• Customizable Web UI override (web.custom.*) – export default index.html and modify



[B]Changelog[/B]

[SPOILER="Full Changelog"]
[B]New in 1.5.8-JAN70109:[/B]
• [B]Shop Editor Delete Category:[/B] Fixed "Delete Category" button which was showing "coming soon" instead of deleting
• [B]Shop Editor Save Changes:[/B] Fixed "Save Changes" button which was only playing sound without saving
• [B]Language Files:[/B] Added complete shop-editor section (~60 keys) to Korean, Russian, Portuguese, and Chinese language files
• [B]Missing Permissions:[/B] Added endex.shop.editor, endex.shop.admin, endex.web.trade, endex.web.admin to plugin.yml

[B]New in 1.5.8-JAN60132:[/B]
• [B]Shop Editor Category Fix:[/B] Fixed "Create Category" dialog in the shop editor not responding to clicks
• Clicking compass (Filter mode) or chest (Manual mode) icons now properly creates new categories
• Added CREATE_CATEGORY state tracking and proper click handling flow
• Categories now persist immediately to shop YAML config files

[B]New in 1.5.7-dec1038:[/B]
• Polish Language Support: Complete Polish (Polski) translation for plugin and documentation
• Config Translations Auto-Extract: 10 language configs now auto-extract to plugins/TheEndex/config_translations/ on first run
• Optimized Default Configuration: Ships with minimal resource usage settings by default
• World storage scanning DISABLED by default (easily re-enabled for full features)
• Inventory-based pricing DISABLED by default
• Save-on-each-update DISABLED by default (reduced disk I/O)
• Update interval increased to 120 seconds (was 60)
• Prominent optimization guide added to top of config.yml

[B]New in 1.5.7-dec1022:[/B]
• Web Dashboard Translation: Google Translate integration for 26+ languages
• GUI Bug Fix: Fixed ArrayIndexOutOfBoundsException when middle-clicking items in market details
• Performance Indicators: Config options now show [PERF: LOW/MEDIUM/HIGH] impact tags
• Translated Configs: Pre-translated config files in 9 languages (Chinese, Spanish, French, German, Japanese, Korean, Portuguese, Russian, Arabic)
• Market GUI: Increased details inventory size from 27 to 36 slots to fix button placement

[B]New in 1.5.7:[/B]
• Sell from Holdings: Sell items directly from virtual holdings without withdrawing to inventory first
• Arclight/Hybrid Server Compatibility: Fixed plugin recognition, SLF4J logging, ServiceLoader, and clickable chat links
• Market GUI: Item details now shows "Inventory: X | Holdings: Y" with new sell buttons (slots 33 & 35)
• Custom Shop GUI: Vanilla items can now be sold from holdings when inventory is insufficient
• Web API: New POST /api/sell-holdings endpoint for web dashboard selling
• New Command: /market sellholdings <material> <amount>

[B]New in 1.5.6:[/B]
• Minecraft 1.20.1+ Compatibility: Lowered api-version from '1.21' to '1.20' to restore support for 1.20.1+ servers (Arclight, etc.)
• In-Game Layout Editor: Complete visual GUI editor for custom shop layouts — no config editing required!
• Custom Items Support: Add any material as decoration with personalized names and lores
• 5 Editor Modes: Place Category, Place Decoration, Place Button, Edit Slot, Remove Slot
• Right-Click Editing: Customize item names and lores via chat prompts
• Real-Time Preview: See changes instantly before saving
• One-Click Save: Save layouts directly to shop config file
• Custom Category Icons: Categories display custom names/lores in /market

[B]New in 1.5.5:[/B]
• Economy Plugin Compatibility: Fixed "Economy unavailable" error with late-loading economy plugins (SimpleEconomy, etc.)
• Added delayed retry (2 seconds) for economy provider detection
• Economy plugins that register with Vault after TheEndex loads are now properly detected
• Added common economy plugins to softdepend list to improve load order

[B]New in 1.5.4:[/B]
• bStats Metrics: Plugin analytics with custom charts (storage mode, shop mode, web UI, holdings, item count)
• Custom Shop Enhancements: Holdings button (slot 45) and Sort button (slot 53) in category pages
• Filter-Based Categories: Auto-populate shop categories using material filters instead of manual item lists
• Default GUI Improvements: Removed Amount button, Sort moved to slot 49, cleaner layout
• Fixed: Hotbar interaction no longer blocked while GUI is open
• Fixed: Sort order now correctly shows highest prices/changes first (descending)
• Fixed: Holdings button in Custom Shop now properly opens holdings panel
• Fixed: /ex market command respects shop.mode config setting

[B]New in 1.5.3:[/B]
• PlaceholderAPI Integration: 30+ placeholders for prices, trends, holdings, leaderboards, and stats
• Update Checker: Automatic notifications on startup and OP join when updates available
• GUI Customization: Per-GUI config files for layout, colors, slot positions, and categories
• Command Aliases: Create custom shortcuts (e.g., /shop → /market) via commands.yml
• Full documentation available at [URL]https://lokixcz-plugins.kagsystems.tech/[/URL]

[B]New in 1.5.2:[/B]
• Optimized World Storage Scanner: Complete rewrite with intelligent chunk caching and dirty tracking
• Chunk-level caching with configurable expiry — 80-90% reduction in redundant scanning
• Event-driven dirty tracking — only re-scans chunks where containers were modified
• Disk persistence for cache data across server restarts
• GUI Fix (MC 1.21+): Fixed critical bug where market items could be taken and clicks weren't registering
• UUID-based GUI state tracking replaces unreliable title matching

[B]New in 1.5.1:[/B]
• World Storage Scanner: Prices now react to ALL items stored on your server (chests, barrels, shulker boxes, etc.)
• Global item tracking for true server-wide scarcity economics
• Anti-manipulation protection: per-chunk caps, per-material limits, suspicious activity logging
• TPS-aware throttling: scanner skips if server is under load
• Double chest deduplication prevents double-counting
• Nested shulker content scanning (items inside shulkers in chests)
• Configurable container types and world exclusions

[B]New in 1.5.0:[/B]
• Virtual Holdings System: Complete redesign where purchased items go into virtual holdings instead of inventory
• Holdings GUI: New panel showing all holdings with quantity, avg cost, current price, and profit/loss
• Holdings Commands: /market holdings to view, /market withdraw <item> [amount] to claim items
• Web UI Withdraw: Withdraw buttons on each holding row plus "Withdraw All" button
• Minecraft 1.21 Support: Full compatibility with Minecraft 1.21.x servers
• New permissions: theendex.holdings and theendex.withdraw (both default: true)
• Configurable limits: holdings.max-per-player (default 10,000) and holdings.max-materials-per-player (default 100)

[B]New in 1.4.0:[/B]
• Virtual Delivery System: overflow purchases now enter a pending delivery queue
• Delivery Commands: /market delivery list|claim|claim-all|gui
• Web + API updates: new /api/deliveries endpoints
• Fixed buy loop issue on 1.20.1 servers

[B]New in 1.3.x:[/B]
• Inventory capacity checked before charging; oversized orders capped
• Security hardening: removed reflective access, session token moved to Authorization header
• Hashed API token support via web.api.token-hashes (SHA-256)

See CHANGELOG.md for full release notes.
[/SPOILER]



[B]⚠️ Disclaimer[/B]
This plugin was created with the help of AI. While thoroughly tested, please expect potential bugs or issues. Report problems on Discord or GitHub.

[CENTER]
[URL='https://discord.gg/ujFRXksUBE'][IMG]https://i.postimg.cc/5tz22qFS/discord-icon-png-0-1.jpg[/IMG][/URL]
[B]Join our Discord![/B]
[/CENTER]