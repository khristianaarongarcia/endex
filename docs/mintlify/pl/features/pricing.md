---
title: "Dynamiczne Ceny"
description: "Jak The Endex oblicza ceny na podstawie podaży, popytu i opcjonalnych sygnałów."
---

Ceny w The Endex zmieniają się w czasie. W każdym cyklu aktualizacji każdy materiał otrzymuje nową cenę opartą na presji rynkowej.

## Główna idea

- **Popyt** rośnie, gdy gracze kupują.
- **Podaż** rośnie, gdy gracze sprzedają.
- Wartość dostrajania (**wrażliwość cenowa**) kontroluje, jak zmienna jest ekonomia.

## Interwał aktualizacji

Aktualizacje cen działają według ustalonego harmonogramu skonfigurowanego w plugins/TheEndex/config.yml:

```yaml
pricing:
  update-interval-seconds: 5
```

## Wpływy na Ceny

### 1. Aktywność Handlowa (Główna)

Główny czynnik cenotwórczy. Każde kupno/sprzedaż wpływa na cenę:

```yaml
# Jak wrażliwe są ceny na handel (0.0 - 1.0)
sensitivity: 0.1
```

### 2. Ceny Zależne od Ekwipunku

Ceny mogą reagować na przedmioty, które gracze trzymają przy sobie:

```yaml
price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

### 3. Skaner Magazynów Świata

Ceny reagują na WSZYSTKIE przedmioty przechowywane na serwerze:

```yaml
price-world-storage:
  enabled: true
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  max-impact-percent: 5.0
  chunks-per-tick: 50
```

### 4. Wydarzenia Rynkowe

Wyzwalane przez admina mnożniki, które wpływają na konkretne przedmioty:

```yaml
events:
  ore_rush:
    multipliers:
      DIAMOND: 1.5
      EMERALD: 1.5
```

Zobacz: [Wydarzenia](events)

## Ograniczenia Cen Min/Max

Każdy przedmiot ma konfigurowalne limity cen:

```yaml
items:
  DIAMOND:
    base-price: 100.0
    min-price: 10.0
    max-price: 1000.0
```

## Wygładzanie

Plugin stosuje wygładzanie EMA (wykładnicza średnia krocząca):

```yaml
smoothing:
  enabled: true
  factor: 0.3
```

Tworzy to naturalnie wyglądające krzywe cen zamiast poszarpanych skoków.

## Zrozumienie Ruchu Cen

### Przykładowy Scenariusz

Cena początkowa: **100 monet** za Diament

1. **Gracz A kupuje 64 diamenty** - Popyt rośnie, cena wzrasta do ~105 monet
2. **Gracz B kupuje 128 diamentów** - Większy popyt, cena wzrasta do ~115 monet
3. **Gracz C sprzedaje 256 diamentów** - Podaż zalewa rynek, cena spada do ~95 monet
4. **Brak handlu przez 10 minut** - Cena powoli stabilizuje się wokół ~98 monet

### Historia Cen

Zobacz historię cen w grze:

```text
/market price diamond
```

Lub przez wykresy cen w dashboardzie web.

## Wskazówki dla Właścicieli Serwerów

- **Zacznij Konserwatywnie** - Zacznij od niskiej wrażliwości (0.05-0.1) i zwiększaj, jeśli rynki wydają się zbyt stabilne.
- **Skaner Magazynów Świata** - Włącz na ustalonych serwerach. Nowe serwery mogą mieć zmienne ceny z powodu małej liczby przedmiotów.
- **Testuj Wydarzenia** - Używaj wydarzeń, aby tworzyć ekscytujące momenty rynkowe. Gracze uwielbiają skoki cen!

## Powiązane Strony

- [Wydarzenia Rynkowe](events) - Mnożniki cen
- [Konfiguracja](../reference/configuration) - Pełne odniesienie do konfiguracji
- [REST API](../web-api/rest-api) - Endpointy historii cen
