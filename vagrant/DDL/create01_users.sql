CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ユーザID',
  `name` varchar(255) NOT NULL COMMENT '名前',
  `email` varchar(319) NOT NULL COMMENT 'メールアドレス',
  `password` varchar(255) NOT NULL COMMENT 'パスワード',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日付',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) COMMENT='ユーザ'
