---
title: "PlaceholderAPI"
description: "Utilisation des placeholders The Endex"
---

# PlaceholderAPI

The Endex supporte entièrement **PlaceholderAPI** (PAPI), vous permettant d'afficher les informations du marché partout (Scoreboards, Chat, Tablist, Hologrammes, Menus DeluxeMenus, etc.).

## Installation

Assurez-vous d'avoir :
1. PlaceholderAPI installé.
2. The Endex installé.
3. Exécuté `/papi ecloud download TheEndex` (si disponible sur ecloud) ou simplement rechargé PAPI `/papi reload` car The Endex enregistre ses propres placeholders.

## Liste des Placeholders

### Placeholders d'Objets

Remplacez `<item>` par l'ID de l'objet (ex: `DIAMOND`, `OAK_LOG`).

| Placeholder | Description |
|-------------|-------------|
| `%endex_price_<item>%` | Prix actuel de l'objet. |
| `%endex_price_formatted_<item>%` | Prix formaté avec la devise (ex: $100.50). |
| `%endex_change_<item>%` | Changement de prix en pourcentage (ex: +5.2%). |
| `%endex_trend_<item>%` | Symbole de tendance (ex: 📈 ou 📉). |
| `%endex_base_price_<item>%` | Prix de base configuré. |
| `%endex_volume_<item>%` | Volume de transactions récent. |

### Placeholders de Joueur

| Placeholder | Description |
|-------------|-------------|
| `%endex_holdings_value%` | Valeur totale du portefeuille du joueur. |
| `%endex_holdings_count_<item>%` | Nombre de participations pour un objet spécifique. |
| `%endex_profit_loss%` | Profit/Perte total du joueur (P&L). |

### Placeholders Globaux

| Placeholder | Description |
|-------------|-------------|
| `%endex_top_gainer%` | Nom de l'objet ayant le plus augmenté. |
| `%endex_top_loser%` | Nom de l'objet ayant le plus baissé. |
| `%endex_market_status%` | État du marché (Ouvert/Fermé/Krach). |

## Exemples d'Utilisation

**Dans un Scoreboard :**
```yaml
lines:
  - "&6Marché:"
  - " Diamant: %endex_price_formatted_DIAMOND% %endex_trend_DIAMOND%"
  - " Or: %endex_price_formatted_GOLD_INGOT% %endex_trend_GOLD_INGOT%"
  - "&ePortefeuille: $%endex_holdings_value%"
```

**Dans un Hologramme :**
```text
Prix du Bitcoin (Virtuel)
%endex_price_formatted_BTC%
```
