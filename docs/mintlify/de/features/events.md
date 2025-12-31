---
title: "Marktereignisse"
description: "Zufällige und geplante Ereignisse, die die Wirtschaft beeinflussen"
---

# Marktereignisse

Marktereignisse bringen Unvorhersehbarkeit und Spannung in die Wirtschaft. Sie können plötzliche Preisanstiege (Bullenmarkt) oder Zusammenbrüche (Bärenmarkt/Crash) verursachen.

## Arten von Ereignissen

### 1. Börsencrash (Market Crash)
Alle Preise oder eine bestimmte Gruppe von Gegenständen fallen drastisch.
- **Ursache**: Panik, Überproduktion oder Zufall.
- **Effekt**: Preise fallen um X%.

### 2. Marktboom (Market Boom)
Preise steigen schnell an.
- **Ursache**: Knappheit, hohe Nachfrage oder Zufall.
- **Effekt**: Preise steigen um X%.

### 3. Spezifische Ereignisse
Betreffen einen einzelnen Gegenstand oder eine Kategorie.
- *Beispiel: "Neue Goldader entdeckt!" -> Goldpreis fällt.*
- *Beispiel: "Schlechte Weizenernte!" -> Brotpreis steigt.*

## Konfiguration von Ereignissen

Ereignisse werden in `events.yml` konfiguriert.

```yaml
events:
  gold_rush:
    display-name: "Goldrausch"
    message: "&6Eine neue Goldader wurde entdeckt! Goldpreise fallen!"
    items: [GOLD_INGOT, GOLD_BLOCK]
    price-multiplier: 0.5 # Preise werden halbiert
    duration: 3600 # Dauert 1 Stunde
    chance: 0.01 # 1% Chance pro Zyklus
```

## Auslösung

### Automatisch
Das Plugin prüft periodisch (konfigurierbar), ob ein Ereignis basierend auf seiner Wahrscheinlichkeit (`chance`) ausgelöst werden soll.

### Manuell (Admin)
Administratoren können ein Ereignis erzwingen:

```bash
/endex admin event start <event_name>
```

## Benachrichtigungen

Wenn ein Ereignis beginnt, kann eine Ankündigung gemacht werden:
- Im Chat.
- Über die BossBar.
- Über einen Titel auf dem Bildschirm.

Dies alarmiert die Spieler, dass es eine Handelsmöglichkeit gibt.
