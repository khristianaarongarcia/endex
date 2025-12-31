---
title: "API REST"
description: "Documentation de l'API HTTP pour les développeurs"
---

# API REST

The Endex expose une API RESTful permettant aux développeurs externes d'interagir avec les données du marché. Utile pour créer des sites web de statistiques, des bots Discord, ou des applications mobiles.

## Point de Terminaison (Base URL)

`http://<ip-serveur>:<port>/api/v1`

## Endpoints

### Récupérer tous les objets

`GET /items`

Renvoie la liste de tous les objets suivis.

**Réponse :**
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
    "name": "Lingot d'Or",
    "price": 48.20,
    "basePrice": 50.0,
    "trend": "DOWN",
    "change": -3.6
  }
]
```

### Récupérer un objet spécifique

`GET /items/{id}`

**Exemple :** `GET /items/DIAMOND`

**Réponse :**
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

### Récupérer le statut du marché

`GET /status`

Renvoie l'état global du marché (événements en cours, etc.).

**Réponse :**
```json
{
  "status": "OPEN",
  "activeEvents": [],
  "lastUpdate": 1620007200
}
```

## Authentification

Actuellement, l'API est en lecture seule et publique par défaut. Les versions futures incluront une authentification par clé API pour les actions d'écriture (POST/PUT).

## Rate Limiting

Il n'y a pas de limite de taux stricte codée en dur, mais veuillez faire preuve de bon sens et mettre en cache les résultats si vous faites beaucoup de requêtes.
