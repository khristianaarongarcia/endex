---
title: "Virtual Holdings"
description: "Manage virtual holdings with profit/loss tracking."
---
# Virtual Holdings

**New in v1.5.0** — A complete redesign of how purchases work.

---

## Overview

The Virtual Holdings System changes how buying works:

```
OLD WAY:  Buy → Items go directly to inventory
NEW WAY:  Buy → Items go to virtual holdings → Withdraw when ready
```

This provides better tracking, prevents item loss, and enables profit/loss calculation.

---

## How It Works

### 1. Buy Items

When you purchase items, they go to your virtual holdings:

```
/market buy diamond 64
```

Output: `Purchased 64 Diamond for $6,400. Added to holdings.`

### 2. Track Holdings

View your portfolio:

```
/market holdings
```

Shows:
- Quantity of each material
- Average cost basis
- Current market price
- Profit/Loss per item

### 3. Withdraw to Inventory

When you need items, withdraw them:

```
/market withdraw diamond         # All diamonds
/market withdraw diamond 32      # Just 32
/market withdraw all             # Everything
```

---

## Why Virtual Holdings?

| Benefit | Description |
|---------|-------------|
| ✅ **No Lost Items** | Full inventory? No problem—holdings store unlimited* |
| ✅ **Cost Tracking** | Know exactly what you paid per item |
| ✅ **P/L Calculation** | Real-time profit/loss display |
| ✅ **Web Integration** | Trade from browser seamlessly |
| ✅ **Global Limits** | Prevents economy abuse |

*Subject to configurable limits

---

## Holdings GUI

Access from Market GUI or:

```
/market holdings
```

### Interface

```
┌─────────────────────────────────────────────────┐
│                 MY HOLDINGS                      │
│          Total Value: $45,280                   │
│          Total P/L: +$3,120 (+7.4%)             │
├─────────────────────────────────────────────────┤
│                                                 │
│  [DIAMOND] ×128      [EMERALD] ×64              │
│  Avg: $95.00         Avg: $48.00                │
│  Now: $100.00        Now: $52.00                │
│  P/L: +$640 ✓        P/L: +$256 ✓               │
│                                                 │
│  [GOLD] ×256         [IRON] ×512                │
│  Avg: $10.50         Avg: $5.20                 │
│  Now: $9.80          Now: $5.50                 │
│  P/L: -$179 ✗        P/L: +$154 ✓               │
│                                                 │
├─────────────────────────────────────────────────┤
│   ◀ Back   │  📦 Withdraw All  │   📊 Stats    │
└─────────────────────────────────────────────────┘
```

### Interactions

| Action | Result |
|--------|--------|
| **Left-click** item | Withdraw all (up to inventory space) |
| **Right-click** item | Withdraw one stack (64 or max) |
| **Withdraw All** button | Claim everything that fits |
| **Stats** button | View detailed portfolio stats |

---

## Commands

### View Holdings

```
/market holdings
```

Chat output:
```
=== Your Holdings ===
Diamond: 128 (Avg: $95.00, P/L: +$640.00)
Emerald: 64 (Avg: $48.00, P/L: +$256.00)
Gold Ingot: 256 (Avg: $10.50, P/L: -$179.20)
---
Total Items: 448
Total Value: $45,280.00
Total P/L: +$716.80 (+1.6%)
```

### Withdraw Items

```
/market withdraw <item>           # All of item
/market withdraw <item> <amount>  # Specific amount
/market withdraw all              # Everything
```

Examples:
```
/market withdraw diamond
/market withdraw emerald 32
/market withdraw all
```

---

## Cost Basis Tracking

The system tracks your **average cost** per material:

### Example

1. Buy 64 diamonds at $100 each = $6,400
2. Buy 64 more at $110 each = $7,040
3. **Average cost**: ($6,400 + $7,040) / 128 = **$104.69 per diamond**

When you withdraw:
- FIFO (First-In-First-Out) applies
- Cost basis adjusts accordingly

---

## Profit/Loss Calculation

P/L is calculated in real-time:

```
P/L = (Current Price - Average Cost) × Quantity
P/L% = ((Current Price / Average Cost) - 1) × 100
```

### Example

- Holding: 128 diamonds
- Average cost: $95.00
- Current price: $100.00

```
P/L = ($100 - $95) × 128 = +$640
P/L% = ((100 / 95) - 1) × 100 = +5.26%
```

---

## Limits

Holdings have configurable limits to prevent abuse:

```yaml
holdings:
  max-per-player: 10000           # Total items
  max-materials-per-player: 100   # Different materials
```

### What Happens at Limits?

- **Max items reached** → Overflow goes to delivery queue
- **Max materials reached** → Cannot buy new material types

Check your limits:
```
/market holdings stats
```

---

## Configuration

```yaml
holdings:
  # Enable/disable the holdings system
  enabled: true
  
  # Maximum total items a player can hold
  max-per-player: 10000
  
  # Maximum different materials per player
  max-materials-per-player: 100
  
  # Mode: VIRTUAL (new) or LEGACY (old direct-to-inventory)
  mode: VIRTUAL
```

### Mode Options

| Mode | Behavior |
|------|----------|
| **VIRTUAL** | Items go to holdings, withdraw manually |
| **LEGACY** | Items go directly to inventory (old behavior) |

---

## Web Dashboard Integration

Holdings work seamlessly with the web dashboard:

### View Holdings Online

```
GET /api/holdings
```

### Withdraw from Browser

Click the 📤 button next to any holding, or use "Withdraw All".

Changes sync immediately with in-game.

---

## Migration from Legacy

If upgrading from an older version:

1. Existing inventory items remain in inventory
2. New purchases go to holdings
3. No data migration needed

To use old behavior:
```yaml
holdings:
  mode: LEGACY
```

---

## Tips

<Tip>
**Hold for Profit**  
Don't withdraw until you need items. Holdings show your P/L in real-time!
</Tip>

<Info>
**Web Trading**  
For bulk withdrawals, use the web dashboard. It's faster for managing large portfolios.
</Info>

<Warning>
**Inventory Space**  
Withdraw commands check inventory space. Clear your inventory before bulk withdrawals.
</Warning>

---

## Related Pages

- [Market GUI](market-gui.md) — Holdings panel in GUI
- [Delivery System](delivery.md) — Overflow handling
- [REST API](../web-api/rest-api.md) — Holdings endpoints
