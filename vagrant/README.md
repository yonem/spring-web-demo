## DB構築

TODOリストアプリケーションで使用するDBを仮想マシンに構築する  
DBはMySQLで構築される

## 動作環境

- Vagrant

https://developer.hashicorp.com/vagrant/downloads?product_intent=vagrant

## 構築手順

1. ターミナルで`vagrant`フォルダに移動する
2. `vagrant up`コマンドを実行する
3. `MySQL installation and configuration completed.`のメッセージが出力されれば構築成功

※  
`MySQLを起動して自動起動設定`のメッセージが出力され、`provision.sh`がそれ以上進まなくなる時がある  
その場合は、強制終了し`vagrant destroy`で仮想マシンを削除した後で、もう一度最初から手順を実行する。

## DBの接続とテーブルの作成

- DBクライアントツールから接続する
- `provision.sh`に記述のある情報で接続する
    - NEW_USER
    - NEW_USER_PASSWORD
    - NEW_DATABASE
    - ポート番号:3306
