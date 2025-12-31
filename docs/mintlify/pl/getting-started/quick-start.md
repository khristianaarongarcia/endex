---
title: "Szybki Start"
description: "Otwórz rynek, kupuj, sprzedawaj i wypłacaj w kilka minut."
---

## 1) Otwórz rynek

```text
/market
```

## 2) Kup przedmiot

W GUI kliknij przedmiot i wybierz ilość.

Lub użyj komendy (jeśli włączone na twoim serwerze):

```text
/market buy diamond 10
```

W zależności od konfiguracji, zakupione przedmioty trafiają do **Zasobów** (zalecane) lub bezpośrednio do ekwipunku (tryb legacy).

## 3) Sprawdź swoje zasoby

```text
/market holdings
```

## 4) Wypłać do ekwipunku

```text
/market withdraw diamond
/market withdraw diamond 64
/market withdraw all
```

<Tip>
Wypłacaj tylko wtedy, gdy faktycznie potrzebujesz przedmiotów. Trzymanie przedmiotów w zasobach sprawia, że handel jest płynniejszy i zmniejsza ryzyko zgubienia przedmiotów.
</Tip>

## 5) Sprzedaj przedmiot

```text
/market sell diamond 5
```

## Co dalej

- Poznaj algorytm: [Dynamiczne Ceny](../features/pricing)
- Zrozum przechowywanie: [Wirtualne Zasoby](../features/holdings)
- Nigdy nie trać nadmiaru: [System Dostaw](../features/delivery)

## Dla Administratorów

### Uruchamianie Wydarzeń

Wywołuj wydarzenia rynkowe, które wpływają na ceny:

```
/market event list           # Zobacz dostępne wydarzenia
/market event ore_rush       # Rozpocznij wydarzenie
/market event end ore_rush   # Zakończ wcześniej
/market event clear          # Zakończ wszystkie wydarzenia
```

### Sprawdzanie Statusu

```
/endex version    # Wersja pluginu i status
/endex reload     # Przeładuj konfigurację
```

### Zarządzanie Dashboardem Web

```
/endex web              # Pobierz swój link sesji
/endex webui export     # Eksportuj UI do dostosowania
/endex webui reload     # Przeładuj własne UI
```

---

## Zrozumienie Ekonomii

### Jak Działają Ceny

The Endex używa prawdziwej podaży i popytu:

- **Gracze kupują** → Popyt rośnie → **Cena idzie W GÓRĘ** 📈
- **Gracze sprzedają** → Podaż rośnie → **Cena idzie W DÓŁ** 📉
- **Nikt nie handluje** → Cena powoli wraca do poziomu bazowego

### Wpływy na Ceny

Ceny mogą być kształtowane przez:

1. **Aktywność Handlową** — Transakcje kupna/sprzedaży
2. **Ekwipunki Graczy** — Co gracze trzymają przy sobie
3. **Magazyny Świata** — Przedmioty w skrzyniach na całym serwerze
4. **Wydarzenia Rynkowe** — Wyzwalane przez admina mnożniki

### System Zasobów

System wirtualnych zasobów:

```
Kup Przedmioty → Wirtualne Zasoby → Wypłać do Ekwipunku
                ↓
        Śledź Zysk/Stratę
```

Korzyści:
- Nigdy nie trać przedmiotów przez pełny ekwipunek
- Śledź swoją średnią cenę zakupu
- Zobacz zysk/stratę w czasie rzeczywistym
- Działa z dashboardem web

---

## Typowe Scenariusze

### Day Trading

1. Obserwuj ceny za pomocą `/market price <item>`
2. Kupuj tanio: `/market buy diamond 64`
3. Czekaj na wzrost ceny
4. Wypłać: `/market withdraw diamond`
5. Sprzedaj drogo: `/market sell diamond 64`

### Handel Hurtowy (Dashboard Web)

1. Pobierz link sesji: `/endex web`
2. Otwórz w przeglądarce
3. Handluj wieloma przedmiotami szybko
4. Monitoruj wykresy cen na żywo

### Arbitraż Wydarzeń

1. Obserwuj wydarzenia: `/market event list`
2. Kupuj objęte przedmioty przed wydarzeniem
3. Czekaj na skok ceny podczas wydarzenia
4. Sprzedaj w szczycie

---

## Wskazówki dla Sukcesu

<Tip>
**Kupuj Tanio, Sprzedawaj Drogo**  
Obserwuj trendy cenowe i kupuj, gdy ceny spadają. Sprzedawaj, gdy popyt rośnie.
</Tip>

<Info>
**Używaj Systemu Zasobów**  
Nie wypłacaj, dopóki nie potrzebujesz przedmiotów. Zasoby automatycznie śledzą twój zysk/stratę.
</Info>

<Warning>
**Uważaj na Wydarzenia**  
Wydarzenia rynkowe mogą dramatycznie wpłynąć na ceny. Sprawdzaj `/market event list` regularnie.
</Warning>

<Tip>
**Dashboard Web**  
Dla poważnego handlu używaj dashboardu web. Jest szybszy i pokazuje wykresy.
</Tip>

---

## Następne Kroki

- [Dynamiczne Ceny](../features/pricing) — Zrozum algorytm
- [Wirtualne Zasoby](../features/holdings) — Opanuj system zasobów
- [Dashboard Web](../web-api/dashboard) — Handluj z przeglądarki
- [Odniesienie do Komend](../reference/commands) — Wszystkie komendy wyjaśnione
