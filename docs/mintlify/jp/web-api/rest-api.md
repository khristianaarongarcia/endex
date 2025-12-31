---
title: "REST API"
description: "開発者向けHTTP APIドキュメント"
---

# REST API

The Endex は、外部の開発者が市場データと対話できるようにするRESTful APIを提供します。統計ウェブサイト、Discordボット、またはモバイルアプリの作成に役立ちます。

## エンドポイント (ベースURL)

`http://<サーバーIP>:<ポート>/api/v1`

## エンドポイント

### 全アイテムの取得

`GET /items`

追跡されているすべてのアイテムのリストを返します。

**レスポンス:**
```json
[
  {
    "id": "DIAMOND",
    "name": "ダイヤモンド",
    "price": 105.50,
    "basePrice": 100.0,
    "trend": "UP",
    "change": 5.5
  },
  {
    "id": "GOLD_INGOT",
    "name": "金インゴット",
    "price": 48.20,
    "basePrice": 50.0,
    "trend": "DOWN",
    "change": -3.6
  }
]
```

### 特定のアイテムの取得

`GET /items/{id}`

**例:** `GET /items/DIAMOND`

**レスポンス:**
```json
{
  "id": "DIAMOND",
  "name": "ダイヤモンド",
  "price": 105.50,
  "history": [
    {"time": 1620000000, "price": 100.0},
    {"time": 1620003600, "price": 102.0},
    {"time": 1620007200, "price": 105.5}
  ]
}
```

### 市場ステータスの取得

`GET /status`

市場のグローバルステータス（進行中のイベントなど）を返します。

**レスポンス:**
```json
{
  "status": "OPEN",
  "activeEvents": [],
  "lastUpdate": 1620007200
}
```

## 認証

現在、APIはデフォルトで公開されており、読み取り専用です。将来のバージョンでは、書き込みアクション（POST/PUT）のためのAPIキー認証が含まれる予定です。

## レート制限 (Rate Limiting)

ハードコードされたレート制限はありませんが、多くのリクエストを行う場合は常識の範囲内で使用し、結果をキャッシュしてください。
