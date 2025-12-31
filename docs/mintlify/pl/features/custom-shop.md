---
title: "System Własnego Sklepu"
description: "Twórz sklepy oparte na kategoriach w stylu EconomyShopGUI z własnymi układami"
---

# System Własnego Sklepu

<Info>
Dostępne od v1.5.0. Ulepszone w v1.5.4 o kategorie oparte na filtrach i przyciski Zasoby/Sortuj. Edytor Układu naprawiony w v1.5.6.
</Info>

## Przegląd

System Własnego Sklepu zapewnia alternatywę dla domyślnego przewijanego interfejsu rynku. Zamiast jednej listy wszystkich przedmiotów, gracze widzą **menu główne z ikonami kategorii**, które prowadzą do dedykowanych **stron kategorii** z przedmiotami.

### Funkcje

- **Nawigacja oparta na kategoriach** - Organizuj przedmioty w logiczne kategorie (Bloki, Rudy, Jedzenie, itp.)
- **Automatyczne wypełnianie oparte na filtrach** - Kategorie automatycznie zawierają pasujące przedmioty (v1.5.4+)
- **Przycisk Zasobów** - Dostęp do wirtualnych zasobów z dowolnej strony kategorii (v1.5.4+)
- **Przycisk Sortowania** - Sortuj przedmioty według Nazwy, Ceny lub Zmiany w kategoriach (v1.5.4+)
- **Własne układy** - Pełna kontrola nad pozycjami slotów, dekoracjami i ramkami
- **Edytor Układu w Grze** - Wizualny edytor GUI dla układów sklepów (poprawki v1.5.6)
- **Integracja cen rynkowych** - Przedmioty automatycznie używają dynamicznych cen rynkowych
- **Stałe ceny** - Nadpisz konkretne przedmioty stałymi cenami kupna/sprzedaży
- **Wiele sklepów** - Twórz różne sklepy do różnych celów
- **Stronicowanie** - Automatyczna nawigacja stron dla dużych kategorii
- **Edycja przez admina** - Shift+klik na przedmioty, aby edytować (z uprawnieniami)
- **Efekty dźwiękowe** - Konfigurowalne dźwięki dla interakcji
- **Przedmioty oparte na uprawnieniach** - Ogranicz przedmioty do konkretnych grup

## Włączanie Trybu Własnego Sklepu

Edytuj swój `config.yml`:

```yaml
shop:
  # Zmień z DEFAULT na CUSTOM
  mode: CUSTOM
  
  # Którą konfigurację sklepu użyć jako główną
  main-shop: main
  
  # Własna komenda (opcjonalne)
  command: shop
```

Po zmianie trybu uruchom `/endex reload` lub zrestartuj serwer.

## Komendy Sklepu

| Komenda | Opis |
|---------|-------------|
| `/market` | Otwiera własny sklep (gdy mode=CUSTOM) |
| `/market shop [id]` | Otwiera konkretny sklep |
| `/market stock` | Otwiera domyślny rynek (pomija tryb) |

## Tworzenie Sklepu

Konfiguracje sklepów są przechowywane w `plugins/TheEndex/shops/` jako pliki YAML. Domyślny sklep to `main.yml`.

### Podstawowa Struktura

```yaml
# Unikalny identyfikator
id: main
enabled: true
title: "&5&lSklep Serwera"

# Ustawienia menu głównego
menu:
  title: "&8Sklep Serwera"
  size: 54  # Musi być wielokrotnością 9
  layout:
    # Zdefiniuj co idzie do każdego slotu
    19:
      type: CATEGORY
      category: blocks
    21:
      type: CATEGORY
      category: ores

# Dekoracja
decoration:
  fill-empty: false
  empty-material: GRAY_STAINED_GLASS_PANE

# Kategorie z przedmiotami
categories:
  blocks:
    name: "Bloki"
    icon: BRICKS
    items:
      COBBLESTONE: true
      STONE: true
```

## Typy Układu Menu

Sekcja `menu.layout` definiuje co pojawia się w każdym slocie:

| Typ | Opis |
|------|-------------|
| `CATEGORY` | Linkuje do strony kategorii |
| `DECORATION` | Statyczny przedmiot dekoracyjny |
| `EMPTY` | Pusty slot (powietrze) |
| `CLOSE` | Przycisk zamknięcia |
| `INFO` | Wyświetlanie informacji (pokazuje balans, itp.) |

### Slot Kategorii

```yaml
layout:
  19:
    type: CATEGORY
    category: blocks  # Odnosi się do categories.blocks
```

