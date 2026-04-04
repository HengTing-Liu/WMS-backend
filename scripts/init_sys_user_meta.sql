-- ============================================
-- 动态表单模块 - 用户管理表元数据初始化
-- 更新日期: 2026-03-20
-- ============================================

-- 确保元数据表已存在
-- 请先执行 ddl_sys_table_meta.sql

-- ============================================
-- 1. 用户表元数据配置 (sys_user)
-- ============================================

-- 1.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, create_by, create_time) 
VALUES ('sys_user', '用户管理', 'sys', 'com.abtk.product.dao.gengrator.entity.SysUser', 'com.abtk.product.service.sys.service.SysUserService', 'system:user', 20, 0, 1, '系统用户信息管理', 'system', NOW())
ON DUPLICATE KEY UPDATE 
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    update_time = NOW();

-- 1.2 字段元数据
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, dict_type, is_show_in_list, is_show_in_form, is_searchable, is_sortable, is_required, width, sort_order, status, create_by, create_time) VALUES
-- 主键
('sys_user', 'userId', '用户ID', 'int', 'number', '', 0, 0, 0, 1, 0, NULL, 0, 1, 'system', NOW()),
-- 部门ID（外键）
('sys_user', 'deptId', '所属部门', 'int', 'select', '', 1, 1, 1, 0, 0, 120, 1, 1, 'system', NOW()),
-- 部门名称（只读显示，通过dept_id关联查询）
('sys_user', 'deptName', '部门名称', 'string', 'input', '', 1, 0, 0, 0, 0, 150, 2, 1, 'system', NOW()),
-- 用户账号
('sys_user', 'userName', '用户账号', 'string', 'input', '', 1, 1, 1, 1, 1, 100, 3, 1, 'system', NOW()),
-- 用户昵称
('sys_user', 'nickName', '用户昵称', 'string', 'input', '', 1, 1, 1, 0, 1, 100, 4, 1, 'system', NOW()),
-- 用户类型
('sys_user', 'userType', '用户类型', 'string', 'select', 'sys_user_type', 0, 1, 0, 0, 0, 100, 5, 1, 'system', NOW()),
-- 用户邮箱
('sys_user', 'email', '用户邮箱', 'string', 'input', '', 1, 1, 1, 0, 0, 150, 6, 1, 'system', NOW()),
-- 手机号码
('sys_user', 'phonenumber', '手机号码', 'string', 'input', '', 1, 1, 1, 0, 0, 120, 7, 1, 'system', NOW()),
-- 用户性别
('sys_user', 'sex', '用户性别', 'int', 'select', 'sys_user_sex', 1, 1, 1, 0, 0, 80, 8, 1, 'system', NOW()),
-- 头像地址
('sys_user', 'avatar', '头像', 'string', 'input', '', 0, 1, 0, 0, 0, 200, 9, 1, 'system', NOW()),
-- 密码（不在表单中显示）
('sys_user', 'password', '密码', 'string', 'password', '', 0, 0, 0, 0, 0, NULL, 10, 0, 'system', NOW()),
-- 账号状态
('sys_user', 'status', '状态', 'string', 'select', 'sys_normal_disable', 1, 1, 1, 0, 1, 80, 11, 1, 'system', NOW()),
-- 删除标志（不在列表/表单显示）
('sys_user', 'delFlag', '删除标志', 'string', 'number', '', 0, 0, 0, 0, 0, NULL, 12, 0, 'system', NOW()),
-- 最后登录IP
('sys_user', 'loginIp', '最后登录IP', 'string', 'input', '', 0, 0, 0, 0, 0, 150, 13, 1, 'system', NOW()),
-- 最后登录时间
('sys_user', 'loginDate', '最后登录时间', 'datetime', 'datetime', '', 0, 0, 0, 0, 0, 160, 14, 1, 'system', NOW()),
-- 密码更新时间
('sys_user', 'pwdUpdateDate', '密码更新时间', 'datetime', 'datetime', '', 0, 0, 0, 0, 0, 160, 15, 1, 'system', NOW()),
-- 创建者
('sys_user', 'createBy', '创建者', 'string', 'input', '', 0, 0, 0, 0, 0, 100, 16, 1, 'system', NOW()),
-- 创建时间
('sys_user', 'createTime', '创建时间', 'datetime', 'datetime', '', 1, 0, 1, 1, 0, 160, 17, 1, 'system', NOW()),
-- 更新者
('sys_user', 'updateBy', '更新者', 'string', 'input', '', 0, 0, 0, 0, 0, 100, 18, 1, 'system', NOW()),
-- 更新时间
('sys_user', 'updateTime', '更新时间', 'datetime', 'datetime', '', 0, 0, 0, 1, 0, 160, 19, 1, 'system', NOW()),
-- 备注
('sys_user', 'remark', '备注', 'string', 'textarea', '', 0, 1, 0, 0, 0, NULL, 20, 1, 'system', NOW())
ON DUPLICATE KEY UPDATE 
    title = VALUES(title),
    data_type = VALUES(data_type),
    form_type = VALUES(form_type),
    dict_type = VALUES(dict_type),
    is_show_in_list = VALUES(is_show_in_list),
    is_show_in_form = VALUES(is_show_in_form),
    is_searchable = VALUES(is_searchable),
    is_required = VALUES(is_required),
    width = VALUES(width),
    sort_order = VALUES(sort_order),
    status = VALUES(status),
    update_time = NOW();

