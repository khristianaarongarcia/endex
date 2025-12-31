---
title: "REST API"
description: "Referencja HTTP API dla The Endex (autoryzacja, endpointy, przykłady)."
---

Bazowy URL:

```text
http://<host>:<port>
```

Domyślny (wg dokumentacji repozytorium):

```text
http://127.0.0.1:3434
```

## Uwierzytelnianie

Możesz uwierzytelnić się używając:

- parametru zapytania `?session=TOKEN`, lub
- nagłówka `Authorization: Bearer TOKEN`

Tokeny API mogą być skonfigurowane w `config.yml` pod `web.api.*`.

<Info>
Preferuj `web.api.token-hashes` (skróty SHA-256) nad zwykłymi tokenami.
</Info>

## Typowe endpointy

### Sesja

```http
GET /api/session
```

### Przedmioty

```http
GET /api/items
```

### Saldo

```http
GET /api/balance
```

### Zasoby

```http
GET /api/holdings
GET /api/holdings/combined
GET /api/holdings/{uuid}   # admin
```

### Handel

```http
POST /api/buy
POST /api/sell
```

### Historia

```http
GET /api/history?material=IRON_INGOT&limit=120
```

### Ikony

Ikony są publiczne (bez autoryzacji):

```http
GET /icon/{material}
```

## Limitowanie zapytań (Rate limiting)

Limitowanie zapytań może być włączone w konfiguracji:

```yaml
web:
  rate-limit:
    enabled: true
    requests: 30
    per-seconds: 10
    exempt-ui: true
```

## Przykład (PowerShell)

```powershell
# Użyj tokena sesji w zmiennej środowiskowej
$env:ENDEX_TOKEN = "<token>"

# Informacje o sesji
curl "http://127.0.0.1:3434/api/session?session=$env:ENDEX_TOKEN"

# Przedmioty
curl "http://127.0.0.1:3434/api/items?session=$env:ENDEX_TOKEN"
```

## Pełna referencja

Repozytorium zawiera dłuższą definicję API z dodatkowymi endpointami (SSE/WS, dodatki, sumy ekwipunku, ETags): zobacz `docs/API.md`.
