---
title: "REST API"
description: "Complete REST API reference for The Endex."
---
# REST API

Complete HTTP API reference for The Endex.

---

## Base URL

```
http://<host>:<port>
```

Default: `http://127.0.0.1:8080`

---

## Authentication

### Session Token

From in-game session link:

```
GET /api/items?session=TOKEN
```

Or via header:

```
Authorization: Bearer TOKEN
```

### API Token

For automated access:

```
Authorization: Bearer API_TOKEN
```

Configure in `config.yml`:
```yaml
web:
  api:
    tokens: ["your-api-token"]
    token-hashes: ["sha256-hash"]  # Recommended
```

---

## Response Format

All responses are JSON:

```json
{
  "success": true,
  "data": { ... }
}
```

Error responses:

```json
{
  "success": false,
  "error": "Error message"
}
```

---

## Endpoints

### Session Info

Get current session details:

```http
GET /api/session
```

**Response:**
```json
{
  "serverName": "The Endex",
  "role": "TRADER",
  "iconsEnabled": true,
  "theme": {
    "primary": "#a78bfa",
    "accent": "#7c3aed",
    "bg": "#0f0f23",
    "fg": "#e8e9ea"
  },
  "pollMs": 1000,
  "historyLimit": 120,
  "invHoldingsEnabled": true
}
```

---

### Market Data

#### List Items

```http
GET /api/items
```

**Response:**
```json
{
  "items": [
    {
      "material": "DIAMOND",
      "price": 100.50,
      "change": 5.2,
      "basePrice": 100.0,
      "minPrice": 10.0,
      "maxPrice": 1000.0
    }
  ]
}
```

#### Price History

```http
GET /api/history?material=DIAMOND&limit=120
```

**Parameters:**
- `material` (required) — Bukkit material name
- `limit` (optional) — Number of points (default: config value)
- `windowSec` (optional) — Time window in seconds
- `bucketSec` (optional) — Aggregate into buckets
- `since` (optional) — Timestamp filter

**Response:**
```json
{
  "history": [
    {"timestamp": 1693240000, "price": 98.50},
    {"timestamp": 1693240060, "price": 99.25},
    {"timestamp": 1693240120, "price": 100.50}
  ]
}
```

---

### Trading

#### Buy Items

```http
POST /api/buy
Content-Type: application/json

{
  "material": "DIAMOND",
  "amount": 64
}
```

**Response:**
```json
{
  "success": true,
  "message": "Purchased 64 DIAMOND for $6,432.00",
  "newBalance": 43568.00
}
```

#### Sell Items

```http
POST /api/sell
Content-Type: application/json

{
  "material": "DIAMOND",
  "amount": 32
}
```

**Response:**
```json
{
  "success": true,
  "message": "Sold 32 DIAMOND for $3,136.00",
  "newBalance": 46704.00
}
```

---

### Holdings

#### Get Holdings

```http
GET /api/holdings
```

**Response:**
```json
{
  "holdings": [
    {
      "material": "DIAMOND",
      "quantity": 128,
      "avgCost": 95.00,
      "currentPrice": 100.50
    }
  ],
  "totalValue": 12864.00,
  "totalCost": 12160.00,
  "totalPL": 704.00
}
```

#### Combined Holdings

Includes inventory items:

```http
GET /api/holdings/combined
```

**Response:**
```json
{
  "holdings": [
    {
      "material": "DIAMOND",
      "quantity": 192,
      "investQty": 128,
      "invQty": 64,
      "avgCost": 95.00,
      "currentPrice": 100.50
    }
  ]
}
```

#### Holdings Stats

```http
GET /api/holdings/stats
```

**Response:**
```json
{
  "totalItems": 448,
  "totalMaterials": 12,
  "maxItems": 10000,
  "maxMaterials": 100,
  "totalValue": 45280.00
}
```

#### Withdraw Items

```http
POST /api/holdings/withdraw
Content-Type: application/json

{
  "material": "DIAMOND",
  "amount": 64
}
```

Or withdraw all of one material:
```json
{
  "material": "DIAMOND"
}
```

Or withdraw everything:
```json
{
  "material": "ALL"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Withdrew 64 DIAMOND to inventory",
  "withdrawn": 64
}
```

#### View Other Player (Admin)

