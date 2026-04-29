-- sys_column_meta：新增 is_unique（唯一）字段
-- MySQL 5.7+

ALTER TABLE sys_column_meta
  ADD COLUMN is_unique TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否唯一: 0-否 1-是' AFTER `is_sortable`;
