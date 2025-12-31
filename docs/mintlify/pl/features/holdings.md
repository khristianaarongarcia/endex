---
title: "Wirtualne Zasoby"
description: "Wirtualny magazyn dla handlowanych przedmiotów ze śledzeniem średniego kosztu i zysku/straty."
---

Zasoby to **wirtualny ekwipunek** posiadany przez każdego gracza. Zamiast wysyłać zakupy bezpośrednio do ekwipunku Minecraft gracza, zakupy mogą być najpierw deponowane w zasobach.

## Dlaczego istnieją zasoby

- Handluj dużymi ilościami bez zarządzania slotami w ekwipunku
- Śledź średnią cenę zakupu i zysk/stratę (P/L)
- Umożliwia handel przez web bez interakcji z ekwipunkiem
- Zmniejsz utratę przedmiotów, gdy ekwipunki są pełne

## Typowy przepływ

```text
Kup -> Zasoby -> Wypłać kiedy gotowy
Sprzedaj <- Zasoby (lub ekwipunek w trybie legacy)
```

## Komendy (typowe)

```text
/market holdings
/market withdraw <material> [ilość]
/market withdraw all
```

## Powiązane

- [System Dostaw](delivery) — ochrona przed przepełnieniem
- [Inwestycje](investments) — zablokuj przedmioty i zarabiaj APR
- [REST API](../web-api/rest-api) — endpointy zasobów

## Dlaczego Wirtualne Zasoby?

| Korzyść | Opis |
|---------|-------------|
| ✅ **Brak Utraconych Przedmiotów** | Pełny ekwipunek? Żaden problem—zasoby przechowują bez limitu* |
| ✅ **Śledzenie Kosztów** | Wiedz dokładnie ile zapłaciłeś za przedmiot |
| ✅ **Obliczanie P/L** | Wyświetlanie zysku/straty w czasie rzeczywistym |
| ✅ **Integracja Web** | Handluj z przeglądarki bezproblemowo |
| ✅ **Globalne Limity** | Zapobiega nadużyciom ekonomii |

*Podlega konfigurowalnym limitom

---

## GUI Zasobów

Dostęp z GUI Rynku lub:

```
/market holdings
```

### Interfejs

```
┌─────────────────────────────────────────────────┐
│                 MOJE ZASOBY                      │
│          Całkowita Wartość: $45,280             │
│          Całkowity P/L: +$3,120 (+7.4%)         │
├─────────────────────────────────────────────────┤
│                                                 │
│  [DIAMOND] ×128      [EMERALD] ×64              │
│  Śred: $95.00        Śred: $48.00               │
│  Teraz: $100.00      Teraz: $52.00              │
│  P/L: +$640 ✓        P/L: +$256 ✓               │
│                                                 │
│  [GOLD] ×256         [IRON] ×512                │
│  Śred: $10.50        Śred: $5.20                │
│  Teraz: $9.80        Teraz: $5.50               │
│  P/L: -$179 ✗        P/L: +$154 ✓               │
│                                                 │
├─────────────────────────────────────────────────┤
│   ◀ Powrót   │  📦 Wypłać Wszystko  │   📊 Staty   │
└─────────────────────────────────────────────────┘
```

### Interakcje

| Akcja | Wynik |
|--------|--------|
| **Lewy-klik** przedmiot | Wypłać wszystko (do miejsca w ekwipunku) |
| **Prawy-klik** przedmiot | Wypłać jeden stack (64 lub max) |
| **Przycisk Wypłać Wszystko** | Odbierz wszystko co się zmieści |
| **Przycisk Staty** | Zobacz szczegółowe statystyki portfela |

---

## Komendy

### Zobacz Zasoby

```
/market holdings
```

Wyjście czatu:
```
=== Twoje Zasoby ===
Diamond: 128 (Śred: $95.00, P/L: +$640.00)
Emerald: 64 (Śred: $48.00, P/L: +$256.00)
Gold Ingot: 256 (Śred: $10.50, P/L: -$179.20)
---
Całkowite Przedmioty: 448
Całkowita Wartość: $45,280.00
Całkowity P/L: +$716.80 (+1.6%)
```

