---
title: "Investitionen"
description: "Erweiterte Investitionssysteme"
---

# Investitionen

Über den einfachen Kauf/Verkauf von Gegenständen hinaus bietet The Endex komplexere Investitionsmechanismen für fortgeschrittene Server.

## Investmentfonds (ETF)

*Kommende Funktion / In Entwicklung*

Ermöglicht es Spielern, in Körbe von Gegenständen zu investieren (z. B. "Bergbaufonds" bestehend aus Eisen, Gold, Diamant, Kohle). Der Wert des Fonds folgt dem gewichteten Durchschnitt der zugrunde liegenden Gegenstände.

## Dividenden

Wenn Sie Gegenstände als (virtuelle) "Aktien" konfigurieren, können Sie periodische Dividenden einrichten.

```yaml
# Beispiel in items.yml
SERVER_SHARE:
  base-price: 1000.0
  dividend:
    enabled: true
    amount: 10.0 # Zahlt 10$ pro Aktie
    interval: 86400 # Alle 24h
```

Spieler, die diesen Gegenstand in ihrem [Portfolio](/de/features/holdings) besitzen, erhalten in jedem Intervall automatisch Geld.

## Leerverkäufe (Short Selling)

*Erweiterte Funktion*

Ermöglicht es Spielern, auf fallende Preise zu wetten.
1. Der Spieler "leiht" sich einen Gegenstand und verkauft ihn zum aktuellen Preis (z. B. 100$).
2. Er muss ihn später zurückkaufen, um seine Schulden zu begleichen.
3. Wenn der Preis auf 80$ gefallen ist, kauft er ihn zurück und behält die Differenz (20$ Gewinn).
4. Wenn der Preis auf 120$ gestiegen ist, verliert er 20$.

Diese Funktion muss explizit aktiviert werden, da sie Risiken für unerfahrene Spieler birgt.
