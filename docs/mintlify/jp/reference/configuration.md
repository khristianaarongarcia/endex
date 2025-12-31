---
title: "設定"
description: "設定ファイルの詳細ガイド"
---

# 設定

The Endex は `plugins/TheEndex/` フォルダ内のYAMLファイルを通じて高度に設定可能です。

## config.yml

グローバル設定のためのメインファイルです。

```yaml
# 一般設定
plugin-prefix: "&8[&5The Endex&8] "
language: jp # 言語 (en, fr, es, de, jp, etc.)
debug-mode: false

# 経済
economy:
  currency-symbol: "$"
  format: "#,##0.00"
  
# 市場設定
market:
  update-interval: 60 # 価格更新の間隔（秒）
  save-interval: 300 # 自動保存の間隔（秒）
  
# ポートフォリオシステム
holdings:
  enabled: true
  tax-rate: 0.05

# Webサーバー (ダッシュボード)
web:
  enabled: true
  port: 8080
  host: "0.0.0.0"
```

## items.yml

すべての取引可能なアイテムとそのプロパティを定義します。

```yaml
DIAMOND:
  material: DIAMOND # Minecraftマテリアル
  display-name: "&bダイヤモンド" # 表示名（オプション）
  base-price: 100.0
  min-price: 10.0
  max-price: 500.0
  volatility: 0.5
  stock: 1000 # 仮想初期在庫
  elasticity: 1.0 # 在庫変化に対する感度
  
GOLD_INGOT:
  material: GOLD_INGOT
  base-price: 50.0
  volatility: 0.3
```

## events.yml

ランダムな市場イベントを設定します。

```yaml
events:
  market_crash:
    display-name: "&c市場暴落"
    chance: 0.005
    duration: 1200
    global-multiplier: 0.7 # 全価格 x0.7
    
  gold_rush:
    display-name: "&6ゴールドラッシュ"
    items: [GOLD_INGOT, GOLD_BLOCK]
    price-multiplier: 0.5
```

## messages.yml

プレイヤーに表示されるすべてのテキストが含まれています。ここでメッセージを翻訳またはカスタマイズできます。カラーコード（`&` またはバージョンに応じてMiniMessage）をサポートしています。

```yaml
prefix: "&8[&5The Endex&8] "
buy-success: "&aあなたは &e%amount%x %item% &aを &e%price% &aで購入しました。"
sell-success: "&aあなたは &e%amount%x %item% &aを &e%price% &aで売却しました。"
insufficient-funds: "&c資金が不足しています。"
```
