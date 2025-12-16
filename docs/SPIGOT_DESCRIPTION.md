[CENTER][B][SIZE=6]The Endex | Dynamic Market & Addons [1.20.1 - 1.21.x][/SIZE][/B][/CENTER]

[CENTER][IMG]https://i.imgur.com/onDbDSW.png[/IMG][/CENTER]

[CENTER][I]Bring a living economy to your server. Prices move with player demand and supply, with a slick Market GUI, timed events, optional web dashboard, virtual holdings system, and an addon framework.[/I][/CENTER]

[B][SIZE=5]🆕 New in 1.5.1 — World Storage Scanner[/SIZE][/B]
[B]Global Item Tracking:[/B] Prices now react to ALL items stored on your server — chests, barrels, shulker boxes, and more! True server-wide scarcity economics.
[B]Anti-Manipulation Protection:[/B] Per-chunk item caps, per-material limits, and suspicious activity logging prevent storage farm exploits.
[B]TPS-Aware:[/B] Scanner automatically skips if server is under load (configurable threshold).
[B]Smart Scanning:[/B] Double chest deduplication, nested shulker contents, and configurable container types.
[B]World Exclusions:[/B] Skip creative, minigame, or spawn worlds from affecting prices.

[B][SIZE=5]Features from 1.5.0 — Virtual Holdings System[/SIZE][/B]
[B]Virtual Holdings:[/B] Complete redesign! Items purchased now go into virtual holdings instead of your inventory. Withdraw when you're ready with full P/L tracking.
[B]Holdings GUI:[/B] Beautiful new panel showing all your holdings with quantity, avg cost, current price, and profit/loss. Left-click to withdraw all, right-click for one stack.
[B]Holdings Commands:[/B] [ICODE]/market holdings[/ICODE] to view, [ICODE]/market withdraw <item> [amount][/ICODE] to claim items.
[B]Web UI Withdraw:[/B] Withdraw buttons on each holding plus "Withdraw All" button with real-time notifications.
[B]Minecraft 1.21 Support:[/B] Full compatibility with Minecraft 1.21.x servers.

[B]Key Features[/B]
[B]Dynamic Pricing[/B] — Prices react to demand/supply with configurable sensitivity, EMA smoothing, and per‑item min/max caps.
[B]World Storage Scanner[/B] — Prices adapt to global item quantities across ALL server storage (chests, barrels, shulkers). Above-baseline = price drops, scarce = price rises.
[B]Inventory‑Aware Pricing (Optional)[/B] — Prices can gently adapt to how many items players are holding (online inventories). Above‑baseline stock nudges price down; scarce items nudge it up. Fully capped and smoothed.
[B]Market GUI[/B] — Categories, search, sorting, quick buy/sell amounts, a details panel with a tiny sparkline, plus last‑cycle demand/supply and an estimated impact percentage. GUI auto‑refreshes on price updates.
[B]Virtual Holdings[/B] — Buy items into virtual storage with average cost tracking and P/L display. Withdraw to inventory when ready via GUI, commands, or web interface.
[B]Delivery System[/B] — Backup overflow system when holdings are full. Claimable via GUI buttons or [ICODE]/market delivery[/ICODE] commands.

[IMG]https://i.imgur.com/2NVDOxj.gif[/IMG]
[SPOILER="Screenshots"]
[IMG]https://i.imgur.com/SY0ZO4F.png[/IMG]
[IMG]https://i.imgur.com/fdWGBho.png[/IMG]
[/SPOILER]

[B]Investments[/B] — Buy positions in items, view holdings, and redeem accrued value with simple in‑game commands.

[B]Events & Shocks[/B] — Time‑boxed multipliers (e.g., Ore Rush) with broadcasts; configurable and hot‑reloadable.

[IMG]https://i.imgur.com/Qa5Hrhw.gif[/IMG]

[B]Addon Framework[/B] — Lightweight API for addons to plug in new behaviors.
[B]Web Dashboard (Optional)[/B] — REST API, live updates (WS/SSE), charts, real item icons from your resource pack with caching, withdraw buttons, and [B]Combined Holdings[/B] with badges.

[IMG]https://i.imgur.com/2hXIQfx.gif[/IMG]


[B]Commands[/B]
[CODE]
/endex help
/endex version

/market
/market buy <item> <amount>
/market sell <item> <amount>
/market price <item>
/market top

/market holdings
/market withdraw <item> [amount]
/market withdraw all

/market invest buy <item> <amount>
/market invest list
/market invest redeem-all

/market delivery list
/market delivery claim <item> [amount]
/market delivery claim-all
/market delivery gui