### Wypłać Przedmioty

```
/market withdraw <przedmiot>           # Wszystko z przedmiotu
/market withdraw <przedmiot> <ilość>   # Konkretna ilość
/market withdraw all                   # Wszystko
```

Przykłady:
```
/market withdraw diamond
/market withdraw emerald 32
/market withdraw all
```

---

## Śledzenie Podstawy Kosztowej

System śledzi twój **średni koszt** na materiał:

### Przykład

1. Kup 64 diamenty po $100 każdy = $6,400
2. Kup 64 więcej po $110 każdy = $7,040
3. **Średni koszt**: ($6,400 + $7,040) / 128 = **$104.69 za diament**

Kiedy wypłacasz:
- Stosuje się FIFO (First-In-First-Out)
- Podstawa kosztowa dostosowuje się odpowiednio

---

## Obliczanie Zysku/Straty

P/L jest obliczany w czasie rzeczywistym:

```
P/L = (Obecna Cena - Średni Koszt) × Ilość
P/L% = ((Obecna Cena / Średni Koszt) - 1) × 100
```

### Przykład

- Posiadane: 128 diamentów
- Średni koszt: $95.00
- Obecna cena: $100.00

```
P/L = ($100 - $95) × 128 = +$640
P/L% = ((100 / 95) - 1) × 100 = +5.26%
```

---

## Limity

Zasoby mają konfigurowalne limity aby zapobiec nadużyciom:

```yaml
holdings:
  max-per-player: 10000           # Całkowite przedmioty
  max-materials-per-player: 100   # Różne materiały
```

### Co się dzieje przy limitach?

- **Osiągnięto max przedmiotów** → Nadmiar trafia do kolejki dostaw
- **Osiągnięto max materiałów** → Nie można kupić nowych typów materiałów

Sprawdź swoje limity:
```
/market holdings stats
```

---

## Konfiguracja

```yaml
holdings:
  # Włącz/wyłącz system zasobów
  enabled: true
  
  # Maksymalna całkowita liczba przedmiotów jaką gracz może posiadać
  max-per-player: 10000
  
  # Maksymalna liczba różnych materiałów na gracza
  max-materials-per-player: 100
  
  # Tryb: VIRTUAL (nowy) lub LEGACY (stary bezpośrednio-do-ekwipunku)
  mode: VIRTUAL
```

### Opcje Trybu

| Tryb | Zachowanie |
|------|----------|
| **VIRTUAL** | Przedmioty trafiają do zasobów, wypłata ręczna |
| **LEGACY** | Przedmioty trafiają bezpośrednio do ekwipunku (stare zachowanie) |

---

## Integracja z Dashboardem Web

Zasoby działają bezproblemowo z dashboardem web:

### Zobacz Zasoby Online

```
GET /api/holdings
```

### Wypłać z Przeglądarki

Kliknij przycisk 📤 obok dowolnego zasobu, lub użyj "Wypłać Wszystko".

Zmiany synchronizują się natychmiast z grą.

---

## Migracja z Legacy

Jeśli aktualizujesz ze starszej wersji:

1. Istniejące przedmioty w ekwipunku pozostają w ekwipunku
2. Nowe zakupy trafiają do zasobów
3. Brak potrzeby migracji danych

Aby użyć starego zachowania:
```yaml
holdings:
  mode: LEGACY
```

---

## Wskazówki

<Tip>
**Trzymaj dla Zysku**  
Nie wypłacaj, dopóki nie potrzebujesz przedmiotów. Zasoby pokazują twój P/L w czasie rzeczywistym!
</Tip>

<Info>
**Handel Web**  
Dla masowych wypłat używaj dashboardu web. Jest szybszy do zarządzania dużymi portfelami.
</Info>

<Warning>
**Miejsce w Ekwipunku**  
Komendy wypłaty sprawdzają miejsce w ekwipunku. Wyczyść swój ekwipunek przed masowymi wypłatami.
</Warning>

---

## Powiązane Strony

- [GUI Rynku](market-gui) — Panel zasobów w GUI
- [System Dostaw](delivery) — Obsługa nadmiaru
- [REST API](../web-api/rest-api) — Endpointy zasobów
