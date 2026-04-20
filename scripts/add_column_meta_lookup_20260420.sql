-- =============================================================================
-- WMS-LOWCODE-LOOKUP-001 : sys_column_meta 增加关联表（Lookup）配置字段
-- 日期: 2026-04-20
-- 目的: 低代码支持主表列表 / 搜索 / 排序联表显示子表字段
-- 幂等: 使用 INFORMATION_SCHEMA 判断，可重复执行
-- 兼容: 全部字段 NULL，不影响任何现有 column_meta 记录
-- =============================================================================

-- 1. ref_table_code : 关联表 tableCode（必须存在于 sys_table_meta）
SET @col_exists := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_column_meta'
      AND COLUMN_NAME = 'ref_table_code'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE sys_column_meta ADD COLUMN ref_table_code VARCHAR(64) NULL COMMENT ''关联表tableCode(lookup虚拟列用)'' AFTER value_field',
    'SELECT ''column ref_table_code already exists'' AS msg');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 2. ref_match_field : 关联表匹配字段（snake_case）
SET @col_exists := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_column_meta'
      AND COLUMN_NAME = 'ref_match_field'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE sys_column_meta ADD COLUMN ref_match_field VARCHAR(64) NULL COMMENT ''关联表匹配字段(snake_case)'' AFTER ref_table_code',
    'SELECT ''column ref_match_field already exists'' AS msg');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 3. ref_target_field : 关联表展示字段（snake_case）
SET @col_exists := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_column_meta'
      AND COLUMN_NAME = 'ref_target_field'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE sys_column_meta ADD COLUMN ref_target_field VARCHAR(64) NULL COMMENT ''关联表展示字段(snake_case)'' AFTER ref_match_field',
    'SELECT ''column ref_target_field already exists'' AS msg');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 4. ref_local_field : 当前表外键字段（snake_case），为空时运行时默认取 field 自身
SET @col_exists := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_column_meta'
      AND COLUMN_NAME = 'ref_local_field'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE sys_column_meta ADD COLUMN ref_local_field VARCHAR(64) NULL COMMENT ''当前表外键字段(snake_case),空则取field'' AFTER ref_target_field',
    'SELECT ''column ref_local_field already exists'' AS msg');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 索引：按关联表反查，便于校验关联关系是否被引用（可选，先建一个）
SET @idx_exists := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'sys_column_meta'
      AND INDEX_NAME = 'idx_ref_table_code'
);
SET @sql := IF(@idx_exists = 0,
    'ALTER TABLE sys_column_meta ADD INDEX idx_ref_table_code (ref_table_code)',
    'SELECT ''index idx_ref_table_code already exists'' AS msg');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 验证
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'sys_column_meta'
  AND COLUMN_NAME LIKE 'ref_%'
ORDER BY ORDINAL_POSITION;
