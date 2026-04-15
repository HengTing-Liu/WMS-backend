-- ========================================================
-- WMS 仓库管理系统 - 默认数据初始化脚本
-- ========================================================
-- 执行顺序：先部门 -> 角色 -> 菜单 -> 用户 -> 角色菜单关联 -> 角色部门关联
-- ========================================================

-- ========================================================
-- 1. 初始化部门数据
-- ========================================================

-- 清空现有部门数据（谨慎使用）
-- DELETE FROM sys_dept WHERE dept_id > 0;

-- 插入根部门（总公司）
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) 
VALUES (1, 0, '0', '总公司', 1, '总经理', '13800138000', 'admin@wms.com', '0', '0', 'system', NOW());

-- 插入一级部门
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) 
VALUES 
(2, 1, '0,1', '仓储中心', 1, '仓储经理', '13800138001', 'warehouse@wms.com', '0', '0', 'system', NOW()),
(3, 1, '0,1', '运营中心', 2, '运营经理', '13800138002', 'operation@wms.com', '0', '0', 'system', NOW()),
(4, 1, '0,1', '财务中心', 3, '财务经理', '13800138003', 'finance@wms.com', '0', '0', 'system', NOW()),
(5, 1, '0,1', '质量中心', 4, '质量经理', '13800138004', 'qc@wms.com', '0', '0', 'system', NOW());

-- 插入二级部门（仓储中心下）
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) 
VALUES 
(6, 2, '0,1,2', '入库组', 1, '入库主管', '13800138005', 'inbound@wms.com', '0', '0', 'system', NOW()),
(7, 2, '0,1,2', '出库组', 2, '出库主管', '13800138006', 'outbound@wms.com', '0', '0', 'system', NOW()),
(8, 2, '0,1,2', '库存组', 3, '库存主管', '13800138007', 'inventory@wms.com', '0', '0', 'system', NOW());

-- 插入二级部门（运营中心下）
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time) 
VALUES 
(9, 3, '0,1,3', '订单组', 1, '订单主管', '13800138008', 'order@wms.com', '0', '0', 'system', NOW()),
(10, 3, '0,1,3', '客服组', 2, '客服主管', '13800138009', 'service@wms.com', '0', '0', 'system', NOW());

-- ========================================================
-- 2. 初始化角色数据
-- ========================================================

-- 清空现有角色数据（谨慎使用）
-- DELETE FROM sys_role WHERE role_id > 0;

-- 插入默认角色
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark) 
VALUES 
-- 系统管理员：全部数据权限
(1, '系统管理员', 'admin', 1, '1', 1, 1, '0', '0', 'system', NOW(), '系统超级管理员，拥有所有权限'),

-- 仓库经理：部门及以下数据权限
(2, '仓库经理', 'warehouse_manager', 2, '4', 1, 1, '0', '0', 'system', NOW(), '负责仓库整体管理'),

-- 入库专员：本部门数据权限
(3, '入库专员', 'inbound_operator', 3, '3', 1, 1, '0', '0', 'system', NOW(), '负责入库收货、上架'),

-- 出库专员：本部门数据权限
(4, '出库专员', 'outbound_operator', 4, '3', 1, 1, '0', '0', 'system', NOW(), '负责拣货、打包、发货'),

-- 库存管理员：本部门数据权限
(5, '库存管理员', 'inventory_manager', 5, '3', 1, 1, '0', '0', 'system', NOW(), '负责库存盘点、调整、移库'),

-- 质检员：本部门数据权限
(6, '质检员', 'qc_inspector', 6, '3', 1, 1, '0', '0', 'system', NOW(), '负责来料检验、出库抽检'),

-- 订单管理员：自定义数据权限（需要通过角色部门关联指定部门）
(7, '订单管理员', 'order_manager', 7, '2', 1, 1, '0', '0', 'system', NOW(), '负责订单处理、跟踪'),

-- 财务专员：自定义数据权限
(8, '财务专员', 'finance_officer', 8, '2', 1, 1, '0', '0', 'system', NOW(), '负责成本核算、费用统计'),

-- 报表查看员：仅本人数据权限
(9, '报表查看员', 'report_viewer', 9, '5', 1, 1, '0', '0', 'system', NOW(), '仅可查看报表，无操作权限');

-- ========================================================
-- 3. 初始化菜单数据（基础菜单，不含按钮权限）
-- ========================================================

-- 注意：菜单数据较为复杂，建议通过界面配置或使用已有的初始化脚本
-- 这里仅插入基础目录结构

-- 插入基础菜单（如果菜单表为空）
-- 目录：首页
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (1, '首页', 0, 1, '/dashboard', 'Layout', '1', '0', 'M', '0', '0', NULL, 'dashboard', 'system', NOW());

-- 目录：系统管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (2, '系统管理', 0, 2, '/system', 'Layout', '1', '0', 'M', '0', '0', NULL, 'system', 'system', NOW());

-- 菜单：用户管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (3, '用户管理', 2, 1, '/user', '#/views/system/user/index.vue', '1', '0', 'C', '0', '0', 'system:user:list', 'user', 'system', NOW());

