---
title: "Rozwiązywanie Problemów"
description: "Napraw typowe problemy z instalacją, handlem i dashboardem webowym."
---

## Plugin się nie ładuje

Lista kontrolna:

- Serwer jest wspieranym forkiem Paper/Spigot
- Java 17 jest zainstalowana
- Jar znajduje się w `plugins/`
- Vault jest zainstalowany (dla ekonomii)

Sprawdź logi pod kątem błędów.

## GUI Rynku się nie otwiera

- Zweryfikuj czy masz uprawnienia do używania komend rynku
- Spróbuj uruchomić:

```text
/endex
/endex market
/market
```

## Dashboard webowy się nie ładuje

Lista kontrolna:

- `web.host` i `web.port` są skonfigurowane
- Port jest otwarty na hoście/firewallu
- Używasz poprawnego URL

Debuguj używając API:

```text
GET /api/session
GET /api/items
```

## Nieważna lub wygasła sesja

- Wygeneruj nowy token/link sesji
- Sprawdź ustawienia limitu czasu sesji (jeśli skonfigurowane)

## Przedmioty nie pojawiają się / brakujące przedmioty

- Sprawdź czy materiał nie jest na czarnej liście
- Sprawdź nadmiar dostaw:

```text
/market delivery list
```

## Przed poproszeniem o pomoc

- Odtwórz na czystej konfiguracji jeśli to możliwe
- Skopiuj odpowiednie logi konsoli
- Dołącz wersję pluginu i wersję serwera
