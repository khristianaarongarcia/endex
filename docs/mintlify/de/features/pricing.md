---
title: "Dynamische Preise"
description: "Wie das dynamische Preissystem funktioniert"
---

# Dynamisches Preissystem

Das Herzstück von The Endex ist sein dynamischer Preisalgorithmus, der in Echtzeit auf Angebot und Nachfrage reagiert.

## Das Konzept

Im Gegensatz zu traditionellen Shops, in denen Preise fest (statisch) sind, passt The Endex die Preise automatisch an:

- **Wenn Spieler kaufen**, sinkt der Bestand (theoretisch) und die Nachfrage steigt -> **Der Preis steigt**.
- **Wenn Spieler verkaufen**, steigt der Bestand und das Angebot steigt -> **Der Preis fällt**.

Dies schafft eine lebendige Wirtschaft, in der Spieler Gewinne erzielen können, indem sie niedrig kaufen und hoch verkaufen.

## Preisalgorithmus

Der Preis wird anhand einer Formel berechnet, die Folgendes berücksichtigt:

1. **Basispreis**: Der vom Administrator festgelegte Startpunkt.
2. **Volatilität**: Wie schnell sich der Preis ändert.
3. **Transaktionsvolumen**: Wie viele Gegenstände gehandelt werden.
4. **Grenzen**: Mindest- und Höchstpreise, um extreme Inflation/Deflation zu vermeiden.

### Vereinfachte Formel

$$
P_{neu} = P_{aktuell} \times (1 + \frac{Volumen \times Volatilität}{Konstante})
$$

## Konfiguration der Volatilität

Jeder Gegenstand kann seine eigene Volatilität in `items.yml` haben.

- **Niedrige Volatilität (0.1 - 0.3)**: Stabile Preise, ändern sich langsam. Gut für Basisressourcen (Stein, Holz).
- **Mittlere Volatilität (0.4 - 0.7)**: Normale Schwankungen. Gut für Erze und Mob-Drops.
- **Hohe Volatilität (0.8 - 1.5)**: Sehr instabile Preise, hohes Risiko/hohe Belohnung. Gut für seltene Gegenstände.

```yaml
GOLD_INGOT:
  base-price: 50.0
  volatility: 0.5  # Mittlere Volatilität
```

## Preisgrenzen

Um Ihre Wirtschaft zu schützen, können Sie strikte Grenzen festlegen:

- `min-price`: Der Preis wird niemals unter diesen Wert fallen.
- `max-price`: Der Preis wird niemals über diesen Wert steigen.

```yaml
DIAMOND:
  base-price: 100.0
  min-price: 20.0   # Geht nie unter 20
  max-price: 500.0  # Geht nie über 500
```

## Preisregeneration

Sie können konfigurieren, dass Preise im Laufe der Zeit langsam zu ihrem Basispreis zurückkehren, wenn keine Aktivität stattfindet. Dies verhindert, dass Preise auf unbestimmte Zeit in Extremen stecken bleiben.

Aktivieren Sie dies in `config.yml` unter `price-regeneration`.
