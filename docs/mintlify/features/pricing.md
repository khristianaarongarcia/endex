---
title: "Dynamic Pricing"
description: "Learn how The Endex's dynamic pricing system works."
---
# Dynamic Pricing

The Endex uses a sophisticated pricing algorithm based on real supply and demand.

---

## How It Works

The pricing system simulates a real market economy:

| Action | Effect |
|--------|--------|
| **Buying** | Increases demand → Prices rise 📈 |
| **Selling** | Increases supply → Prices fall 📉 |
| **No activity** | Prices slowly stabilize |

---

## Price Formula

```
newPrice = currentPrice × (1 + sensitivity × (demand - supply) / volume)
```

Where:
- `sensitivity` — How reactive prices are (0.0 - 1.0)
- `demand` — Recent buy volume
- `supply` — Recent sell volume
- `volume` — Total trading volume

---

## EMA Smoothing

To prevent wild price swings, The Endex uses Exponential Moving Average (EMA) smoothing:

```yaml
smoothing:
  enabled: true
  factor: 0.3    # Higher = more responsive, lower = smoother
```

This creates natural-looking price curves instead of jagged spikes.

---

## Min/Max Price Clamps

Each item has configurable price limits:

```yaml
items:
  DIAMOND:
    base-price: 100.0
    min-price: 10.0      # Won't go below this
    max-price: 1000.0    # Won't go above this
```

---

## Price Influences

### 1. Trading Activity (Primary)

The core pricing driver. Every buy/sell affects price:

```yaml
# How sensitive prices are to trading (0.0 - 1.0)
sensitivity: 0.1
```

### 2. Inventory-Aware Pricing

Prices can react to items players are holding:

```yaml
price-inventory:
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
