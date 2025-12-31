---
title: "Dashboard Web"
description: "Handluj z przeglądarki z cenami na żywo, zasobami i wykresami."
---
The Endex zawiera opcjonalny interfejs webowy do szybkiego handlu.

## Włączanie (serwer)

Ustawienia web znajdują się w `plugins/TheEndex/config.yml`.

```yaml
web:
  enabled: true
  host: "0.0.0.0"
  port: 3434
  sse:
    enabled: false
  websocket:
    enabled: true
```

## Uzyskiwanie linku sesji

Większość serwerów udostępnia komendę generującą link/token sesji.

<Warning>
Linki sesji są osobiste. Każdy z Twoim linkiem/tokenem może handlować jako Ty.
</Warning>

## Reverse proxy (HTTPS)

Jeśli chcesz TLS, umieść dashboard za reverse proxy (Nginx/Caddy).

Zobacz `docs/REVERSE_PROXY.md` w repozytorium.

## Własne nadpisanie UI

Możesz zastąpić wbudowane UI serwując własne pliki statyczne:

```yaml
web:
  custom:
    enabled: true
    root: webui
    reload: false
    export-default: true
```

Zobacz `docs/CUSTOM_WEBUI.md` dla pełnego przepływu pracy.

## Uwagi

- Dashboard uwierzytelnia się za pomocą tokena sesji.
- Opcjonalne aktualizacje na żywo są dostarczane przez WebSocket (zalecane) lub SSE.

---

## Wsparcie Wielojęzyczne

Dashboard zawiera wbudowaną integrację Google Translate dla **26+ języków**:

- Angielski, Chiński, Hiszpański, Francuski, Niemiecki, Japoński, Koreański, Portugalski, Rosyjski
- Włoski, Tajski, Wietnamski, Indonezyjski, Turecki, Polski, Holenderski, Szwedzki
- Duński, Fiński, Czeski, Rumuński, Ukraiński, Hindi, Bengalski, Tagalski, Arabski

<Tip>
Użyj menu rozwijanego języka w nagłówku (pomiędzy nazwą gracza a przełącznikiem motywu), aby zmienić język.
</Tip>

Widget tłumaczenia jest stylizowany tak, aby pasował do ciemnego motywu dashboardu.

---

### Tokeny API

Dla zautomatyzowanego dostępu (boty, integracje):

```yaml
web:
  api:
    # Zwykłe tokeny (niezalecane)
    tokens:
      - "my-secret-token"
      
    # Haszowane tokeny (zalecane)
    token-hashes:
      - "sha256-hash-of-token"
```

Generowanie hasha:
```bash
echo -n "my-secret-token" | sha256sum
```

---

## Uprawnienia

| Uprawnienie | Opis |
|------------|-------------|
| `endex.web.trade` | Dostęp do handlu webowego |
| `endex.web.admin` | Przeglądanie zasobów innych |

Przyznaj dostęp webowy:
```
/lp user <player> permission set endex.web.trade true
```

---

## Dostęp Mobilny

Dashboard jest responsywny mobilnie:

- Działa na telefonach/tabletach
- Interfejs przyjazny dla dotyku
- Responsywny układ

---

## Rozwiązywanie Problemów

### Nie Można Połączyć

1. Sprawdź czy firewall pozwala na port
2. Zweryfikuj ustawienie `host`
3. Spróbuj `127.0.0.1` dla testów lokalnych

### Sesja Nieważna

1. Wygeneruj nową sesję: `/endex web`
2. Sprawdź czy sesja nie wygasła
3. Wyczyść pamięć podręczną przeglądarki

### Wolne Aktualizacje

1. Włącz WebSocket zamiast SSE
2. Sprawdź TPS serwera
3. Zmniejsz `update-interval`

### Ikony Nie Ładują Się

1. Zweryfikuj źródło resource packa
2. Sprawdź `web.icons.enabled: true`
3. Spróbuj innego resource packa

---

## Zaawansowane

### Osadzanie

Osadź dashboard na swojej stronie:

```html
<iframe 
  src="http://your-server:8080/?session=TOKEN" 
  width="100%" 
  height="600">
</iframe>
```

### Integracja API

Zobacz [REST API](rest-api) dla dostępu programistycznego.

### Własne Motywy

Nadpisz zmienne CSS:

```css
:root {
  --primary: #a78bfa;
  --accent: #7c3aed;
  --bg: #0f0f23;
  --fg: #e8e9ea;
}
```

---

## Powiązane Strony

- [REST API](rest-api) — Dokumentacja API
- [Konfiguracja](../reference/configuration) — Pełna konfiguracja web
- [Instalacja](../getting-started/installation) — Wstępna konfiguracja
