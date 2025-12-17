---
title: "Market Events"
description: "Timed events that apply multipliers to prices for specific categories or the whole market."
---

# Market Events

Market events are admin-triggered price multipliers that create temporary buy/sell opportunities.

Events are defined in `plugins/TheEndex/events.yml`.

## Commands

```text
/market event list
/market event <name>
/market event end <name>
/market event clear
```

<Tip>
Recurring events (weekly ore rush, seasonal harvest, etc.) are a great way to keep trading active.
</Tip>

## Example configuration

```yaml
events:
  ore_rush:
    display-name: "&6⛏ Ore Rush!"
    description: "Mining materials are in high demand!"
    duration: 3600
    broadcast: true
    multipliers:
      DIAMOND: 1.5
      EMERALD: 1.5
      GOLD_INGOT: 1.3
```

## How events apply

The effective price is the base price multiplied by the combined event multiplier.

<Info>
See `docs/EVENTS.md` in the repository for the full schema and stacking rules.
</Info>

## Scheduled Events

While The Endex doesn't have built-in scheduling, you can use:

### External Schedulers

Use cron jobs or server schedulers to trigger events:

```bash
# Every day at 6 PM
0 18 * * * screen -S minecraft -X stuff "/market event ore_rush^M"
```

### Other Plugins

Plugins like **DeluxeScheduler** or **ScheduledCommands** can trigger:
```
/market event <name>
```

---

## Permissions

| Permission | Description |
|------------|-------------|
| `theendex.admin` | Manage events |

---

## Player Strategies

<Tip>
**Buy Before Events**  
If you know an event is coming, buy affected items beforehand.
</Tip>

<Info>
**Watch Announcements**  
Events broadcast to chat. React quickly to profit!
</Info>

<Warning>
**Crash Events**  
During market crashes, it's often better to hold than sell at low prices.
</Warning>

---

## Admin Tips

<Tip>
**Regular Events**  
Schedule events to keep the economy exciting. Weekly events work well.
</Tip>

<Info>
**Seasonal Themes**  
Match events to real-world or server seasons for immersion.
</Info>

<Warning>
**Balance Multipliers**  
Extreme multipliers (greater than 2.0 or less than 0.5) can destabilize the economy. Use sparingly.
</Warning>

---

## Related Pages

- [Dynamic Pricing](pricing.md) — How events affect prices
- [Configuration](../reference/configuration.md) — Full events.yml reference
- [Commands](../reference/commands.md) — Event commands
