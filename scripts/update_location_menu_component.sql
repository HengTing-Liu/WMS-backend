-- =============================================
-- 修改库位档案菜单，将component指向新页面
-- =============================================

-- Step 1: 查询当前库位相关的菜单
-- 查看当前 sys_menu 表中与库位相关的菜单记录

SELECT menu_id, menu_name, parent_id, path, component, perms, menu_type
FROM sys_menu
WHERE menu_name LIKE '%库位%'
   OR path LIKE '%location%'
   OR component LIKE '%location%'
ORDER BY parent_id, order_num;

-- =============================================
-- Step 2: 更新菜单的 component 路径
-- 旧路径: wms/location/index
-- 新路径: views/location/index
-- =============================================

-- 方式1：根据 component 精确匹配
UPDATE sys_menu
SET component = 'views/location/index'
WHERE component = 'wms/location/index';

-- 方式2：根据菜单名称匹配（如果上面的方式没有匹配到）
-- UPDATE sys_menu
-- SET component = 'views/location/index'
-- WHERE menu_name = '库位档案';

-- 方式3：根据路径匹配（适用于不同路径格式）
-- UPDATE sys_menu
-- SET component = 'views/location/index'
-- WHERE path = 'location' OR path = '/location';

-- =============================================
-- Step 3: 确认更新结果
-- =============================================

SELECT menu_id, menu_name, path, component
FROM sys_menu
WHERE menu_name LIKE '%库位%'
   OR component LIKE '%location%'
ORDER BY menu_id;

-- =============================================
-- Step 4: 清除权限缓存（重要！）
-- 执行以下命令清除 Redis 缓存
-- =============================================

-- 连接 Redis 并清除菜单缓存
-- redis-cli -h localhost -p 6379
-- FLUSHDB
-- 或者选择性清除
-- DEL "menu::1"
-- DEL "perms:1"

-- =============================================
-- 验证方法
-- =============================================
-- 1. 退出登录，重新登录系统
-- 2. 清除浏览器缓存 (Ctrl+Shift+Delete)
-- 3. 强制刷新页面 (Ctrl+F5)
-- 4. 访问 基础设置 -> 库位档案
-- 5. 检查 Network 面板中菜单接口返回的 component 值
-- =============================================

-- =============================================
-- 回滚方法（如果需要恢复）
-- =============================================

-- UPDATE sys_menu
-- SET component = 'wms/location/index'
-- WHERE component = 'views/location/index';
