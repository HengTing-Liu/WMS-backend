-- ============================================
-- DDL: sys_column_meta 导出字段控制扩展
-- 表: sys_column_meta
-- 日期: 2026-04-09
-- 执行前请备份数据库
-- ============================================

-- ----------------------------------------
-- 扩展 sys_column_meta 表，新增导出控制字段
-- 注意：MySQL 8.0 以下版本不支持 IF NOT EXISTS，请确保列不存在时再执行
-- ----------------------------------------

ALTER TABLE sys_column_meta
    ADD COLUMN show_in_export TINYINT(1) DEFAULT 1 COMMENT '导出显示: 0-否 1-是';

-- ----------------------------------------
-- 验证执行结果
-- ----------------------------------------
DESCRIBE sys_column_meta;

-- 预期新增字段：
-- show_in_export | tinyint(1) | YES  |     | NULL    |     | DEFAULT 1 | 导出显示

-- ============================================
-- 撤销脚本（如需回滚）
-- ============================================
/*
ALTER TABLE sys_column_meta
    DROP COLUMN IF EXISTS show_in_export;
*/
