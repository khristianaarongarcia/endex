---
title: "개발자 API"
description: "플러그인 개발용 Java API"
---

# 개발자 API (Java)

The Endex는 다른 플러그인이 경제와 상호 작용할 수 있도록 강력한 Java API를 제공합니다.

## 종속성 (Maven/Gradle)

API를 사용하려면 프로젝트에 The Endex를 추가하세요.

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

## API 사용

메인 진입 클래스는 `TheEndexAPI`입니다.

### 인스턴스 가져오기

```java
import com.theendex.api.TheEndexAPI;

TheEndexAPI api = TheEndexAPI.getInstance();
```

### 아이템 가격 가져오기

```java
double price = api.getPrice("DIAMOND");
```

### 가격 변경

```java
// 가격을 200으로 설정
api.setPrice("DIAMOND", 200.0);
```

### 이벤트 리스닝

The Endex는 커스텀 Bukkit 이벤트를 트리거합니다.

```java
@EventHandler
public void onPriceChange(EndexPriceChangeEvent event) {
    String item = event.getItemId();
    double oldPrice = event.getOldPrice();
    double newPrice = event.getNewPrice();
    
    Bukkit.getLogger().info(item + " 가격이 " + oldPrice + "에서 " + newPrice + "로 변경되었습니다");
}
```

## Javadocs

전체 Javadoc 문서는 소스 코드 또는 문서 사이트(링크는 나중에 제공됨)에서 확인할 수 있습니다.
