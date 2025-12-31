---
title: "PlaceholderAPI"
description: "The Endex プレースホルダーの使用"
---

# PlaceholderAPI

The Endex は **PlaceholderAPI** (PAPI) を完全にサポートしており、市場情報をどこにでも表示できます（スコアボード、チャット、タブリスト、ホログラム、DeluxeMenusメニューなど）。

## インストール

以下があることを確認してください：
1. PlaceholderAPI がインストールされていること。
2. The Endex がインストールされていること。
3. `/papi ecloud download TheEndex` を実行する（ecloudで利用可能な場合）か、The Endex が独自のプレースホルダーを登録するため、単に `/papi reload` でPAPIをリロードします。

## プレースホルダーリスト

### アイテムプレースホルダー

`<item>` をアイテムのID（例：`DIAMOND`, `OAK_LOG`）に置き換えてください。

| プレースホルダー | 説明 |
|------------------|------|
| `%endex_price_<item>%` | アイテムの現在の価格。 |
| `%endex_price_formatted_<item>%` | 通貨付きのフォーマットされた価格（例：$100.50）。 |
| `%endex_change_<item>%` | 価格変動率（例：+5.2%）。 |
| `%endex_trend_<item>%` | トレンドアイコン（例：📈 または 📉）。 |
| `%endex_base_price_<item>%` | 設定された基本価格。 |
| `%endex_volume_<item>%` | 現在の取引量。 |

### プレイヤープレースホルダー

| プレースホルダー | 説明 |
|------------------|------|
| `%endex_holdings_value%` | プレイヤーのポートフォリオの総価値。 |
| `%endex_holdings_count_<item>%` | 特定のアイテムの保有数。 |
| `%endex_profit_loss%` | プレイヤーの総損益 (P&L)。 |

### グローバルプレースホルダー

| プレースホルダー | 説明 |
|------------------|------|
| `%endex_top_gainer%` | 最も上昇したアイテムの名前。 |
| `%endex_top_loser%` | 最も下落したアイテムの名前。 |
| `%endex_market_status%` | 市場の状態（オープン/クローズ/クラッシュ）。 |

## 使用例

**スコアボード内:**
```yaml
lines:
  - "&6市場:"
  - " ダイヤモンド: %endex_price_formatted_DIAMOND% %endex_trend_DIAMOND%"
  - " 金: %endex_price_formatted_GOLD_INGOT% %endex_trend_GOLD_INGOT%"
  - "&eポートフォリオ: $%endex_holdings_value%"
```

**ホログラム内:**
```text
ビットコイン価格 (仮想)
%endex_price_formatted_BTC%
```
