-- =====================================================
-- 删除低代码管理菜单
-- 用途：删除之前插入的低代码管理相关菜单
-- =====================================================

-- ========== 步骤 1：删除角色菜单关联 ==========
DELETE FROM sys_role_menu WHERE role_id = 1 AND menu_id >= 2400 AND menu_id < 2500;

-- ========== 步骤 2：删除菜单记录 ==========
DELETE FROM sys_menu WHERE menu_id >= 2400 AND menu_id < 2500;

-- ========== 步骤 3：验证查询 ==========
-- 查询是否还有残留的菜单
SELECT menu_id, menu_name, parent_id, path
FROM sys_menu
WHERE menu_id >= 2400 AND menu_id < 2500;

-- 查询是否还有残留的角色菜单关联
SELECT rm.role_id, rm.menu_id, sm.menu_name
FROM sys_role_menu rm
LEFT JOIN sys_menu sm ON rm.menu_id = sm.menu_id
WHERE rm.role_id = 1 AND rm.menu_id >= 2400 AND rm.menu_id < 2500;