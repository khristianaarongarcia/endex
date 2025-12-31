---
title: "API de Desenvolvedor"
description: "API Java para desenvolvimento de plugins"
---

# API de Desenvolvedor (Java)

O The Endex fornece uma API Java robusta para permitir que outros plugins interajam com a economia.

## Dependência (Maven/Gradle)

Para usar a API, adicione o The Endex ao seu projeto.

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

## Usando a API

A classe de entrada principal é `TheEndexAPI`.

### Obter Instância

```java
import com.theendex.api.TheEndexAPI;

TheEndexAPI api = TheEndexAPI.getInstance();
```

### Obter Preço de Item

```java
double price = api.getPrice("DIAMOND");
```

### Modificar Preço

```java
// Definir preço para 200
api.setPrice("DIAMOND", 200.0);
```

### Ouvir Eventos

O The Endex dispara eventos Bukkit personalizados.

```java
@EventHandler
public void onPriceChange(EndexPriceChangeEvent event) {
    String item = event.getItemId();
    double oldPrice = event.getOldPrice();
    double newPrice = event.getNewPrice();
    
    Bukkit.getLogger().info("Preço de " + item + " mudou de " + oldPrice + " para " + newPrice);
}
```

## Javadocs

A documentação completa do Javadoc está disponível no código-fonte ou no nosso site de documentação (link a ser fornecido).
