-- ============================================
-- Bug修复: 为 sys_table_meta 添加 is_deleted_column 列
-- 背景: 不同表使用不同删除标记列
--   - sys_warehouse 等表: is_deleted (int)
--   - sys_user 等系统表: del_flag (char '0'/'2')
-- 日期: 2026-03-20
-- ============================================

ALTER TABLE sys_table_meta
ADD COLUMN IF NOT EXISTS is_deleted_column VARCHAR(50) DEFAULT 'is_deleted' COMMENT '逻辑删除字段名';

-- 为 sys_user 设置正确的删除列
UPDATE sys_table_meta SET is_deleted_column = 'del_flag' WHERE table_code = 'sys_user';

-- 修复 sys_warehouse 的删除列
UPDATE sys_table_meta SET is_deleted_column = 'is_deleted' WHERE table_code = 'sys_warehouse';
