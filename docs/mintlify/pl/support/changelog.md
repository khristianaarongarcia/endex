---
title: "Dziennik Zmian"
description: "Historia wersji i notatki wydania dla The Endex."
---

Pełne notatki wydania znajdują się w `CHANGELOG.md` repozytorium.

<Tip>
Przed aktualizacją, zrób kopię zapasową `plugins/TheEndex/` i przejrzyj notatki wydania pod kątem zmian łamiących kompatybilność.
</Tip>

## Gdzie szukać

- `CHANGELOG.md` (główny katalog repozytorium) — pełna historia
- GitHub Releases — spakowane buildy i notatki

---

## Wersja 1.5.7-dec1038 — 31 Grudnia 2025

### Wsparcie dla Języka Polskiego

Pełne tłumaczenie na język Polski:

- **Plik Językowy Pluginu** — Nowy `lang/pl.yml` z pełnymi polskimi tłumaczeniami
- **Tłumaczenie Konfiguracji** — Nowy `config_translations/config_pl.yml` z polskimi komentarzami
- **Dokumentacja** — 18 w pełni przetłumaczonych stron w `docs/mintlify/pl/`
- **Nawigacja** — Opcja języka polskiego na stronie dokumentacji

### Automatyczne Wyodrębnianie Tłumaczeń Konfiguracji

Przetłumaczone pliki konfiguracji są teraz automatycznie wyodrębniane przy pierwszym uruchomieniu pluginu:

- **10 Dostępnych Języków** — Angielski, Chiński, Hiszpański, Francuski, Niemiecki, Japoński, Koreański, Portugalski, Rosyjski, Polski
- **Lokalizacja** — Wyodrębnione do `plugins/TheEndex/config_translations/`
- **Użycie** — Skopiuj preferowaną konfigurację językową do `config.yml`

### Zoptymalizowana Domyślna Konfiguracja

Konfiguracja teraz domyślnie ustawia **minimalne zużycie zasobów**:

- **Skanowanie Magazynów Świata** — WYŁĄCZONE (największa oszczędność CPU)
- **Ceny Oparte na Ekwipunku** — WYŁĄCZONE
- **Zapisuj-Przy-Każdej-Aktualizacji** — WYŁĄCZONE (zmniejszone I/O dysku)
- **Interwał Aktualizacji** — Zwiększony do 120 sekund (było 60)
- **Przewodnik Optymalizacji** — Widoczny nagłówek w config.yml wyjaśniający wszystkie ustawienia

<Info>
Aby włączyć pełne funkcje (dynamiczne ceny niedoboru, wpływ ekwipunku), zobacz nagłówek config.yml, aby dowiedzieć się, które ustawienia zmienić.
</Info>

---

## Wersja 1.5.7-dec1022 — 30 Grudnia 2025

### Tłumaczenie Dashboardu Web

Integracja Google Translate dla dashboardu webowego:

- **26+ Języków** — Angielski, Chiński, Hiszpański, Francuski, Niemiecki, Japoński, Koreański, Portugalski, Rosyjski, Włoski, Tajski, Wietnamski, Indonezyjski, Turecki, Polski, Holenderski, Szwedzki, Duński, Fiński, Czeski, Rumuński, Ukraiński, Hindi, Bengalski, Tagalski, Arabski
- **Wybór Języka** — Menu rozwijane w nagłówku między nazwą gracza a przełącznikiem motywu
- **Stylizowany Ciemny Motyw** — Widget tłumaczenia pasuje do motywu dashboardu

### Wskaźniki Wydajności Konfiguracji

Dodano tagi `[PERF: LOW/MEDIUM/HIGH]` do opcji config.yml:

- Pomaga właścicielom serwerów zrozumieć wpływ każdej funkcji na wydajność
- Zobacz natychmiast, które opcje są kosztowne

### Przetłumaczone Pliki Konfiguracyjne

Wstępnie przetłumaczone pliki konfiguracyjne w 9 językach dostępne w `resources/translations/`:

- Chiński (Uproszczony), Hiszpański, Francuski, Niemiecki, Japoński, Koreański, Portugalski, Rosyjski, Arabski

### Poprawki Błędów

- **GUI ArrayIndexOutOfBoundsException** — Naprawiono błąd "Index 33 out of bounds for length 27" w panelu szczegółów rynku
- Rozmiar ekwipunku zwiększony z 27 do 36 slotów, aby pomieścić wszystkie przyciski

---

## Wersja 1.5.7 — 29 Grudnia 2025

### Sprzedaż z Zasobów

Gracze mogą sprzedawać przedmioty bezpośrednio z wirtualnych zasobów:

