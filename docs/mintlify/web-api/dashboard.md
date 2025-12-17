---
title: "Web Dashboard"
description: "Access your market from the browser with the web dashboard."
---
# Web Dashboard

Trade from your browser with live price updates.

---

## Overview

![Web Dashboard](https://i.imgur.com/2hXIQfx.gif)

The Web Dashboard provides:
- Real-time price updates
- Trading from any device
- Price charts and history
- Holdings management
- Resource pack item icons

---

## Enabling the Dashboard

In `config.yml`:

```yaml
web:
  enabled: true
  host: "0.0.0.0"
  port: 8080
```

**Host options:**
- `0.0.0.0` — Listen on all interfaces (public)
- `127.0.0.1` — Localhost only (private)

---

## Accessing the Dashboard

### Get Session Link

In-game, run:

```
/endex web
```

You'll receive a URL like:
```
http://your-server:8080/?session=abc123xyz...
```

### Open in Browser

1. Copy the URL
2. Open in any web browser
3. Session auto-authenticates you

{% hint style="warning" %}
Session links are personal. Don't share them—anyone with the link can trade as you!
{% endhint %}

---

## Features

### Live Prices

Prices update in real-time via WebSocket:

- Green arrows for rising prices
- Red arrows for falling prices
- Percentage change displayed

### Trading

Click any item to buy or sell:

1. Select item
2. Enter amount
3. Click Buy/Sell
4. Instant feedback

### Holdings Panel

View and manage your holdings:

- Total portfolio value
- Per-item profit/loss
- Withdraw buttons (📤)
- "Withdraw All" option

### Price Charts

Click an item for detailed view:

- Price history graph
- Min/max prices
- Current trend
- Trading volume

---

## Configuration

### Basic Setup

```yaml
web:
  enabled: true
  host: "0.0.0.0"
  port: 8080
```

### Session Settings

```yaml
web:
  session:
    # How long sessions last (minutes)
    timeout-minutes: 60
```

### Real-Time Updates

```yaml
web:
  # Server-Sent Events
  sse:
    enabled: false
    
  # WebSocket (recommended)
  websocket:
    enabled: true
```

### Rate Limiting

```yaml
web:
  rate-limit:
    enabled: true
    requests: 30        # Max requests
    per-seconds: 10     # Time window
    exempt-ui: true     # Don't limit built-in UI
```

### Icons

```yaml
web:
  icons:
    enabled: true
    # Resource pack source (zip, folder, or URL)
    source: ""
```

---

## Custom UI

Override the default web interface:

### Export Default UI

```
/endex webui export
```

Creates files in `plugins/TheEndex/webui/`.

### Modify Files

Edit HTML, CSS, JavaScript:

```
plugins/TheEndex/
└── webui/
    ├── index.html
    ├── style.css
    └── app.js
```

### Enable Custom UI

```yaml
web:
  custom:
    enabled: true
    root-dir: "webui"
```

### Reload Changes

```
/endex webui reload
```

---

## Security

### Session Security

- Sessions are tied to player UUID
- Token validated on each request
- Auto-expires after timeout
- Single-use recommended for sensitive servers

### HTTPS Setup

For production, use a reverse proxy with SSL:

```nginx
server {
    listen 443 ssl;
    server_name market.yourserver.com;
    
    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}
```

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
