# The Endex — HTTP API

Base URL: `http://<host>:<port>` (defaults: 127.0.0.1:3434)

Auth:
- Provide `?session=TOKEN` from an in-game generated link, or
- Provide `Authorization: Bearer <TOKEN>` (read-only tokens via `web.api.tokens`), or
- Provide `?token=TOKEN` (read-only)

Rate limiting:
- Configurable per-session. The built-in UI sets `X-Endex-UI: 1` and can be exempted by config.

Notes:
- JSON responses unless specified. `ETag`/`If-None-Match` supported on some endpoints.
- WebSocket and SSE are optional; enable in config.

---

## GET /
Serves the web UI. Requires `?session=TOKEN` to load.

## GET /api/session
Returns session info, UI flags, branding, and feature configuration used by the web client.

Example:
```json
{
  "serverName": "The Endex",
  "role": "TRADER",
  "iconsEnabled": true,
  "theme": {"primary": "#a78bfa", "accent": "#7c3aed", "bg": "#0f0f23", "fg": "#e8e9ea"},
  "pollMs": 1000,
  "historyLimit": 120,
  "invHoldingsEnabled": true
}
```

## GET /api/items
List current items with prices and stats used in the UI.

## GET /api/balance
Returns player balance (Vault) and currency symbol.

## GET /api/watchlist
Returns player watchlist.

## POST /api/watchlist/add
Body: `{ "material": "IRON_INGOT" }`

## POST /api/watchlist/remove
Body: `{ "material": "IRON_INGOT" }`

## GET /api/holdings
Returns player-owned quantities per material.

## GET /api/holdings/combined
Returns per-material holdings merged from investments (DB) and live inventory snapshots.

Notes:
- Requires inventory snapshots feature to be enabled in config (see `web.holdings.inventory.enabled`).
- Live inventory is scanned for online players only and cached for `cache-seconds`.

Response (array):
```json
[
  {
    "material": "IRON_INGOT",
    "quantity": 96,
    "investQty": 64,
    "invQty": 32,
    "avgCost": 12.5,
    "currentPrice": 13.1
  }
]
```

## GET /api/holdings/{uuid}
Admin-only endpoint to retrieve combined holdings for the specified player UUID.

Auth:
- Caller must have the permission specified in `web.roles.admin-view-permission`.

Response: same shape as `/api/holdings/combined`.

## GET /api/receipts?limit=N
Recent trades list. `limit` optional.

## POST /api/buy
Body: `{ "material": "IRON_INGOT", "amount": 5 }`

## POST /api/sell
Body: `{ "material": "IRON_INGOT", "amount": 5 }`

## GET /api/history?material=MAT&limit=N&windowSec=S&bucketSec=S&since=TS
Returns price history points. Supports ETag; returns 304 when unchanged.
- `material` (required): Bukkit `Material` name
- `limit` (optional, default from config)
- `windowSec` (optional): sliding window; overrides limit when > 0
- `bucketSec` (optional): aggregate into buckets (average)
- `since` (optional): return only points newer than timestamp

Response: `[{"timestamp": 1693240000, "price": 12.34}, ...]`

## GET /api/sse
Server-Sent Events (if enabled). Public read-only price tick feed.

## WS /api/ws?session=TOKEN
WebSocket (if enabled). Auth via `?session=`. Messages include periodic `tick` payloads mirroring the polling response.

## GET /icon/{material}
PNG icon for a Bukkit `Material` from the configured resource pack. Public, no auth.
Behavior:
- Searches `assets/minecraft/textures/{item,items,block,blocks}/<name>.png` with lowercase material name.
- Includes some aliases for common pack variations (e.g., PUMPKIN, CACTUS).
- Returns `ETag` based on file name + last modified; responds `304` when unchanged.

## GET /api/addons
Returns a string array of addon names for the UI Addons tab. Requires auth.

## GET /api/inventory-totals
Exposes aggregated online inventory totals per material (when inventory snapshots are enabled).

Response example:
```json
{
  "onlinePlayers": 7,
  "totals": {
    "IRON_INGOT": 512,
    "DIAMOND": 23
  }
}
```

---

## Config keys affecting the API

```yaml
web:
  host: 127.0.0.1
  port: 3434
  sse.enabled: false
  websocket.enabled: true
  rate-limit:
    enabled: true
    requests: 30
    per-seconds: 10
    exempt-ui: true
  icons:
    enabled: true
    source: "" # zip, folder, or URL; blank reuses extracted pack
  addons: []
  api:
    tokens: [] # read-only bearer tokens
  holdings:
    inventory:
      enabled: true
      include-enderchest: false
      cache-seconds: 15
logging.verbose: false
```

---

## Examples

PowerShell curl examples (replace token):

```powershell
# Session info
curl "http://127.0.0.1:3434/api/session?session=$env:ENDEX_TOKEN"

# Items
curl "http://127.0.0.1:3434/api/items?session=$env:ENDEX_TOKEN"

# History (last 120)
curl "http://127.0.0.1:3434/api/history?session=$env:ENDEX_TOKEN&material=IRON_INGOT&limit=120"

# Icon (public)
curl "http://127.0.0.1:3434/icon/IRON_INGOT" -OutFile iron_ingot.png
```
