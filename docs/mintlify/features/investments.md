---
title: "Investments"
description: "Lock items, earn APR, and redeem later."
---

Investments let players lock items from holdings into a certificate-like position that accrues **APR** over time.

## How it works

1. Player invests a quantity of a material.
2. Items are removed from liquid holdings.
3. Over time the investment accrues interest.
4. Player redeems and receives the original items back (to holdings) plus interest (typically paid as coins).

## Config (typical)

```yaml
investments:
  enabled: true
  apr-percent: 5.0
```

## Commands (typical)

```text
/market invest buy <material> <amount>
/market invest list
/market invest redeem-all
```

<Warning>
Investments are not risk-free if your server ties redemption value to current market value; prices can move while you're invested.
</Warning>

## Related/market invest list

```

- [Holdings](holdings.md)

- [Pricing](pricing.md)Output:

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

<Warning>
If market price falls, your redemption value also falls. Investments are not risk-free!
</Warning>

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

<Tip>
**Diversify**  
Don't put all your wealth in one investment. Spread across multiple materials.
</Tip>

<Info>
**Watch the Market**  
Invest in items you think will rise in price for double gains (APR + price increase).
</Info>

<Warning>
**Liquidity**  
Invested items are locked. Don't invest items you might need soon.
</Warning>

---

## Related Pages

- [Dynamic Pricing](pricing.md) — How prices affect investments
- [Virtual Holdings](holdings.md) — Where redeemed items go
- [Commands](../reference/commands.md) — Full command reference
