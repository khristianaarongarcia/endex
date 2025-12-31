---
title: "Entwickler API"
description: "Java API für die Plugin-Entwicklung"
---

# Entwickler API (Java)

The Endex bietet eine robuste Java-API, damit andere Plugins mit der Wirtschaft interagieren können.

## Abhängigkeit (Maven/Gradle)

Um die API zu nutzen, fügen Sie The Endex zu Ihrem Projekt hinzu.

**Maven:**
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.KhristianAaronGarcia</groupId>
    <artifactId>TheEndex</artifactId>
    <version>LATEST</version>
    <scope>provided</scope>
</dependency>
```

**Gradle:**
```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("com.github.KhristianAaronGarcia:TheEndex:LATEST")
}
```

## Nutzung der API

Die Haupteinstiegsklasse ist `TheEndexAPI`.

### Eine Instanz erhalten

```java
import com.theendex.api.TheEndexAPI;

TheEndexAPI api = TheEndexAPI.getInstance();
```

### Preis eines Gegenstands abrufen

```java
double price = api.getPrice("DIAMOND");
```

### Einen Preis ändern

```java
// Preis auf 200 setzen
api.setPrice("DIAMOND", 200.0);
```

### Auf Ereignisse hören

The Endex löst benutzerdefinierte Bukkit-Events aus.

```java
@EventHandler
public void onPriceChange(EndexPriceChangeEvent event) {
    String item = event.getItemId();
    double oldPrice = event.getOldPrice();
    double newPrice = event.getNewPrice();
    
    Bukkit.getLogger().info("Der Preis von " + item + " hat sich von " + oldPrice + " auf " + newPrice + " geändert");
}
```

## Javadocs

Die vollständige Javadoc-Dokumentation ist im Quellcode oder auf der Dokumentationsseite verfügbar (Link folgt).