/market event list
/market event <name>
/market event end <name>
/market event clear
[/CODE]

[B]Permissions[/B]
theendex.market — default: true
theendex.buy — default: true
theendex.sell — default: true
theendex.holdings — default: true
theendex.withdraw — default: true
theendex.invest — default: true
theendex.admin — default: op
endex.web.trade — allow web trading role
endex.web.admin — allow viewing others' holdings via /api/holdings/{uuid}

[B]Configuration Highlights[/B]
Versioned configuration with automated migration on startup and reload.
Adjustable update interval, price sensitivity, history length, and autosave frequency.
Transaction tax, item blacklist, category settings, and event multiplier caps.
Storage selection (YAML or SQLite) with automatic seeding and CSV price history export.
Optional web server (host/port, WS/SSE, icons, caching, rate limits).
[B]Virtual Holdings:[/B]
[CODE]
holdings:
  enabled: true
  max-per-player: 10000
  max-materials-per-player: 100
  mode: VIRTUAL
[/CODE]
[B]World Storage Scanner (NEW):[/B]
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
[B]Inventory‑aware pricing[/B]:
[CODE]
price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
[/CODE]
[B]Web combined holdings[/B]:
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
Server: Paper/Spigot 1.20.1 to 1.21.x
Java 17 runtime.
Economy: Vault (soft dependency).

[B]Data & Reliability[/B]
YAML storage in the plugin folder by default; optional SQLite database.
Periodic backups and orderly saves; safe reload reschedules tasks cleanly.
Atomic CSV export to plugins/TheEndex/history.
Holdings operations use atomic transactions to prevent duplication exploits.

[B]API & Addons[/B]
Public API service for other plugins: org.lokixcz.theendex.api.EndexAPI
Addon loader with command routing and aliasing
Customizable Web UI override (`web.custom.*`) – export default `index.html` and modify; unified market view (legacy Addons tab removed)

[B]Support[/B]
[URL='https://discord.gg/vS8xsvWaSU']Join our Discord[/URL]

[B]Disclaimer[/B]
This plugin was created with the help of AI, but the idea itself is mine. I tested it in real-time while using prompts, so it should work. Still, please expect that it may have some bugs, errors, or security issues.

[B]Changelog[/B]
New in 1.5.1:
- World Storage Scanner: Prices now react to ALL items stored on your server (chests, barrels, shulker boxes, etc.)
- Global item tracking for true server-wide scarcity economics
- Anti-manipulation protection: per-chunk caps, per-material limits, suspicious activity logging
- TPS-aware throttling: scanner skips if server is under load
- Double chest deduplication prevents double-counting
- Nested shulker content scanning (items inside shulkers in chests)
- Configurable container types and world exclusions

New in 1.5.0:
- Virtual Holdings System: Complete redesign where purchased items go into virtual holdings instead of inventory. Withdraw when ready with full P/L tracking.
- Holdings GUI: New panel showing all holdings with quantity, avg cost, current price, and profit/loss per material. Left-click withdraws all, right-click withdraws one stack.
- Holdings Commands: `/market holdings` to view, `/market withdraw <item> [amount]` to claim items to inventory.
- Web UI Withdraw: Withdraw buttons on each holding row plus "Withdraw All" button with real-time notifications.
- Minecraft 1.21 Support: Full compatibility with Minecraft 1.21.x servers.
- New permissions: `theendex.holdings` and `theendex.withdraw` (both default: true).
- Configurable limits: `holdings.max-per-player` (default 10,000) and `holdings.max-materials-per-player` (default 100).

New in 1.4.0:
- Virtual Delivery System: overflow purchases now enter a pending delivery queue with FIFO claiming, GUI integration, per-player limits, and optional auto-claim on login.
- Delivery Commands: `/market delivery list|claim|claim-all|gui` provide full management for pending items, including console support.
- Web + API updates: new `/api/deliveries` endpoints and updated dashboard badges reflecting pending deliveries in real time.
- Fixed long-standing buy loop issue where `/market buy <item> 64` could yield only one item on 1.20.1 servers.

New in 1.3.x:
- Inventory capacity checked before charging; oversized orders capped instead of dropping items.
- Security hardening: removed reflective access, session token moved to Authorization header.
- Hashed API token support via `web.api.token-hashes` (SHA-256).

See CHANGELOG.md for full release notes.

[CENTER][URL='https://discord.gg/vS8xsvWaSU'][IMG]https://i.postimg.cc/5tz22qFS/discord-icon-png-0-1.jpg[/IMG][/URL]
Join our Discord![/CENTER]


