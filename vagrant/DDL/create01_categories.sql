CREATE TABLE `categories` (
  `id` int NOT NULL COMMENT 'カテゴリID',
  `name` varchar(100) NOT NULL COMMENT '名前',
  `description` text COMMENT '説明',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日付',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日付',
  `deleted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '削除日付',
  PRIMARY KEY (`id`)
) COMMENT='カテゴリ'
