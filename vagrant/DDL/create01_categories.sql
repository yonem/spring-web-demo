CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'カテゴリID',
  `name` varchar(100) NOT NULL COMMENT '名前',
  `description` text COMMENT '説明',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日付',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日付',
  `deleted_at` timestamp DEFAULT NULL COMMENT '削除日付',
  PRIMARY KEY (`id`)
) COMMENT='カテゴリ'
