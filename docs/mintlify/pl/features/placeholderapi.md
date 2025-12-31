---
title: "Integracja PlaceholderAPI"
description: "Pełna lista placeholderów PlaceholderAPI dla The Endex - używaj w scoreboardach, hologramach, listach tab i nie tylko."
---

The Endex zapewnia pełną integrację z PlaceholderAPI z ponad 30 placeholderami do wyświetlania danych rynkowych, zasobów graczy, rankingów i statystyk.

<Info>
PlaceholderAPI jest **miękką zależnością** - plugin działa bez niego, ale placeholdery nie będą dostępne.
</Info>

## Instalacja

1. Zainstaluj [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) na swoim serwerze
2. Zainstaluj The Endex (1.5.3+)
3. Zrestartuj serwer
4. Rozszerzenie rejestruje się automatycznie - nie potrzeba `/papi ecloud download`!

## Referencja Placeholderów

### Placeholdery Przedmiotów Rynkowych

Pobierz dane dla konkretnych przedmiotów używając nazwy materiału.

| Placeholder | Opis | Przykładowy Wynik |
|-------------|-------------|----------------|
| `%endex_price_<MATERIAL>%` | Obecna cena | `800.00` |
| `%endex_price_formatted_<MATERIAL>%` | Cena z symbolem waluty | `$800.00` |
| `%endex_change_<MATERIAL>%` | Zmiana ceny od ostatniej aktualizacji | `+5.25%` |
| `%endex_trend_<MATERIAL>%` | Strzałka trendu | `↑`, `↓`, lub `→` |
| `%endex_supply_<MATERIAL>%` | Obecna podaż | `1,234` |
| `%endex_demand_<MATERIAL>%` | Obecny popyt | `567` |

**Przykłady:**
```
%endex_price_DIAMOND%           → 800.00
%endex_price_formatted_DIAMOND% → $800.00
%endex_trend_DIAMOND%           → ↑
%endex_change_DIAMOND%          → +5.25%
%endex_supply_IRON_INGOT%       → 1,234
%endex_demand_GOLD_INGOT%       → 567
```

### Top Przedmioty wg Ceny (1-10)

Wyświetl najdroższe i najtańsze przedmioty na rynku.

| Placeholder | Opis |
|-------------|-------------|
| `%endex_top_price_<N>%` | Nazwa N-tego najdroższego przedmiotu |
| `%endex_top_price_<N>_value%` | Cena N-tego najdroższego przedmiotu |
| `%endex_bottom_price_<N>%` | Nazwa N-tego najtańszego przedmiotu |
| `%endex_bottom_price_<N>_value%` | Cena N-tego najtańszego przedmiotu |

**Przykłady:**
```
%endex_top_price_1%        → Netherite Ingot
%endex_top_price_1_value%  → 2,000.00
%endex_top_price_2%        → Diamond
%endex_top_price_2_value%  → 800.00
%endex_bottom_price_1%     → Cobblestone
%endex_bottom_price_1_value% → 4.00
```

### Największe Wzrosty i Spadki (1-10)

Pokaż przedmioty z największymi ruchami cenowymi.

| Placeholder | Opis |
|-------------|-------------|
| `%endex_top_gainer_<N>%` | Nazwa N-tego największego wzrostu |
| `%endex_top_gainer_<N>_change%` | Procent zmiany N-tego wzrostu |
| `%endex_top_loser_<N>%` | Nazwa N-tego największego spadku |
| `%endex_top_loser_<N>_change%` | Procent zmiany N-tego spadku |

**Przykłady:**
```
%endex_top_gainer_1%        → Emerald
%endex_top_gainer_1_change% → +15.50%
%endex_top_loser_1%         → Iron Ingot
%endex_top_loser_1_change%  → -8.20%
```

### Placeholdery Zasobów Gracza

Wyświetl informacje o wirtualnych zasobach gracza.

<Note>
Te placeholdery wymagają, aby gracz był online i są zależne od kontekstu gracza oglądającego.
</Note>

| Placeholder | Opis |
|-------------|-------------|
| `%endex_holdings_total%` | Całkowita wartość zasobów gracza |
| `%endex_holdings_count%` | Całkowita liczba przedmiotów w zasobach |
| `%endex_holdings_top_<N>%` | Nazwa N-tego najbardziej wartościowego przedmiotu |
| `%endex_holdings_top_<N>_value%` | Wartość N-tego najbardziej wartościowego przedmiotu |
| `%endex_holdings_top_<N>_amount%` | Ilość N-tego najbardziej wartościowego przedmiotu |

**Przykłady:**
```
%endex_holdings_total%       → 125,430.50
%endex_holdings_count%       → 2,456
%endex_holdings_top_1%       → Diamond
%endex_holdings_top_1_value% → 64,000.00
%endex_holdings_top_1_amount% → 80
```

### Ranking Zasobów (1-10)

