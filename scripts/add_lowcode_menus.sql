-- =====================================================
-- 低代码管理菜单初始化脚本（修正版）
-- 用途：在「系统设置 / 系统管理」目录下添加：低代码管理 -> 表/字段/操作元数据
--
-- 重要说明（根因）：
--   不同环境「系统设置」的 menu_id 不一定是 2。
--   本脚本通过「用户管理」等已有菜单反查 parent_id，作为低代码目录的父级。
-- =====================================================

-- ========== 步骤 0：解析系统设置目录的 menu_id ==========
-- 优先：取「用户管理」菜单的 parent_id（与侧栏里用户/角色/部门同级）
SET @parent_sys = (
  SELECT m.parent_id
  FROM sys_menu m
  WHERE m.menu_type = 'C'
    AND (
      m.menu_name = '用户管理'
      OR m.path IN ('system/user', '/system/user', 'user', '/user')
      OR m.path LIKE 'system/user%'
      OR IFNULL(m.component, '') LIKE '%system/user%'
    )
  ORDER BY m.menu_id
  LIMIT 1
);

-- 兜底：按名称/路径找一级目录
SET @parent_sys = IFNULL(@parent_sys, (
  SELECT menu_id FROM sys_menu
  WHERE menu_type = 'M'
    AND parent_id = 0
    AND (
      menu_name IN ('系统设置', '系统管理')
      OR path IN ('/system', 'system')
    )
  ORDER BY menu_id
  LIMIT 1
));

-- 再兜底：固定 2（仅适用于标准若依初始化库）
SET @parent_sys = IFNULL(@parent_sys, 2);

SELECT CONCAT('>>> 低代码目录父级 menu_id = ', @parent_sys) AS check_parent;

-- ========== 步骤 1：清理旧数据（本脚本使用的 ID 段）==========
DELETE FROM sys_role_menu WHERE menu_id BETWEEN 2400 AND 2499;
DELETE FROM sys_menu WHERE menu_id BETWEEN 2400 AND 2499;
DELETE FROM sys_role_menu WHERE menu_id = 2300;
DELETE FROM sys_menu WHERE menu_id = 2300;

-- ========== 步骤 2：插入目录「低代码管理」==========
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (2400, '低代码管理', @parent_sys, 50, 'lowcode', '', '1', '0', 'M', '0', '0', '', 'ic:baseline-view-in-ar', 'system', NOW());

-- ========== 步骤 3：插入三个页面菜单 ==========
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES
(2401, '表元数据', 2400, 1, 'lowcode/table', 'system/tableMeta/index', '1', '0', 'C', '0', '0', 'system:tableMeta:list', 'ic:baseline-table', 'system', NOW()),
(2402, '字段元数据', 2400, 2, 'lowcode/column', 'system/columnMeta/index', '1', '0', 'C', '0', '0', 'system:columnMeta:list', 'ic:baseline-table-chart', 'system', NOW()),
(2403, '操作元数据', 2400, 3, 'lowcode/operation', 'system/operationMeta/index', '1', '0', 'C', '0', '0', 'system:operationMeta:list', 'ic:baseline-build', 'system', NOW()),
(2404, '发布管理', 2400, 4, 'lowcode/publish', 'system/metaPublish/index', '1', '0', 'C', '0', '0', 'system:meta:publish:list', 'ic:baseline-rocket-launch', 'system', NOW());

-- ========== 步骤 4：按钮权限（F）==========
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
(2410, '表元数据查询', 2401, 1, '', '', '1', '0', 'F', '0', '0', 'system:tableMeta:query', '#', 'system', NOW()),
(2411, '表元数据新增', 2401, 2, '', '', '1', '0', 'F', '0', '0', 'system:tableMeta:add', '#', 'system', NOW()),
(2412, '表元数据修改', 2401, 3, '', '', '1', '0', 'F', '0', '0', 'system:tableMeta:edit', '#', 'system', NOW()),
(2413, '表元数据删除', 2401, 4, '', '', '1', '0', 'F', '0', '0', 'system:tableMeta:delete', '#', 'system', NOW()),
(2414, '表元数据导出', 2401, 5, '', '', '1', '0', 'F', '0', '0', 'system:tableMeta:export', '#', 'system', NOW()),
(2420, '字段元数据查询', 2402, 1, '', '', '1', '0', 'F', '0', '0', 'system:columnMeta:query', '#', 'system', NOW()),
(2421, '字段元数据新增', 2402, 2, '', '', '1', '0', 'F', '0', '0', 'system:columnMeta:add', '#', 'system', NOW()),
(2422, '字段元数据修改', 2402, 3, '', '', '1', '0', 'F', '0', '0', 'system:columnMeta:edit', '#', 'system', NOW()),
(2423, '字段元数据删除', 2402, 4, '', '', '1', '0', 'F', '0', '0', 'system:columnMeta:delete', '#', 'system', NOW()),
(2425, '字段复制', 2402, 5, '', '', '1', '0', 'F', '0', '0', 'system:columnMeta:copy', '#', 'system', NOW()),
(2430, '操作元数据查询', 2403, 1, '', '', '1', '0', 'F', '0', '0', 'system:operationMeta:query', '#', 'system', NOW()),
(2431, '操作元数据新增', 2403, 2, '', '', '1', '0', 'F', '0', '0', 'system:operationMeta:add', '#', 'system', NOW()),
(2432, '操作元数据修改', 2403, 3, '', '', '1', '0', 'F', '0', '0', 'system:operationMeta:edit', '#', 'system', NOW()),
(2433, '操作元数据删除', 2403, 4, '', '', '1', '0', 'F', '0', '0', 'system:operationMeta:delete', '#', 'system', NOW()),
(2440, '发布计划预览', 2404, 1, '', '', '1', '0', 'F', '0', '0', 'system:meta:publish:plan', '#', 'system', NOW()),
(2441, '发布执行', 2404, 2, '', '', '1', '0', 'F', '0', '0', 'system:meta:publish:execute', '#', 'system', NOW()),
(2442, '发布查询', 2404, 3, '', '', '1', '0', 'F', '0', '0', 'system:meta:publish:query', '#', 'system', NOW()),
(2443, '发布回滚', 2404, 4, '', '', '1', '0', 'F', '0', '0', 'system:meta:publish:rollback', '#', 'system', NOW());

-- ========== 步骤 5：角色菜单（管理员 role_id=1）==========
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 2400), (1, 2401), (1, 2402), (1, 2403), (1, 2404),
(1, 2410), (1, 2411), (1, 2412), (1, 2413), (1, 2414),
(1, 2420), (1, 2421), (1, 2422), (1, 2423), (1, 2425),
(1, 2430), (1, 2431), (1, 2432), (1, 2433),
(1, 2440), (1, 2441), (1, 2442), (1, 2443);

-- ========== 验证 ==========
SELECT '=== 解析到的系统目录 与 低代码菜单 ===' AS info;
SELECT menu_id, menu_name, parent_id, path, component, menu_type, status
FROM sys_menu
WHERE menu_id IN (@parent_sys, 2400, 2401, 2402, 2403, 2404)
ORDER BY menu_id;

SELECT '=== 与「用户管理」同级的所有子菜单（应用为同一 parent_id）===' AS info;
SELECT menu_id, menu_name, parent_id, path, order_num
FROM sys_menu
WHERE parent_id = @parent_sys AND menu_type IN ('M', 'C')
ORDER BY order_num, menu_id;