-- 1.3 操作按钮配置
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, button_type, icon, permission, position, sort_order, status, create_by, create_time) VALUES
-- 工具栏按钮
('sys_user', 'add', '新增', 'button', 'primary', 'plus', 'system:user:add', 'toolbar', 1, 1, 'system', NOW()),
('sys_user', 'edit', '编辑', 'button', 'default', 'edit', 'system:user:edit', 'toolbar', 2, 1, 'system', NOW()),
('sys_user', 'delete', '删除', 'button', 'danger', 'delete', 'system:user:delete', 'toolbar', 3, 1, 'system', NOW()),
('sys_user', 'export', '导出', 'button', 'default', 'download', 'system:user:export', 'toolbar', 4, 1, 'system', NOW()),
('sys_user', 'resetPwd', '重置密码', 'button', 'default', 'key', 'system:user:resetPwd', 'toolbar', 5, 1, 'system', NOW()),
-- 行内按钮
('sys_user', 'row_edit', '编辑', 'link', 'link', 'edit', 'system:user:edit', 'row', 1, 1, 'system', NOW()),
('sys_user', 'row_delete', '删除', 'link', 'link', 'delete', 'system:user:delete', 'row', 2, 1, 'system', NOW()),
('sys_user', 'row_reset_pwd', '重置密码', 'link', 'link', 'key', 'system:user:resetPwd', 'row', 3, 1, 'system', NOW())
ON DUPLICATE KEY UPDATE 
    operation_name = VALUES(operation_name),
    button_type = VALUES(button_type),
    icon = VALUES(icon),
    permission = VALUES(permission),
    position = VALUES(position),
    sort_order = VALUES(sort_order),
    update_time = NOW();

-- ============================================
-- 2. 为 sys_user 添加 id 列（兼容通用 CRUD）
-- id 列值始终等于 user_id，通过触发器自动同步
-- ============================================
DELIMITER //

-- 删除旧触发器（如果存在）
DROP TRIGGER IF EXISTS trg_sys_user_id_auto_sync//

-- 创建触发器：在 INSERT 时自动设置 id = user_id
CREATE TRIGGER trg_sys_user_id_auto_sync BEFORE INSERT ON sys_user
FOR EACH ROW
BEGIN
    SET NEW.id = NEW.user_id;
END//

-- 删除旧更新触发器（如果存在）
DROP TRIGGER IF EXISTS trg_sys_user_id_update_sync//

-- 创建触发器：在 UPDATE 时自动同步 id = user_id
CREATE TRIGGER trg_sys_user_id_update_sync BEFORE UPDATE ON sys_user
FOR EACH ROW
BEGIN
    SET NEW.id = NEW.user_id;
END//

DELIMITER ;

-- 添加 id 列（如果不存在）
SET @exist := (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_user' AND COLUMN_NAME = 'id');
SET @sqlstmt := IF(@exist > 0, 'SELECT ''id column already exists'' as msg', 
                   'ALTER TABLE sys_user ADD COLUMN id BIGINT DEFAULT NULL AFTER user_id');
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 同步现有数据的 id 值
UPDATE sys_user SET id = user_id WHERE id IS NULL OR id != user_id;

-- ============================================
-- 初始化完成
-- ============================================
