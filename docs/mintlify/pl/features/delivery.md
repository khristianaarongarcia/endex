---
title: "System Dostaw"
description: "Ochrona przed przepełnieniem, aby zakupy nigdy nie znikały, gdy magazyn jest pełny."
---

Dostawa to siatka bezpieczeństwa dla zakupów, które nie mogą zostać umieszczone w docelowym magazynie (zasoby lub ekwipunek).

## Jaki problem rozwiązuje

Jeśli docelowy magazyn jest pełny, pozostałe przedmioty trafiają do **kolejki dostaw** zamiast zostać utracone.

## Typowy przepływ

```text
Kup -> Próba depozytu -> Przepełnienie -> Kolejka dostaw -> Odbierz później
```

## Komendy gracza (typowe)

```text
/market delivery list
/market delivery claim <material> [ilość]
/market delivery claim-all
```

<Warning>
Odbieranie dostaw zazwyczaj wymaga wolnego miejsca w ekwipunku.
</Warning>

## Wskazówki

<Info>
Jeśli gracz zgłasza „moje przedmioty zniknęły”, sprawdź najpierw jego kolejkę dostaw.
</Info>

### Odbierz Wszystko

Odbierz wszystkie oczekujące dostawy:

```
/market delivery claim-all
```

Odbiera do ekwipunku (nie zasobów) do dostępnego miejsca.

### GUI Dostaw

Otwórz GUI dostaw bezpośrednio:

```
/market delivery gui
```

---

## GUI Dostaw

### Interfejs

```
┌─────────────────────────────────────────────────┐
│              OCZEKUJĄCE DOSTAWY                  │
│              Razem: 232 przedmioty              │
├─────────────────────────────────────────────────┤
│                                                 │
│  [DIAMOND] ×40      [EMERALD] ×128              │
│  Klik: Odbierz All  Klik: Odbierz All           │
│  Prawy: Odbierz 64  Prawy: Odbierz 64           │
│                                                 │
│  [GOLD] ×64                                     │
│  Klik: Odbierz All                              │
│                                                 │
├─────────────────────────────────────────────────┤
│      ◀ Powrót    │     📦 Odbierz Wszystko     │
└─────────────────────────────────────────────────┘
```

### Interakcje

| Akcja | Wynik |
|--------|--------|
| **Lewy-klik** przedmiot | Odbierz wszystko z tego materiału |
| **Prawy-klik** przedmiot | Odbierz jeden stack (64) |
| **Przycisk Odbierz Wszystko** | Odbierz wszystko co się zmieści |

---

## Dostęp z GUI Rynku

Główne GUI Rynku pokazuje status dostaw:

1. Otwórz `/market`
2. Spójrz na slot 51 (ikona Skrzyni Kresu)
3. Odznaka pokazuje liczbę oczekujących przedmiotów
4. Kliknij aby otworzyć panel dostaw

---

## Konfiguracja

```yaml
delivery:
  # Włącz system dostaw
  enabled: true
  
  # Automatycznie odbieraj dostawy przy logowaniu gracza
  auto-claim-on-login: false
  
  # Maksymalna liczba oczekujących przedmiotów na gracza
  max-pending-per-player: 100000
```

### Auto-Odbiór przy Logowaniu

Gdy włączone:
- Gracz dołącza do serwera
- System sprawdza oczekujące dostawy
- Automatycznie odbiera do ekwipunku
- Gracz powiadamiany o odebranych przedmiotach

```yaml
delivery:
  auto-claim-on-login: true
```

---

## Przechowywanie

Dostawy są przechowywane w bazie danych SQLite:

```
plugins/TheEndex/deliveries.db
```

Funkcje:
- Bezpieczeństwo transakcji
- Zapobiega exploitom duplikacji
- Przetrwa restarty serwera
- Śledzenie na gracza

---

## Limity

### Maksymalne Oczekujące

Skonfiguruj maksymalną liczbę oczekujących przedmiotów na gracza:

```yaml
delivery:
  max-pending-per-player: 100000
```

**Gdy limit osiągnięty:**
- Zakup nieudany
- Gracz powiadomiony
- Musi najpierw odebrać istniejące dostawy

### Miejsce w Ekwipunku

Podczas odbierania:
- System sprawdza dostępne sloty w ekwipunku
- Odbiera tylko to co się zmieści
- Reszta zostaje w kolejce

---

## Zapobieganie Utracie Przedmiotów

System dostaw ma wiele zabezpieczeń:

| Zabezpieczenie | Ochrona |
|-----------|------------|
| **Najpierw baza danych** | Usuwa z DB przed daniem przedmiotów |
| **Bezpieczeństwo transakcji** | Operacje atomowe zapobiegają korupcji |
| **Sprawdzanie miejsca** | Nie odbierze więcej niż się zmieści |
| **Ochrona wylogowania** | Przedmioty bezpieczne jeśli się rozłączysz |

---

## Dashboard Web

Dostawy są dostępne z dashboardu web:

### Zobacz Dostawy

```
GET /api/deliveries
```

### Odbierz z Przeglądarki

```
POST /api/deliveries/claim
{
  "material": "DIAMOND",
  "amount": 64
}
```

Lub odbierz wszystko:
```
POST /api/deliveries/claim
{}
```

---

## Rozwiązywanie Problemów

### Przedmioty Nie Pojawiają Się

1. Sprawdź `/market delivery list`
2. Zweryfikuj `delivery.enabled: true` w konfiguracji
3. Sprawdź czy limit max-pending osiągnięty

### Nie Można Odebrać Przedmiotów

1. Sprawdź miejsce w ekwipunku
2. Spróbuj odebrać mniejsze ilości
3. Wyczyść ekwipunek i spróbuj ponownie

### Utracone Dostawy

Dostawy nigdy nie powinny zostać utracone. Jeśli przedmioty zniknęły:
1. Sprawdź logi serwera pod kątem błędów
2. Zweryfikuj czy plik bazy danych istnieje
3. Skontaktuj się ze wsparciem z logami

---

## Wskazówki

<Tip>
**Regularne Odbiory**  
Nie pozwól dostawom się piętrzyć. Odbieraj regularnie aby utrzymać dostępne miejsce.
</Tip>

<Info>
**Najpierw Wyczyść Ekwipunek**  
Przed masowymi odbiorami, opróżnij swój ekwipunek dla maksymalnego miejsca.
</Info>

<Warning>
**Zasoby vs Dostawa**  
Przedmioty odebrane z dostawy trafiają do EKWIPUNKU, nie zasobów. Inaczej niż przy zakupach!
</Warning>

---

## Powiązane Strony

- [Wirtualne Zasoby](holdings) — Główny system przechowywania
- [GUI Rynku](market-gui) — Dostęp do panelu dostaw
- [REST API](../web-api/rest-api) — Endpointy dostaw
