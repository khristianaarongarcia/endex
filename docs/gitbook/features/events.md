# Events & Shocks

Time-limited events that affect market prices.

---

## Overview

Market events are admin-triggered price multipliers that create excitement and trading opportunities. Events can boost or crash prices for specific items or the entire market.

![Events](https://i.imgur.com/Qa5Hrhw.gif)

---

## Built-in Events

| Event | Effect | Duration |
|-------|--------|----------|
| `ore_rush` | Ore prices +50% | 1 hour |
| `harvest_festival` | Crop prices -30% | 2 hours |
| `war_economy` | Weapons +100% | 30 min |
| `market_crash` | All prices -20% | 30 min |

---

## Commands

### List Events

See available and active events:

```
/market event list
```

Output:
```
=== Available Events ===
ore_rush - Ore Rush! (1h)
harvest_festival - Harvest Festival (2h)
war_economy - War Economy (30m)
market_crash - Market Crash (30m)

=== Active Events ===
ore_rush - 45 minutes remaining
```

### Start Event

Trigger an event:

```
/market event <name>
```

Example:
```
/market event ore_rush
```

Broadcasts to all players (if configured).

### End Event

Stop an event early:

```
/market event end <name>
```

### Clear All Events

End all active events:

```
/market event clear
```

---

## Event Configuration

Define events in `events.yml`:

```yaml
events:
  ore_rush:
    # Display name (supports color codes)
    display-name: "&6⛏ Ore Rush!"
    
    # Description shown to players
    description: "Mining materials are in high demand!"
    
    # Duration in seconds
    duration: 3600
    
    # Broadcast start/end to server
    broadcast: true
    
    # Price multipliers per material
    multipliers:
      DIAMOND: 1.5      # +50%
      EMERALD: 1.5
      GOLD_INGOT: 1.3   # +30%
      IRON_INGOT: 1.2   # +20%
      COAL: 1.1         # +10%
```

---

## Multiplier Examples

| Multiplier | Effect |
|------------|--------|
| `2.0` | Double price (+100%) |
| `1.5` | +50% price |
| `1.0` | No change |
| `0.8` | -20% price |
| `0.5` | Half price (-50%) |

---

## Global Multipliers

Affect all items at once:

```yaml
events:
  market_crash:
    display-name: "&c📉 Market Crash!"
    description: "Economic downturn hits all sectors!"
    duration: 1800
    multipliers:
      "*": 0.8    # All items -20%
```

---

## Custom Event Ideas

### Festival Events

```yaml
events:
  summer_festival:
    display-name: "&e☀ Summer Festival"
    description: "Beach items are hot!"
    duration: 7200
    multipliers:
      TROPICAL_FISH: 2.0
      PUFFERFISH: 1.5
      SEA_PICKLE: 1.5
      PRISMARINE: 1.3
```

### Crisis Events

```yaml
events:
  food_shortage:
    display-name: "&c🍞 Food Shortage"
    description: "Crops are scarce!"
    duration: 3600
    multipliers:
      WHEAT: 2.0
      BREAD: 2.5
      CARROT: 1.8
      POTATO: 1.8
      BEETROOT: 1.8
```

### Building Boom

```yaml
events:
  construction_boom:
    display-name: "&a🏗 Construction Boom"
    description: "Building materials in demand!"
    duration: 5400
    multipliers:
      OAK_LOG: 1.5
      COBBLESTONE: 1.3
      GLASS: 1.4
      BRICK: 1.6
      IRON_BLOCK: 1.4
```

---

## Event Stacking

Multiple events can be active simultaneously. Multipliers stack multiplicatively:

### Example

- **ore_rush** active: DIAMOND × 1.5
- **market_crash** active: ALL × 0.8

Diamond price = Base × 1.5 × 0.8 = Base × 1.2 (+20%)

---

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

{% hint style="success" %}
**Buy Before Events**
If you know an event is coming, buy affected items beforehand.
{% endhint %}

{% hint style="info" %}
**Watch Announcements**
Events broadcast to chat. React quickly to profit!
{% endhint %}

{% hint style="warning" %}
**Crash Events**
During market crashes, it's often better to hold than sell at low prices.
{% endhint %}

---

## Admin Tips

{% hint style="success" %}
**Regular Events**
Schedule events to keep the economy exciting. Weekly events work well.
{% endhint %}

{% hint style="info" %}
**Seasonal Themes**
Match events to real-world or server seasons for immersion.
{% endhint %}

{% hint style="warning" %}
**Balance Multipliers**
Extreme multipliers (>2.0 or <0.5) can destabilize the economy. Use sparingly.
{% endhint %}

---

## Related Pages

- [Dynamic Pricing](pricing.md) — How events affect prices
- [Configuration](../reference/configuration.md) — Full events.yml reference
- [Commands](../reference/commands.md) — Event commands