### Slot Dekoracji

```yaml
layout:
  0:
    type: DECORATION
    material: BLACK_STAINED_GLASS_PANE
    name: " "
```

### Slot Info

```yaml
layout:
  4:
    type: INFO
    material: GOLD_INGOT
    name: "&6Twój Balans: &f%balance%"
    lore:
      - "&7Przeglądaj sklep poniżej!"
```

## Konfiguracja Kategorii

Każda kategoria ma własną stronę z przedmiotami:

```yaml
categories:
  ores:
    # Ustawienia wyświetlania
    name: "Rudy i Minerały"
    icon: DIAMOND
    icon-name: "&bRudy i Minerały"
    icon-lore:
      - "&7Cenne rudy i minerały"
      - "&eKliknij aby przeglądać!"
    
    # Ustawienia strony
    page-title: "&8Rudy i Minerały"
    page-size: 54
    
    # Zakres slotów przedmiotów (dla auto-aranżacji)
    item-slots:
      start: 0
      end: 44
    
    # Wypełnij puste sloty
    fill-empty: false
    empty-material: GRAY_STAINED_GLASS_PANE
    
    # Przedmioty w tej kategorii
    items:
      DIAMOND: true
      EMERALD: true
      GOLD_INGOT: true
```

## Kategorie Oparte na Filtrach (v1.5.4+)

<Info>
Nowość w v1.5.4: Kategorie mogą automatycznie wypełniać się przedmiotami na podstawie filtrów nazw materiałów!
</Info>

Zamiast ręcznie wymieniać każdy przedmiot, użyj właściwości `filter` do automatycznego wypełniania kategorii:

```yaml
categories:
  ores:
    name: "Rudy i Minerały"
    icon: DIAMOND
    filter: "_ORE|RAW_|DIAMOND|EMERALD|LAPIS|COAL|COPPER_INGOT|IRON_INGOT|GOLD_INGOT|NETHERITE"
    
  wood:
    name: "Drewno i Kłody"
    icon: OAK_LOG
    filter: "_LOG|_WOOD|_PLANKS|_SLAB|_STAIRS|_FENCE|_DOOR|_BUTTON|_SIGN"
    
  food:
    name: "Jedzenie"
    icon: GOLDEN_APPLE
    filter: "APPLE|BREAD|BEEF|PORK|CHICKEN|MUTTON|RABBIT|COD|SALMON|CARROT|POTATO|BEETROOT|MELON|COOKIE|CAKE|PIE|STEW|SOUP"
```

### Jak Działają Filtry

- Właściwość `filter` to **wzorzec regex** dopasowywany do nazw materiałów
- Użyj `|` aby oddzielić wiele wzorców (logika LUB)
- Tylko przedmioty, które są **włączone w rynku**, zostaną uwzględnione
- Przedmioty pasujące do filtra są automatycznie dodawane do kategorii

### Przykłady Filtrów

| Kategoria | Wzorzec Filtra |
|----------|---------------|
| Rudy | `_ORE\|RAW_\|DIAMOND\|EMERALD` |
| Drewno | `_LOG\|_WOOD\|_PLANKS` |
| Kamień | `STONE\|COBBLE\|GRANITE\|DIORITE\|ANDESITE\|DEEPSLATE` |
| Wełna | `_WOOL` |
| Szkło | `GLASS` |
| Jedzenie | `APPLE\|BREAD\|BEEF\|PORK\|CHICKEN` |

### Łączenie Filtra + Ręcznych Przedmiotów

Możesz używać obu filtrów i ręcznych przedmiotów razem:

```yaml
categories:
  special:
    name: "Przedmioty Specjalne"
    icon: NETHER_STAR
    filter: "NETHERITE|ELYTRA"
    items:
      # Dodatkowe ręczne przedmioty nie pasujące do filtra
      BEACON: true
      TOTEM_OF_UNDYING:
        buy-price: 10000
        use-market-prices: false
```

## Przyciski Strony Kategorii (v1.5.4+)

Strony kategorii zawierają teraz przyciski nawigacji w dolnym rzędzie:

| Slot | Przycisk | Funkcja |
|------|--------|----------|
| 45 | 📦 Zasoby | Otwiera twój panel wirtualnych zasobów |
| 49 | ⬅️ Powrót | Wraca do głównego menu sklepu |
| 53 | 🔀 Sortuj | Cykl sortowania: Nazwa → Cena → Zmiana |

### Opcje Sortowania

