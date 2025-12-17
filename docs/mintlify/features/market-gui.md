# Market GUI

A beautiful, intuitive interface for all trading operations.

---

## Overview

![Market GUI](https://i.imgur.com/2NVDOxj.gif)

The Market GUI is your central hub for:
- Browsing available items
- Buying and selling
- Viewing price history
- Managing holdings
- Accessing deliveries

---

## Opening the GUI

```
/market
```

Or use the alias:
```
/endex market
```

---

## Layout

### Main Market View

```
┌─────────────────────────────────────────────────┐
│  DIAMOND    EMERALD    GOLD      IRON     COAL  │
│  $100 ▲     $50 ▼      $10 ▲    $5 →     $2 ▲  │
│                                                 │
│  WHEAT     CARROT     POTATO    ...      ...   │
│  $3 ▼      $2 →       $2 ▼     ...      ...   │
│                                                 │
│  ...       ...        ...       ...      ...   │
│                                                 │
│                                                 │
├─────────────────────────────────────────────────┤
│ ◀ Prev │ Category │ Amount │ Holdings │ ▶ Next │
└─────────────────────────────────────────────────┘
```

### Slot Functions

| Slot | Function |
|------|----------|
| **0-44** | Item grid (5 rows × 9 columns) |
| **45** | Previous page |
| **47** | Category filter |
| **48** | Amount selector |
| **49** | Sort options |
| **51** | My Holdings |
| **52** | Search |
| **53** | Next page |

---

## Item Display

Each item shows:

```
┌─────────────┐
│   [ICON]    │
│  DIAMOND    │
│  $100.00 ▲  │
│  +5.2%      │
└─────────────┘
```

- **Icon** — Visual item representation
- **Name** — Material name
- **Price** — Current market price
- **Arrow** — Trend indicator (▲ up, ▼ down, → stable)
- **Percent** — Price change from baseline

---

## Trading

### Buying Items

1. **Select amount** (bottom row) — 1, 8, 16, 32, or 64
2. **Left-click** an item to buy

Items go to your **virtual holdings**, not inventory.

### Selling Items

1. **Select amount** (bottom row)
2. **Right-click** an item to sell

Sells from your **inventory** (not holdings).

### Quick Trade

- **Shift + Left-click** — Buy max amount you can afford
- **Shift + Right-click** — Sell all of that item

---

## Categories

Filter items by category:

| Category | Items |
|----------|-------|
| **All** | Everything |
| **Ores** | Diamond, Emerald, Gold, Iron, Coal |
| **Farming** | Wheat, Carrot, Potato, etc. |
| **Mob Drops** | Bones, String, Gunpowder, etc. |
| **Blocks** | Stone, Wood, Glass, etc. |

Click the **Category** button to cycle through filters.

---

## Sorting

Sort items by:

| Sort | Description |
|------|-------------|
| **Name** | Alphabetical A-Z |
| **Price** | Highest to lowest |
| **Change** | Biggest movers first |

Click the **Sort** button to cycle options.

---

## Search

Find items quickly:

1. Click the **Search** button (magnifying glass)
2. Type in chat
3. GUI filters to matching items

Clear search by clicking Search again.

---

## Amount Selection

Quick amount buttons:

| Button | Amount |
|--------|--------|
| **1** | Single item |
| **8** | Half stack |
| **16** | Quarter stack |
| **32** | Half stack |
| **64** | Full stack |

Click to cycle through amounts.

---

## Holdings Panel

Access your virtual holdings from the GUI:

1. Click **Holdings** button (slot 51)
2. View all owned items
3. Left-click to withdraw all
4. Right-click to withdraw one stack

### Holdings Display

```
┌─────────────────────────────────────────────────┐
│                 MY HOLDINGS                      │
├─────────────────────────────────────────────────┤
│  DIAMOND ×128     EMERALD ×64     GOLD ×256    │
│  Avg: $95         Avg: $48        Avg: $9      │
│  P/L: +$640       P/L: -$128      P/L: +$256   │
│                                                 │
│  ...              ...             ...          │
│                                                 │
├─────────────────────────────────────────────────┤
│    ◀ Back    │  Withdraw All  │    Stats       │
└─────────────────────────────────────────────────┘
```

---

## Item Details

Shift-click any item for detailed view:

```
┌─────────────────────────────────────────────────┐
│                  DIAMOND                         │
├─────────────────────────────────────────────────┤
│  Current Price: $100.00                         │
│  24h Change: +5.2%                              │
│  Min Price: $10.00                              │
│  Max Price: $1000.00                            │
│                                                 │
│  ▁▂▃▄▅▆▇█▇▆ (Price Sparkline)                  │
│                                                 │
│  Your Holdings: 128                             │
│  Avg Cost: $95.00                               │
│  P/L: +$640.00 (+5.3%)                         │
├─────────────────────────────────────────────────┤
│     Buy      │     Sell     │      Back        │
└─────────────────────────────────────────────────┘
```

---

## Visual Indicators

### Price Trends

| Symbol | Meaning |
|--------|---------|
| **▲** | Price rising (green) |
| **▼** | Price falling (red) |
| **→** | Price stable (yellow) |

### Profit/Loss Colors

| Color | Meaning |
|-------|---------|
| **Green** | Profit (current > avg cost) |
| **Red** | Loss (current < avg cost) |
| **White** | Break-even |

### Event Indicators

Active events show a special border/glow on affected items.

---

## Keyboard Shortcuts

| Key | Action |
|-----|--------|
| **Q** | Close GUI |
| **1-5** | Quick amount select |
| **Escape** | Close GUI |

---

## Configuration

Customize GUI appearance in `config.yml`:

```yaml
gui:
  # Main GUI title
  title: "&6The Endex Market"
  
  # Items per page
  page-size: 45
  
  # Show sparklines
  sparklines: true
  
  # Show P/L in tooltips
  show-pl: true
```

---

## Related Pages

- [Virtual Holdings](holdings.md) — Understanding holdings
- [Commands](../reference/commands.md) — GUI-related commands
- [Web Dashboard](../web-api/dashboard.md) — Browser alternative
