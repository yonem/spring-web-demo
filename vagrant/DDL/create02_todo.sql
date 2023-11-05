CREATE TABLE `todo` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'TODO_ID',
  `category_id` int NOT NULL COMMENT 'カテゴリID',
  `subject` varchar(100) NOT NULL COMMENT '件名',
  `text` text COMMENT '本文',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日付',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日付',
  `deleted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '削除日付',
  PRIMARY KEY (`id`),
  KEY `categories_todo_fk` (`category_id`),
  CONSTRAINT `categories_todo_fk` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) COMMENT='TODO'
