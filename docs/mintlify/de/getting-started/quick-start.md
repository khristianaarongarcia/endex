---
title: "Schnellstart"
description: "Richten Sie Ihre erste Marktwirtschaft in wenigen Minuten ein"
---

# Schnellstart

Diese Anleitung hilft Ihnen, Ihre erste dynamische Wirtschaft mit The Endex einzurichten.

## 1. Gegenstände zum Markt hinzufügen

Damit ein Gegenstand einen dynamischen Preis hat, muss er zu The Endex hinzugefügt werden.

### Im Spiel

Halten Sie den Gegenstand, den Sie hinzufügen möchten, in Ihrer Haupthand und geben Sie ein:

```bash
/endex admin additem <startpreis>
```

Um beispielsweise einen Diamanten mit einem Basispreis von 100 hinzuzufügen:

```bash
/endex admin additem 100
```

### Über Konfiguration

Sie können Gegenstände auch manuell in `items.yml` hinzufügen:

```yaml
DIAMOND:
  base-price: 100.0
  min-price: 10.0
  max-price: 1000.0
  volatility: 0.5
```

## 2. Ein Marktschild erstellen (Optional)

Wenn Sie Schilder verwenden möchten, um Preise anzuzeigen:

1. Platzieren Sie ein Schild.
2. Schreiben Sie in die erste Zeile `[Endex]`.
3. Schreiben Sie in die zweite Zeile den Namen des Gegenstands (z. B. `DIAMOND`).

Das Schild aktualisiert sich automatisch mit dem aktuellen Preis.

## 3. Das GUI-Menü öffnen

Spieler können alle Marktgegenstände und deren Trends über das Hauptmenü sehen:

```bash
/market
```

## 4. Transaktionen simulieren

Um Preisschwankungen zu testen, können Sie Käufe und Verkäufe simulieren:

- **Kaufen** lässt den Preis steigen (Nachfrage steigt).
- **Verkaufen** lässt den Preis fallen (Angebot steigt).

Versuchen Sie, mehrere Stacks eines Gegenstands zu kaufen, und beobachten Sie, wie sich der Preis ändert!

## Nächste Schritte

Nachdem die Grundlagen gelegt sind, erkunden Sie die erweiterten Funktionen:

- [Marktereignisse konfigurieren](/de/features/events)
- [Web-Dashboard einrichten](/de/web-api/dashboard)
- [Das Holdings-System verstehen](/de/features/holdings)
