---
title: "Dynamic Pricing"
description: "How The Endex computes prices from supply, demand, and optional signals."
---

# Dynamic Pricing

The Endex prices move over time. Every update cycle, each material gets a new price based on market pressure.

## Core idea

- **Demand** increases when players buy.
- **Supply** increases when players sell.
- A tuning value (**price sensitivity**) controls how volatile the economy feels.

## Update interval

Pricing updates run on a fixed schedule configured in `plugins/TheEndex/config.yml`:

```yaml
pricing:
  update-interval-seconds: 5
```

## Conceptual formula

A simplified mental model:

$$
P_{new} = P_{old} \times (1 + (D - S) \times k)
$$

Where:

- $P$ is price
- $D$ is demand pressure
- $S$ is supply pressure
- $k$ is `price-sensitivity`

<Info>
The exact implementation includes smoothing and guardrails; the formula above is for intuition.
</Info>

## Optional signals

Depending on your server setup, prices can be influenced by more than just buy/sell volume:

1. **Market events** (temporary multipliers)
2. **World storage scanner** (optional) to account for items stored in containers

See also: [Market Events](events.md)

smoothing:

The plugin typically applies safety limits such as min/max price and maximum step size so prices don’t explode from a single trade.  enabled: true

  factor: 0.3    # Higher = more responsive, lower = smoother

## Signals that can influence price```



Depending on your configuration, The Endex can incorporate extra signals:This creates natural-looking price curves instead of jagged spikes.



### 1) Player inventory pressure (optional)---



When enabled, online player inventories can influence supply/demand pressure.## Min/Max Price Clamps



```yamlEach item has configurable price limits:

price-inventory:

  enabled: false```yaml

  sensitivity: 0.02items:

  per-player-baseline: 64  DIAMOND:

  max-impact-percent: 10.0    base-price: 100.0

```    min-price: 10.0      # Won't go below this

    max-price: 1000.0    # Won't go above this

### 2) World storage scanner (optional)```



On established servers, scanning containers (chests, barrels, etc.) can stabilize prices by measuring the server’s actual item stock.---



<Warning>## Price Influences

Enable the scanner on established servers. New servers with low storage counts may see volatility if the scanner is treated as a large signal.

</Warning>### 1. Trading Activity (Primary)



## Events multiplierThe core pricing driver. Every buy/sell affects price:



Events apply an additional multiplier to the effective price:```yaml

# How sensitive prices are to trading (0.0 - 1.0)

- Effective price = base price × combined event multipliersensitivity: 0.1

```

See: [Events](events.md)

### 2. Inventory-Aware Pricing

## Tips for balancing

Prices can react to items players are holding:

- Start with conservative sensitivity (`0.05`–`0.10`).

- Use events to create excitement without permanently changing fundamentals.```yaml

- Don’t blacklist too aggressively; thin markets are volatile.price-inventory:

  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

**How it works:**
- If players hold MORE than baseline → price pressure DOWN
- If players hold LESS than baseline → price pressure UP
- Maximum impact capped at `max-impact-percent`

### 3. World Storage Scanner

**New in v1.5.1** — Prices react to ALL items stored across the server:

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

**How it works:**
- Scans containers in loaded chunks periodically
- Items above `global-baseline` → price pressure DOWN
- Items below `global-baseline` → price pressure UP
- Anti-manipulation prevents storage farm exploits
- TPS-aware: skips scanning during lag

### 4. Market Events

Admin-triggered multipliers that affect specific items:

```yaml
events:
  ore_rush:
    multipliers:
      DIAMOND: 1.5    # 50% price increase
      EMERALD: 1.5
```

---

## Configuration Reference

### Core Pricing Settings

```yaml
# Price update interval in seconds
update-interval: 60

# Base sensitivity (0.0 - 1.0)
# Higher = more volatile
sensitivity: 0.1

# History length for tracking
history-length: 100
```

### Full Pricing Example

```yaml
# ===========================================
# PRICING CONFIGURATION
# ===========================================

# Update prices every 60 seconds
update-interval: 60

# Base sensitivity
sensitivity: 0.1

# EMA smoothing
smoothing:
  enabled: true
  factor: 0.3

# React to player inventories
price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0

# React to world storage (v1.5.1+)
price-world-storage:
  enabled: true
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  max-impact-percent: 5.0
```

---

## Understanding Price Movement

### Example Scenario

Starting price: **100 coins** for Diamond

1. **Player A buys 64 diamonds**
   - Demand increases
   - Price rises to ~105 coins

2. **Player B buys 128 diamonds**
   - More demand
   - Price rises to ~115 coins

3. **Player C sells 256 diamonds**
   - Supply floods market
   - Price drops to ~95 coins

4. **No trading for 10 minutes**
   - Price slowly stabilizes
   - Settles around ~98 coins

### Price History

View price history in-game:
```
/market price diamond
```

Or via the web dashboard's price charts.

---

## Tips for Server Owners

<Tip>
**Start Conservative**  
Begin with low sensitivity (0.05-0.1) and increase if markets feel too stable.
</Tip>

<Warning>
**World Storage Scanner**  
Enable on established servers. New servers may have highly volatile prices due to low item counts.
</Warning>

<Info>
**Test Events**  
Use events to create exciting market moments. Players love price spikes!
</Info>

---

## Related Pages

- [Market Events](events.md) — Price multipliers
- [Configuration](../reference/configuration.md) — Full config reference
- [REST API](../web-api/rest-api.md) — Price history endpoints
