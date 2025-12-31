---
title: "アドオン"
description: "The Endex の機能拡張"
---

# アドオンシステム

The Endex はモジュール式のアドオンシステムをサポートしており、メインプラグインを肥大化させることなく特定の機能を追加できます。

## 公式アドオン

### 1. CryptoAddon
市場に仮想暗号通貨（ビットコイン、イーサリアムなど）を追加します。
- **機能**: 実際のAPI（CoinGecko）に基づく価格、個別の暗号ウォレット。
- **インストール**: `Endex-Crypto.jar` を `plugins/TheEndex/addons/` に配置します。

### 2. DiscordLink
市場をDiscordサーバーに接続します。
- **機能**: Discordスラッシュコマンド（`/price`）、チャンネルでのクラッシュ通知、資産に基づくロール同期。

## アドオンの作成

開発者は `EndexAddon` クラスを拡張することで独自のアドオンを作成できます。

### 基本構造

```java
public class MyCustomAddon extends EndexAddon {
    
    @Override
    public void onEnable() {
        getLogger().info("私のアドオンが有効になりました！");
    }
    
    @Override
    public void onDisable() {
        // クリーンアップ
    }
}
```

### ロード

アドオンは `plugins/TheEndex/addons/` フォルダに配置する必要があります。メインプラグインの起動時に自動的にロードされます。

## マーケットプレイス

（近日公開）他のユーザーが作成したアドオンをダウンロードできるコミュニティマーケットプレイス。