Przycisk Sortuj przełącza między:
1. **Nazwa** (A-Z) - Kolejność alfabetyczna
2. **Cena** (Wysoka-Niska) - Najdroższe pierwsze
3. **Zmiana** (Wysoka-Niska) - Największe wzrosty pierwsze

<Tip>
Preferencja sortowania jest zapisywana na gracza i zachowywana między sesjami!
</Tip>

## Konfiguracja Przedmiotu

### Prosty Format (Ceny Rynkowe)

```yaml
items:
  DIAMOND: true
  EMERALD: true
  IRON_INGOT: true
```

Przedmioty używają dynamicznych cen rynkowych ze spreadem kupna/sprzedaży.

### Zaawansowany Format (Stałe Ceny)

```yaml
items:
  DIAMOND:
    material: DIAMOND
    name: "&b✦ Premium Diamond"
    lore:
      - "&7Błyszczący diament!"
    slot: 0                    # Wymuś konkretny slot
    buy-price: 1000            # Stała cena kupna
    sell-price: 800            # Stała cena sprzedaży
    use-market-prices: false   # Wyłącz integrację z rynkiem
    quantity: 1                # Przedmioty na transakcję
    permission: vip.diamond    # Wymagane uprawnienie
    commands:
      on-buy:
        - "say %player% kupił diament!"
      on-sell:
        - "say %player% sprzedał diament!"
```

### Właściwości Przedmiotu

| Właściwość | Opis | Domyślnie |
|----------|-------------|---------|
| `material` | Nazwa materiału Bukkit | Nazwa klucza |
| `name` | Własna nazwa wyświetlana | Nazwa materiału |
| `lore` | Dodatkowe linie opisu | Puste |
| `slot` | Wymuś konkretny slot | Auto |
| `buy-price` | Stała cena kupna | Rynek |
| `sell-price` | Stała cena sprzedaży | Rynek |
| `use-market-prices` | Użyj dynamicznych cen | true |
| `quantity` | Rozmiar stacka na zakup | 1 |
| `permission` | Wymagane uprawnienie | Brak |
| `commands.on-buy` | Komendy przy zakupie | Brak |
| `commands.on-sell` | Komendy przy sprzedaży | Brak |

## Konfiguracja Dźwięków

```yaml
sounds:
  open-menu: UI_BUTTON_CLICK
  open-category: UI_BUTTON_CLICK
  buy: ENTITY_EXPERIENCE_ORB_PICKUP
  sell: ENTITY_EXPERIENCE_ORB_PICKUP
  error: ENTITY_VILLAGER_NO
  page-change: UI_BUTTON_CLICK
```

Użyj nazw dźwięków Bukkit z [Sound API](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html).

## Dostosowanie Opisu Ceny

W `config.yml`, dostosuj jak ceny pojawiają się w opisie przedmiotu:

```yaml
shop:
  custom:
    price-lore:
      - "&7"
      - "&aLewy Klik: &fKup: &6$%buy_price%"
      - "&cPrawy Klik: &fSprzedaj: &6$%sell_price%"
      - "&7"
      - "&8Zapas: %stock% | Popyt: %demand%"
    
    admin-lore: "&e⚡ Shift Klik: Edytuj przedmiot (Admin)"
```

### Dostępne Placeholdery

| Placeholder | Opis |
|-------------|-------------|
| `%buy_price%` | Obecna cena kupna |
| `%sell_price%` | Obecna cena sprzedaży |
| `%market_price%` | Bazowa cena rynkowa |
| `%stock%` | Poziom podaży |
| `%demand%` | Poziom popytu |

## Ustawienia Nawigacji

```yaml
shop:
  custom:
    items-per-page: 45      # Przedmioty pokazywane na stronę kategorii
    show-back-button: true
    back-button-slot: 49
    show-pagination: true
    prev-page-slot: 48
    next-page-slot: 50
    admin-edit: true        # Pozwól na edycję shift-klik przez admina
```

## Edytor Układu w Grze (v1.5.6)

<Info>
Edytor Układu otrzymał całkowitą przebudowę w v1.5.6 naprawiając wszystkie główne problemy z edycją, zapisywaniem i przełączaniem trybów.
</Info>

Edytor Układu zapewnia wizualne GUI do tworzenia i edycji układów sklepów bez dotykania plików konfiguracyjnych.

### Otwieranie Edytora

```
/endex shop editor <shop-id>
```

Przykład: `/endex shop editor main`

### Tryby Edytora

Edytor ma 5 trybów, wybieranych przez dolny pasek narzędzi:

