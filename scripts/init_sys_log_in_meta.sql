-- ============================================
-- 动态表单模块 - 登录日志表元数据初始化
-- 更新日期: 2026-03-21
-- ============================================

-- 确保元数据表已存在
-- 请先执行 ddl_sys_table_meta.sql

-- ============================================
-- 1. 登录日志表元数据配置 (sys_log_in)
-- ============================================

-- 1.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, create_by, create_time) 
VALUES ('sys_log_in', '登录日志', 'sys', 'com.aboclonal.product.dao.gengrator.entity.LogIn', 'com.aboclonal.product.service.system.ILogInService', 'system:logininfor', 20, 0, 1, '系统登录日志管理', 'system', NOW())
ON DUPLICATE KEY UPDATE 
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    update_time = NOW();

-- 1.2 字段元数据
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, dict_type, is_show_in_list, is_show_in_form, is_searchable, is_sortable, is_required, width, sort_order, status, create_by, create_time) VALUES
('sys_log_in', 'logId', '日志ID', 'bigint', 'number', '', 0, 0, 0, 1, 0, NULL, 0, 1, 'system', NOW()),
('sys_log_in', 'userName', '用户名', 'string', 'input', '', 1, 0, 1, 1, 0, 120, 1, 1, 'system', NOW()),
('sys_log_in', 'ipAddr', 'IP地址', 'string', 'input', '', 1, 0, 1, 0, 0, 150, 2, 1, 'system', NOW()),
('sys_log_in', 'loginLocation', '登录地点', 'string', 'input', '', 1, 0, 0, 0, 0, 200, 3, 1, 'system', NOW()),
('sys_log_in', 'browser', '浏览器', 'string', 'input', '', 1, 0, 0, 0, 0, 100, 4, 1, 'system', NOW()),
('sys_log_in', 'os', '操作系统', 'string', 'input', '', 1, 0, 0, 0, 0, 100, 5, 1, 'system', NOW()),
('sys_log_in', 'status', '状态', 'string', 'select', 'sys_login_status', 1, 0, 1, 0, 0, 80, 6, 1, 'system', NOW()),
('sys_log_in', 'msg', '提示消息', 'string', 'input', '', 1, 0, 0, 0, 0, 200, 7, 1, 'system', NOW()),
('sys_log_in', 'loginTime', '登录时间', 'datetime', 'datetime', '', 1, 0, 1, 1, 0, 160, 8, 1, 'system', NOW())
ON DUPLICATE KEY UPDATE 
    title = VALUES(title),
    data_type = VALUES(data_type),
    form_type = VALUES(form_type),
    dict_type = VALUES(dict_type),
    is_show_in_list = VALUES(is_show_in_list),
    is_show_in_form = VALUES(is_show_in_form),
    is_searchable = VALUES(is_searchable),
    is_sortable = VALUES(is_sortable),
    is_required = VALUES(is_required),
    width = VALUES(width),
    sort_order = VALUES(sort_order),
    status = VALUES(status),
    update_time = NOW();

-- ============================================
-- 2. 为 sys_log_in 添加 id 列（兼容通用 CRUD）
-- ============================================
DELIMITER //

DROP TRIGGER IF EXISTS trg_sys_log_in_id_auto_sync//

CREATE TRIGGER trg_sys_log_in_id_auto_sync BEFORE INSERT ON sys_log_in
FOR EACH ROW
BEGIN
    SET NEW.id = NEW.log_id;
END//

DROP TRIGGER IF EXISTS trg_sys_log_in_id_update_sync//

CREATE TRIGGER trg_sys_log_in_id_update_sync BEFORE UPDATE ON sys_log_in
FOR EACH ROW
BEGIN
    SET NEW.id = NEW.log_id;
END//

DELIMITER ;

-- 添加 id 列（如果不存在）
SET @exist := (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_log_in' AND COLUMN_NAME = 'id');
SET @sqlstmt := IF(@exist > 0, 'SELECT ''id column already exists'' as msg', 
                   'ALTER TABLE sys_log_in ADD COLUMN id BIGINT DEFAULT NULL AFTER log_id');
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 同步现有数据的 id 值
UPDATE sys_log_in SET id = log_id WHERE id IS NULL OR id != log_id;

-- ============================================
-- 初始化完成
-- ============================================
