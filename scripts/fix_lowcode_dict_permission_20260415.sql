-- Fix lowcode dict permissions to align with existing RBAC namespace.
-- Date: 2026-04-15

START TRANSACTION;

-- 1) Align lowcode table-level permission namespace
UPDATE sys_table_meta
SET permission_code = 'wms:base:dict',
    update_by = 'codex',
    update_time = NOW()
WHERE table_code IN ('sys_dict_type', 'sys_dict_data');

-- 2) Align lowcode operation permissions
UPDATE sys_table_operation
SET permission = 'wms:base:dict:add'
WHERE table_code IN ('sys_dict_type', 'sys_dict_data')
  AND operation_code = 'create';

UPDATE sys_table_operation
SET permission = 'wms:base:dict:edit'
WHERE table_code IN ('sys_dict_type', 'sys_dict_data')
  AND operation_code IN ('row_edit', 'toggle');

UPDATE sys_table_operation
SET permission = 'wms:base:dict:delete'
WHERE table_code IN ('sys_dict_type', 'sys_dict_data')
  AND operation_code IN ('row_delete', 'delete_batch');

UPDATE sys_table_operation
SET permission = 'wms:base:dict:import'
WHERE table_code IN ('sys_dict_type', 'sys_dict_data')
  AND operation_code = 'import';

UPDATE sys_table_operation
SET permission = 'wms:base:dict:export'
WHERE table_code IN ('sys_dict_type', 'sys_dict_data')
  AND operation_code = 'export';

UPDATE sys_table_operation
SET permission = 'wms:base:dict:query'
WHERE table_code IN ('sys_dict_type', 'sys_dict_data')
  AND operation_code = 'detail';

UPDATE sys_table_operation
SET permission = 'wms:base:dict:list'
WHERE table_code IN ('sys_dict_type', 'sys_dict_data')
  AND operation_code IN ('query', 'list');

-- 3) Ensure admin role has dict button menus (menu permission source is sys_menu.perms)
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, m.menu_id
FROM sys_menu m
LEFT JOIN sys_role_menu rm
  ON rm.role_id = 1 AND rm.menu_id = m.menu_id
WHERE m.perms LIKE 'wms:base:dict:%'
  AND rm.menu_id IS NULL;

COMMIT;
