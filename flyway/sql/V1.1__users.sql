CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ユーザID',
  `name` varchar(40) NOT NULL COMMENT '名前',
  `email` varchar(255) NOT NULL COMMENT 'メールアドレス',
  `password` varchar(255) NOT NULL COMMENT 'パスワード',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日付',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) COMMENT='ユーザ';

INSERT INTO users VALUES(1, 'yonem', 'hoge@example.com', '$2a$10$ncJnCG7IuLqY2hiHm86btOAMGgLARoPg3QucGxcD3Iai5nNRWuNbm', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

