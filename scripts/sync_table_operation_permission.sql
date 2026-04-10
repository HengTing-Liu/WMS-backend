-- ========================================================
-- 修复 sys_table_operation.permission 与 sys_menu.perms 不匹配问题
-- 根因：operation.permission 用 base:* 前缀，但 sys_menu 里没有这些权限点
-- 策略：
--   1. 先在 sys_menu 里补全所有 base:* 按钮权限点（menu_type='F'）
--   2. 为 admin 角色分配这些按钮权限
--   3. 将 sys_table_operation.permission 对齐到 sys_menu.perms
--   4. 用 ON DUPLICATE KEY UPDATE 确保幂等
-- ========================================================

-- ===== 第一步：创建 base:* 按钮权限点 =====

-- 1.1 仓库管理 (sys_warehouse -> system:warehouse:* 和 base:warehouse:*)
-- system: 前缀（主要）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2200, '仓库新增', 100, 30, '', '', '1', '0', 'F', '0', '0', 'system:warehouse:add', 'plus', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2201, '仓库编辑', 100, 31, '', '', '1', '0', 'F', '0', '0', 'system:warehouse:edit', 'edit', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2202, '仓库删除', 100, 32, '', '', '1', '0', 'F', '0', '0', 'system:warehouse:delete', 'delete', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2203, '仓库导出', 100, 33, '', '', '1', '0', 'F', '0', '0', 'system:warehouse:export', 'download', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2204, '仓库导入', 100, 34, '', '', '1', '0', 'F', '0', '0', 'system:warehouse:import', 'upload', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
-- base: 前缀（兼容旧数据）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2250, '仓库新增(base)', 100, 30, '', '', '1', '0', 'F', '0', '0', 'base:warehouse:add', 'plus', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2251, '仓库编辑(base)', 100, 31, '', '', '1', '0', 'F', '0', '0', 'base:warehouse:edit', 'edit', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2252, '仓库删除(base)', 100, 32, '', '', '1', '0', 'F', '0', '0', 'base:warehouse:delete', 'delete', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2253, '仓库导出(base)', 100, 33, '', '', '1', '0', 'F', '0', '0', 'base:warehouse:export', 'download', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);

-- 1.2 仓库收货人 (sys_warehouse_receiver -> system:warehouseReceiver:* 和 base:warehouseReceiver:*)
-- system: 前缀
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2210, '收货人新增', 100, 35, '', '', '1', '0', 'F', '0', '0', 'system:warehouseReceiver:add', 'plus', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2211, '收货人编辑', 100, 36, '', '', '1', '0', 'F', '0', '0', 'system:warehouseReceiver:edit', 'edit', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2212, '收货人删除', 100, 37, '', '', '1', '0', 'F', '0', '0', 'system:warehouseReceiver:delete', 'delete', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
-- base: 前缀（兼容旧数据）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2260, '收货人新增(base)', 100, 35, '', '', '1', '0', 'F', '0', '0', 'base:warehouseReceiver:add', 'plus', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2261, '收货人编辑(base)', 100, 36, '', '', '1', '0', 'F', '0', '0', 'base:warehouseReceiver:edit', 'edit', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2262, '收货人删除(base)', 100, 37, '', '', '1', '0', 'F', '0', '0', 'base:warehouseReceiver:delete', 'delete', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);

-- ===== 第二步：为 admin 角色分配这些按钮权限 =====
DELETE FROM sys_role_menu WHERE role_id = 1 AND menu_id BETWEEN 2200 AND 2299;
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2200);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2201);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2202);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2203);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2204);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2210);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2211);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2212);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2250);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2251);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2252);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2253);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2260);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2261);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2262);

-- ===== 第三步：将 sys_table_operation.permission 更新为 sys_menu 里已存在的 perms =====
-- 规则：按 [resource]:[action] 匹配，优先用 system: 前缀，其次 wms:base:，最后 base:
-- 操作表里 permission 字段格式: 前缀:资源:动作

-- 3.1 仓库档案 (sys_warehouse)
-- operation.permission 是 base:warehouse:*，替换为 system:warehouse:*（因为 system 是最通用的）
UPDATE sys_table_operation sto
SET sto.permission = 'system:warehouse:add'
WHERE sto.table_code = 'sys_warehouse' AND sto.operation_code = 'add' AND sto.permission = 'base:warehouse:add';

UPDATE sys_table_operation sto
SET sto.permission = 'system:warehouse:edit'
WHERE sto.table_code = 'sys_warehouse' AND sto.operation_code IN ('edit', 'row_edit') AND sto.permission = 'base:warehouse:edit';

UPDATE sys_table_operation sto
SET sto.permission = 'system:warehouse:delete'
WHERE sto.table_code = 'sys_warehouse' AND sto.operation_code IN ('delete', 'row_delete') AND sto.permission = 'base:warehouse:delete';

UPDATE sys_table_operation sto
SET sto.permission = 'system:warehouse:export'
WHERE sto.table_code = 'sys_warehouse' AND sto.operation_code = 'export' AND sto.permission = 'base:warehouse:export';

-- 3.2 仓库收货人 (sys_warehouse_receiver)
UPDATE sys_table_operation sto
SET sto.permission = 'system:warehouseReceiver:add'
WHERE sto.table_code = 'sys_warehouse_receiver' AND sto.operation_code = 'add' AND sto.permission = 'base:warehouseReceiver:add';

UPDATE sys_table_operation sto
SET sto.permission = 'system:warehouseReceiver:edit'
WHERE sto.table_code = 'sys_warehouse_receiver' AND sto.operation_code IN ('edit', 'row_edit', 'row_set_default') AND sto.permission = 'base:warehouseReceiver:edit';

UPDATE sys_table_operation sto
SET sto.permission = 'system:warehouseReceiver:delete'
WHERE sto.table_code = 'sys_warehouse_receiver' AND sto.operation_code IN ('delete', 'row_delete') AND sto.permission = 'base:warehouseReceiver:delete';

-- ===== 第四步：验证结果 =====
SELECT '=== sys_table_operation.permission 对齐结果 ===' AS msg;
SELECT table_code, operation_code, operation_name, permission, position
FROM sys_table_operation
WHERE permission LIKE 'base:%'
ORDER BY table_code, position, sort_order;

SELECT '=== sys_table_operation 所有 warehouse/receiver 按钮 ===' AS msg;
SELECT table_code, operation_code, permission, position
FROM sys_table_operation
WHERE table_code IN ('sys_warehouse', 'sys_warehouse_receiver')
ORDER BY table_code, position, sort_order;

SELECT '=== 新建的 base:* 按钮权限 ===' AS msg;
SELECT menu_id, menu_name, perms FROM sys_menu WHERE menu_id >= 2200 AND menu_id <= 2299 ORDER BY menu_id;