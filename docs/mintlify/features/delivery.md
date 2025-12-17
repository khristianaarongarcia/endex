---
title: "Delivery System"
description: "Overflow protection for purchases exceeding inventory capacity."
---
# Delivery System

Backup system for overflow when holdings are full.

---

## Overview

The Delivery System ensures you **never lose items** from purchases:

```text
Buy Items → Holdings Full? → Overflow to Delivery Queue → Claim Later
```

---

## How It Works

### Normal Flow

1. You buy 100 diamonds
2. Your holdings have space
3. **100 diamonds → Holdings** ✓

### Overflow Flow

1. You buy 100 diamonds
2. Holdings can only fit 60 more
3. **60 diamonds → Holdings** ✓
4. **40 diamonds → Delivery Queue** 📦

---

## Commands

### View Deliveries

Check your pending deliveries:

```
/market delivery list
```

Output:
```
=== Pending Deliveries ===
Diamond: 40 items
Emerald: 128 items
Gold Ingot: 64 items
---
Total: 232 items pending
```

### Claim Specific Item

Claim a specific material:

```
/market delivery claim <item>           # All of item
/market delivery claim <item> <amount>  # Specific amount
```

Examples:
```
/market delivery claim diamond
/market delivery claim emerald 64
```

### Claim Everything

Claim all pending deliveries:

```
/market delivery claim-all
```

Claims to inventory (not holdings) up to available space.

### Delivery GUI

Open the delivery GUI directly:

```
/market delivery gui
```

---

## Delivery GUI

### Interface

```
┌─────────────────────────────────────────────────┐
│              PENDING DELIVERIES                  │
│              Total: 232 items                   │
├─────────────────────────────────────────────────┤
│                                                 │
│  [DIAMOND] ×40      [EMERALD] ×128              │
│  Click: Claim All   Click: Claim All            │
│  Right: Claim 64    Right: Claim 64             │
│                                                 │
│  [GOLD] ×64                                     │
│  Click: Claim All                               │
│                                                 │
├─────────────────────────────────────────────────┤
│      ◀ Back      │     📦 Claim All Items      │
└─────────────────────────────────────────────────┘
```

### Interactions

| Action | Result |
|--------|--------|
| **Left-click** item | Claim all of that material |
| **Right-click** item | Claim one stack (64) |
| **Claim All** button | Claim everything that fits |

---

## Access from Market GUI

The main Market GUI shows delivery status:

1. Open `/market`
2. Look at slot 51 (Ender Chest icon)
3. Badge shows pending item count
4. Click to open delivery panel

---

## Configuration

```yaml
delivery:
  # Enable the delivery system
  enabled: true
  
  # Auto-claim deliveries when player logs in
  auto-claim-on-login: false
  
  # Maximum pending items per player
  max-pending-per-player: 100000
```

### Auto-Claim on Login

When enabled:
- Player joins server
- System checks for pending deliveries
- Automatically claims to inventory
- Player notified of claimed items

```yaml
delivery:
  auto-claim-on-login: true
```

---

## Storage

Deliveries are stored in SQLite database:

```
plugins/TheEndex/deliveries.db
```

Features:
- Transaction-safe
- Prevents duplication exploits
- Survives server restarts
- Per-player tracking

---

## Limits

### Maximum Pending

Configure maximum pending items per player:

```yaml
delivery:
  max-pending-per-player: 100000
```

**When limit reached:**
- Purchase fails
- Player notified
- Must claim existing deliveries first

### Inventory Space

When claiming:
- System checks available inventory slots
- Only claims what fits
- Remainder stays in queue

---

## Preventing Item Loss

The delivery system has multiple safeguards:

| Safeguard | Protection |
|-----------|------------|
| **Database-first** | Removes from DB before giving items |
| **Transaction safety** | Atomic operations prevent corruption |
| **Space checking** | Won't claim more than fits |
| **Logout protection** | Items safe if you disconnect |

---

## Web Dashboard

Deliveries are accessible from the web dashboard:

### View Deliveries

```
GET /api/deliveries
```

### Claim from Browser

```
POST /api/deliveries/claim
{
  "material": "DIAMOND",
  "amount": 64
}
```

Or claim all:
```
POST /api/deliveries/claim
{}
```

---

## Troubleshooting

### Items Not Appearing

1. Check `/market delivery list`
2. Verify `delivery.enabled: true` in config
3. Check if max-pending limit reached

### Can't Claim Items

1. Check inventory space
2. Try claiming smaller amounts
3. Clear inventory and try again

### Lost Deliveries

Deliveries should never be lost. If items disappeared:
1. Check server logs for errors
2. Verify database file exists
3. Contact support with logs

---

## Tips

<Tip>
**Regular Claims**  
Don't let deliveries pile up. Claim regularly to keep space available.
</Tip>

<Info>
**Clear Inventory First**  
Before bulk claims, empty your inventory for maximum space.
</Info>

<Warning>
**Holdings vs Delivery**  
Items claimed from delivery go to INVENTORY, not holdings. Different from purchases!
</Warning>

---

## Related Pages

- [Virtual Holdings](holdings.md) — Primary storage system
- [Market GUI](market-gui.md) — Delivery panel access
- [REST API](../web-api/rest-api.md) — Delivery endpoints
