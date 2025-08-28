# The Endex — Commands

Everyone
- /endex — Show info and quick help
- /endex market — Open the GUI
- /market buy <material> <amount> — Buy items using Vault currency
- /market sell <material> <amount> — Sell items from your inventory
- /market price <material> — Show price, min/max, event multiplier, and (optionally) a sparkline
- /market top — Show top movers
- /market invest buy <amount> — Buy an investment note
- /market invest list — List your notes and accrued value
- /market invest redeem-all — Redeem and cash out all notes

Admin (permission: theendex.admin)
- /endex reload — Save + reload configs, events, and market data; reschedules tasks
- /endex version — Print plugin version and storage mode
- /endex track dump — Print top gathered resources (from ResourceTracker)
- /market event list — List events (defined and active)
- /market event <name> — Trigger/start an event by name
- /market event end <name> — End an active event by name
- /market event clear — End all active events

Notes
- Use TAB completion with materials.
- After editing `config.yml`, `events.yml`, or `market.yml`, use `/endex reload`.
- Addon commands integrate under `/endex <addon> ...` and can also register short aliases (e.g., `/cc ...`). Tab completion is provided by each addon.

## Crypto Addon (if installed)

Everyone
- /endex crypto help — Show crypto commands
- /endex crypto info — Token details, price mode, fees, and limits
- /endex crypto balance — View your token balance
- /endex crypto buy <amount>
- /endex crypto sell <amount>
- /endex crypto transfer <player> <amount>
- /endex crypto shop — Open YAML-driven shop GUI

Admin (permission: theendex.crypto.admin)
- /endex crypto admin setprice <value>
- /endex crypto admin mint|give <player> <amount>
- /endex crypto admin burn|take <player> <amount>
- /endex crypto admin limits set <dailyBuyCap|dailySellCap|cooldown> <value>
- /endex crypto admin limits get
- /endex crypto admin reload — Reloads crypto config and shop.yml
