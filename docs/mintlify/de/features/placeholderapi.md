---
title: "PlaceholderAPI"
description: "Verwendung von The Endex Platzhaltern"
---

# PlaceholderAPI

The Endex unterstützt **PlaceholderAPI** (PAPI) vollständig, sodass Sie Marktinformationen überall anzeigen können (Scoreboards, Chat, Tablist, Hologramme, DeluxeMenus-Menüs usw.).

## Installation

Stellen Sie sicher, dass Sie Folgendes haben:
1. PlaceholderAPI installiert.
2. The Endex installiert.
3. Führen Sie `/papi ecloud download TheEndex` aus (falls auf ecloud verfügbar) oder laden Sie PAPI einfach mit `/papi reload` neu, da The Endex seine eigenen Platzhalter registriert.

## Liste der Platzhalter

### Gegenstands-Platzhalter

Ersetzen Sie `<item>` durch die ID des Gegenstands (z. B. `DIAMOND`, `OAK_LOG`).

| Platzhalter | Beschreibung |
|-------------|--------------|
| `%endex_price_<item>%` | Aktueller Preis des Gegenstands. |
| `%endex_price_formatted_<item>%` | Formatierter Preis mit Währung (z. B. $100.50). |
| `%endex_change_<item>%` | Preisänderung in Prozent (z. B. +5.2%). |
| `%endex_trend_<item>%` | Trendsymbol (z. B. 📈 oder 📉). |
| `%endex_base_price_<item>%` | Konfigurierter Basispreis. |
| `%endex_volume_<item>%` | Aktuelles Transaktionsvolumen. |

### Spieler-Platzhalter

| Platzhalter | Beschreibung |
|-------------|--------------|
| `%endex_holdings_value%` | Gesamtwert des Portfolios des Spielers. |
| `%endex_holdings_count_<item>%` | Anzahl der Bestände für einen bestimmten Gegenstand. |
| `%endex_profit_loss%` | Gesamtgewinn/-verlust des Spielers (P&L). |

### Globale Platzhalter

| Platzhalter | Beschreibung |
|-------------|--------------|
| `%endex_top_gainer%` | Name des Gegenstands mit dem größten Anstieg. |
| `%endex_top_loser%` | Name des Gegenstands mit dem größten Rückgang. |
| `%endex_market_status%` | Marktstatus (Offen/Geschlossen/Crash). |

## Anwendungsbeispiele

**In einem Scoreboard:**
```yaml
lines:
  - "&6Markt:"
  - " Diamant: %endex_price_formatted_DIAMOND% %endex_trend_DIAMOND%"
  - " Gold: %endex_price_formatted_GOLD_INGOT% %endex_trend_GOLD_INGOT%"
  - "&ePortfolio: $%endex_holdings_value%"
```

**In einem Hologramm:**
```text
Bitcoin Preis (Virtuell)
%endex_price_formatted_BTC%
```