| Tryb | Opis |
|------|-------------|
| **Umieść Kategorię** | Kliknij pusty slot aby umieścić ikonę kategorii |
| **Umieść Dekorację** | Kliknij pusty slot aby umieścić przedmioty dekoracyjne |
| **Umieść Przycisk** | Kliknij pusty slot aby umieścić przyciski nawigacji |
| **Edytuj Slot** | Kliknij dowolny istniejący przedmiot aby zmodyfikować jego nazwę/opis |
| **Usuń Slot** | Kliknij dowolny istniejący przedmiot aby go usunąć |

### Edycja Nazw i Opisów

1. Przełącz na tryb **Edytuj Slot** (ikona ołówka)
2. **Prawy-klik** na dowolny istniejący przedmiot
3. Wpisz nową **nazwę** na czacie (lub "skip" aby zachować obecną)
4. Wpisz nowy **opis** na czacie (linie oddzielone przez `|`)
5. Zmiany pojawiają się natychmiast w podglądzie

<Warning>
Pamiętaj aby kliknąć **Zapisz** przed zamknięciem edytora! Niezapisane zmiany są zachowywane podczas nawigacji w edytorze, ale tracone przy zamknięciu.
</Warning>

### Przyciski Paska Narzędzi

| Slot | Przycisk | Funkcja |
|------|--------|----------|
| 45 | 💾 Zapisz | Zapisz układ do pliku konfiguracyjnego |
| 46 | ❌ Anuluj | Odrzuć zmiany i zamknij |
| 47-51 | Przyciski trybu | Przełącz tryb edytora |
| 53 | ℹ️ Pomoc | Pokaż instrukcje edytora |

### Wskazówki

- **Lewy-klik** na istniejące przedmioty używa ich domyślnej akcji (zależy od trybu)
- **Prawy-klik** na istniejące przedmioty w trybie Edycji otwiera monit nazwy/opisu
- Zmiany są zachowywane przy wybieraniu kategorii lub przycisków z podmenu
- Edytor używa wyraźnego czerwonego prefiksu tytułu (`§4§l⚙`) aby uniknąć konfliktów ze sklepem gracza

## Wiele Sklepów

Twórz dodatkowe pliki sklepów w `plugins/TheEndex/shops/`:

```
shops/
├── main.yml        # Domyślny sklep
├── vip.yml         # Sklep tylko dla VIP
└── seasonal.yml    # Sklep eventowy
```

Otwórz konkretne sklepy za pomocą:
```
/market shop vip
/market shop seasonal
```

## Uprawnienia

| Uprawnienie | Opis |
|------------|-------------|
| `endex.shop.admin` | Edytuj przedmioty przez Shift+Klik |
| (uprawnienie przedmiotu) | Dostęp do konkretnych przedmiotów |

## Przykład: Sklep VIP

```yaml
# shops/vip.yml
id: vip
enabled: true
title: "&6&lSklep VIP"

menu:
  title: "&8Ekskluzywny Sklep VIP"
  size: 27
  layout:
    13:
      type: CATEGORY
      category: exclusive

categories:
  exclusive:
    name: "Przedmioty VIP"
    icon: NETHER_STAR
    icon-name: "&6Ekskluzywne VIP"
    icon-lore:
      - "&7Specjalne przedmioty dla członków VIP!"
    page-title: "&8Przedmioty VIP"
    page-size: 54
    items:
      ENCHANTED_GOLDEN_APPLE:
        buy-price: 5000
        sell-price: 2500
        use-market-prices: false
        permission: group.vip
      NETHERITE_BLOCK:
        buy-price: 50000
        sell-price: 25000
        use-market-prices: false
        permission: group.vip
```

## Porównanie: Tryb DEFAULT vs CUSTOM

| Funkcja | DEFAULT | CUSTOM |
|---------|---------|--------|
| Interfejs | Przewijana lista | Oparty na kategoriach |
| Nawigacja | Sortowanie i filtry | Menu główne → Kategorie |
| Układ | Stały | W pełni konfigurowalny |
| Aranżacja przedmiotów | Automatyczna | Ręczna lub automatyczna |
| Wiele sklepów | Nie | Tak |
| Wyświetlanie ceny | Opis | Konfigurowalny opis |
| Najlepsze dla | Odczucia giełdy | Tradycyjnego odczucia sklepu |

<Tip>
Zawsze możesz uzyskać dostęp do domyślnego rynku za pomocą `/market stock`, nawet gdy tryb własny jest włączony!
</Tip>
