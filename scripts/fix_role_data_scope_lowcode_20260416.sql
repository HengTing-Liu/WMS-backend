-- 角色 data_scope 低代码配置修复（2026-04-16）
-- 目标：
-- 1) sys_role.data_scope 在低代码表单中改为“字典下拉”
-- 2) 补齐 data_scope 字典数据（值与 sys_role.data_scope 实际值一致：1~5）

START TRANSACTION;

-- 1. 修复 lowcode 字段配置：input -> select(dict)
UPDATE sys_column_meta
SET
  form_type = 'select',
  data_source = 'dict',
  dict_type = 'data_scope',
  update_by = 'cursor',
  update_time = NOW()
WHERE table_code = 'sys_role'
  AND field = 'data_scope';

-- 2. 确保字典类型存在
INSERT INTO sys_dict_type (
  dict_type, dict_name, status, is_deleted, remarks, create_by, create_time, update_by, update_time
)
SELECT
  'data_scope', '数据权限范围', '0', 0, '角色数据范围（sys_role.data_scope）', 'cursor', NOW(), 'cursor', NOW()
WHERE NOT EXISTS (
  SELECT 1 FROM sys_dict_type WHERE dict_type = 'data_scope'
);

UPDATE sys_dict_type
SET status = '0', update_by = 'cursor', update_time = NOW()
WHERE dict_type = 'data_scope';

-- 3. 清理旧字典项并按 1~5 重建
DELETE FROM sys_dict_data
WHERE dict_type = 'data_scope';

INSERT INTO sys_dict_data (
  dict_type, dict_sort, dict_label, dict_value, css_class, list_class, language_type,
  is_default, status, is_deleted, remarks, create_by, create_time, update_by, update_time
) VALUES
  ('data_scope', 1, '全部数据权限', '1', '', 'primary', 'CN', 'N', '0', '0', '对应 DATA_SCOPE_ALL', 'cursor', NOW(), 'cursor', NOW()),
  ('data_scope', 2, '自定义数据权限', '2', '', 'success', 'CN', 'N', '0', '0', '对应 DATA_SCOPE_CUSTOM', 'cursor', NOW(), 'cursor', NOW()),
  ('data_scope', 3, '本部门数据权限', '3', '', 'warning', 'CN', 'N', '0', '0', '对应 DATA_SCOPE_DEPT', 'cursor', NOW(), 'cursor', NOW()),
  ('data_scope', 4, '本部门及以下数据权限', '4', '', 'info', 'CN', 'N', '0', '0', '对应 DATA_SCOPE_DEPT_AND_CHILD', 'cursor', NOW(), 'cursor', NOW()),
  ('data_scope', 5, '仅本人数据权限', '5', '', 'default', 'CN', 'N', '0', '0', '对应 DATA_SCOPE_SELF', 'cursor', NOW(), 'cursor', NOW());

COMMIT;

-- 验证：
-- SELECT id, table_code, field, form_type, data_source, dict_type
-- FROM sys_column_meta
-- WHERE table_code = 'sys_role' AND field = 'data_scope';
--
-- SELECT dict_type, dict_label, dict_value, dict_sort, language_type, status
-- FROM sys_dict_data
-- WHERE dict_type = 'data_scope'
-- ORDER BY dict_sort, id;
