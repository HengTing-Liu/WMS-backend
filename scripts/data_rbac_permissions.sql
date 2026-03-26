-- ========================================================
-- WMS 系统 - RBAC 权限数据初始化脚本
-- ========================================================
-- 说明：修复 6 个模块返回 403 问题，为 admin 用户分配以下模块权限
-- 模块：customer / storage / location / dict / user / permission
-- 执行：连接数据库后执行此脚本即可
-- ========================================================

-- ========================================================
-- 1. 客户管理模块（WMS0040）
-- ========================================================
-- 父目录
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2040, '客户管理', 100, 4, '/customer', NULL, '1', '0', 'M', '0', '0', NULL, 'user', 'admin', NOW());

-- 页面菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2041, '客户列表', 2040, 1, '/customer/index', 'wms/customer/index', '1', '0', 'C', '0', '0', 'wms:base:customer:list', 'list', 'admin', NOW());

-- 按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2042, '客户查询', 2041, 1, '', '', '1', '0', 'F', '0', '0', 'wms:base:customer:query', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2043, '客户新增', 2041, 2, '', '', '1', '0', 'F', '0', '0', 'wms:base:customer:add', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2044, '客户修改', 2041, 3, '', '', '1', '0', 'F', '0', '0', 'wms:base:customer:edit', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2045, '客户删除', 2041, 4, '', '', '1', '0', 'F', '0', '0', 'wms:base:customer:delete', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2046, '客户导出', 2041, 5, '', '', '1', '0', 'F', '0', '0', 'wms:base:customer:export', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2047, '客户导入', 2041, 6, '', '', '1', '0', 'F', '0', '0', 'wms:base:customer:import', '#', 'admin', NOW());

-- ========================================================
-- 2. 仓储管理模块（WMS0041）
-- ========================================================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2050, '仓储管理', 100, 5, '/storage', NULL, '1', '0', 'M', '0', '0', NULL, 'warehouse', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2051, '仓储列表', 2050, 1, '/storage/index', 'wms/storage/index', '1', '0', 'C', '0', '0', 'wms:base:storage:list', 'list', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2052, '仓储查询', 2051, 1, '', '', '1', '0', 'F', '0', '0', 'wms:base:storage:query', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2053, '仓储新增', 2051, 2, '', '', '1', '0', 'F', '0', '0', 'wms:base:storage:add', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2054, '仓储修改', 2051, 3, '', '', '1', '0', 'F', '0', '0', 'wms:base:storage:edit', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2055, '仓储删除', 2051, 4, '', '', '1', '0', 'F', '0', '0', 'wms:base:storage:delete', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2056, '仓储导出', 2051, 5, '', '', '1', '0', 'F', '0', '0', 'wms:base:storage:export', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2057, '仓储导入', 2051, 6, '', '', '1', '0', 'F', '0', '0', 'wms:base:storage:import', '#', 'admin', NOW());

-- ========================================================
-- 3. 库位管理模块（WMS0042）
-- ========================================================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2060, '库位管理', 100, 6, '/location', NULL, '1', '0', 'M', '0', '0', NULL, 'location', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2061, '库位列表', 2060, 1, '/location/index', 'wms/location/index', '1', '0', 'C', '0', '0', 'wms:base:location:list', 'list', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2062, '库位查询', 2061, 1, '', '', '1', '0', 'F', '0', '0', 'wms:base:location:query', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2063, '库位新增', 2061, 2, '', '', '1', '0', 'F', '0', '0', 'wms:base:location:add', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2064, '库位修改', 2061, 3, '', '', '1', '0', 'F', '0', '0', 'wms:base:location:edit', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2065, '库位删除', 2061, 4, '', '', '1', '0', 'F', '0', '0', 'wms:base:location:delete', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2066, '库位导出', 2061, 5, '', '', '1', '0', 'F', '0', '0', 'wms:base:location:export', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2067, '库位导入', 2061, 6, '', '', '1', '0', 'F', '0', '0', 'wms:base:location:import', '#', 'admin', NOW());

