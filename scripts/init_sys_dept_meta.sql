-- ============================================
-- 动态表单模块 - 部门表元数据初始化
-- 日期: 2026-04-25
-- 表: sys_dept（部门表）
-- ============================================

-- ============================================
-- 1. 表级元数据 (sys_table_meta)
-- ============================================
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, create_by, create_time)
VALUES ('sys_dept', '部门管理', 'sys', 'com.abtk.product.dao.entity.SysDept', 'com.abtk.product.service.sys.service.SysDeptService', 'system:dept', 20, 1, 1, '系统部门信息管理', 'system', NOW())
ON DUPLICATE KEY UPDATE
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    page_size = VALUES(page_size),
    is_tree = VALUES(is_tree),
    remark = VALUES(remark),
    update_time = NOW();

-- ============================================
-- 2. 字段元数据 (sys_column_meta)
-- 关键说明：
--   - field: 字段标识（驼峰，与后端 API 返回的 JSON 字段名一致）
--   - column_name: 数据库列名（蛇形，用于 SQL 查询）
--   - show_in_list = 1: 在表格列表中显示
--   - show_in_form = 1: 在表单中显示
--   - searchable = 1: 可作为搜索条件
--   - is_sortable = 1: 列可排序
--   - required = 1: 表单必填
-- ============================================
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, dict_type, show_in_list, show_in_form, searchable, is_sortable, required, width, sort_order, status, create_by, create_time, column_name)
VALUES
-- -------- 主键（隐藏）--------
-- 注意：sys_dept 表主键是 dept_id，无 id 列，故不使用通用 id 映射
('sys_dept', 'deptId', '部门ID', 'bigint', 'number', '', 0, 0, 0, 1, 0, NULL, 0, 0, 'system', NOW(), 'dept_id'),

-- -------- 树形关键字段（隐藏，但表单需要）--------
-- 父部门ID：树形关联字段，表单中用于选择上级部门
('sys_dept', 'parentId', '上级部门', 'bigint', 'treeSelect', '', 0, 1, 1, 0, 0, NULL, 1, 1, 'system', NOW(), 'parent_id'),
-- 祖级列表（系统内部维护）
('sys_dept', 'ancestors', '祖级列表', 'string', 'input', '', 0, 0, 0, 0, 0, NULL, 2, 0, 'system', NOW(), 'ancestors'),

-- -------- 部门核心字段 --------
-- 部门编码（可搜索）
('sys_dept', 'deptCode', '部门编码', 'string', 'input', '', 1, 1, 1, 1, 0, 120, 3, 1, 'system', NOW(), 'dept_code'),
-- 部门名称（列表重点展示，核心字段）
('sys_dept', 'deptName', '部门名称', 'string', 'input', '', 1, 1, 1, 1, 1, 200, 4, 1, 'system', NOW(), 'dept_name'),
-- 部门全路径（树形展示用，只读）
('sys_dept', 'deptFullPath', '部门全路径', 'string', 'input', '', 1, 0, 0, 0, 0, 280, 5, 1, 'system', NOW(), 'dept_full_path'),
-- 部门分类（如：总经办、销售部等）
('sys_dept', 'deptCategory', '部门分类', 'string', 'input', '', 1, 1, 1, 0, 0, 140, 6, 1, 'system', NOW(), 'dept_category'),
-- 显示顺序
('sys_dept', 'orderNum', '排序', 'int', 'inputNumber', '', 1, 1, 0, 1, 1, 80, 7, 1, 'system', NOW(), 'order_num'),
-- 负责人
('sys_dept', 'leader', '负责人', 'string', 'input', '', 1, 1, 1, 0, 0, 100, 8, 1, 'system', NOW(), 'leader'),
-- 联系电话
('sys_dept', 'phone', '联系电话', 'string', 'input', '', 1, 1, 0, 0, 0, 120, 9, 1, 'system', NOW(), 'phone'),
-- 邮箱
('sys_dept', 'email', '邮箱', 'string', 'input', '', 1, 1, 0, 0, 0, 160, 10, 1, 'system', NOW(), 'email'),
-- 部门状态（0=正常, 1=停用）
('sys_dept', 'status', '状态', 'string', 'select', 'sys_normal_disable', 1, 1, 1, 0, 1, 80, 11, 1, 'system', NOW(), 'status'),

