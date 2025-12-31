---
title: "Befehle"
description: "Vollständige Liste der Befehle und deren Verwendung"
---

# Befehle

Hier ist die vollständige Liste der in The Endex verfügbaren Befehle.

## Spieler-Befehle

Diese Befehle sind in der Regel für alle Spieler zugänglich (abhängig von Berechtigungen).

| Befehl | Alias | Beschreibung | Berechtigung |
|--------|-------|--------------|--------------|
| `/endex` | `/market` | Öffnet das Hauptmarkt-Menü. | `endex.use` |
| `/endex help` | | Zeigt die Hilfe an. | `endex.help` |
| `/endex price <item>` | | Zeigt den aktuellen Preis eines Gegenstands an. | `endex.price` |
| `/endex buy <item> <menge>` | | Kauft einen Gegenstand zum Marktpreis. | `endex.buy` |
| `/endex sell <item> <menge>` | | Verkauft einen Gegenstand zum Marktpreis. | `endex.sell` |
| `/endex sellall` | | Verkauft das gesamte kompatible Inventar. | `endex.sellall` |
| `/holdings` | `/portfolio` | Öffnet das Portfolio-Menü. | `endex.holdings` |
| `/endex invest <item> <menge>` | | Kauft Bestände (virtuell). | `endex.invest` |
| `/endex divest <item> <menge>` | | Verkauft Bestände (virtuell). | `endex.divest` |

## Admin-Befehle

Diese Befehle sind Administratoren und Operatoren vorbehalten.

| Befehl | Beschreibung | Berechtigung |
|--------|--------------|--------------|
| `/endex admin reload` | Lädt die Konfiguration neu. | `endex.admin.reload` |
| `/endex admin additem <preis>` | Fügt den gehaltenen Gegenstand zum Markt hinzu. | `endex.admin.additem` |
| `/endex admin removeitem <item>` | Entfernt einen Gegenstand vom Markt. | `endex.admin.removeitem` |
| `/endex admin setprice <item> <preis>` | Erzwingt den Preis eines Gegenstands. | `endex.admin.setprice` |
| `/endex admin setvolatility <item> <wert>` | Ändert die Volatilität eines Gegenstands. | `endex.admin.setvolatility` |
| `/endex admin event start <event>` | Erzwingt den Start eines Ereignisses. | `endex.admin.event` |
| `/endex admin event stop` | Stoppt das laufende Ereignis. | `endex.admin.event` |
| `/endex admin debug` | Aktiviert den Debug-Modus in der Konsole. | `endex.admin.debug` |

## Argumente

- `<item>`: Der interne Name des Gegenstands (z. B. `DIAMOND`, `IRON_INGOT`). Muss mit `items.yml` übereinstimmen.
- `<menge>`: Die Menge (Ganzzahl).
- `<preis>`: Der Preis (Dezimalzahl).