-- ========================================================
-- 4. 字典管理模块（WMS0043）
-- ========================================================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2070, '字典管理', 100, 7, '/dict', NULL, '1', '0', 'M', '0', '0', NULL, 'dict', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2071, '字典列表', 2070, 1, '/dict/index', 'wms/dict/index', '1', '0', 'C', '0', '0', 'wms:base:dict:list', 'list', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2072, '字典查询', 2071, 1, '', '', '1', '0', 'F', '0', '0', 'wms:base:dict:query', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2073, '字典新增', 2071, 2, '', '', '1', '0', 'F', '0', '0', 'wms:base:dict:add', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2074, '字典修改', 2071, 3, '', '', '1', '0', 'F', '0', '0', 'wms:base:dict:edit', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2075, '字典删除', 2071, 4, '', '', '1', '0', 'F', '0', '0', 'wms:base:dict:delete', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2076, '字典导出', 2071, 5, '', '', '1', '0', 'F', '0', '0', 'wms:base:dict:export', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2077, '字典导入', 2071, 6, '', '', '1', '0', 'F', '0', '0', 'wms:base:dict:import', '#', 'admin', NOW());

-- ========================================================
-- 5. 用户管理模块（WMS0044）
-- ========================================================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2080, '用户管理', 100, 8, '/user', NULL, '1', '0', 'M', '0', '0', NULL, 'user', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2081, '用户列表', 2080, 1, '/user/index', 'wms/user/index', '1', '0', 'C', '0', '0', 'wms:base:user:list', 'list', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2082, '用户查询', 2081, 1, '', '', '1', '0', 'F', '0', '0', 'wms:base:user:query', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2083, '用户新增', 2081, 2, '', '', '1', '0', 'F', '0', '0', 'wms:base:user:add', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2084, '用户修改', 2081, 3, '', '', '1', '0', 'F', '0', '0', 'wms:base:user:edit', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2085, '用户删除', 2081, 4, '', '', '1', '0', 'F', '0', '0', 'wms:base:user:delete', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2086, '用户导出', 2081, 5, '', '', '1', '0', 'F', '0', '0', 'wms:base:user:export', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2087, '用户导入', 2081, 6, '', '', '1', '0', 'F', '0', '0', 'wms:base:user:import', '#', 'admin', NOW());

-- ========================================================
-- 6. 权限管理模块（WMS0045）
-- ========================================================
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2090, '权限管理', 100, 9, '/permission', NULL, '1', '0', 'M', '0', '0', NULL, 'lock', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2091, '权限列表', 2090, 1, '/permission/index', 'wms/permission/index', '1', '0', 'C', '0', '0', 'wms:base:permission:list', 'list', 'admin', NOW());

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2092, '权限查询', 2091, 1, '', '', '1', '0', 'F', '0', '0', 'wms:base:permission:query', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2093, '权限新增', 2091, 2, '', '', '1', '0', 'F', '0', '0', 'wms:base:permission:add', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2094, '权限修改', 2091, 3, '', '', '1', '0', 'F', '0', '0', 'wms:base:permission:edit', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2095, '权限删除', 2091, 4, '', '', '1', '0', 'F', '0', '0', 'wms:base:permission:delete', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2096, '权限导出', 2091, 5, '', '', '1', '0', 'F', '0', '0', 'wms:base:permission:export', '#', 'admin', NOW());
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES (2097, '权限导入', 2091, 6, '', '', '1', '0', 'F', '0', '0', 'wms:base:permission:import', '#', 'admin', NOW());

-- ========================================================
-- 7. 为 admin 角色（role_id=1）分配所有权限
-- ========================================================
-- 先清理可能存在的旧数据
DELETE FROM sys_role_menu WHERE role_id = 1 AND menu_id >= 2040 AND menu_id <= 2097;

-- 插入所有新权限关联
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2040);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2041);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2042);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2043);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2044);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2045);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2046);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2047);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2050);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2051);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2052);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2053);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2054);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2055);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2056);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2057);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2060);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2061);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2062);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2063);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2064);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2065);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2066);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2067);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2070);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2071);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2072);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2073);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2074);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2075);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2076);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2077);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2080);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2081);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2082);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2083);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2084);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2085);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2086);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2087);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2090);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2091);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2092);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2093);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2094);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2095);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2096);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2097);

-- ========================================================
-- 脚本执行完成
-- ========================================================
