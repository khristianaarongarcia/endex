---
title: "Berechtigungen"
description: "Liste der Berechtigungsknoten"
---

# Berechtigungen

Verwalten Sie den Zugriff auf die Funktionen von The Endex mit diesen Berechtigungen.

## Benutzer-Berechtigungen

Diese Berechtigungen werden standardmäßig für Spieler empfohlen.

| Berechtigung | Beschreibung | Empfohlen |
|--------------|--------------|-----------|
| `endex.use` | Erlaubt die Nutzung des Plugins und das Öffnen des GUI. | **Ja** |
| `endex.help` | Erlaubt das Anzeigen der Hilfe. | **Ja** |
| `endex.price` | Erlaubt das Überprüfen von Preisen. | **Ja** |
| `endex.buy` | Erlaubt das Kaufen von Gegenständen. | **Ja** |
| `endex.sell` | Erlaubt das Verkaufen von Gegenständen. | **Ja** |
| `endex.sellall` | Erlaubt die Nutzung von `/endex sellall`. | **Ja** |
| `endex.holdings` | Erlaubt den Zugriff auf das Portfolio. | **Ja** |
| `endex.invest` | Erlaubt das Investieren (Bestände kaufen). | **Ja** |
| `endex.divest` | Erlaubt das Desinvestieren (Bestände verkaufen). | **Ja** |
| `endex.history` | Erlaubt das Anzeigen der Preishistorie. | **Ja** |

## Administrator-Berechtigungen

⚠️ **Warnung**: Geben Sie diese Berechtigungen nur vertrauenswürdigen Administratoren.

| Berechtigung | Beschreibung |
|--------------|--------------|
| `endex.admin.*` | Gibt alle Admin-Berechtigungen. |
| `endex.admin.reload` | Erlaubt das Neuladen der Konfiguration. |
| `endex.admin.additem` | Erlaubt das Hinzufügen von Gegenständen zum Markt. |
| `endex.admin.removeitem` | Erlaubt das Entfernen von Gegenständen. |
| `endex.admin.setprice` | Erlaubt das Manipulieren von Preisen. |
| `endex.admin.event` | Erlaubt das Starten/Stoppen von Ereignissen. |
| `endex.admin.debug` | Erlaubt das Anzeigen von Debug-Infos. |
| `endex.admin.update` | Empfängt Update-Benachrichtigungen. |

## Spezielle Berechtigungen

| Berechtigung | Beschreibung |
|--------------|--------------|
| `endex.bypass.tax` | Der Spieler zahlt keine Steuern auf Bestände. |
| `endex.bypass.limit` | Der Spieler hat kein Bestands-Limit. |
