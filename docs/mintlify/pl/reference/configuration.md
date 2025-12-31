---
title: "Konfiguracja"
description: "Kluczowe ustawienia w config.yml i typowe wskazówki dotyczące dostrajania."
---

Główny plik konfiguracyjny to:

```text
plugins/TheEndex/config.yml
```

Po zmianach uruchom:

```text
/endex reload
```

## Podstawowe ceny

```yaml
update-interval-seconds: 60
price-sensitivity: 0.05
history-length: 5
autosave-minutes: 5
save-on-each-update: true
```

## Tryb przechowywania

```yaml
storage:
  sqlite: false
```

Włączając SQLite, zrestartuj serwer, aby plugin mógł zmigrować dane w razie potrzeby.

## Sprawdzanie Aktualizacji

```yaml
update-checker:
  enabled: true      # Sprawdzaj aktualizacje przy starcie
  notify-ops: true   # Powiadamiaj OPów przy dołączeniu
```

## Inwestycje

```yaml
investments:
  enabled: true
  apr-percent: 5.0
```

## Wydarzenia

```yaml
events:
  multiplier-cap: 10.0
  stacking:
    mode: multiplicative
    default-weight: 1.0
    max-active: 0
```

Zobacz: [Wydarzenia](../features/events)

## Ceny zależne od ekwipunku (opcjonalne)

```yaml
price-inventory:
  enabled: false
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

## Ustawienia Web (wysoki poziom)

```yaml
web:
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
  api:
    tokens: []
    token-hashes: []
```

## Własne Web UI

Aby serwować własny pakiet dashboardu:

```yaml
web:
  custom:
    enabled: false
    root: webui
    reload: false
    export-default: true
```

## Dostosowywanie GUI

Układy GUI są konfigurowane w oddzielnych plikach w `guis/`:

- `guis/market.yml` — Główny interfejs rynku
- `guis/details.yml` — Panel szczegółów przedmiotu
- `guis/holdings.yml` — Panel wirtualnych zasobów
- `guis/deliveries.yml` — Panel kolejki dostaw

Każdy plik obsługuje:
- Własne tytuły z kodami kolorów
- Rozmiar ekwipunku (rzędy × 9)
- Pozycje slotów dla przycisków i przedmiotów
- Definicje kategorii z własnymi materiałami

## Aliasy Komend

Własne aliasy komend są konfigurowane w `commands.yml`:

```yaml
aliases:
  shop: "market"           # /shop → /market
  stock: "market holdings" # /stock → /market holdings
  prices: "market top"     # /prices → /market top
```

Dla pełnego przewodnika po konfiguracji, zobacz `docs/CONFIG.md` w repozytorium.