```http
GET /api/holdings/{uuid}
```

Requires `endex.web.admin` permission.

---

### Deliveries

#### List Deliveries

```http
GET /api/deliveries
```

**Response:**
```json
{
  "enabled": true,
  "deliveries": [
    {"material": "DIAMOND", "amount": 40},
    {"material": "EMERALD", "amount": 128}
  ],
  "total": 168
}
```

#### Claim Deliveries

Claim specific:
```http
POST /api/deliveries/claim
Content-Type: application/json

{
  "material": "DIAMOND",
  "amount": 40
}
```

Claim all:
```http
POST /api/deliveries/claim
Content-Type: application/json

{}
```

---

### Player Data

#### Get Balance

```http
GET /api/balance
```

**Response:**
```json
{
  "balance": 45280.50,
  "currency": "$"
}
```

#### Trade History

```http
GET /api/receipts?limit=50
```

**Response:**
```json
{
  "receipts": [
    {
      "type": "BUY",
      "material": "DIAMOND",
      "amount": 64,
      "price": 100.50,
      "total": 6432.00,
      "timestamp": 1693240120
    }
  ]
}
```

#### Watchlist

```http
GET /api/watchlist
```

```http
POST /api/watchlist/add
{"material": "DIAMOND"}
```

```http
POST /api/watchlist/remove
{"material": "DIAMOND"}
```

---

### Inventory Totals

Aggregated online player inventories:

```http
GET /api/inventory-totals
```

**Response:**
```json
{
  "onlinePlayers": 7,
  "totals": {
    "DIAMOND": 512,
    "IRON_INGOT": 2048
  }
}
```

---

### Icons

Get item icon:

```http
GET /icon/{material}
```

Returns PNG image. No authentication required.

**Example:**
```
GET /icon/DIAMOND
```

---

### Addons

List installed addons:

```http
GET /api/addons
```

**Response:**
```json
{
  "addons": ["crypto", "sample"]
}
```

---

## Real-Time Updates

### WebSocket

Connect for live updates:

```javascript
const ws = new WebSocket('ws://localhost:8080/ws?session=TOKEN');

ws.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('Update:', data);
};
```

**Messages:**
```json
{
  "type": "tick",
  "items": [...],
  "timestamp": 1693240120
}
```

### Server-Sent Events

Alternative to WebSocket:

```javascript
const sse = new EventSource('/sse?session=TOKEN');

sse.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('Update:', data);
};
```

---

## Rate Limiting

Default limits:
- 30 requests per 10 seconds
- Built-in UI exempted

Headers returned:
```
X-RateLimit-Limit: 30
X-RateLimit-Remaining: 25
X-RateLimit-Reset: 1693240130
```

---

## Errors

| Code | Meaning |
|------|---------|
| `400` | Bad request (invalid params) |
| `401` | Unauthorized (bad token) |
| `403` | Forbidden (no permission) |
| `404` | Not found |
| `429` | Rate limited |
| `500` | Server error |

---

## Examples

### cURL

```bash
# Get items
curl "http://localhost:8080/api/items" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Buy items
curl "http://localhost:8080/api/buy" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"material":"DIAMOND","amount":10}'

# Withdraw
curl "http://localhost:8080/api/holdings/withdraw" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"material":"DIAMOND","amount":64}'
```

### JavaScript

```javascript
const API = 'http://localhost:8080';
const TOKEN = 'your-session-token';

async function buyItem(material, amount) {
  const res = await fetch(`${API}/api/buy`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${TOKEN}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ material, amount })
  });
  return res.json();
}
```

### Python

```python
import requests

API = 'http://localhost:8080'
TOKEN = 'your-session-token'

def get_items():
    r = requests.get(
        f'{API}/api/items',
        headers={'Authorization': f'Bearer {TOKEN}'}
    )
    return r.json()

def buy_item(material, amount):
    r = requests.post(
        f'{API}/api/buy',
        headers={'Authorization': f'Bearer {TOKEN}'},
        json={'material': material, 'amount': amount}
    )
    return r.json()
```

---

## Related Pages

- [Web Dashboard](dashboard.md) — Browser interface
- [Developer API](../developers/api.md) — Java/Kotlin API
- [Configuration](../reference/configuration.md) — API config
