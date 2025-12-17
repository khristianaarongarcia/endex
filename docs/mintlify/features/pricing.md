---
title: "Dynamic Pricing"
description: "How The Endex computes prices from supply, demand, and optional signals."
---

The Endex prices move over time. Every update cycle, each material gets a new price based on market pressure.

## Core idea

- **Demand** increases when players buy.
- **Supply** increases when players sell.
- A tuning value (**price sensitivity**) controls how volatile the economy feels.

## Update interval

Pricing updates run on a fixed schedule configured in plugins/TheEndex/config.yml:

```yaml
pricing:
  update-interval-seconds: 5
```

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

### 3. World Storage Scanner

Prices react to ALL items stored across the server:

```yaml
price-world-storage:
  enabled: true
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  max-impact-percent: 5.0
  chunks-per-tick: 50
```

### 4. Market Events

Admin-triggered multipliers that affect specific items:

```yaml
events:
  ore_rush:
    multipliers:
      DIAMOND: 1.5
      EMERALD: 1.5
```

See: [Events](events.md)

## Min/Max Price Clamps

Each item has configurable price limits:

```yaml
items:
  DIAMOND:
    base-price: 100.0
    min-price: 10.0
    max-price: 1000.0
```

## Smoothing

The plugin applies EMA (exponential moving average) smoothing:

```yaml
smoothing:
  enabled: true
  factor: 0.3
```

This creates natural-looking price curves instead of jagged spikes.

## Understanding Price Movement

### Example Scenario

Starting price: **100 coins** for Diamond

1. **Player A buys 64 diamonds** - Demand increases, price rises to ~105 coins
2. **Player B buys 128 diamonds** - More demand, price rises to ~115 coins
3. **Player C sells 256 diamonds** - Supply floods market, price drops to ~95 coins
4. **No trading for 10 minutes** - Price slowly stabilizes around ~98 coins

### Price History

View price history in-game:

```text
/market price diamond
```

Or via the web dashboard price charts.

## Tips for Server Owners

- **Start Conservative** - Begin with low sensitivity (0.05-0.1) and increase if markets feel too stable.
- **World Storage Scanner** - Enable on established servers. New servers may have volatile prices due to low item counts.
- **Test Events** - Use events to create exciting market moments. Players love price spikes!

## Related Pages

- [Market Events](events.md) - Price multipliers
- [Configuration](../reference/configuration.md) - Full config reference
- [REST API](../web-api/rest-api.md) - Price history endpoints
