[CENTER][B][SIZE=6]The Endex | Dynamic Market & Addons [1.20.1 - 1.21.8][/SIZE][/B][/CENTER]

[CENTER][IMG]https://i.imgur.com/onDbDSW.png[/IMG][/CENTER]

[CENTER][I]Bring a living economy to your server. Prices move with player demand and supply, with a slick Market GUI, timed events, optional web dashboard, and an addon framework.[/I][/CENTER]

[B]Key Features[/B]
[B]Dynamic Pricing[/B] — Prices react to demand/supply with configurable sensitivity, EMA smoothing, and per‑item min/max caps.
[B]Inventory‑Aware Pricing (Optional)[/B] — Prices can gently adapt to how many items players are holding (online inventories). Above‑baseline stock nudges price down; scarce items nudge it up. Fully capped and smoothed.
[B]Market GUI[/B] — Categories, search, sorting, quick buy/sell amounts, a details panel with a tiny sparkline, plus last‑cycle demand/supply and an estimated impact percentage. GUI auto‑refreshes on price updates.

[IMG]https://i.imgur.com/2NVDOxj.gif[/IMG]
[SPOILER="Screenshots"]
[IMG]https://i.imgur.com/SY0ZO4F.png[/IMG]
[IMG]https://i.imgur.com/fdWGBho.png[/IMG]
[/SPOILER]

[B]Investments[/B] — Buy positions in items, view holdings, and redeem accrued value with simple in‑game commands.

[B]Events & Shocks[/B] — Time‑boxed multipliers (e.g., Ore Rush) with broadcasts; configurable and hot‑reloadable.

[IMG]https://i.imgur.com/Qa5Hrhw.gif[/IMG]

[B]Addon Framework[/B] — Lightweight API for addons to plug in new behaviors.
[B]Web Dashboard (Optional)[/B] — REST API, live updates (WS/SSE), charts, real item icons from your resource pack with caching, and [B]Combined Holdings[/B] that show both [I]Invest[/I] (DB) and [I]Inv[/I] (live inventory) with badges.

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

/market invest buy <item> <amount>
/market invest list
/market invest redeem-all

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

[B]Support[/B]
Issues: https://github.com/khristianaarongarcia/endex/issues
[URL='https://discord.gg/vS8xsvWaSU']Join our Discord[/URL]

[B]Changelog[/B]
Latest highlights:
- Inventory‑aware pricing (optional) with smoothing & per‑cycle impact caps
- Web Combined Holdings with [Inv]/[Invest] badges; admin holdings view endpoint
- Web icons improvements, WS live updates, rate‑limit polish
See docs/changelogs.md for full notes.

[CENTER][URL='https://discord.gg/vS8xsvWaSU'][IMG]https://i.postimg.cc/5tz22qFS/discord-icon-png-0-1.jpg[/IMG][/URL]
Join our Discord![/CENTER]