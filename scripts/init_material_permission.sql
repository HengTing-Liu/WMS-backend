-- ============================================================
-- 物料模块权限数据补充脚本
-- 补充 sys_menu 按钮权限节点 + sys_role_menu 分配
-- 执行前提：menu_id=2110（物料管理）已存在于 sys_menu
-- ============================================================

-- 1. 为菜单补充 perms（让页面菜单有操作权限）
UPDATE sys_menu
SET perms = 'wms:base:material:list'
WHERE menu_id = 2110
  AND (perms IS NULL OR perms = '' OR perms = '#');

-- 2. sys_menu 中补充物料按钮权限节点（F 型）
INSERT INTO sys_menu (menu_id, parent_id, menu_name, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2111, 2110, '物料查询', 1, '', '', '', 1, 0, 'F', 0, 0, 'wms:base:material:query', '#', 'admin', NOW(), NULL, NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2111);

INSERT INTO sys_menu (menu_id, parent_id, menu_name, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2112, 2110, '物料新增', 2, '', '', '', 1, 0, 'F', 0, 0, 'wms:base:material:add', '#', 'admin', NOW(), NULL, NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2112);

INSERT INTO sys_menu (menu_id, parent_id, menu_name, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2113, 2110, '物料修改', 3, '', '', '', 1, 0, 'F', 0, 0, 'wms:base:material:edit', '#', 'admin', NOW(), NULL, NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2113);

INSERT INTO sys_menu (menu_id, parent_id, menu_name, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2114, 2110, '物料删除', 4, '', '', '', 1, 0, 'F', 0, 0, 'wms:base:material:delete', '#', 'admin', NOW(), NULL, NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2114);

INSERT INTO sys_menu (menu_id, parent_id, menu_name, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2115, 2110, '物料导出', 5, '', '', '', 1, 0, 'F', 0, 0, 'wms:base:material:export', '#', 'admin', NOW(), NULL, NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2115);

INSERT INTO sys_menu (menu_id, parent_id, menu_name, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT 2116, 2110, '物料导入', 6, '', '', '', 1, 0, 'F', 0, 0, 'wms:base:material:import', '#', 'admin', NOW(), NULL, NULL, ''
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2116);

-- 3. sys_role_menu 中分配给 admin 角色（role_id=1）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2110 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2110);
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2111 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2111);
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2112 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2112);
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2113 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2113);
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2114 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2114);
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2115 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2115);
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, 2116 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_role_menu WHERE role_id = 1 AND menu_id = 2116);

-- ============================================================
-- 验证查询
-- ============================================================
-- SELECT menu_id, menu_name, menu_type, perms FROM sys_menu WHERE parent_id = 2110 OR menu_id = 2110 ORDER BY menu_id;
-- SELECT * FROM sys_role_menu WHERE role_id = 1 AND menu_id BETWEEN 2110 AND 2120;
-- ============================================================
