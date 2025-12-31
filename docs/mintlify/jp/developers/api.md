---
title: "開発者API"
description: "プラグイン開発用Java API"
---

# 開発者API (Java)

The Endex は、他のプラグインが経済と対話できるように、堅牢なJava APIを提供します。

## 依存関係 (Maven/Gradle)

APIを使用するには、プロジェクトに The Endex を追加します。

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

## APIの使用

メインのエントリークラスは `TheEndexAPI` です。

### インスタンスの取得

```java
import com.theendex.api.TheEndexAPI;

TheEndexAPI api = TheEndexAPI.getInstance();
```

### アイテム価格の取得

```java
double price = api.getPrice("DIAMOND");
```

### 価格の変更

```java
// 価格を200に設定
api.setPrice("DIAMOND", 200.0);
```

### イベントのリスニング

The Endex はカスタムBukkitイベントをトリガーします。

```java
@EventHandler
public void onPriceChange(EndexPriceChangeEvent event) {
    String item = event.getItemId();
    double oldPrice = event.getOldPrice();
    double newPrice = event.getNewPrice();
    
    Bukkit.getLogger().info(item + " の価格が " + oldPrice + " から " + newPrice + " に変更されました");
}
```

## Javadocs

完全なJavadocドキュメントは、ソースコードまたはドキュメントサイト（リンクは後日公開）で入手可能です。
