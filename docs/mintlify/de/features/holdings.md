---
title: "Bestände (Holdings)"
description: "Virtuelles Handelssystem und Investitionen"
---

# Bestands-System (Holdings)

Das **Holdings**-System (Bestände/Portfolio) ermöglicht es Spielern, in Gegenstände zu investieren, ohne sie physisch in ihrem Inventar lagern zu müssen. Dies ähnelt dem Kauf von Aktien oder Terminkontrakten in der realen Welt.

## Wie es funktioniert

Anstatt einen physischen "Diamant" zu kaufen, kauft ein Spieler eine "Diamant-Beteiligung".

1. Der Spieler zahlt den aktuellen Marktpreis.
2. Die Beteiligung wird in seinem virtuellen Portfolio gespeichert.
3. Der physische Gegenstand wird dem Spieler **nicht** gegeben.
4. Später kann der Spieler seine Beteiligung zum neuen Marktpreis verkaufen.

### Vorteile

- **Keine Inventarverwaltung**: Spieler können Tausende von Einheiten handeln, ohne Kisten zu benötigen.
- **Reine Spekulation**: Ideal für Spieler, die Händler spielen wollen, ohne Ressourcen zu farmen.
- **Schutz**: Bestände können nicht gestohlen, verbrannt oder durch Tod verloren werden.

## Portfolio-Befehle

- `/holdings`: Öffnet das Portfolio-GUI, um Investitionen zu sehen.
- `/endex invest <item> <menge>`: Kauft Bestände.
- `/endex divest <item> <menge>`: Verkauft Bestände.

## Konfiguration

Sie können dieses System in `config.yml` aktivieren oder deaktivieren:

```yaml
holdings:
  enabled: true
  max-holdings-per-item: 10000 # Limit pro Spieler
  tax-rate: 0.05 # 5% Steuer auf Holdings-Gewinne
```

## Kapitalertragssteuer

Um zu verhindern, dass die Wirtschaft durch Spekulation zu schnell aufgebläht wird, können Sie eine spezifische Steuer auf den Verkauf von Beständen erheben (`tax-rate`). Diese Steuer wird beim Verkauf der Bestände vom Gewinn oder der Gesamtsumme abgezogen.
