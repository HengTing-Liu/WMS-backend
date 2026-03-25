-- =============================================
-- 库位管理菜单及权限配置 SQL
-- =============================================
-- 执行前请确认 sys_menu 表的最大 menu_id，适当调整本脚本中的 ID 值
-- 建议 menu_id 从 2000 开始，避免与现有菜单冲突
-- =============================================

-- =============================================
-- 1. 插入父菜单（目录）
-- =============================================
INSERT INTO sys_menu (menu_name, menu_type, order_num, status, perms, component, path, icon, create_by, create_time, remark)
VALUES 
('库位管理', 'M', 100, '0', NULL, NULL, '/location', 'el-icon-s-home', 'admin', NOW(), '库位档案管理目录');

-- 获取刚插入的父菜单ID（假设为 @parent_id）
-- 注意：实际执行时请根据情况调整或使用变量

-- =============================================
-- 2. 插入子菜单（菜单项）
-- =============================================
-- 假设父菜单ID为 2000，请根据实际情况调整 parent_id

-- 库位列表页面
INSERT INTO sys_menu (menu_name, menu_type, parent_id, order_num, status, perms, component, path, icon, create_by, create_time, remark)
VALUES 
('库位列表', 'C', 2000, 1, '0', 'wms:location:list', 'wms/location/index', 'location', 'el-icon-s-grid', 'admin', NOW(), '库位档案列表页面');

-- =============================================
-- 3. 插入按钮权限
-- =============================================
-- 假设库位列表菜单ID为 2001，请根据实际情况调整 parent_id

-- 查询权限
INSERT INTO sys_menu (menu_name, menu_type, parent_id, order_num, status, perms, create_by, create_time, remark)
VALUES 
('库位查询', 'F', 2001, 1, '0', 'wms:location:list', 'admin', NOW(), '分页查询库位列表'),
('库位详情', 'F', 2001, 2, '0', 'wms:location:query', 'admin', NOW(), '根据ID查询库位详情'),
('占用率查询', 'F', 2001, 3, '0', 'wms:location:occupancy', 'admin', NOW(), '查询库位占用率统计'),
('树形查询', 'F', 2001, 4, '0', 'wms:location:tree', 'admin', NOW(), '查询库位树形结构');

-- 新增权限
INSERT INTO sys_menu (menu_name, menu_type, parent_id, order_num, status, perms, create_by, create_time, remark)
VALUES 
('库位新增', 'F', 2001, 5, '0', 'wms:location:add', 'admin', NOW(), '新增库位档案'),
('批量新增', 'F', 2001, 6, '0', 'wms:location:batchAdd', 'admin', NOW(), '批量新增库位'),
('批量创建', 'F', 2001, 7, '0', 'wms:location:batchCreate', 'admin', NOW(), '根据模板批量创建库位');

-- 编辑权限
INSERT INTO sys_menu (menu_name, menu_type, parent_id, order_num, status, perms, create_by, create_time, remark)
VALUES 
('库位编辑', 'F', 2001, 8, '0', 'wms:location:edit', 'admin', NOW(), '编辑库位档案'),
('批量更新', 'F', 2001, 9, '0', 'wms:location:batchEdit', 'admin', NOW(), '批量更新库位');

-- 删除权限
INSERT INTO sys_menu (menu_name, menu_type, parent_id, order_num, status, perms, create_by, create_time, remark)
VALUES 
('库位删除', 'F', 2001, 10, '0', 'wms:location:delete', 'admin', NOW(), '删除库位档案（逻辑删除）'),
('递归删除', 'F', 2001, 11, '0', 'wms:location:deleteRecursive', 'admin', NOW(), '递归删除库位及其子节点'),
('批量删除', 'F', 2001, 12, '0', 'wms:location:batchDelete', 'admin', NOW(), '批量删除库位');

-- 导入导出权限
INSERT INTO sys_menu (menu_name, menu_type, parent_id, order_num, status, perms, create_by, create_time, remark)
VALUES 
('库位导出', 'F', 2001, 13, '0', 'wms:location:export', 'admin', NOW(), '导出库位数据到Excel'),
('库位导入', 'F', 2001, 14, '0', 'wms:location:import', 'admin', NOW(), '从Excel导入库位数据'),
('下载模板', 'F', 2001, 15, '0', 'wms:location:downloadTemplate', 'admin', NOW(), '下载导入模板');


-- =============================================
-- 4. 角色权限关联（可选）
-- 为管理员角色（role_id=1）分配所有库位权限
-- =============================================
-- 假设管理员角色ID为 1
-- 先删除已存在的权限，避免重复
DELETE FROM sys_role_menu WHERE role_id = 1 AND menu_id IN (
    SELECT menu_id FROM sys_menu WHERE perms LIKE 'wms:location:%'
);

-- 为管理员角色分配所有库位菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE perms LIKE 'wms:location:%' OR menu_name = '库位管理' OR menu_name = '库位列表';


-- =============================================
-- 5. 插入字典数据（可选）
-- 库位管理相关枚举值
-- =============================================
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('库位等级', 'wms_location_grade', '0', 'admin', NOW(), '库位等级分类');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, status, create_by, create_time, remark)
VALUES 
(1, '类型', 'Type', 'wms_location_grade', '0', 'admin', NOW(), '根节点类型'),
(2, '类型分区', 'TypeSection', 'wms_location_grade', '0', 'admin', NOW(), '层/架/行等'),
(3, '容器', 'Container', 'wms_location_grade', '0', 'admin', NOW(), '盒/箱/瓶等'),
(4, '容器孔位', 'ContainerPosition', 'wms_location_grade', '0', 'admin', NOW(), '孔位');

INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('存储模式', 'wms_storage_mode', '0', 'admin', NOW(), '库位存储模式');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, status, create_by, create_time, remark)
VALUES 
(1, '独占模式', 'Exclusive', 'wms_storage_mode', '0', 'admin', NOW(), '独占使用'),
(2, '共享模式', 'Shared', 'wms_storage_mode', '0', 'admin', NOW(), '共享使用');


-- =============================================
-- 6. 国际化消息（可选）
-- =============================================
-- 如果系统支持国际化，可以添加以下消息
-- INSERT INTO sys_message (message_key, message_zh, message_en, create_by, create_time)
-- VALUES 
-- ('wms.location.not.found', '库位不存在', 'Location not found', 'admin', NOW()),
-- ('wms.location.parent.not.found', '上级库位不存在', 'Parent location not found', 'admin', NOW()),
-- ('wms.location.has.children', '该库位存在子节点，无法删除', 'Location has children nodes', 'admin', NOW()),
-- ('wms.location.id.required', '库位ID不能为空', 'Location ID is required', 'admin', NOW());


-- =============================================
-- 执行后请刷新缓存或重启服务！
-- =============================================
