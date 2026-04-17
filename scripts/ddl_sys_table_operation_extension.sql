-- ============================================
-- sys_table_operation 表结构扩展
-- 新增字段支持工具栏按钮事件配置
--
-- 日期：2026-04-10
-- ============================================

-- 新增字段：事件类型、事件配置、确认消息、启用状态
ALTER TABLE sys_table_operation
ADD COLUMN IF NOT EXISTS event_type VARCHAR(50) DEFAULT NULL COMMENT '事件类型: builtin|api|download|redirect|modal|drawer',
ADD COLUMN IF NOT EXISTS event_config TEXT DEFAULT NULL COMMENT '事件配置JSON',
ADD COLUMN IF NOT EXISTS confirm_message VARCHAR(200) DEFAULT NULL COMMENT '确认提示消息',
ADD COLUMN IF NOT EXISTS is_enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用',
ADD COLUMN IF NOT EXISTS show_button TINYINT(1) DEFAULT 1 COMMENT '是否展示按钮: 0-隐藏 1-展示';

-- 查看表结构
DESC sys_table_operation;
