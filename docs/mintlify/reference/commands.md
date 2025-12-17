---
title: "Commands"
description: "Complete command reference for The Endex."
---
# Commands

Complete reference for all The Endex commands.

---

## Core Commands

### Market GUI

| Command | Description | Permission |
|---------|-------------|------------|
| `/market` | Open market GUI | `theendex.market` |
| `/endex market` | Alias for `/market` | `theendex.market` |

### Trading

| Command | Description | Permission |
|---------|-------------|------------|
| `/market buy <item> <amount>` | Buy items to holdings | `theendex.buy` |
| `/market sell <item> <amount>` | Sell items from inventory | `theendex.sell` |
| `/market price <item>` | Check item price | `theendex.market` |
| `/market top` | View top traded items | `theendex.market` |

**Examples:**
```
/market buy diamond 64
/market sell emerald 32
/market price gold_ingot
/market top
```

---

## Holdings Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/market holdings` | View your holdings | `theendex.holdings` |
| `/market withdraw <item>` | Withdraw all of item | `theendex.withdraw` |
| `/market withdraw <item> <amount>` | Withdraw specific amount | `theendex.withdraw` |
| `/market withdraw all` | Withdraw everything | `theendex.withdraw` |

**Examples:**
```
/market holdings
/market withdraw diamond
/market withdraw emerald 32
/market withdraw all
```

---

## Investment Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/market invest buy <item> <amount>` | Create investment | `theendex.invest` |
| `/market invest list` | View investments | `theendex.invest` |
| `/market invest redeem-all` | Redeem matured investments | `theendex.invest` |

**Examples:**
```
/market invest buy diamond 100
/market invest list
/market invest redeem-all
```

---

## Delivery Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/market delivery list` | View pending deliveries | `theendex.market` |
| `/market delivery claim <item>` | Claim all of item | `theendex.market` |
| `/market delivery claim <item> <amount>` | Claim specific amount | `theendex.market` |
| `/market delivery claim-all` | Claim all deliveries | `theendex.market` |
| `/market delivery gui` | Open delivery GUI | `theendex.market` |

**Examples:**
```
/market delivery list
/market delivery claim diamond
/market delivery claim emerald 64
/market delivery claim-all
/market delivery gui
```

---

## Event Commands (Admin)

| Command | Description | Permission |
|---------|-------------|------------|
| `/market event list` | List all events | `theendex.admin` |
| `/market event <name>` | Start an event | `theendex.admin` |
| `/market event end <name>` | End an event early | `theendex.admin` |
| `/market event clear` | End all events | `theendex.admin` |

**Examples:**
```
/market event list
/market event ore_rush
/market event end ore_rush
/market event clear
```

---

## Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/endex help` | Show help | `theendex.market` |
| `/endex version` | Show version info | `theendex.market` |
| `/endex reload` | Reload configuration | `theendex.admin` |
| `/endex track dump` | Print resource tracking | `theendex.admin` |

### Web UI Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/endex web` | Get web session link | `theendex.market` |
| `/endex webui export` | Export UI for customization | `theendex.admin` |
| `/endex webui reload` | Reload custom web UI | `theendex.admin` |

**Examples:**
```
/endex help
/endex version
/endex reload
/endex web
/endex webui export
```

---

## Crypto Addon Commands

If the Crypto addon is installed:

### Player Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/endex crypto help` | Show crypto help | `theendex.crypto.info` |
| `/endex crypto info` | Token details | `theendex.crypto.info` |
| `/endex crypto balance` | View balance | `theendex.crypto.balance` |
| `/endex crypto buy <amount>` | Buy tokens | `theendex.crypto.buy` |
| `/endex crypto sell <amount>` | Sell tokens | `theendex.crypto.sell` |
| `/endex crypto transfer <player> <amount>` | Transfer tokens | `theendex.crypto.transfer` |
| `/endex crypto shop` | Open token shop | `theendex.crypto.shop` |

### Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/endex crypto admin setprice <value>` | Set token price | `theendex.crypto.admin` |
| `/endex crypto admin mint <player> <amount>` | Give tokens | `theendex.crypto.admin` |
| `/endex crypto admin burn <player> <amount>` | Remove tokens | `theendex.crypto.admin` |
| `/endex crypto admin limits set <type> <value>` | Set limits | `theendex.crypto.admin` |
| `/endex crypto admin limits get` | View limits | `theendex.crypto.admin` |
| `/endex crypto admin reload` | Reload crypto config | `theendex.crypto.admin` |

**Aliases:** `/cc`, `/crypto`

---

## Tab Completion

All commands support tab completion:

- Material names auto-complete
- Amounts suggest common values
- Subcommands show available options

---

## Command Aliases

| Alias | Full Command |
|-------|--------------|
| `/m` | `/market` |
| `/endex m` | `/endex market` |
| `/cc` | `/endex crypto` |
| `/crypto` | `/endex crypto` |

---

## Notes

### Material Names

Use Bukkit material names (case-insensitive):

```
diamond, DIAMOND, Diamond  ✓ All work
iron_ingot, IRON_INGOT     ✓ Underscores required
```

### Amount Limits

- Minimum: 1
- Maximum: Configurable (default varies)
- Holdings limited by `max-per-player`

### Error Messages

Common errors and meanings:

| Error | Cause |
|-------|-------|
| "Not enough funds" | Insufficient balance |
| "Holdings full" | Hit max-per-player limit |
| "Item not tradeable" | Material is blacklisted |
| "Not enough items" | Trying to sell more than you have |

---

## Related Pages

- [Permissions](permissions.md) — Required permissions
- [Configuration](configuration.md) — Customize limits
- [Market GUI](../features/market-gui.md) — Visual alternative
