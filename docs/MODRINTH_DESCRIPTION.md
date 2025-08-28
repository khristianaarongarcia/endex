![The Endex Banner](https://i.postimg.cc/FKKnJybw/EXNDEX-BANNER.png)

# The Endex | Dynamic Market & Addons [1.20.1 - 1.21.8]

Dynamic, demand-driven market with GUI, events, and Vault economy

## Overview
The Endex introduces a dynamic, demand-driven market to your server. Prices react to player behavior through a transparent demand/supply model, while a polished GUI and robust command set make trading simple and engaging. Admins retain full control via configurable parameters, market events, blacklists, and safe reloads. Data can be stored in human-readable YAML or SQLite, with automated migration and versioned configuration.

## Key Features
- Dynamic pricing driven by demand/supply with clamping and rolling history.
- Full-featured market GUI: pagination, sorting, category filters, text search, and a details view.
- Vault economy integration with configurable transaction tax.
- Market events and shocks: stackable multipliers with a configurable cap; broadcasts and persistence across restarts.
- Investments: purchase, list, and redeem-all accrued value with simple commands.
- Storage flexibility: YAML by default or SQLite; automatic migration from YAML when enabling SQLite.
- Player preferences: remembers amount, sort, category, search, and last page across sessions.
- History insights: ASCII sparkline trends in chat/GUI and CSV history export for analysis.
- Administrative tooling: safe reloads, event control, blacklists, CSV export, and version reporting.
- Addon framework: drop-in jars with auto command routing, aliases, and tab completion.
- Crypto Addon (optional): YAML-driven shop, per-item permissions, fixed/market pricing with mean reversion, `/endex crypto info`.
- Resource tracking: track gathered materials (block breaks, mob drops, fishing), periodic persistence, and `/endex track dump`.

## Gameplay Mechanics
- Periodic price updates use a sensitivity-based formula reflecting recent demand and supply.
- Prices are clamped between per-item minimums and maximums; demand/supply reset each cycle.
- Blacklisted items are excluded from trading and display.
- Events apply temporary multipliers to effective buy/sell prices (display and transaction) and can stack up to a defined cap.

## Graphical Interface
- Paginated 54-slot inventory view with quick amount selectors (1/8/16/32/64).
- Left-click to buy, right-click to sell; Shift/Middle-click opens a rich details panel.
- Sorting by name, price, and change; category filters and text search.
- Per-player preferences persist; interface auto-refreshes after trades.

## Commands
```
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
```

## Permissions
- theendex.market — default: true
- theendex.buy — default: true
- theendex.sell — default: true
- theendex.invest — default: true
- theendex.admin — default: op
- Crypto addon: `theendex.crypto.*` (info, balance, buy, sell, transfer, shop, admin)

## Configuration Highlights
- Versioned configuration with automated migration on startup and reload.
- Adjustable price update interval, sensitivity, history length, and autosave frequency.
- Transaction tax, item blacklist, category settings, and event multiplier caps.
- Storage selection (YAML or SQLite) with path configuration and automatic seeding.

## Compatibility
- Server: Paper/Spigot 1.20.1 to latest (built against API 1.20.1).
- Java 17 runtime.
- Economy: Vault (soft dependency).

## Data & Reliability
- YAML storage in the plugin’s folder by default; optional SQLite database.
- Periodic backups and orderly saves protect against data loss.
- Safe reload reinitializes managers, reloads configuration/events, and reschedules tasks.

## Support
For questions, feedback, or issue reports, please use the resource discussion and include the output of `/endex version` along with any relevant logs.

<p align="center">
  <a href="https://discord.gg/vS8xsvWaSU">
    <img src="https://i.postimg.cc/5tz22qFS/discord-icon-png-0-1.jpg" alt="Join our Discord!" width="64" height="64" />
  </a>
  <br/>
  Join our Discord!
</p>
