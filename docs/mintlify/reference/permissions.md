# Permissions

Complete reference for all The Endex permissions.

---

## Player Permissions

Basic permissions for normal players:

| Permission | Default | Description |
|------------|---------|-------------|
| `theendex.market` | `true` | Access market GUI and basic commands |
| `theendex.buy` | `true` | Buy items |
| `theendex.sell` | `true` | Sell items |
| `theendex.holdings` | `true` | View holdings |
| `theendex.withdraw` | `true` | Withdraw from holdings |
| `theendex.invest` | `true` | Use investment system |

---

## Admin Permissions

Administrative permissions:

| Permission | Default | Description |
|------------|---------|-------------|
| `theendex.admin` | `op` | All admin commands (reload, events, etc.) |

---

## Web Permissions

Web dashboard access:

| Permission | Default | Description |
|------------|---------|-------------|
| `endex.web.trade` | `false` | Access web trading interface |
| `endex.web.admin` | `op` | View other players' holdings via web |

---

## Crypto Addon Permissions

If using the Crypto addon:

### Player Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `theendex.crypto.info` | `true` | View crypto info |
| `theendex.crypto.balance` | `true` | Check balance |
| `theendex.crypto.buy` | `true` | Buy crypto tokens |
| `theendex.crypto.sell` | `true` | Sell crypto tokens |
| `theendex.crypto.transfer` | `true` | Transfer to other players |
| `theendex.crypto.shop` | `true` | Access crypto shop |

### Admin Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `theendex.crypto.admin` | `op` | All crypto admin commands |

---

## Permission Groups

### Suggested LuckPerms Setup

**Default Group (Players):**
```yaml
# All defaults are true, so no config needed for basic access
```

**VIP Group:**
```yaml
permissions:
  - endex.web.trade  # Web dashboard access
```

**Admin Group:**
```yaml
permissions:
  - theendex.admin
  - endex.web.admin
  - theendex.crypto.admin
```

### Example LuckPerms Commands

```bash
# Give web access to VIP
/lp group vip permission set endex.web.trade true

# Give admin perms
/lp group admin permission set theendex.admin true
/lp group admin permission set endex.web.admin true

# Remove buy permission from a player
/lp user <player> permission set theendex.buy false
```

---

## Restricting Access

### Disable Trading for Group

```bash
/lp group restricted permission set theendex.buy false
/lp group restricted permission set theendex.sell false
```

### Create View-Only Access

```bash
/lp group viewer permission set theendex.buy false
/lp group viewer permission set theendex.sell false
/lp group viewer permission set theendex.withdraw false
/lp group viewer permission set theendex.invest false
```

### Disable Specific Features

```bash
# No investments
/lp group <group> permission set theendex.invest false

# No withdrawals (holdings only grow)
/lp group <group> permission set theendex.withdraw false

# No crypto
/lp group <group> permission set theendex.crypto.* false
```

---

## Permission Nodes

### Wildcard Permissions

```
theendex.*           # All The Endex permissions
theendex.crypto.*    # All crypto permissions
endex.web.*          # All web permissions
```

### Negation

```bash
# Grant all but remove admin
/lp group mod permission set theendex.* true
/lp group mod permission set theendex.admin false
```

---

## Plugin.yml Defaults

From the plugin's `plugin.yml`:

```yaml
permissions:
  theendex.market:
    description: Access the market GUI
    default: true
    
  theendex.buy:
    description: Buy items from the market
    default: true
    
  theendex.sell:
    description: Sell items to the market
    default: true
    
  theendex.holdings:
    description: View virtual holdings
    default: true
    
  theendex.withdraw:
    description: Withdraw items from holdings
    default: true
    
  theendex.invest:
    description: Create and manage investments
    default: true
    
  theendex.admin:
    description: Administrative commands
    default: op
    
  endex.web.trade:
    description: Access web trading
    default: false
    
  endex.web.admin:
    description: View others' holdings on web
    default: op
```

---

## Checking Permissions

### In-Game

```
/lp user <player> permission check theendex.buy
```

### Via API

```kotlin
if (player.hasPermission("theendex.buy")) {
    // Allow purchase
}
```

---

## Troubleshooting

### "You don't have permission"

1. Check player's permissions:
   ```
   /lp user <player> permission info
   ```

2. Verify permission node spelling

3. Check for negated permissions

### Permission Not Working

1. Reload permissions:
   ```
   /lp sync
   ```

2. Player may need to relog

3. Check for conflicting plugins

---

## Related Pages

- [Commands](commands.md) — Command reference
- [Configuration](configuration.md) — Config options
- [Installation](../getting-started/installation.md) — Initial setup
