---
title: "REST API"
description: "Dokumentation der HTTP-API für Entwickler"
---

# REST API

The Endex stellt eine RESTful API bereit, die es externen Entwicklern ermöglicht, mit den Marktdaten zu interagieren. Nützlich für die Erstellung von Statistik-Websites, Discord-Bots oder mobilen Apps.

## Endpunkt (Basis-URL)

`http://<server-ip>:<port>/api/v1`

## Endpunkte

### Alle Gegenstände abrufen

`GET /items`

Gibt die Liste aller verfolgten Gegenstände zurück.

**Antwort:**
```json
[
  {
    "id": "DIAMOND",
    "name": "Diamant",
    "price": 105.50,
    "basePrice": 100.0,
    "trend": "UP",
    "change": 5.5
  },
  {
    "id": "GOLD_INGOT",
    "name": "Goldbarren",
    "price": 48.20,
    "basePrice": 50.0,
    "trend": "DOWN",
    "change": -3.6
  }
]
```

### Einen bestimmten Gegenstand abrufen

`GET /items/{id}`

**Beispiel:** `GET /items/DIAMOND`

**Antwort:**
```json
{
  "id": "DIAMOND",
  "name": "Diamant",
  "price": 105.50,
  "history": [
    {"time": 1620000000, "price": 100.0},
    {"time": 1620003600, "price": 102.0},
    {"time": 1620007200, "price": 105.5}
  ]
}
```

### Marktstatus abrufen

`GET /status`

Gibt den globalen Status des Marktes zurück (laufende Ereignisse usw.).

**Antwort:**
```json
{
  "status": "OPEN",
  "activeEvents": [],
  "lastUpdate": 1620007200
}
```

## Authentifizierung

Derzeit ist die API standardmäßig öffentlich und schreibgeschützt. Zukünftige Versionen werden eine API-Schlüssel-Authentifizierung für Schreibaktionen (POST/PUT) enthalten.

## Ratenbegrenzung (Rate Limiting)

Es gibt keine fest codierte Ratenbegrenzung, aber bitte nutzen Sie gesunden Menschenverstand und cachen Sie Ergebnisse, wenn Sie viele Anfragen stellen.
