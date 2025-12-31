---
title: "Lieferung"
description: "Physische Abholung virtueller Investitionen"
---

# Liefersystem

Das **Liefersystem** schlägt eine Brücke zwischen dem [Virtuellen Portfolio](/de/features/holdings) und dem physischen Inventar. Es ermöglicht Spielern, ihre virtuellen Bestände in reale Gegenstände umzuwandeln.

## Konzept

Wenn ein Spieler 100 "Eisen-Beteiligungen" besitzt und Eisen zum Bauen benötigt, kann er eine "Lieferung" anfordern.

1. Der Spieler wählt die abzuhebenden Bestände aus.
2. Die Bestände werden aus seinem virtuellen Portfolio entfernt.
3. Die entsprechenden physischen Gegenstände werden seinem Inventar hinzugefügt.

## Befehl

```bash
/endex deliver <item> <menge>
```

Oder über das `/holdings` Menü durch Klicken auf das Liefer-Symbol (normalerweise eine Lore oder eine Kiste).

## Liefergebühren

Um die Wirtschaft auszugleichen und den virtuellen vs. physischen Handel zu steuern, können Sie **Liefergebühren** konfigurieren.

```yaml
delivery:
  enabled: true
  fee-per-item: 0.0 # Fixkosten pro Item
  fee-percentage: 0.02 # 2% des aktuellen Wertes des Gegenstands
```

Wenn Gebühren konfiguriert sind, muss der Spieler diesen Betrag zahlen, um seine Aktien in Gegenstände umzuwandeln.

## Einschränkungen

- **Inventar voll**: Wenn das Inventar des Spielers voll ist, wird die Lieferung abgebrochen oder ist nur teilweise möglich (je nach Konfiguration).
- **Nicht lieferbare Gegenstände**: Sie können bestimmte Gegenstände in `items.yml` als "rein virtuell" (nicht lieferbar) konfigurieren, indem Sie `deliverable: false` setzen. Dies ist nützlich für "Unternehmensaktien" oder Marktindizes, die physisch nicht existieren.
