-- 流水号页面菜单与按钮权限配置
-- Date: 2026-04-15

START TRANSACTION;

-- 确保流水号菜单存在并启用（已存在时仅修正状态）
UPDATE sys_menu
SET status='0', visible='0', menu_type='M', component='system/serial/index', update_by='codex', update_time=NOW()
WHERE menu_id=3006;

-- 生成新增按钮菜单
SET @menu_id_add := NULL;
SELECT menu_id INTO @menu_id_add FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:add' LIMIT 1;
SET @menu_id_add := IFNULL(@menu_id_add, (SELECT COALESCE(MAX(menu_id), 1000) + 1 FROM sys_menu));
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remarks)
SELECT @menu_id_add, 'Add', 3006, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'sys:serial:add', '', 'codex', NOW(), '流水号按钮权限'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:add');
UPDATE sys_menu SET menu_name='Add', menu_type='F', visible='0', status='0', order_num=1, path='#', component='', is_frame=1, is_cache=0, update_by='codex', update_time=NOW() WHERE menu_id=@menu_id_add;

-- 编辑按钮
SET @menu_id_edit := NULL;
SELECT menu_id INTO @menu_id_edit FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:edit' LIMIT 1;
SET @menu_id_edit := IFNULL(@menu_id_edit, (SELECT COALESCE(MAX(menu_id), 1000) + 1 FROM sys_menu));
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remarks)
SELECT @menu_id_edit, 'Edit', 3006, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'sys:serial:edit', '', 'codex', NOW(), '流水号按钮权限'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:edit');
UPDATE sys_menu SET menu_name='Edit', menu_type='F', visible='0', status='0', order_num=2, path='#', component='', is_frame=1, is_cache=0, update_by='codex', update_time=NOW() WHERE menu_id=@menu_id_edit;

-- 删除按钮
SET @menu_id_delete := NULL;
SELECT menu_id INTO @menu_id_delete FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:delete' LIMIT 1;
SET @menu_id_delete := IFNULL(@menu_id_delete, (SELECT COALESCE(MAX(menu_id), 1000) + 1 FROM sys_menu));
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remarks)
SELECT @menu_id_delete, 'Delete', 3006, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'sys:serial:delete', '', 'codex', NOW(), '流水号按钮权限'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:delete');
UPDATE sys_menu SET menu_name='Delete', menu_type='F', visible='0', status='0', order_num=3, path='#', component='', is_frame=1, is_cache=0, update_by='codex', update_time=NOW() WHERE menu_id=@menu_id_delete;

-- 导出按钮
SET @menu_id_export := NULL;
SELECT menu_id INTO @menu_id_export FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:export' LIMIT 1;
SET @menu_id_export := IFNULL(@menu_id_export, (SELECT COALESCE(MAX(menu_id), 1000) + 1 FROM sys_menu));
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remarks)
SELECT @menu_id_export, 'Export', 3006, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'sys:serial:export', '', 'codex', NOW(), '流水号按钮权限'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:export');
UPDATE sys_menu SET menu_name='Export', menu_type='F', visible='0', status='0', order_num=4, path='#', component='', is_frame=1, is_cache=0, update_by='codex', update_time=NOW() WHERE menu_id=@menu_id_export;

-- 生成按钮
SET @menu_id_generate := NULL;
SELECT menu_id INTO @menu_id_generate FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:generate' LIMIT 1;
SET @menu_id_generate := IFNULL(@menu_id_generate, (SELECT COALESCE(MAX(menu_id), 1000) + 1 FROM sys_menu));
INSERT INTO sys_menu(menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remarks)
SELECT @menu_id_generate, 'Generate', 3006, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'sys:serial:generate', '', 'codex', NOW(), '流水号按钮权限'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=3006 AND perms='sys:serial:generate');
UPDATE sys_menu SET menu_name='Generate', menu_type='F', visible='0', status='0', order_num=5, path='#', component='', is_frame=1, is_cache=0, update_by='codex', update_time=NOW() WHERE menu_id=@menu_id_generate;

-- 授权系统管理员角色（role_id=1）
INSERT INTO sys_role_menu(role_id, menu_id)
SELECT 1, 3006 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id=1 AND menu_id=3006);

INSERT INTO sys_role_menu(role_id, menu_id)
SELECT 1, m.menu_id
FROM sys_menu m
LEFT JOIN sys_role_menu rm ON rm.role_id=1 AND rm.menu_id=m.menu_id
WHERE m.parent_id=3006 AND m.perms IN (
  'sys:serial:add', 'sys:serial:edit', 'sys:serial:delete', 'sys:serial:export', 'sys:serial:generate'
) AND rm.menu_id IS NULL;

-- 授权仓库管理员角色（role_id=11）
INSERT INTO sys_role_menu(role_id, menu_id)
SELECT 11, 3006 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id=11 AND menu_id=3006);

INSERT INTO sys_role_menu(role_id, menu_id)
SELECT 11, m.menu_id
FROM sys_menu m
LEFT JOIN sys_role_menu rm ON rm.role_id=11 AND rm.menu_id=m.menu_id
WHERE m.parent_id=3006 AND m.perms IN (
  'sys:serial:add', 'sys:serial:edit', 'sys:serial:delete', 'sys:serial:export', 'sys:serial:generate'
) AND rm.menu_id IS NULL;

COMMIT;
