#!/bin/bash
sudo su

if [[ `mysql --version` == *"mysql"* ]];then
  echo "MySQL already installed"
  echo `mysql --version`
  exit 0
fi

# MySQLリポジトリの追加
rpm -Uvh https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2022

# MySQLをインストールする
yum-config-manager --disable mysql57-community
yum-config-manager --enable mysql80-community
yum install -y mysql-community-server

echo "MySQLを自動起動設定し再起動する"
systemctl enable mysqld
systemctl restart mysqld

echo "MySQLの初回パスワードを取得"
TEMP_PASSWORD=$(grep 'temporary password' /var/log/mysqld.log | awk '{print $NF}')
echo "$TEMP_PASSWORD"

echo "パスワードポリシーを設定する"
echo "validate_password.policy=LOW" >> /etc/my.cnf
echo "validate_password.length=6" >> /etc/my.cnf

echo "リモート接続を許可する設定"
echo "bind-address=0.0.0.0" >> /etc/my.cnf

echo "MySQLを再起動する"
systemctl restart mysqld

# MySQLのルートパスワードを設定する
NEW_USER="yonem"
NEW_USER_PASSWORD="@yonem"
NEW_DATABASE="todo-list"
NEW_TEST_DATABASE="todo-list_test"

echo "新しいユーザーとデータベースを作成する"
mysql -u root -p$TEMP_PASSWORD --connect-expired-password <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY '$NEW_USER_PASSWORD';
CREATE USER 'root'@'%' IDENTIFIED BY '$NEW_USER_PASSWORD';
CREATE USER '$NEW_USER'@'%' IDENTIFIED BY '$NEW_USER_PASSWORD';
GRANT ALL PRIVILEGES ON $NEW_DATABASE.* TO '$NEW_USER'@'%';
GRANT ALL PRIVILEGES ON $NEW_TEST_DATABASE.* TO '$NEW_USER'@'%';
FLUSH PRIVILEGES;
CREATE DATABASE IF NOT EXISTS $NEW_DATABASE;
CREATE DATABASE IF NOT EXISTS $NEW_TEST_DATABASE;
EOF

echo "ファイアウォールでMySQLポートを開放"
firewall-cmd --zone=public --add-port=3306/tcp --permanent
firewall-cmd --reload

echo "MySQL installation and configuration completed."
