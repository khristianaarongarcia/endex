---
title: "Konfiguration"
description: "Detaillierte Anleitung zu den Konfigurationsdateien"
---

# Konfiguration

The Endex ist über seine YAML-Dateien im Ordner `plugins/TheEndex/` hochgradig konfigurierbar.

## config.yml

Die Hauptdatei für globale Einstellungen.

```yaml
# Allgemeine Einstellungen
plugin-prefix: "&8[&5The Endex&8] "
language: de # Sprache (en, fr, es, de, etc.)
debug-mode: false

# Wirtschaft
economy:
  currency-symbol: "$"
  format: "#,##0.00"
  
# Markteinstellungen
market:
  update-interval: 60 # Sekunden zwischen Preisaktualisierungen
  save-interval: 300 # Sekunden zwischen automatischen Speicherungen
  
# Portfolio-System
holdings:
  enabled: true
  tax-rate: 0.05

# Webserver (Dashboard)
web:
  enabled: true
  port: 8080
  host: "0.0.0.0"
```

## items.yml

Definiert alle handelbaren Gegenstände und ihre Eigenschaften.

```yaml
DIAMOND:
  material: DIAMOND # Minecraft Material
  display-name: "&bDiamant" # Anzeigename (Optional)
  base-price: 100.0
  min-price: 10.0
  max-price: 500.0
  volatility: 0.5
  stock: 1000 # Virtueller Anfangsbestand
  elasticity: 1.0 # Empfindlichkeit gegenüber Bestandsänderungen
  
GOLD_INGOT:
  material: GOLD_INGOT
  base-price: 50.0
  volatility: 0.3
```

## events.yml

Konfiguriert zufällige Marktereignisse.

```yaml
events:
  market_crash:
    display-name: "&cBörsencrash"
    chance: 0.005
    duration: 1200
    global-multiplier: 0.7 # Alle Preise x0.7
    
  gold_rush:
    display-name: "&6Goldrausch"
    items: [GOLD_INGOT, GOLD_BLOCK]
    price-multiplier: 0.5
```

## messages.yml

Enthält alle Texte, die den Spielern angezeigt werden. Sie können die Nachrichten hier übersetzen oder anpassen. Unterstützt Farbcodes (`&` oder MiniMessage je nach Version).

```yaml
prefix: "&8[&5The Endex&8] "
buy-success: "&aSie haben &e%amount%x %item% &afür &e%price% &agekauft."
sell-success: "&aSie haben &e%amount%x %item% &afür &e%price% &averkauft."
insufficient-funds: "&cSie haben nicht genug Geld."
```