- **GUI Rynku** — Przyciski "Sprzedaj 1 z Zasobów" i "Sprzedaj Wszystko z Zasobów" w panelu szczegółów
- **Własny Sklep** — Przedmioty vanilla mogą być sprzedawane z zasobów, gdy ekwipunek jest pusty
- **Web API** — Nowy endpoint `POST /api/sell-holdings`
- **Komenda** — Nowa komenda `/market sellholdings <material> <amount>`

### Kompatybilność z Serwerami Arclight/Hybrid

Pełne wsparcie dla Arclight, Mohist i innych serwerów hybrydowych:

- Naprawiono problemy z rozpoznawaniem pluginu
- Dodano prosty logger SLF4J
- Scalono pliki usług w JAR Spigot
- Naprawiono klikalne linki na czacie
- Naprawiono rejestrację rozszerzenia PlaceholderAPI

---

## Wersja 1.5.6-DEC0759 — 28 Grudnia 2025

### Kompatybilność z 1.20.1

- Obniżono `api-version` z `'1.21'` na `'1.20'` dla wsparcia serwerów 1.20.x

---

## Wersja 1.5.6-DEC0338 — 22 Grudnia 2025

### Poprawka Folderu Danych

- Przywrócono folder pluginu do `plugins/TheEndex/` (był przypadkowo `plugins/Endex/`)

---

## Wersja 1.5.6-DEC0252 — 22 Grudnia 2025

### Poprawki Komend i GUI

- **Komenda `/shop`** — Naprawiono rejestrację komendy
- **Konflikt GUI** — The Endex nie przechwytuje już kliknięć GUI innych pluginów
- **Aliasy Komend** — Dodano `/shop`, `/m`, `/trade`, `/exchange`, `/bazaar`

---

## Wersja 1.5.6 — 21 Grudnia 2025

### Edytor Układu w Grze

Kompletny wizualny edytor GUI dla układów własnego sklepu:

- **5 Trybów Edytora** — Umieść Kategorię, Umieść Dekorację, Umieść Przycisk, Edytuj Slot, Usuń Slot
- **Własne Przedmioty** — Dodaj dowolny materiał jako dekorację z własnymi nazwami/opisami
- **Podgląd na Żywo** — Zobacz zmiany w czasie rzeczywistym
- **Edycja Prawym Przyciskiem** — Dostosuj przez wprowadzanie na czacie
- **Zapis Jednym Kliknięciem** — Zapisuje bezpośrednio do konfiguracji sklepu

#### Otwieranie Edytora

```
/endex shop editor <shop-id>
```

### Własne Ikony Kategorii

Kategorie obsługują w pełni dostosowane nazwy wyświetlane i opisy.

---

## Wersja 1.5.5 — 20 Grudnia 2025

### Kompatybilność z Pluginami Ekonomii

Naprawiono błąd "Economy unavailable":

- **Opóźnione Ponowienie** — 2-sekundowe ponowienie dla późno ładujących się pluginów ekonomii
- **Rozszerzone Zależności** — Popularne pluginy ekonomii dodane do `softdepend`

---

## Wersja 1.5.4 — 19 Grudnia 2025

### Integracja Metryk bStats

Analityka pluginu do śledzenia wzorców użycia:

- **ID Pluginu:** 28421
- **Własne Wykresy:**
  - Tryb przechowywania (yaml/sqlite)
  - Tryb sklepu (DEFAULT/CUSTOM)
  - Status Web UI (włączone/wyłączone)
  - Status Zasobów (włączone/wyłączone)
  - Liczba śledzonych przedmiotów (1-10, 11-25, 26-50, 51-100, 100+)

### Ulepszenia GUI Własnego Sklepu

- **Przycisk Zasobów** — Zobacz wirtualne zasoby bezpośrednio ze stron kategorii (slot 45)
- **Przycisk Sortowania** — Przełączaj opcje sortowania (Nazwa, Cena, Zmiana) na stronach kategorii (slot 53)
- **Automatyczne Wypełnianie oparte na Filtrach** — Kategorie teraz używają właściwości `filter` do automatycznego wypełniania przedmiotów pasujących do kryteriów

### Ulepszenia Domyślnego GUI Rynku

- Usunięto przycisk Ilości dla czystszego interfejsu
- Przycisk Sortowania przeniesiony do slotu 49 dla lepszego układu
- Naprawiono kolejność sortowania: Cena i Zmiana teraz sortują malejąco (najwyższe pierwsze)

### Poprawki Błędów

- **Kliknięcia Hotbara** — Naprawiono problem, gdzie gracze nie mogli interweniować ze swoim hotbarem, gdy GUI było otwarte
- **Kolejność Sortowania** — Sortowanie Ceny i Zmiany teraz poprawnie pokazuje najwyższe wartości jako pierwsze
- **Przycisk Zasobów** — Ikona zasobów w GUI Własnego Sklepu teraz poprawnie otwiera panel zasobów
- **Komenda `/ex market`** — Teraz poprawnie otwiera sklep DEFAULT lub CUSTOM w oparciu o konfigurację `shop.mode`

