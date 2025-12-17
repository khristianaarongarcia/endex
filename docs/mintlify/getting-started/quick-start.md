---
title: "Quick Start"
description: "Open the market, buy, sell, and withdraw in a few minutes."
---

# Quick Start

## 1) Open the market

```text
/market
```

## 2) Buy an item

In the GUI, click an item and choose an amount.

Or use a command (if enabled on your server):

```text
/market buy diamond 10
```

Depending on configuration, purchased items go to **Holdings** (recommended) or directly to inventory (legacy mode).

## 3) Check your holdings

```text
/market holdings
```

## 4) Withdraw to your inventory

```text
/market withdraw diamond
/market withdraw diamond 64
/market withdraw all
```

<Tip>
Withdraw only when you actually need items. Keeping items in holdings makes trading smoother and reduces lost items.
</Tip>

## 5) Sell an item

```text
/market sell diamond 5
```

## What’s next

- Learn the algorithm: [Dynamic Pricing](../features/pricing.md)
- Understand storage: [Virtual Holdings](../features/holdings.md)
- Never lose overflow: [Delivery System](../features/delivery.md)

## For Administrators

### Starting Events

Trigger market events that affect prices:

```
/market event list           # See available events
/market event ore_rush       # Start an event
/market event end ore_rush   # End early
/market event clear          # End all events
```

### Checking Status

```
/endex version    # Plugin version and status
/endex reload     # Reload configuration
```

### Managing the Web Dashboard

```
/endex web              # Get your session link
/endex webui export     # Export UI for customization
/endex webui reload     # Reload custom UI
```

---

## Understanding the Economy

### How Prices Work

The Endex uses real supply and demand:

- **Players buy** → Demand increases → **Price goes UP** 📈
- **Players sell** → Supply increases → **Price goes DOWN** 📉
- **Nobody trades** → Price slowly returns to baseline

### Price Influences

Prices can be affected by:

1. **Trading Activity** — Buy/sell transactions
2. **Player Inventories** — What players are holding
3. **World Storage** — Items in chests across the server
4. **Market Events** — Admin-triggered multipliers

### Holdings System

The virtual holdings system:

```
Buy Items → Virtual Holdings → Withdraw to Inventory
                ↓
        Track Profit/Loss
```

Benefits:
- Never lose items to full inventory
- Track your average cost basis
- See real-time profit/loss
- Works with web dashboard

---

## Common Workflows

### Day Trading

1. Watch prices with `/market price <item>`
2. Buy low: `/market buy diamond 64`
3. Wait for price increase
4. Withdraw: `/market withdraw diamond`
5. Sell high: `/market sell diamond 64`

### Bulk Trading (Web Dashboard)

1. Get session link: `/endex web`
2. Open in browser
3. Trade multiple items quickly
4. Monitor live price charts

### Event Arbitrage

1. Watch for events: `/market event list`
2. Buy affected items before event
3. Wait for price spike during event
4. Sell at peak

---

## Tips for Success

<Tip>
**Buy Low, Sell High**  
Watch price trends and buy when prices dip. Sell when demand spikes.
</Tip>

<Info>
**Use the Holdings System**  
Don't withdraw until you need items. Holdings track your profit/loss automatically.
</Info>

<Warning>
**Watch for Events**  
Market events can dramatically affect prices. Check `/market event list` regularly.
</Warning>

<Tip>
**Web Dashboard**  
For serious trading, use the web dashboard. It's faster and shows charts.
</Tip>

---

## Next Steps

- [Dynamic Pricing](../features/pricing.md) — Understand the algorithm
- [Virtual Holdings](../features/holdings.md) — Master the holdings system
- [Web Dashboard](../web-api/dashboard.md) — Trade from your browser
- [Commands Reference](../reference/commands.md) — All commands explained