-- -------- 附加信息（列表隐藏）--------
-- 删除标志（0=存在, 2=删除）
('sys_dept', 'delFlag', '删除标志', 'string', 'number', '', 0, 0, 0, 0, 0, NULL, 12, 0, 'system', NOW(), 'del_flag'),
-- 父部门名称（只读显示）
('sys_dept', 'parentName', '上级部门名称', 'string', 'input', '', 0, 0, 0, 0, 0, NULL, 13, 0, 'system', NOW(), 'parent_name'),
-- 用户数量（附加统计信息）
('sys_dept', 'userCount', '用户数', 'int', 'inputNumber', '', 1, 0, 0, 0, 0, 80, 14, 1, 'system', NOW(), 'user_count'),

-- -------- 系统字段（隐藏）--------
-- 创建者
('sys_dept', 'createBy', '创建者', 'string', 'input', '', 0, 0, 0, 0, 0, 100, 15, 1, 'system', NOW(), 'create_by'),
-- 创建时间
('sys_dept', 'createTime', '创建时间', 'datetime', 'datetime', '', 1, 0, 0, 1, 0, 170, 16, 1, 'system', NOW(), 'create_time'),
-- 更新者
('sys_dept', 'updateBy', '更新者', 'string', 'input', '', 0, 0, 0, 0, 0, 100, 17, 1, 'system', NOW(), 'update_by'),
-- 更新时间
('sys_dept', 'updateTime', '更新时间', 'datetime', 'datetime', '', 0, 0, 0, 1, 0, 170, 18, 1, 'system', NOW(), 'update_time'),
-- 备注
('sys_dept', 'remarks', '备注', 'string', 'textarea', '', 0, 1, 0, 0, 0, NULL, 19, 1, 'system', NOW(), 'remarks')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    data_type = VALUES(data_type),
    form_type = VALUES(form_type),
    dict_type = VALUES(dict_type),
    show_in_list = VALUES(show_in_list),
    show_in_form = VALUES(show_in_form),
    searchable = VALUES(searchable),
    is_sortable = VALUES(is_sortable),
    required = VALUES(required),
    width = VALUES(width),
    sort_order = VALUES(sort_order),
    column_name = VALUES(column_name),
    status = VALUES(status),
    update_time = NOW();

-- ============================================
-- 3. 操作按钮配置 (sys_table_operation)
-- ============================================
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, is_enabled, event_type, event_config)
VALUES
-- 工具栏按钮
('sys_dept', 'create', '新增', 'button', 'PlusOutlined', 'system:dept:add', 'toolbar', 1, 1, 1, 'builtin', '{"handler":"create"}'),
('sys_dept', 'export', '导出', 'button', 'DownloadOutlined', 'system:dept:export', 'toolbar', 2, 1, 1, 'builtin', '{"handler":"export","payloadType":"filtered"}'),
-- 行内按钮
('sys_dept', 'row_edit', '编辑', 'link', 'EditOutlined', 'system:dept:edit', 'row', 1, 1, 1, 'builtin', '{"handler":"edit"}'),
('sys_dept', 'row_delete', '删除', 'link', 'DeleteOutlined', 'system:dept:delete', 'row', 2, 1, 1, 'builtin', '{"handler":"delete"}'),
('sys_dept', 'toggle', '启用/停用', 'link', 'CheckCircleOutlined', 'system:dept:edit', 'row', 3, 1, 1, 'builtin', '{"handler":"toggle"}'),
-- 树形特有：行内新增子部门
('sys_dept', 'addChild', '新增子部门', 'link', 'PlusCircleOutlined', 'system:dept:add', 'row', 4, 1, 1, 'builtin', '{"handler":"addChild"}')
ON DUPLICATE KEY UPDATE
    operation_name = VALUES(operation_name),
    operation_type = VALUES(operation_type),
    icon = VALUES(icon),
    permission = VALUES(permission),
    position = VALUES(position),
    sort_order = VALUES(sort_order),
    event_type = VALUES(event_type),
    event_config = VALUES(event_config),
    update_time = NOW();

-- ============================================
-- 4. 字段分组配置 (sys_form_group) - 可选
-- 如系统支持分组功能，在此定义
-- ============================================
-- 目前 sys_dept 不需要复杂分组，保持默认即可

-- ============================================
-- 初始化完成
-- ============================================
-- 执行此脚本后，刷新部门管理页面 http://localhost:5666/sys/system/dept
-- 即可看到基于字段元数据配置的表格列
