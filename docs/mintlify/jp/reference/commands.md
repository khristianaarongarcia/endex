---
title: "コマンド"
description: "コマンドの完全なリストとその使用法"
---

# コマンド

The Endex で利用可能なコマンドの完全なリストです。

## プレイヤーコマンド

これらのコマンドは通常、すべてのプレイヤーがアクセスできます（権限によります）。

| コマンド | エイリアス | 説明 | 権限 |
|----------|------------|------|------|
| `/endex` | `/market` | メインの市場メニューを開きます。 | `endex.use` |
| `/endex help` | | ヘルプを表示します。 | `endex.help` |
| `/endex price <アイテム>` | | アイテムの現在の価格を表示します。 | `endex.price` |
| `/endex buy <アイテム> <数量>` | | 市場価格でアイテムを購入します。 | `endex.buy` |
| `/endex sell <アイテム> <数量>` | | 市場価格でアイテムを売却します。 | `endex.sell` |
| `/endex sellall` | | 互換性のあるインベントリ全体を売却します。 | `endex.sellall` |
| `/holdings` | `/portfolio` | ポートフォリオメニューを開きます。 | `endex.holdings` |
| `/endex invest <アイテム> <数量>` | | 保有資産を購入します（仮想）。 | `endex.invest` |
| `/endex divest <アイテム> <数量>` | | 保有資産を売却します（仮想）。 | `endex.divest` |

## 管理者コマンド

これらのコマンドは管理者とオペレーター専用です。

| コマンド | 説明 | 権限 |
|----------|------|------|
| `/endex admin reload` | 設定をリロードします。 | `endex.admin.reload` |
| `/endex admin additem <価格>` | 手に持っているアイテムを市場に追加します。 | `endex.admin.additem` |
| `/endex admin removeitem <アイテム>` | アイテムを市場から削除します。 | `endex.admin.removeitem` |
| `/endex admin setprice <アイテム> <価格>` | アイテムの価格を強制設定します。 | `endex.admin.setprice` |
| `/endex admin setvolatility <アイテム> <値>` | アイテムのボラティリティを変更します。 | `endex.admin.setvolatility` |
| `/endex admin event start <イベント>` | イベントを強制的に開始します。 | `endex.admin.event` |
| `/endex admin event stop` | 進行中のイベントを停止します。 | `endex.admin.event` |
| `/endex admin debug` | コンソールでのデバッグモードを有効にします。 | `endex.admin.debug` |

## 引数

- `<アイテム>`: アイテムの内部名（例：`DIAMOND`, `IRON_INGOT`）。`items.yml` と一致する必要があります。
- `<数量>`: 数量（整数）。
- `<価格>`: 価格（小数）。
