---
title: "Instalacja"
description: "Zainstaluj The Endex, uruchom serwer i zweryfikuj działanie."
---

## Wymagania

- Serwer kompatybilny z Paper/Spigot (zalecany Paper/Purpur)
- Java **17+**
- Minecraft **1.20.1 - 1.21.x**

Opcjonalne (zalecane dla kupna/sprzedaży):

- **Vault**
- Dostawca ekonomii (EssentialsX, CMI, itp.)

## Pobieranie

Pobierz najnowszy plik `TheEndex-*.jar` z:

- [Modrinth](https://modrinth.com/plugin/theendex)
- [SpigotMC](https://www.spigotmc.org/resources/theendex)
- [GitHub Releases](https://github.com/khristianaarongarcia/endex/releases)

## Instalacja

1. Zatrzymaj swój serwer.
2. Skopiuj plik jar do folderu `plugins/` swojego serwera.
3. Uruchom serwer.

## Weryfikacja

Uruchom jedną z tych komend w grze lub w konsoli:

```text
/endex
/market
```

Jeśli zobaczysz wyjście pomocy (lub otworzy się GUI rynku), instalacja przebiegła pomyślnie.

## Utworzone pliki

Po pierwszym uruchomieniu plugin tworzy pliki w `plugins/TheEndex/`:

```text
plugins/
└── TheEndex/
    ├── config.yml
    ├── market.yml
    ├── events.yml
    └── data/
```

<Info>
The Endex migruje wiele wartości konfiguracyjnych między wersjami, ale nadal powinieneś wykonać kopię zapasową `plugins/TheEndex/` przed aktualizacją.
</Info>


***

## Krok 3: Konfiguracja Ekonomii (Opcjonalne)

Dla funkcjonalności kupna/sprzedaży potrzebujesz systemu ekonomii:

### Zainstaluj Vault

1. Pobierz [Vault](https://www.spigotmc.org/resources/vault.34315/)
2. Umieść w folderze `plugins/`
3. Zrestartuj serwer

### Zainstaluj Dostawcę Ekonomii

Wybierz jedną z tych popularnych opcji:

| Plugin                                              | Opis                       |
| --------------------------------------------------- | -------------------------- |
| [EssentialsX](https://essentialsx.net/)             | Najpopularniejszy, bogaty w funkcje |
| [CMI](https://www.spigotmc.org/resources/cmi.3742/) | Zarządzanie wszystko-w-jednym |
| [LuckPerms Vault](https://luckperms.net/)           | Lekka opcja                |

***

## Krok 4: Weryfikacja Instalacji

1. Dołącz do swojego serwera
2.  Uruchom komendę wersji:

    ```
    /endex version
    ```
3. Powinieneś zobaczyć informacje o wersji i status
4.  Otwórz GUI rynku:

    ```
    /market
    ```

***

## Krok 5: Wstępna Konfiguracja

Edytuj `plugins/TheEndex/config.yml` aby dostosować:

```yaml
# Podstawowe ustawienia do przejrzenia
update-interval: 60        # Jak często ceny się aktualizują (sekundy)
sensitivity: 0.1           # Zmienność cen (0.0 - 1.0)
tax: 2.5                   # Procent podatku od transakcji

# Przechowywanie (zalecane sqlite)
storage:
  type: sqlite
  database: market.db

# Włącz dashboard web
web:
  enabled: true
  port: 8080
```

Po edycji przeładuj konfigurację:

```
/endex reload
```

***

## Instalowanie Addonów

Addony rozszerzają The Endex o dodatkowe funkcje.

1. Pobierz plik JAR addonu
2.  Umieść w `plugins/TheEndex/addons/`:

    ```
    plugins/TheEndex/
    └── addons/
        └── CryptoAddon.jar
    ```
3.  Zrestartuj serwer lub przeładuj:

    ```
    /endex reload
    ```

***

## Aktualizacja

Aby zaktualizować The Endex:

1. Pobierz nową wersję
2. Zastąp stary plik JAR w `plugins/`
3. Zrestartuj serwer
4. Sprawdź dziennik zmian pod kątem migracji konfiguracji

<Info>
The Endex automatycznie migruje konfiguracje między wersjami. Zawsze wykonuj kopię zapasową przed aktualizacją!
</Info>

***

## Następne Kroki

* [Przewodnik Szybkiego Startu](quick-start) — Poznaj podstawy
* [Konfiguracja](../reference/configuration) — Dostosuj wszystko
* [Komendy](../reference/commands) — Wszystkie dostępne komendy