### Techniczne

- Zależność bStats z relokacją Shadow (`org.bstats` → `org.lokixcz.theendex.bstats`)
- `MarketGUI.openHoldings()` upublicznione dla dostępu między GUI

---

## Wersja 1.5.3 — 18 Grudnia 2025

### Integracja PlaceholderAPI

Pełne wsparcie dla PlaceholderAPI z 30+ placeholderami:

- **Dane Rynkowe:** `%endex_price_<MATERIAL>%`, `%endex_trend_<MATERIAL>%`, `%endex_change_<MATERIAL>%`
- **Top Przedmioty:** `%endex_top_price_<1-10>%`, `%endex_bottom_price_<1-10>%`
- **Wzrosty/Spadki:** `%endex_top_gainer_<1-10>%`, `%endex_top_loser_<1-10>%`
- **Zasoby:** `%endex_holdings_total%`, `%endex_holdings_count%`
- **Ranking:** `%endex_top_holdings_<1-10>%`
- **Statystyki:** `%endex_total_items%`, `%endex_total_volume%`, `%endex_active_events%`

Zobacz pełną [Referencję PlaceholderAPI](../features/placeholderapi) dla wszystkich placeholderów.

### Sprawdzanie Aktualizacji

Automatyczne powiadomienia o aktualizacjach:

- Sprawdza API Spigot i Modrinth przy starcie
- Baner w konsoli, gdy dostępne aktualizacje
- Gracze OP powiadamiani przy dołączeniu

```yaml
update-checker:
  enabled: true
  notify-ops: true
```

### Dostosowywanie GUI

Pełne dostosowywanie układu GUI przez pliki konfiguracyjne w folderze `guis/`:

- `market.yml` — Główny interfejs rynku
- `details.yml` — Panel szczegółów przedmiotu
- `holdings.yml` — Panel wirtualnych zasobów
- `deliveries.yml` — Panel kolejki dostaw

Dostosuj tytuły, rozmiary, pozycje slotów, kategorie i więcej.

### Aliasy Komend

Twórz własne skróty komend przez `commands.yml`:

```yaml
aliases:
  shop: "market"
  stock: "market holdings"
  prices: "market top"
```

---

## Wersja 1.5.2 — 17 Grudnia 2025

### Zoptymalizowany Skaner Świata

Całkowite przepisanie z inteligentnym cache'owaniem chunków:

- Cache'owanie na poziomie chunka z konfigurowalnym wygasaniem
- Śledzenie zmian oparte na zdarzeniach
- Trwałość na dysku między restartami
- 80-90% redukcja zbędnego skanowania

### Poprawka GUI (MC 1.21+)

Naprawiono krytyczny błąd, gdzie przedmioty rynkowe mogły być zabierane, a kliknięcia nie działały na serwerach Minecraft 1.21+.

- Śledzenie stanu GUI oparte na UUID
- Zastępuje zawodne dopasowywanie tytułów

---

## Wersja 1.5.1 — 17 Grudnia 2025

### Skaner Magazynów Świata

Ceny teraz reagują na WSZYSTKIE przedmioty przechowywane na Twoim serwerze:

- Skanuje skrzynie, beczki, shulker boxy i więcej
- Globalne śledzenie przedmiotów dla prawdziwej ekonomii całego serwera
- Ochrona przed manipulacją z limitami na chunk
- Dławienie zależne od TPS

---

## Wersja 1.5.0 — 17 Grudnia 2025

### System Wirtualnych Zasobów

Całkowite przeprojektowanie architektury zasobów:

- **Bezpośrednio do Zasobów** — Zakupy trafiają do wirtualnych zasobów, nie ekwipunku
- **Duża Pojemność** — Przechowuj do 10,000 przedmiotów na gracza
- **100 Materiałów** — Śledź do 100 różnych typów przedmiotów
- **Śledzenie Kosztów** — Średnia podstawa kosztowa dla dokładnego P/L
- **Wypłaty FIFO** — System Pierwsze-Weszło-Pierwsze-Wyszło

#### Panel GUI Zasobów

Nowy interfejs w grze:

- Zobacz wszystkie zasoby z ilością, kosztem i P/L
- Lewy-klik: Wypłać wszystko z materiału
- Prawy-klik: Wypłać jeden stack
- Przycisk "Wypłać Wszystko"
- Wyświetlanie statystyk portfela

#### Komendy Zasobów

```
/market holdings          # Zobacz zasoby
/market withdraw <item>   # Wypłać konkretny przedmiot
/market withdraw all      # Wypłać wszystko
```

