---
title: "Investments"
description: "Buy and hold items for potential profit."
---
# Investments

Create APR-based investment certificates for items.

---

## Overview

Investments let you earn passive income by locking items for a period of time. Think of it as a "savings account" for materials.

---

## How It Works

1. **Create Investment** — Lock items at current price
2. **Wait** — Investment accrues value based on APR
3. **Redeem** — Claim original items + earned interest

---

## Commands

### Buy Investment

Lock items as an investment:

```
/market invest buy <item> <amount>
```

Example:
```
/market invest buy diamond 100
```

Output: `Created investment: 100 Diamond at $100.00/each. Earns 5% APR.`

### View Investments

List your active investments:

```
/market invest list
```

Output:
```
=== Your Investments ===
#1: 100 Diamond
    Purchased: $10,000.00
    Current Value: $10,125.00 (+1.25%)
    Time Held: 3 days, 2 hours
    Matured: ✓ Ready to redeem

#2: 500 Iron Ingot  
    Purchased: $2,500.00
    Current Value: $2,531.25 (+1.25%)
    Time Held: 2 hours
    Matured: ✗ 22 hours remaining
```

### Redeem Investments

Claim matured investments:

```
/market invest redeem-all
```

Only redeems investments that have passed the minimum hold time.

---

## APR Calculation

Interest accrues based on Annual Percentage Rate (APR):

```
Value = Principal × (1 + APR × (Days Held / 365))
```

### Example

- Investment: 100 diamonds at $100 each = $10,000
- APR: 5%
- Time held: 30 days

```
Interest = $10,000 × 0.05 × (30 / 365) = $41.10
Total Value = $10,041.10
```

---

## Maturity

Investments have a **minimum hold time** before redemption:

```yaml
investments:
  min-hold-time: 3600  # seconds (1 hour)
```

- **Before maturity** — Cannot redeem
- **After maturity** — Can redeem anytime, continues earning

---

## Configuration

```yaml
investments:
  # Enable investment system
  enabled: true
  
  # Annual Percentage Rate (5.0 = 5%)
  apr: 5.0
  
  # Minimum hold time before redemption (seconds)
  min-hold-time: 3600
  
  # Maximum investments per player
  max-per-player: 10
  
  # Maximum value per investment
  max-value: 100000
```

---

## Investment Strategy

### Long-Term Hold

Higher APR means longer holds are more profitable:

| Hold Time | 5% APR Return |
|-----------|---------------|
| 1 day | +0.014% |
| 7 days | +0.096% |
| 30 days | +0.411% |
| 90 days | +1.233% |
| 365 days | +5.000% |

### Price Speculation

Investments lock in your purchase price. If market price rises:

- Your investment value reflects the higher price
- Redeem for more coins than you invested

{% hint style="warning" %}
If market price falls, your redemption value also falls. Investments are not risk-free!
{% endhint %}

---

## Redemption

When you redeem:

1. Items return to your **holdings** (not inventory)
2. You receive the accrued value in coins
3. Investment is closed

### What You Get

```
Redemption = (Original Items) + (Interest in coins)
```

Example:
- Invested: 100 diamonds at $100 = $10,000
- APR earned: $200
- **Receive:** 100 diamonds + $200 coins

---

## GUI Access

Access investments from the Market GUI:

1. Open `/market`
2. Click **Investments** button
3. View all active investments
4. Click to redeem matured ones

---

## Permissions

| Permission | Description |
|------------|-------------|
| `theendex.invest` | Create and manage investments |

---

## Tips

{% hint style="success" %}
**Diversify**
Don't put all your wealth in one investment. Spread across multiple materials.
{% endhint %}

{% hint style="info" %}
**Watch the Market**
Invest in items you think will rise in price for double gains (APR + price increase).
{% endhint %}

{% hint style="warning" %}
**Liquidity**
Invested items are locked. Don't invest items you might need soon.
{% endhint %}

---

## Related Pages

- [Dynamic Pricing](pricing.md) — How prices affect investments
- [Virtual Holdings](holdings.md) — Where redeemed items go
- [Commands](../reference/commands.md) — Full command reference
