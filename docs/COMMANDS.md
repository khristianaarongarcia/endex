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
- /market event list — List events (defined and active)
- /market event <name> — Trigger/start an event by name
- /market event end <name> — End an active event by name
- /market event clear — End all active events

Notes
- Use TAB completion with materials.
- After editing `config.yml`, `events.yml`, or `market.yml`, use `/endex reload`.
