-- 低代码按钮显示配置与查看按钮初始化
-- Date: 2026-04-17

START TRANSACTION;

-- 1) 为 sys_table_operation 增加 show_button 字段（若不存在）
ALTER TABLE sys_table_operation
  ADD COLUMN IF NOT EXISTS show_button TINYINT(1) DEFAULT 1 COMMENT '是否展示按钮: 0-隐藏 1-展示';

-- 2) 为历史数据补齐默认值
UPDATE sys_table_operation
SET show_button = 1
WHERE show_button IS NULL;

-- 3) 为 sys_material 补充 row_read 按钮（若不存在）
INSERT INTO sys_table_operation
  (table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, event_type, event_config, confirm_message, is_enabled, show_button)
SELECT 'sys_material', 'row_read', '查看', 'link', 'material-symbols:visibility',
       'wms:base:material:query', 'row', 2, 1, 'builtin', '{"handler":"read"}', '', 1, 1
FROM dual
WHERE NOT EXISTS (
  SELECT 1 FROM sys_table_operation WHERE table_code = 'sys_material' AND operation_code = 'row_read'
);

-- 4) 对 sys_material 常用行按钮统一 show_button 默认值
UPDATE sys_table_operation
SET show_button = 1
WHERE table_code = 'sys_material'
  AND operation_code IN ('row_edit', 'row_read', 'row_delete');

COMMIT;