Wyświetl najbogatszych graczy według całkowitej wartości zasobów.

| Placeholder | Opis |
|-------------|-------------|
| `%endex_top_holdings_<N>%` | Nazwa N-tego najbogatszego gracza (wg zasobów) |
| `%endex_top_holdings_<N>_value%` | Wartość zasobów N-tego najbogatszego gracza |

**Przykłady:**
```
%endex_top_holdings_1%       → Steve
%endex_top_holdings_1_value% → 1,250,000.00
%endex_top_holdings_2%       → Alex
%endex_top_holdings_2_value% → 890,500.00
```

### Statystyki Rynku

Ogólne statystyki całego rynku.

| Placeholder | Opis | Przykładowy Wynik |
|-------------|-------------|----------------|
| `%endex_total_items%` | Całkowita liczba przedmiotów na rynku | `57` |
| `%endex_total_volume%` | Suma cen wszystkich przedmiotów | `25,430.00` |
| `%endex_average_price%` | Średnia cena przedmiotu | `446.14` |
| `%endex_active_events%` | Liczba aktywnych wydarzeń rynkowych | `2` |

## Przykłady Użycia

### Przykład Scoreboard

Stwórz scoreboard rynkowy używając pluginów takich jak [TAB](https://www.spigotmc.org/resources/tab-1-5-x-1-21-1.57806/) lub [AnimatedScoreboard](https://www.spigotmc.org/resources/animatedscoreboard.20848/):

```yaml
scoreboard:
  title: "&6⚡ Rynek"
  lines:
    - "&7Twoje Zasoby:"
    - "&f%endex_holdings_total%"
    - ""
    - "&7Diament: &f%endex_price_DIAMOND% %endex_trend_DIAMOND%"
    - "&7Szmaragd: &f%endex_price_EMERALD% %endex_trend_EMERALD%"
    - "&7Żelazo: &f%endex_price_IRON_INGOT% %endex_trend_IRON_INGOT%"
    - ""
    - "&7Top Wzrost: &a%endex_top_gainer_1%"
    - "&7  %endex_top_gainer_1_change%"
```

### Przykład Hologramu

Wyświetl informacje rynkowe używając [DecentHolograms](https://www.spigotmc.org/resources/decentholograms.96927/) lub [HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays):

```yaml
# /dh create market
# /dh line add market &6&l⚡ THE ENDEX
# /dh line add market &7Rynek Czasu Rzeczywistego
# /dh line add market &8─────────────
# /dh line add market &fDiament: &a$%endex_price_DIAMOND% %endex_trend_DIAMOND%
# /dh line add market &fSzmaragd: &a$%endex_price_EMERALD% %endex_trend_EMERALD%
# /dh line add market &fNetherite: &a$%endex_price_NETHERITE_INGOT% %endex_trend_NETHERITE_INGOT%
# /dh line add market &8─────────────
# /dh line add market &7Top Trader: &e%endex_top_holdings_1%
```

### Przykład Listy Tab

Pokaż informacje rynkowe na tabie używając [TAB](https://www.spigotmc.org/resources/tab.57806/):

```yaml
header:
  - "&6⚡ The Endex Market"
  - "&7Twoje Zasoby: &f%endex_holdings_total%"
  - "&7Aktywne Wydarzenia: &f%endex_active_events%"
```

### Tabliczki z Rankingiem

Stwórz tabliczki z rankingiem używając [PlaceholderAPI Sign](https://www.spigotmc.org/resources/placeholderapi-sign.41475/):

```
[papi]
%endex_top_holdings_1%
$%endex_top_holdings_1_value%
&a#1 Trader
```

## Rozwiązywanie Problemów

### Placeholdery Nie Działają

1. **Sprawdź czy PlaceholderAPI jest zainstalowane:**
   ```
   /papi info
   ```

2. **Zweryfikuj czy rozszerzenie The Endex jest załadowane:**
   ```
   /papi list
   ```
   Szukaj `endex` na liście.

3. **Przetestuj placeholder:**
   ```
   /papi parse me %endex_total_items%
   ```

4. **Sprawdź konsolę pod kątem błędów** przy starcie serwera dotyczących PlaceholderAPI.

### Typowe Problemy

| Problem | Rozwiązanie |
|-------|----------|
| Zwraca `%endex_...%` dosłownie | PlaceholderAPI nie zainstalowane lub rozszerzenie nie zarejestrowane |
| Zwraca `N/A` | Materiał nie jest na rynku lub nieprawidłowa nazwa materiału |
| Zwraca `0` | Brak dostępnych danych (np. brak zasobów, brak historii) |
| Placeholdery gracza puste | Gracz musi być online dla danych o zasobach |

## Powiązane Strony

- [System Zasobów](/features/holdings) — Zrozumienie wirtualnych zasobów
- [Konfiguracja](/reference/configuration) — Ustawienia pluginu
- [Komendy](/reference/commands) — Dostępne komendy
