---
title: "Addons"
description: "Erweiterung der Funktionen von The Endex"
---

# Addon-System

The Endex unterstützt ein modulares Addon-System, das es ermöglicht, spezifische Funktionen hinzuzufügen, ohne das Hauptplugin aufzublähen.

## Offizielle Addons

### 1. CryptoAddon
Fügt virtuelle Kryptowährungen (Bitcoin, Ethereum usw.) zum Markt hinzu.
- **Funktionen**: Preise basierend auf echten APIs (CoinGecko), separate Krypto-Wallets.
- **Installation**: Legen Sie `Endex-Crypto.jar` in `plugins/TheEndex/addons/`.

### 2. DiscordLink
Verbindet den Markt mit Ihrem Discord-Server.
- **Funktionen**: Discord Slash-Befehle (`/price`), Crash-Benachrichtigungen in einem Kanal, Rollensynchronisation basierend auf Reichtum.

## Ein Addon erstellen

Entwickler können ihre eigenen Addons erstellen, indem sie die Klasse `EndexAddon` erweitern.

### Grundstruktur

```java
public class MyCustomAddon extends EndexAddon {
    
    @Override
    public void onEnable() {
        getLogger().info("Mein Addon ist aktiviert!");
    }
    
    @Override
    public void onDisable() {
        // Aufräumen
    }
}
```

### Laden

Addons müssen im Ordner `plugins/TheEndex/addons/` platziert werden. Sie werden beim Start des Hauptplugins automatisch geladen.

## Marktplatz

(Demnächst) Ein Community-Marktplatz, auf dem Sie von anderen Benutzern erstellte Addons herunterladen können.