-- 菜单：角色管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (4, '角色管理', 2, 2, '/role', '#/views/system/role/index.vue', '1', '0', 'C', '0', '0', 'system:role:list', 'role', 'system', NOW());

-- 菜单：部门管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (5, '部门管理', 2, 3, '/dept', '#/views/system/dept/index.vue', '1', '0', 'C', '0', '0', 'system:dept:list', 'dept', 'system', NOW());

-- 菜单：菜单管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (6, '菜单管理', 2, 4, '/menu', '#/views/system/menu/index.vue', '1', '0', 'C', '0', '0', 'system:menu:list', 'menu', 'system', NOW());

-- 菜单：字典管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (7, '字典管理', 2, 5, '/dict', '#/views/system/dict/index.vue', '1', '0', 'C', '0', '0', 'system:dict:list', 'dict', 'system', NOW());

-- 目录：WMS业务
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (100, 'WMS业务', 0, 3, '/wms', 'Layout', '1', '0', 'M', '0', '0', NULL, 'warehouse', 'system', NOW());

-- 菜单：入库管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (101, '入库管理', 100, 1, '/inbound', '#', '1', '0', 'C', '0', '0', 'wms:inbound:list', 'inbound', 'system', NOW());

-- 菜单：出库管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (102, '出库管理', 100, 2, '/outbound', '#', '1', '0', 'C', '0', '0', 'wms:outbound:list', 'outbound', 'system', NOW());

-- 菜单：库存管理
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES (103, '库存管理', 100, 3, '/inventory', '#', '1', '0', 'C', '0', '0', 'wms:inventory:list', 'inventory', 'system', NOW());

-- ========================================================
-- 4. 初始化用户数据
-- ========================================================

-- 注意：密码需要使用系统加密方式，这里假设明文为 admin123
-- 实际使用时请替换为加密后的密码

-- 插入超级管理员（admin）
-- 密码：admin123（请根据实际加密方式替换）
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, is_deleted, login_ip, login_date, create_by, create_time, remark) 
VALUES (1, 1, 'admin', '系统管理员', '00', 'admin@wms.com', '13800138000', '0', '', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '0', '0', '127.0.0.1', NOW(), 'system', NOW(), '系统管理员');

-- 插入仓库经理
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, is_deleted, create_by, create_time, remark) 
VALUES (2, 2, 'warehouse', '仓库经理', '00', 'warehouse@wms.com', '13800138001', '0', '', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '0', '0', 'system', NOW(), '仓库经理');

-- 插入入库专员
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, is_deleted, create_by, create_time, remark) 
VALUES (3, 6, 'inbound', '入库专员', '00', 'inbound@wms.com', '13800138005', '0', '', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '0', '0', 'system', NOW(), '入库专员');

-- 插入出库专员
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, is_deleted, create_by, create_time, remark) 
VALUES (4, 7, 'outbound', '出库专员', '00', 'outbound@wms.com', '13800138006', '0', '', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '0', '0', 'system', NOW(), '出库专员');

-- 插入库存管理员
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, is_deleted, create_by, create_time, remark) 
VALUES (5, 8, 'inventory', '库存管理员', '00', 'inventory@wms.com', '13800138007', '0', '', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '0', '0', 'system', NOW(), '库存管理员');

-- ========================================================
-- 5. 初始化用户角色关联
-- ========================================================

-- admin -> 系统管理员
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- warehouse -> 仓库经理
INSERT INTO sys_user_role (user_id, role_id) VALUES (2, 2);

-- inbound -> 入库专员
INSERT INTO sys_user_role (user_id, role_id) VALUES (3, 3);

-- outbound -> 出库专员
INSERT INTO sys_user_role (user_id, role_id) VALUES (4, 4);

-- inventory -> 库存管理员
INSERT INTO sys_user_role (user_id, role_id) VALUES (5, 5);

-- ========================================================
-- 6. 初始化角色菜单关联（系统管理员拥有所有菜单权限）
-- ========================================================

-- 系统管理员 -> 所有菜单
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 4);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 5);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 6);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 7);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 101);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 102);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 103);

-- 仓库经理 -> WMS业务菜单
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (2, 1);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (2, 100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (2, 101);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (2, 102);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (2, 103);

-- 入库专员 -> 入库管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 1);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 101);

-- 出库专员 -> 出库管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (4, 1);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (4, 100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (4, 102);

-- 库存管理员 -> 库存管理
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (5, 1);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (5, 100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (5, 103);

-- ========================================================
-- 7. 初始化角色部门关联（用于自定义数据权限）
-- ========================================================

-- 订单管理员 -> 订单组、客服组
INSERT INTO sys_role_dept (role_id, dept_id) VALUES (7, 9);
INSERT INTO sys_role_dept (role_id, dept_id) VALUES (7, 10);

-- 财务专员 -> 总公司
INSERT INTO sys_role_dept (role_id, dept_id) VALUES (8, 1);

-- ========================================================
-- 初始化完成
-- ========================================================

-- 默认账号密码：
-- admin / admin123
-- warehouse / admin123
-- inbound / admin123
-- outbound / admin123
-- inventory / admin123
