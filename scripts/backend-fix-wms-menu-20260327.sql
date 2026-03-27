-- ============================================================
-- WMS 菜单配置修复脚本
-- 修复日期: 2026-03-27
-- 问题: WMS0030~WMS0090 的 parentId 错误(1→1080) 且 path 缺少 wms/ 前缀
-- ============================================================

-- 1. 查看修复前的状态
SELECT menu_id, menu_name, path, parent_id, component
FROM sys_menu
WHERE menu_id IN (2040, 2050, 2060, 2070, 2080, 2090, 2110);

-- 2. 执行修复
-- WMS0030 物料管理: material → wms/material, parent_id 1 → 1080
UPDATE sys_menu SET path = 'wms/material', parent_id = 1080, component = 'wms/material/index' WHERE menu_id = 2110;

-- WMS0040 客户管理: customer → wms/customer, parent_id 1 → 1080
UPDATE sys_menu SET path = 'wms/customer', parent_id = 1080, component = 'wms/customer/index' WHERE menu_id = 2040;

-- WMS0050 库区管理: storage → wms/storage, parent_id 1 → 1080
UPDATE sys_menu SET path = 'wms/storage', parent_id = 1080, component = 'wms/storage/index' WHERE menu_id = 2050;

-- WMS0060 货位管理: location → wms/location, parent_id 1 → 1080
UPDATE sys_menu SET path = 'wms/location', parent_id = 1080, component = 'wms/location/index' WHERE menu_id = 2060;

-- WMS0070 字典管理: dict → wms/dict, parent_id 1 → 1080
UPDATE sys_menu SET path = 'wms/dict', parent_id = 1080, component = 'wms/dict/index' WHERE menu_id = 2070;

-- WMS0080 用户管理: user → wms/user, parent_id 1 → 1080
UPDATE sys_menu SET path = 'wms/user', parent_id = 1080, component = 'wms/user/index' WHERE menu_id = 2080;

-- WMS0090 权限管理: permission → wms/permission, parent_id 1 → 1080
UPDATE sys_menu SET path = 'wms/permission', parent_id = 1080, component = 'wms/permission/index' WHERE menu_id = 2090;

-- 3. 验证修复后的状态
SELECT menu_id, menu_name, path, parent_id, component
FROM sys_menu
WHERE menu_id IN (2040, 2050, 2060, 2070, 2080, 2090, 2110);
