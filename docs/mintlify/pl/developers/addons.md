---
title: "Dodatki"
description: "Rozszerz The Endex za pomocą dodatkowych plików JAR ładowanych przez ServiceLoader."
---

Dodatki to pliki JAR ładowane z:

```text
plugins/TheEndex/addons/
```

Nie są to pełne pluginy Bukkit (brak plugin.yml). The Endex ładuje je używając Java ServiceLoader.

## Co mogą robić dodatki

- Dodawać własne komendy
- Dodawać lub rozszerzać trasy API
- Dodawać własne zachowania handlowe

## Jak działa ładowanie

Przy starcie, The Endex:

1. Skanuje plugins/TheEndex/addons/*.jar
2. Używa ServiceLoader do znalezienia implementacji org.lokixcz.theendex.addon.EndexAddon
3. Wywołuje hook inicjalizacyjny dodatku

## Minimalny szkielet dodatku (Kotlin)

```kotlin
import org.lokixcz.theendex.TheEndex
import org.lokixcz.theendex.addon.EndexAddon

class MyAddon : EndexAddon {
    override fun id() = "my-addon"
    override fun name() = "My Addon"

    override fun init(plugin: TheEndex) {
        // register routes, commands, listeners, etc.
    }
}
```

## Deskryptor usługi

Wewnątrz pliku JAR dodatku, dołącz:

```text
src/main/resources/META-INF/services/org.lokixcz.theendex.addon.EndexAddon
```

Ten plik powinien zawierać nazwę klasy implementacji (jedna na linię), na przykład:

```text
com.example.myaddon.MyAddon
```

## Ustawienia dodatku

Jeśli dodatek potrzebuje plików konfiguracyjnych, typową konwencją jest:

```text
plugins/TheEndex/addons/settings/addon-id/
```

Przykład:

```text
plugins/TheEndex/addons/settings/crypto/crypto.yml
```

## Weryfikacja załadowania

Jeśli Twoja wersja zawiera komendę listy dodatków, możesz sprawdzić:

```text
/endex addons
```

## Uprawnienia

Dodatki mogą używać systemu uprawnień The Endex:

| Wzorzec | Opis |
| --- | --- |
| endex.addon.name | Podstawowy dostęp do dodatku |
| endex.addon.name.admin | Funkcje administratora dodatku |
| endex.addon.name.trade | Handel przez dodatek |

## Rozwiązywanie problemów

- Upewnij się, że JAR jest wewnątrz plugins/TheEndex/addons/
- Sprawdź logi konsoli pod kątem błędów ServiceLoader
- Zweryfikuj czy ścieżka deskryptora usługi i nazwy klas pasują dokładnie

## Powiązane Strony

- [API Deweloperskie](api)
- [REST API](../web-api/rest-api)
- [Komendy](../reference/commands)
