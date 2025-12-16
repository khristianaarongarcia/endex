# Troubleshooting

Common issues and solutions for The Endex.

---

## Installation Issues

### Plugin Won't Load

**Symptoms:** Plugin not in `/plugins` list, no Endex folder created

**Solutions:**
1. Check server version (1.20.1 - 1.21.x required)
2. Verify JAR is in `plugins/` folder
3. Check for errors in startup log:
   ```
   grep -i "endex" logs/latest.log
   ```
4. Ensure Vault is installed

---

### Missing Dependency Errors

**Error:** `Vault not found` or economy errors

**Solutions:**
1. Install [Vault](https://www.spigotmc.org/resources/vault.34315/)
2. Install an economy plugin (EssentialsX, CMI, etc.)
3. Restart server

---

### Config Errors on Startup

**Error:** `Invalid configuration` or YAML errors

**Solutions:**
1. Check `config.yml` for syntax errors
2. Use a YAML validator
3. Delete config and let it regenerate
4. Check for tab characters (use spaces only)

---

## Trading Issues

### "Item Not Tracked" Error

**Cause:** Trying to trade an item not in the market

**Solutions:**
1. Check tracked items: `/endex list`
2. Add item in `config.yml`:
   ```yaml
   market:
     items:
       MATERIAL_NAME:
         basePrice: 10.0
   ```
3. Reload: `/endex reload`

---

### Transaction Failed

**Possible causes:**
- Insufficient balance (buying)
- Item not in inventory (selling)
- Full inventory (buying)
- Price limits reached

**Debug:**
```
/bal                    # Check balance
/endex price <item>     # Check current price
```

---

### Holdings Not Working

**Check configuration:**
```yaml
holdings:
  enabled: true
  maxItems: 10000
  maxMaterials: 100
```

**Verify permissions:**
- `endex.holdings.view`
- `endex.holdings.withdraw`

---

## Web Dashboard Issues

### Dashboard Won't Load

**Checklist:**
1. Web server enabled?
   ```yaml
   web:
     enabled: true
     port: 8080
   ```
2. Port not blocked by firewall?
3. Try `localhost:8080` first

**Debug:**
```bash
curl http://localhost:8080/api/items
```

---

### Session Link Not Working

**Error:** "Invalid or expired session"

**Solutions:**
1. Generate new link: `/endex session`
2. Check session timeout in config
3. Verify web server is running

---

### WebSocket Connection Failed

**Symptoms:** Prices not updating in real-time

**Solutions:**
1. Check WebSocket port matches web port
2. Try Server-Sent Events (SSE) instead
3. Check reverse proxy configuration
4. Disable ad blockers

---

### HTTPS/SSL Issues

**For reverse proxy setup:**
1. Use proper SSL termination at proxy
2. Set trusted proxies:
   ```yaml
   web:
     api:
       trusted-proxies: ["127.0.0.1"]
   ```
3. See [Reverse Proxy Guide](../reference/configuration.md)

---

## Performance Issues

### High Memory Usage

**Solutions:**
1. Reduce tracked items
2. Lower price history limit:
   ```yaml
   priceHistory:
     maxHistorySize: 1000
   ```
3. Reduce web polling interval

---

### Lag Spikes on Price Updates

**Solutions:**
1. Increase tick interval:
   ```yaml
   market:
     tickInterval: 120  # seconds
   ```
2. Reduce number of tracked items
3. Disable unnecessary features

---

### Database Errors

**SQLite issues:**
1. Check disk space
2. Verify write permissions
3. Backup and reset database:
   ```
   plugins/TheEndex/data/market.db
   ```

---

## GUI Issues

### GUI Not Opening

**Checklist:**
1. Permission: `endex.gui`
2. No errors in console
3. Try: `/market` or `/endex gui`

---

### Items Not Displaying

**Solutions:**
1. Check items are tracked
2. Verify GUI configuration
3. Check for material name errors

---

### Click Actions Not Working

**Paper servers:**
This is typically caused by InventoryView API changes.

**Solution:** Update to latest Endex version (1.5.0+)

---

## Command Issues

### Commands Not Found

**Checklist:**
1. Plugin loaded: `/plugins`
2. Correct syntax: `/endex help`
3. Check aliases in `plugin.yml`

---

### Permission Denied

**Solutions:**
1. Check permissions:
   ```
   /perm player <name> check endex.*
   ```
2. Add permissions via your permission plugin
3. OP players have all permissions by default

---

## Events Issues

### Market Events Not Triggering

**Check configuration:**
```yaml
events:
  enabled: true
  events:
    market_crash:
      enabled: true
      chance: 5
```

**Verify:**
- Event file syntax is correct
- Conditions are meetable

---

### Events Affecting Wrong Items

**Check event configuration:**
```yaml
affectedItems:
  - DIAMOND
  - EMERALD
```

Use exact material names from `/endex list`

---

## Addon Issues

### Addon Not Loading

**Checklist:**
1. JAR in `plugins/TheEndex/addons/`
2. Compatible Endex version
3. No dependency errors

**Debug:**
```
grep -i "addon" logs/latest.log
```

---

### Addon Commands Not Working

**Verify:**
```
/endex addons              # List loaded addons
/endex <addon> help        # Addon help
```

---

## Getting Help

### Before Asking for Help

1. Check this troubleshooting guide
2. Review console errors
3. Verify configuration syntax
4. Test with minimal config

### Reporting Issues

Include:
- Server version (`/version`)
- Endex version
- Full error message/stack trace
- Steps to reproduce
- Relevant config sections

### Support Channels

- GitHub Issues (bugs)
- Discord (questions)
- SpigotMC discussion

---

## Related Pages

- [Configuration](../reference/configuration.md) — Config reference
- [Changelog](changelog.md) — Version history
- [Installation](../getting-started/installation.md) — Setup guide
