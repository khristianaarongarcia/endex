---
title: "Benutzerdefinierter Shop"
description: "Integration mit bestehenden Shop-Plugins"
---

# Benutzerdefinierter Shop

The Endex ist so konzipiert, dass es als Preismotor für Ihre bestehenden Shops fungiert, anstatt diese vollständig zu ersetzen.

## GUI-Integration

The Endex bietet eine eigene grafische Benutzeroberfläche (GUI), um Preise und Trends anzuzeigen, aber für physische Transaktionen integriert es sich oft mit anderen Systemen oder ermöglicht den direkten Kauf/Verkauf über Befehle.

### Hauptmenü

Der Befehl `/market` öffnet das Hauptmenü, in dem Spieler:

- Alle gelisteten Gegenstände sehen können.
- Den aktuellen Preis, die prozentuale Änderung (24h) und den Trend (Hoch/Runter) sehen können.
- Auf ihre Portfolios (Holdings) zugreifen können.

## Erstellung physischer Shops

Sie können physische Shops erstellen, die die Preise von The Endex verwenden.

### Schilder (Signs)

The Endex unterstützt dynamische Handelsschilder.

**Erstellungsformat:**
1. `[Endex]`
2. `BUY` oder `SELL`
3. `<Item_Name>`
4. `<Menge>`

**Beispiel:**
```text
[Endex]
BUY
DIAMOND
1
```

Dies erstellt ein Schild, das es Spielern ermöglicht, 1 Diamant zum aktuellen Marktpreis zu kaufen. Der auf dem Schild angezeigte Preis aktualisiert sich automatisch.

### Citizens-Integration (NPC)

Wenn Sie das Plugin **Citizens** haben, können Sie Händler-Traits erstellen.

```bash
/npc create Händler --type villager
/trait endex-trader
```

*(Hinweis: Diese Funktion hängt von der Version von The Endex und den installierten Addons ab)*

## Schnelle Kauf-/Verkaufsbefehle

Spieler können auch Befehle verwenden, um schnell ohne GUI zu handeln:

- `/endex buy <item> <menge>`
- `/endex sell <item> <menge>`
- `/endex sellall`: Verkauft das gesamte kompatible Inventar an den Markt.

## Anpassung der Nachrichten

Alle transaktionsbezogenen Nachrichten (Erfolg, Unzureichende Mittel, Inventar voll) sind in `messages.yml` konfigurierbar.
