---
title: "インストール"
description: "サーバーに The Endex をインストールする方法"
---

# インストールガイド

以下の手順に従って、Minecraftサーバーに The Endex をインストールしてください。

## 前提条件

始める前に、以下のものがあることを確認してください：

- **Spigot**、**Paper**、または **Purpur** を実行しているMinecraftサーバー（バージョン 1.16 - 1.21+）。
- **Java 17** 以上がインストールされていること。
- **Vault** 互換の経済プラグイン（例：EssentialsX）。
- **Vault** がインストールされていること。
- **PlaceholderAPI**（オプションですが、プレースホルダーの使用に推奨されます）。

## インストール手順

<Steps>
  <Step title="プラグインのダウンロード">
    [SpigotMC](https://www.spigotmc.org/) または [Modrinth](https://modrinth.com/) から最新バージョンの `TheEndex.jar` をダウンロードします。
  </Step>
  <Step title="ファイルのインストール">
    ダウンロードした `.jar` ファイルをサーバーの `plugins` フォルダに配置します。
  </Step>
  <Step title="サーバーの再起動">
    サーバーを再起動してプラグインを読み込みます。`/reload` は使用しないでください。
  </Step>
  <Step title="インストールの確認">
    コンソールで The Endex の起動メッセージを確認するか、ゲーム内で `/plugins` と入力して The Endex が緑色で表示されていることを確認します。
  </Step>
</Steps>

## 初期設定

最初の起動後、`plugins` ディレクトリに `TheEndex` フォルダが作成されます。このフォルダには以下が含まれます：

- `config.yml`: メイン設定ファイル。
- `messages.yml`: ゲーム内メッセージの翻訳ファイル。
- `items.yml`: アイテムとその基本価格の設定。

インストールのカスタマイズに関する詳細は、[設定](/jp/reference/configuration) セクションを参照してください。