#### Wsparcie Web UI

- Indywidualne przyciski wypłaty (📤)
- Przycisk "Wypłać Wszystko"
- Informacje zwrotne w czasie rzeczywistym
- Zaktualizowane odznaki

#### Nowe Uprawnienia

- `theendex.holdings` — Dostęp do zasobów
- `theendex.withdraw` — Wypłata z zasobów

### Zmiany

- **Wsparcie Minecraft 1.21** — Zaktualizowana wersja API
- **Przeprojektowanie Przepływu Zakupu** — Zakupy najpierw do zasobów
- **Zmiana Nazwy GUI** — "Oczekujące Dostawy" → "Moje Zasoby"

### Notatki Migracyjne

<Warning>
**Zmiana Łamiąca:** Przepływ zakupu domyślnie używa wirtualnych zasobów.
Ustaw `holdings.mode: LEGACY`, aby przywrócić poprzednie zachowanie.
</Warning>

---

## Wersja 1.4.0 — 30 Października 2025

### System Wirtualnych Dostaw

Gdy ekwipunek jest pełny, zakupy trafiają do kolejki dostaw:

- Przechowywanie oparte na SQLite
- Odbiór FIFO
- Bezpieczeństwo transakcji
- Zapobieganie duplikacji

### Integracja GUI

- Przycisk Skrzyni Kresu pokazuje liczbę oczekujących
- Pełny panel dostaw
- Przyciski odbioru (wszystko/stack)
- Masowy odbiór

### Komendy Dostaw

```
/market delivery list
/market delivery claim <material> [amount]
/market delivery claim-all
/market delivery gui
```

### Endpointy API

- `GET /api/deliveries`
- `POST /api/deliveries/claim`

### Poprawki Błędów

- **Krytyczny:** Naprawiono błąd pętli zakupu (64 przedmioty → 1 dostarczony)

### Bezpieczeństwo

- Ochrona przed wyścigami (race condition)
- Zapobieganie duplikacji
- Bezpieczeństwo transakcji

---

## Wersja 1.3.1 — 30 Października 2025

### Krytyczna Poprawka

- **Sprawdzanie ekwipunku przed płatnością** — Wcześniej kupno nadmiarowych przedmiotów pobierało pełną kwotę, ale dostarczało tylko to, co się zmieściło
- Zakup teraz ograniczony do dostępnego miejsca
- Jasna informacja zwrotna, gdy zamówienia są ograniczone

---

## Wersja 1.3.0 — 26 Września 2025

### Ulepszenia Bezpieczeństwa

- Usunięto refleksyjny dostęp do pól prywatnych
- Token sesji migruje do nagłówka Authorization
- Token URL usuwany automatycznie
- Wsparcie dla haszowanych tokenów API (`token-hashes`)
- Porównywanie w stałym czasie

### Konfiguracja

Nowe `web.api.token-hashes` dla bezpiecznego przechowywania tokenów.

---

## Wersja 1.2.0 — 25 Września 2025

### Funkcje

- Własne nadpisanie Web UI
- Ujednolicony widok rynku
- Ulepszenia filtrów
- Ulepszona konfiguracja

---

## Wersja 1.1.0 — 29 Sierpnia 2025

### Funkcje

- Ikony resource packa
- Zakładka dodatków
- Dokumentacja HTTP API
- Ulepszenia wydajności

---

## Wersja 1.0.0 — 28 Sierpnia 2025

### Wstępne Wydanie

- Silnik dynamicznych cen
- GUI Rynku
- Dashboard webowy
- REST API
- System wydarzeń
- Śledzenie inwestycji
- Wsparcie dodatków

---

## Przewodnik Aktualizacji

### Z 1.4.x do 1.5.x

1. Zrób kopię zapasową `config.yml` i bazy danych
2. Podmień JAR pluginu
3. Zrestartuj serwer
4. Przejrzyj nową sekcję konfiguracji `holdings`
5. Rozważ migrację dostaw do zasobów

### Z 1.3.x do 1.4.x

1. Podmień JAR pluginu
2. Zrestartuj serwer
3. Nowa baza `deliveries.db` utworzona automatycznie
4. Skonfiguruj ustawienia dostaw w `config.yml`

### Ogólne Aktualizacje

1. Zawsze rób kopię zapasową przed aktualizacją
2. Czytaj notatki wydania pod kątem zmian łamiących kompatybilność
3. Sprawdź konfigurację pod kątem nowych opcji
4. Testuj najpierw na serwerze testowym

---

## Powiązane Strony

- [Rozwiązywanie Problemów](troubleshooting) — Typowe problemy
- [Konfiguracja](../reference/configuration) — Referencja konfiguracji
- [Instalacja](../getting-started/installation) — Przewodnik instalacji
