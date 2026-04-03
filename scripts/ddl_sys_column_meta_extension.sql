-- ============================================
-- Phase 1: 低代码字段扩展 DDL
-- 表: sys_column_meta
-- 日期: 2026-04-03
-- 执行前请备份数据库
-- ============================================

-- ----------------------------------------
-- 扩展 sys_column_meta 表，新增 4 个字段
-- ----------------------------------------

ALTER TABLE sys_column_meta
    ADD COLUMN IF NOT EXISTS col_span INT DEFAULT 6 COMMENT '表单栅格列宽（1-24）',
    ADD COLUMN IF NOT EXISTS section_key VARCHAR(50) DEFAULT 'basic' COMMENT '字段分组标识，用于表单分组展示',
    ADD COLUMN IF NOT EXISTS i18n_key VARCHAR(100) COMMENT '多语言 key',
    ADD COLUMN IF NOT EXISTS visible_condition TEXT COMMENT '显示条件（JSON 格式），如 {"field":"status","value":"1"}';

-- ----------------------------------------
-- 验证执行结果
-- ----------------------------------------
DESCRIBE sys_column_meta;

-- 预期新增字段（MySQL 8.0）：
-- col_span              | int(11)       | YES  | MUL | NULL    |     | DEFAULT 6 | 显示条件
-- section_key           | varchar(50)   | YES  |     | basic   |     |          |
-- i18n_key              | varchar(100)  | YES  |     | NULL    |     |          |
-- visible_condition     | text          | YES  |     | NULL    |     |          |

-- ============================================
-- 撤销脚本（如需回滚）
-- ============================================
/*
ALTER TABLE sys_column_meta
    DROP COLUMN IF EXISTS col_span,
    DROP COLUMN IF EXISTS section_key,
    DROP COLUMN IF EXISTS i18n_key,
    DROP COLUMN IF EXISTS visible_condition;
*/
