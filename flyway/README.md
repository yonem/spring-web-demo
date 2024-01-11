## Flywayコマンドラインツールのインストール

以下からzipファイルをダウンロード解凍し任意の場所に置く ※ 2024/1/10現在

https://documentation.red-gate.com/flyway/flyway-cli-and-api/usage/command-line

## 設定ファイルの内容

conf/flyway.toml

```
[environments.default]
url = "jdbc:mysql://localhost:3306/接続DB"
user = "接続ユーザ"
password = "接続パスワード"

[environments.test]
url = "jdbc:mysql://localhost:3306/接続DB_test"
user = "接続ユーザ"
password = "接続パスワード"

[flyway]
cleanDisabled = false
```

## マイグレーションファイルの準備

sqlディレクトリ内にマイグレーションファイルを格納する

### 書式

    V<Version>__<Description>.sql

### V：プレフィックス

	SQLファイルの先頭は必ず V から始める

### <Version\>：バージョン番号

	半角数値と、ドット . またはアンダーバー _ の組み合わせで指定する
	例）
	2
	2.1.0
	3_1_2

### __：仕切文字

	アンダーバーを２つ続ける

### <Description\>：説明

	Descriptionで表示される説明

## Flywayコマンド

flywayフォルダに移動する

    cd flywayフォルダ

バージョン確認コマンドを実行する

```
./flyway info
```

マイグレーションコマンドを実行する

```
./flyway migrate
```

テスト環境のマイグレーションコマンドを実行する

```
./flyway -environment=test -locations=filesystem:sql/create migrate
```

テスト環境のバージョン確認コマンドを実行する

```
./flyway -environment=test info
```

DBのクリアコマンド

```
./flyway clean
```

コマンドを実行するとDB上の全てのオブジェクトが削除される
設定ファイルでcleanDisabledを設定していないとコマンドが使用できない
