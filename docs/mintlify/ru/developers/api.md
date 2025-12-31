---
title: "API разработчика"
description: "Java API для разработки плагинов"
---

# API разработчика (Java)

The Endex предоставляет надежный Java API, позволяющий другим плагинам взаимодействовать с экономикой.

## Зависимость (Maven/Gradle)

Чтобы использовать API, добавьте The Endex в ваш проект.

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

## Использование API

Основной класс входа — `TheEndexAPI`.

### Получить экземпляр

```java
import com.theendex.api.TheEndexAPI;

TheEndexAPI api = TheEndexAPI.getInstance();
```

### Получить цену предмета

```java
double price = api.getPrice("DIAMOND");
```

### Изменить цену

```java
// Установить цену на 200
api.setPrice("DIAMOND", 200.0);
```

### Слушать события

The Endex вызывает кастомные события Bukkit.

```java
@EventHandler
public void onPriceChange(EndexPriceChangeEvent event) {
    String item = event.getItemId();
    double oldPrice = event.getOldPrice();
    double newPrice = event.getNewPrice();
    
    Bukkit.getLogger().info("Цена " + item + " изменилась с " + oldPrice + " на " + newPrice);
}
```

## Javadocs

Полная документация Javadoc доступна в исходном коде или на нашем сайте документации (ссылка будет предоставлена).
