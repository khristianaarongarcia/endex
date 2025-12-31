---
title: "FAQ"
description: "Częste pytania dotyczące handlu, zasobów i dashboardu webowego."
---

## Jakie wersje Minecraft są wspierane?

The Endex celuje w nowoczesne Paper/Spigot i Java 17. Zobacz README projektu dla dokładnych wspieranych wersji MC.

## Gdzie trafiają zakupione przedmioty?

Większość serwerów używa zalecanego przepływu:

- Zakupy → **Zasoby** (wirtualne)

Jeśli Twój serwer jest skonfigurowany w trybie legacy, zakupy mogą trafiać bezpośrednio do ekwipunku.

## Kupiłem przedmioty, ale ich nie otrzymałem

Sprawdź kolejkę dostaw:

```text
/market delivery list
```

Jeśli Twój ekwipunek/zasoby były pełne, nadmiar może czekać w Dostawach.

## Dashboard webowy mówi, że moja sesja jest nieważna

- Wygeneruj nowy link/token sesji z komendy serwera.
- Upewnij się, że serwer webowy jest włączony i osiągalny.

## Czy mogę udostępnić panel webowy w internecie?

Tak, ale zrób to bezpiecznie:

- Umieść go za HTTPS (reverse proxy)
- Nie udostępniaj linków sesji
- Rozważ dodatkową autoryzację proxy, jeśli potrzebna

Zobacz `docs/REVERSE_PROXY.md`.
