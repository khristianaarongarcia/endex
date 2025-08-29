# The Endex — Events

Events let you temporarily boost or reduce prices for specific items or whole categories. They’re defined in `plugins/TheEndex/events.yml`, can be triggered in-game by admins, and persist across restarts until they expire or are ended.

## Where events live
- File: `plugins/TheEndex/events.yml`
- Active events snapshot: `plugins/TheEndex/active_events.yml` (managed by the plugin)

## Event fields (events.yml)
Each event is a map/object in a top-level list `events:`

- `name`: Unique display name of the event
- `affected_item`: Bukkit Material name to affect, or `"*"` for all items (ignored if `affected_category` is set)
- `affected_category`: Optional category name instead of a single item. Supported: `ALL`, `ORES`, `FARMING`, `MOB_DROPS`, `BLOCKS`
- `multiplier` (double): e.g., `1.25` means +25% effective price
- `weight` (double 0..1, optional): Only used when stacking mode is `weighted-sum` (see below)
- `duration_minutes` (long): How long the event stays active when triggered
- `broadcast` (bool): Announce start/end server-wide
- `start_message`, `end_message` (optional): Custom messages; supports `&` color codes

Example:
```yaml
# plugins/TheEndex/events.yml
events:
  - name: Ore Rush
    affected_category: ORES
    multiplier: 1.5
    duration_minutes: 60
    broadcast: true
    start_message: "&6[The Endex] &bOre Rush &7is live! Mining pays &ax1.5&7."
    end_message: "&6[The Endex] &bOre Rush &7has ended."

  - name: Rotten Sunday
    affected_item: ROTTEN_FLESH
    multiplier: 0.6
    duration_minutes: 45
    broadcast: false
```

## How events apply to price
Effective price = Base price × Event multiplier(s)

Multiple active events can stack, controlled by config:
- `events.multiplier-cap` (double): cap the final multiplier, default 10.0
- `events.stacking.mode`: `multiplicative` (default) or `weighted-sum`
- `events.stacking.default-weight`: default per-event weight in weighted-sum (0..1)
- `events.stacking.max-active`: limit how many applicable events stack (0 = unlimited)

Formulas:
- multiplicative (default): `combined = min(cap, Π m_i)`
- weighted-sum: `combined = min(cap, 1 + Σ ((m_i - 1) * w_i))`

Items can be targeted by a single material or by category. Category matching uses sensible heuristics (e.g., `_ORE`, `_INGOT`, `_BLOCK` → ORES; isBlock → BLOCKS; known crop or drop names → FARMING, MOB_DROPS).

## Commands (admin)
- `/market event list` — Show defined events and currently active ones
- `/market event <name>` — Trigger an event by name
- `/market event end <name>` — End a single event
- `/market event clear` — End all events

Permissions: `theendex.admin`

## Persistence and expiration
- When triggered, active events are saved to `active_events.yml` and reloaded on startup.
- The plugin checks expiry regularly and ends events automatically when their time is up (with optional broadcast).

## Surfaces and UI
- Multipliers are reflected in:
  - Web UI: effective prices and multiplier value in item details
  - GUI lore: shows `Event: xN` and effective price
  - `/market price <MATERIAL>` output

## Config keys (config.yml)
```yaml
events:
  multiplier-cap: 10.0
  stacking:
    mode: multiplicative # or weighted-sum
    default-weight: 1.0
    max-active: 0 # 0 = unlimited
```

After editing configs or events, run `/endex reload`.
