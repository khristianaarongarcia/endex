---
title: "REST API"
description: "HTTP API reference for The Endex (auth, endpoints, examples)."
---

Base URL:

```text
http://<host>:<port>
```

Default (per repo docs):

```text
http://127.0.0.1:3434
```

## Authentication

You can authenticate using either:

- `?session=TOKEN` query parameter, or
- `Authorization: Bearer TOKEN` header

API tokens can be configured in `config.yml` under `web.api.*`.

<Info>
Prefer `web.api.token-hashes` (SHA-256 digests) over plain tokens.
</Info>

## Common endpoints

### Session

```http
GET /api/session
```

### Items

```http
GET /api/items
```

### Balance

```http
GET /api/balance
```

### Holdings

```http
GET /api/holdings
GET /api/holdings/combined
GET /api/holdings/{uuid}   # admin
```

### Trading

```http
POST /api/buy
POST /api/sell
```

### History

```http
GET /api/history?material=IRON_INGOT&limit=120
```

### Icons

Icons are public (no auth):

```http
GET /icon/{material}
```

## Rate limiting

Rate limiting can be enabled in config:

```yaml
web:
  rate-limit:
    enabled: true
    requests: 30
    per-seconds: 10
    exempt-ui: true
```

## Example (PowerShell)

```powershell
# Use a session token in an env var
$env:ENDEX_TOKEN = "<token>"

# Session info
curl "http://127.0.0.1:3434/api/session?session=$env:ENDEX_TOKEN"

# Items
curl "http://127.0.0.1:3434/api/items?session=$env:ENDEX_TOKEN"
```

## Full reference

The repository includes a longer API definition with additional endpoints (SSE/WS, addons, inventory totals, ETags): see `docs/API.md`.
