-- 低代码字典页面配置修复：新增/删除按钮 + 字典数据页 dict_type 下拉
-- Date: 2026-04-15

START TRANSACTION;

-- 1) 保障 sys_dict_type / sys_dict_data 的新增、删除按钮元数据可用
INSERT INTO sys_table_operation
(table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, event_type, event_config, confirm_message, is_enabled)
SELECT 'sys_dict_type', 'create', '新增', 'button', 'material-symbols:add', 'wms:base:dict:add', 'toolbar', 10, 1, 'builtin', '{"handler":"create"}', '', 1
FROM dual
WHERE NOT EXISTS (
  SELECT 1 FROM sys_table_operation WHERE table_code='sys_dict_type' AND operation_code='create'
);

INSERT INTO sys_table_operation
(table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, event_type, event_config, confirm_message, is_enabled)
SELECT 'sys_dict_type', 'row_delete', '删除', 'link', 'material-symbols:delete', 'wms:base:dict:delete', 'row', 20, 1, 'builtin', '{"handler":"delete"}', '', 1
FROM dual
WHERE NOT EXISTS (
  SELECT 1 FROM sys_table_operation WHERE table_code='sys_dict_type' AND operation_code='row_delete'
);

INSERT INTO sys_table_operation
(table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, event_type, event_config, confirm_message, is_enabled)
SELECT 'sys_dict_data', 'create', '新增', 'button', 'material-symbols:add', 'wms:base:dict:add', 'toolbar', 10, 1, 'builtin', '{"handler":"create"}', '', 1
FROM dual
WHERE NOT EXISTS (
  SELECT 1 FROM sys_table_operation WHERE table_code='sys_dict_data' AND operation_code='create'
);

INSERT INTO sys_table_operation
(table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, event_type, event_config, confirm_message, is_enabled)
SELECT 'sys_dict_data', 'row_delete', '删除', 'link', 'material-symbols:delete', 'wms:base:dict:delete', 'row', 20, 1, 'builtin', '{"handler":"delete"}', '', 1
FROM dual
WHERE NOT EXISTS (
  SELECT 1 FROM sys_table_operation WHERE table_code='sys_dict_data' AND operation_code='row_delete'
);

UPDATE sys_table_operation
SET permission='wms:base:dict:add', status=1, is_enabled=1, position='toolbar'
WHERE table_code IN ('sys_dict_type','sys_dict_data') AND operation_code='create';

UPDATE sys_table_operation
SET permission='wms:base:dict:delete', status=1, is_enabled=1, position='row'
WHERE table_code IN ('sys_dict_type','sys_dict_data') AND operation_code='row_delete';

-- 2) 配置 sys_dict_data.dict_type 为下拉搜索
UPDATE sys_column_meta
SET form_type='select',
    dict_type='sys_dict_type_selector',
    data_source='dict',
    component_props='{"allowClear":true,"showSearch":true}'
WHERE table_code='sys_dict_data' AND field='dict_type';

-- 3) 创建下拉数据源字典类型（若不存在）
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remarks, is_deleted)
SELECT '字典类型选择', 'sys_dict_type_selector', '0', 'codex', NOW(), '低代码字典数据页面-字典类型下拉数据源', '0'
FROM dual
WHERE NOT EXISTS (
  SELECT 1 FROM sys_dict_type WHERE dict_type='sys_dict_type_selector' AND is_deleted='0'
);

-- 4) 从 sys_dict_type 生成下拉选项
DELETE FROM sys_dict_data WHERE dict_type='sys_dict_type_selector';
SET @rn := 0;
INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, status, create_by, create_time, remarks)
SELECT (@rn := @rn + 1) * 10,
       t.dict_name,
       t.dict_type,
       'sys_dict_type_selector',
       '0',
       'codex',
       NOW(),
       '自动生成：来源 sys_dict_type'
FROM sys_dict_type t
WHERE COALESCE(t.is_deleted, '0') = '0'
ORDER BY t.dict_name;

COMMIT;