[B]Commands[/B]
[CODE]
/endex help
/endex version

/market
/market buy <item> <amount>
/market sell <item> <amount>
/market price <item>
/market top

/market invest buy <item> <amount>
/market invest list
/market invest redeem-all

/market delivery list
/market delivery claim <item> [amount]
/market delivery claim-all
/market delivery gui

/market event list
/market event <name>
/market event end <name>
/market event clear
[/CODE]

[B]Permissions[/B]
theendex.market — default: true
theendex.buy — default: true
theendex.sell — default: true
theendex.invest — default: true
theendex.admin — default: op
endex.web.trade — allow web trading role
endex.web.admin — allow viewing others’ holdings via /api/holdings/{uuid}

[B]Configuration Highlights[/B]
Versioned configuration with automated migration on startup and reload.
Adjustable update interval, price sensitivity, history length, and autosave frequency.
Transaction tax, item blacklist, category settings, and event multiplier caps.
Storage selection (YAML or SQLite) with automatic seeding and CSV price history export.
Optional web server (host/port, WS/SSE, icons, caching, rate limits).
[B]Inventory‑aware pricing[/B]:
[CODE]
price-inventory:
	enabled: true
	sensitivity: 0.02
	per-player-baseline: 64
	max-impact-percent: 10.0
[/CODE]
[B]Web combined holdings[/B]:
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
Server: Paper/Spigot 1.20.1 to latest.
Java 17 runtime.
Economy: Vault (soft dependency).

[B]Data & Reliability[/B]
YAML storage in the plugin folder by default; optional SQLite database.
Periodic backups and orderly saves; safe reload reschedules tasks cleanly.
Atomic CSV export to plugins/TheEndex/history.
Last‑cycle demand/supply persist (no more 0% on idle cycles).

[B]API & Addons[/B]
Public API service for other plugins: org.lokixcz.theendex.api.EndexAPI
Addon loader with command routing and aliasing
Customizable Web UI override (`web.custom.*`) – export default `index.html` and modify; unified market view (legacy Addons tab removed)

[B]Support[/B]
[URL='https://discord.gg/vS8xsvWaSU']Join our Discord[/URL]

[B]Disclaimer[/B]
This plugin was created with the help of AI, but the idea itself is mine. I tested it in real-time while using prompts, so it should work. Still, please expect that it may have some bugs, errors, or security issues.

[B]Changelog[/B]
New in 1.4.0:
- Virtual Delivery System: overflow purchases now enter a pending delivery queue with FIFO claiming, GUI integration, per-player limits, and optional auto-claim on login.
- Delivery Commands: `/market delivery list|claim|claim-all|gui` provide full management for pending items, including console support.
- Web + API updates: new `/api/deliveries` endpoints and updated dashboard badges reflecting pending deliveries in real time.
- Fixed long-standing buy loop issue where `/market buy <item> 64` could yield only one item on 1.20.1 servers; loop now instantiates fresh stacks and routes overflow safely.
- Delivery storage hardened with atomic SQLite transactions and graceful fallbacks to prevent duplication exploits.

New in 1.3.1:
- Inventory capacity is checked before charging players; oversized orders are capped (or delivered) instead of dropping items on the ground.
- Added detailed feedback when purchases are capped and shared the `calculateInventoryCapacity()` helper with the web tier.

New in 1.3.0:
- Security hardening: removed all reflective private field access (cleaner, safer internals).
- Web auth improvement: session token now moved to Authorization header after first load; URL is sanitized automatically.
- Hashed API token support via `web.api.token-hashes` (SHA-256). Plain tokens still work but are deprecated (one-time warning when both present).
- Updated SECURITY.md and added CHANGELOG.md; preparing groundwork for upcoming resource pack & rate limiter enhancements.

New in 1.2.0:
- Customizable Web UI override (`web.custom.*`) – export default assets then edit `index.html`, CSS, JS; `/endex webui export|reload` admin commands.
- Unified single‑page market view (legacy separate Addons tab removed) with improved filters & layout stability.
- Performance & live update polish (layout reflow reductions, caching tweaks, rate‑limit refinements).
- Minor icon & holdings panel improvements.
Retained from 1.1.x: inventory‑aware pricing, Combined Holdings badges, resource‑pack item icons.
See docs/changelogs.md for full release notes.

[CENTER][URL='https://discord.gg/vS8xsvWaSU'][IMG]https://i.postimg.cc/5tz22qFS/discord-icon-png-0-1.jpg[/IMG][/URL]
Join our Discord![/CENTER]