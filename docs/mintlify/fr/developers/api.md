---
title: "API Développeur"
description: "API Java pour le développement de plugins"
---

# API Développeur (Java)

The Endex fournit une API Java robuste pour permettre aux autres plugins d'interagir avec l'économie.

## Dépendance (Maven/Gradle)

Pour utiliser l'API, ajoutez The Endex à votre projet.

**Maven :**
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

**Gradle :**
```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("com.github.KhristianAaronGarcia:TheEndex:LATEST")
}
```

## Utilisation de l'API

La classe principale d'entrée est `TheEndexAPI`.

### Obtenir une instance

```java
import com.theendex.api.TheEndexAPI;

TheEndexAPI api = TheEndexAPI.getInstance();
```

### Récupérer le prix d'un objet

```java
double price = api.getPrice("DIAMOND");
```

### Modifier un prix

```java
// Définir le prix à 200
api.setPrice("DIAMOND", 200.0);
```

### Écouter les événements

The Endex déclenche des événements Bukkit personnalisés.

```java
@EventHandler
public void onPriceChange(EndexPriceChangeEvent event) {
    String item = event.getItemId();
    double oldPrice = event.getOldPrice();
    double newPrice = event.getNewPrice();
    
    Bukkit.getLogger().info("Le prix de " + item + " a changé de " + oldPrice + " à " + newPrice);
}
```

## Javadocs

La documentation complète Javadoc est disponible dans le code source ou sur le site de documentation (lien à venir).
