---
title: "Inwestycje"
description: "Zablokuj przedmioty, zarabiaj APR i odbierz później."
---

Inwestycje pozwalają graczom zablokować przedmioty z zasobów w pozycji przypominającej certyfikat, która z czasem nalicza **APR** (Roczną Stopę Procentową).

## Jak to działa

1. Gracz inwestuje ilość materiału.
2. Przedmioty są usuwane z płynnych zasobów.
3. Z czasem inwestycja nalicza odsetki.
4. Gracz odbiera i otrzymuje oryginalne przedmioty z powrotem (do zasobów) plus odsetki (zazwyczaj wypłacane w monetach).

## Konfiguracja (typowa)

```yaml
investments:
  enabled: true
  apr-percent: 5.0
```

## Komendy (typowe)

```text
/market invest buy <material> <amount>
/market invest list
/market invest redeem-all
```

<Warning>
Inwestycje nie są wolne od ryzyka, jeśli Twój serwer wiąże wartość wykupu z aktualną wartością rynkową; ceny mogą się zmieniać, gdy jesteś zainwestowany.
</Warning>

## Lista Inwestycji

```
/market invest list
```

Wynik:

```
=== Twoje Inwestycje ===
#1: 100 Diamond
    Zakupiono: $10,000.00
    Obecna Wartość: $10,125.00 (+1.25%)
    Czas Trzymania: 3 dni, 2 godziny
    Dojrzałość: ✓ Gotowe do odbioru

#2: 500 Iron Ingot  
    Zakupiono: $2,500.00
    Obecna Wartość: $2,531.25 (+1.25%)
    Czas Trzymania: 2 godziny
    Dojrzałość: ✗ pozostało 22 godziny
```

### Odbierz Inwestycje

Odbierz dojrzałe inwestycje:

```
/market invest redeem-all
```

Odbiera tylko inwestycje, które przekroczyły minimalny czas trzymania.

---

## Obliczanie APR

Odsetki naliczane są na podstawie Rocznej Stopy Procentowej (APR):

```
Wartość = Kapitał × (1 + APR × (Dni Trzymania / 365))
```

### Przykład

- Inwestycja: 100 diamentów po $100 każdy = $10,000
- APR: 5%
- Czas trzymania: 30 dni

```
Odsetki = $10,000 × 0.05 × (30 / 365) = $41.10
Całkowita Wartość = $10,041.10
```

---

## Dojrzałość

Inwestycje mają **minimalny czas trzymania** przed odbiorem:

```yaml
investments:
  min-hold-time: 3600  # sekundy (1 godzina)
```

- **Przed dojrzałością** — Nie można odebrać
- **Po dojrzałości** — Można odebrać w każdej chwili, nadal zarabia

---

## Konfiguracja

```yaml
investments:
  # Włącz system inwestycji
  enabled: true
  
  # Roczna Stopa Procentowa (5.0 = 5%)
  apr: 5.0
  
  # Minimalny czas trzymania przed odbiorem (sekundy)
  min-hold-time: 3600
  
  # Maksymalna liczba inwestycji na gracza
  max-per-player: 10
  
  # Maksymalna wartość na inwestycję
  max-value: 100000
```

---

## Strategia Inwestycyjna

### Długoterminowe Trzymanie

Wyższe APR oznacza, że dłuższe trzymanie jest bardziej opłacalne:

| Czas Trzymania | Zwrot 5% APR |
|-----------|---------------|
| 1 dzień | +0.014% |
| 7 dni | +0.096% |
| 30 dni | +0.411% |
| 90 dni | +1.233% |
| 365 dni | +5.000% |

### Spekulacja Cenowa

Inwestycje blokują cenę zakupu. Jeśli cena rynkowa wzrośnie:

- Wartość Twojej inwestycji odzwierciedla wyższą cenę
- Odbierz za więcej monet niż zainwestowałeś

<Warning>
Jeśli cena rynkowa spadnie, wartość wykupu również spadnie. Inwestycje nie są wolne od ryzyka!
</Warning>

---

## Odbiór

Kiedy odbierasz:

1. Przedmioty wracają do Twoich **zasobów** (nie ekwipunku)
2. Otrzymujesz naliczoną wartość w monetach
3. Inwestycja jest zamykana

### Co Otrzymujesz

```
Odbiór = (Oryginalne Przedmioty) + (Odsetki w monetach)
```

Przykład:
- Zainwestowano: 100 diamentów po $100 = $10,000
- Zarobione APR: $200
- **Otrzymujesz:** 100 diamentów + $200 monet

---

## Dostęp z GUI

Dostęp do inwestycji z GUI Rynku:

1. Otwórz `/market`
2. Kliknij przycisk **Inwestycje**
3. Zobacz wszystkie aktywne inwestycje
4. Kliknij, aby odebrać dojrzałe

---

## Uprawnienia

| Uprawnienie | Opis |
|------------|-------------|
| `theendex.invest` | Tworzenie i zarządzanie inwestycjami |

---

## Wskazówki

<Tip>
**Dywersyfikuj**  
Nie wkładaj całego majątku w jedną inwestycję. Rozłóż na wiele materiałów.
</Tip>

<Info>
**Obserwuj Rynek**  
Inwestuj w przedmioty, które Twoim zdaniem wzrosną na wartości, dla podwójnych zysków (APR + wzrost ceny).
</Info>

<Warning>
**Płynność**  
Zainwestowane przedmioty są zablokowane. Nie inwestuj przedmiotów, których możesz wkrótce potrzebować.
</Warning>

---

## Powiązane Strony

- [Dynamiczne Ceny](pricing) — Jak ceny wpływają na inwestycje
- [Wirtualne Zasoby](holdings) — Gdzie trafiają odebrane przedmioty
- [Komendy](../reference/commands) — Pełna referencja komend
