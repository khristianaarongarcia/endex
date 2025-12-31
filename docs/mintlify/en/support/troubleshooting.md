---
title: "Troubleshooting"
description: "Fix common installation, trading, and web dashboard issues."
---

## Plugin won’t load

Checklist:

- Server is a supported Paper/Spigot fork
- Java 17 is installed
- The jar is in `plugins/`
- Vault is installed (for economy)

Check logs for errors.

## Market GUI doesn’t open

- Verify you have permission to use market commands
- Try running:

```text
/endex
/endex market
/market
```

## Web dashboard won’t load

Checklist:

- `web.host` and `web.port` are configured
- Port is open on the host/firewall
- You’re using the correct URL

Debug using the API:

```text
GET /api/session
GET /api/items
```

## Invalid or expired session

- Generate a new session token/link
- Check session timeout settings (if configured)

## Items not appearing / missing items

- Check if the material is blacklisted
- Check delivery overflow:

```text
/market delivery list
```

## Before asking for help

- Reproduce on a clean config if possible
- Copy relevant console logs
- Include plugin version and server version
