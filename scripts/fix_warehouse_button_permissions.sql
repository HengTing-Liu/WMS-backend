-- ========================================================
-- 修复 sys_warehouse 低代码按钮权限缺失问题
-- 根因：sys_table_operation 里用 base:warehouse:* 权限，
--       但 sys_menu 表里从来没有创建过这些权限点按钮
-- ========================================================

-- 1. 在仓库档案菜单（parent_id 查仓库页面菜单）下创建按钮权限点
-- 先查找仓库档案页面的 menu_id
SET @parent_id = (SELECT menu_id FROM sys_menu WHERE path = '/sys/warehouse' LIMIT 1);
SELECT @parent_id AS warehouse_menu_id;

-- 如果找不到，用 sys 菜单下的仓库子菜单
SET @parent_id = COALESCE(
  (SELECT menu_id FROM sys_menu WHERE path = '/sys/warehouse' LIMIT 1),
  (SELECT menu_id FROM sys_menu WHERE path LIKE '%warehouse%' AND menu_type = 'M' LIMIT 1),
  1
);

-- 2. 创建 base:warehouse 按钮权限（工具栏按钮）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2100, '仓库新增', @parent_id, 1, '', '', '1', '0', 'F', '0', '0', 'base:warehouse:add', 'plus', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2101, '仓库编辑', @parent_id, 2, '', '', '1', '0', 'F', '0', '0', 'base:warehouse:edit', 'edit', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2102, '仓库删除', @parent_id, 3, '', '', '1', '0', 'F', '0', '0', 'base:warehouse:delete', 'delete', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2103, '仓库导出', @parent_id, 4, '', '', '1', '0', 'F', '0', '0', 'base:warehouse:export', 'download', 'system', NOW())
ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name), perms = VALUES(perms);

-- 3. 为 admin 角色（role_id=1）分配这些按钮权限
DELETE FROM sys_role_menu WHERE role_id = 1 AND menu_id BETWEEN 2100 AND 2103;
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2101);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2102);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2103);

-- 4. 验证：查看已创建的仓库按钮权限
SELECT menu_id, menu_name, perms FROM sys_menu WHERE perms LIKE 'base:warehouse:%';

-- 5. 验证：查看 admin 角色当前持有的所有权限码
SELECT sm.perms FROM sys_role_menu srm
JOIN sys_menu sm ON srm.menu_id = sm.menu_id
WHERE srm.role_id = 1 AND sm.perms IS NOT NULL AND sm.perms != '' AND sm.perms != '#'
ORDER BY sm.perms;