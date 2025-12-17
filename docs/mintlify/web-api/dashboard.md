---
title: "Web Dashboard"
description: "Trade from a browser with live prices, holdings, and charts."
---

# Web Dashboard

The Endex includes an optional web UI for fast trading.

## Enable (server)

Web settings live in `plugins/TheEndex/config.yml`.

```yaml
web:
  enabled: true
  host: "0.0.0.0"
  port: 3434
  sse:
    enabled: false
  websocket:
    enabled: true
```

## Getting a session link

Most servers provide a command that generates a session link/token.

<Warning>
Session links are personal. Anyone with your link/token can trade as you.
</Warning>

## Reverse proxy (HTTPS)

If you want TLS, put the dashboard behind a reverse proxy (Nginx/Caddy).

See `docs/REVERSE_PROXY.md` in the repo.

## Custom UI override

You can replace the embedded UI by serving your own static files:

```yaml
web:
  custom:
    enabled: true
    root: webui
    reload: false
    export-default: true
```

See `docs/CUSTOM_WEBUI.md` for the full workflow.

## Notes

- The dashboard authenticates using a session token.
- Optional live updates are delivered via WebSocket (recommended) or SSE.

### API Tokens

For automated access (bots, integrations):

```yaml
web:
  api:
    # Plain tokens (not recommended)
    tokens:
      - "my-secret-token"
      
    # Hashed tokens (recommended)
    token-hashes:
      - "sha256-hash-of-token"
```

Generate hash:
```bash
echo -n "my-secret-token" | sha256sum
```

---

## Permissions

| Permission | Description |
|------------|-------------|
| `endex.web.trade` | Access web trading |
| `endex.web.admin` | View others' holdings |

Grant web access:
```
/lp user <player> permission set endex.web.trade true
```

---

## Mobile Access

The dashboard is mobile-responsive:

- Works on phones/tablets
- Touch-friendly interface
- Responsive layout

---

## Troubleshooting

### Can't Connect

1. Check firewall allows the port
2. Verify `host` setting
3. Try `127.0.0.1` for local testing

### Session Invalid

1. Generate new session: `/endex web`
2. Check session hasn't expired
3. Clear browser cache

### Slow Updates

1. Enable WebSocket over SSE
2. Check server TPS
3. Reduce `update-interval`

### Icons Not Loading

1. Verify resource pack source
2. Check `web.icons.enabled: true`
3. Try different resource pack

---

## Advanced

### Embedding

Embed the dashboard in your website:

```html
<iframe 
  src="http://your-server:8080/?session=TOKEN" 
  width="100%" 
  height="600">
</iframe>
```

### API Integration

See [REST API](rest-api.md) for programmatic access.

### Custom Themes

Override CSS variables:

```css
:root {
  --primary: #a78bfa;
  --accent: #7c3aed;
  --bg: #0f0f23;
  --fg: #e8e9ea;
}
```

---

## Related Pages

- [REST API](rest-api.md) — API documentation
- [Configuration](../reference/configuration.md) — Full web config
- [Installation](../getting-started/installation.md) — Initial setup
