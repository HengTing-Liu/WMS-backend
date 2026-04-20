-- =============================================================================
-- WMS-LOWCODE-LOOKUP-SEP : sys_column_meta 增加虚拟列分隔符字段
-- 日期: 2026-04-20
-- 目的: 虚拟列多字段拼接支持自定义分隔符（默认 ❤，老数据兜底不需要迁移）
-- 幂等: 使用 INFORMATION_SCHEMA 判断，可重复执行
-- 兼容: NULL-able，NULL 时运行时默认使用 ❤，老数据零迁移
-- =============================================================================

-- ref_separator : 多字段拼接分隔符（VARCHAR(16) NULL，NULL 时运行时默认 ❤）
SET @col_exists := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_column_meta'
      AND COLUMN_NAME = 'ref_separator'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE sys_column_meta ADD COLUMN ref_separator VARCHAR(16) NULL COMMENT ''虚拟列多字段拼接分隔符,NULL时默认心形'' AFTER ref_local_field',
    'SELECT ''column ref_separator already exists'' AS msg');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 验证
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, CHARACTER_MAXIMUM_LENGTH, COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'sys_column_meta'
  AND COLUMN_NAME = 'ref_separator';
