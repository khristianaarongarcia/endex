---
title: "FAQ"
description: "Common questions about trading, holdings, and the web dashboard."
---

# FAQ

## What Minecraft versions are supported?

The Endex targets modern Paper/Spigot and Java 17. See the project README for the exact supported MC versions.

## Where do purchased items go?

Most servers use the recommended flow:

- Purchases → **Holdings** (virtual)

If your server is configured in legacy mode, purchases may go directly into inventory.

## I bought items but didn’t receive them

Check the delivery queue:

```text
/market delivery list
```

If your inventory/holdings were full, overflow may be waiting in Delivery.

## The web dashboard says my session is invalid

- Generate a new session link/token from the server command.
- Ensure the web server is enabled and reachable.

## Can I expose the web panel to the internet?

Yes, but do it safely:

- Put it behind HTTPS (reverse proxy)
- Don’t share session links
- Consider additional proxy auth if needed

See `docs/REVERSE_PROXY.md`.
