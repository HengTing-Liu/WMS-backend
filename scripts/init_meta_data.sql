-- ============================================
-- 动态表单模块 - 仓库管理表元数据初始化
-- 更新日期: 2026-03-20
-- ============================================

-- 确保元数据表已存在
-- 请先执行 ddl_sys_table_meta.sql

-- ============================================
-- 1. 仓库表元数据配置 (sys_warehouse)
-- ============================================

-- 1.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, is_deleted_column, create_by, create_time) 
VALUES ('sys_warehouse', '仓库档案', 'base', 'com.abtk.product.dao.gengrator.entity.Warehouse', 'com.abtk.product.service.sys.service.WarehouseService', 'base:warehouse', 20, 0, 1, '仓库基础信息管理', 'is_deleted', 'system', NOW())
ON DUPLICATE KEY UPDATE 
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    is_deleted_column = VALUES(is_deleted_column),
    update_time = NOW();

-- 1.2 字段元数据
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, dict_type, is_show_in_list, is_show_in_form, is_searchable, is_sortable, is_required, width, sort_order, status, create_by, create_time) VALUES
-- 主键
('sys_warehouse', 'id', '主键ID', 'int', 'number', NULL, 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW()),
-- 仓库编码
('sys_warehouse', 'warehouseCode', '仓库编码', 'string', 'input', NULL, 1, 1, 1, 1, 1, 120, 1, 1, 'system', NOW()),
-- 仓库名称
('sys_warehouse', 'warehouseName', '仓库名称', 'string', 'input', NULL, 1, 1, 1, 1, 1, 150, 2, 1, 'system', NOW()),
-- 温度分区
('sys_warehouse', 'temperatureZone', '温度分区', 'string', 'select', NULL, 1, 1, 1, 0, 0, 100, 3, 1, 'system', NOW()),
-- 质量分区
('sys_warehouse', 'qualityZone', '质量分区', 'string', 'select', NULL, 1, 1, 1, 0, 0, 100, 4, 1, 'system', NOW()),
-- 责任人工号
('sys_warehouse', 'employeeCode', '责任人工号', 'string', 'input', NULL, 0, 1, 0, 0, 0, 120, 5, 1, 'system', NOW()),
-- 责任人
('sys_warehouse', 'employeeName', '责任人', 'string', 'input', NULL, 1, 1, 1, 0, 0, 100, 6, 1, 'system', NOW()),
-- 责任部门编号
('sys_warehouse', 'deptCode', '责任部门编号', 'string', 'input', NULL, 0, 1, 0, 0, 0, 120, 7, 1, 'system', NOW()),
-- 责任部门全路径
('sys_warehouse', 'deptNameFullPath', '责任部门', 'string', 'input', NULL, 1, 1, 0, 0, 0, 200, 8, 1, 'system', NOW()),
-- 所属公司（关联已有字典 sys_company）
('sys_warehouse', 'company', '所属公司', 'string', 'select', 'sys_company', 1, 1, 1, 0, 0, 150, 9, 1, 'system', NOW()),
-- 是否启用
('sys_warehouse', 'isEnabled', '状态', 'int', 'switch', NULL, 1, 1, 1, 0, 1, 80, 10, 1, 'system', NOW()),
-- 备注
('sys_warehouse', 'remark', '备注', 'string', 'textarea', NULL, 0, 1, 0, 0, 0, NULL, 11, 1, 'system', NOW()),
-- 创建时间
('sys_warehouse', 'createTime', '创建时间', 'datetime', 'datetime', NULL, 1, 0, 1, 1, 0, 160, 12, 1, 'system', NOW()),
-- 创建人
('sys_warehouse', 'createBy', '创建人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 13, 1, 'system', NOW()),
-- 更新时间
('sys_warehouse', 'updateTime', '更新时间', 'datetime', 'datetime', NULL, 0, 0, 0, 1, 0, 160, 14, 1, 'system', NOW()),
-- 更新人
('sys_warehouse', 'updateBy', '更新人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 15, 1, 'system', NOW()),
-- 逻辑删除
('sys_warehouse', 'is_deleted', '是否删除', 'int', 'number', NULL, 0, 0, 0, 0, 0, NULL, 16, 0, 'system', NOW())
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
    update_time = NOW();

-- 1.3 操作按钮配置
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, button_type, icon, permission, position, sort_order, status, create_by, create_time) VALUES
-- 工具栏按钮
('sys_warehouse', 'add', '新增', 'button', 'primary', 'plus', 'base:warehouse:add', 'toolbar', 1, 1, 'system', NOW()),
('sys_warehouse', 'edit', '编辑', 'button', 'default', 'edit', 'base:warehouse:edit', 'toolbar', 2, 1, 'system', NOW()),
('sys_warehouse', 'delete', '删除', 'button', 'danger', 'delete', 'base:warehouse:delete', 'toolbar', 3, 1, 'system', NOW()),
('sys_warehouse', 'export', '导出', 'button', 'default', 'download', 'base:warehouse:export', 'toolbar', 4, 1, 'system', NOW()),
-- 行内按钮
('sys_warehouse', 'row_edit', '编辑', 'link', 'link', 'edit', 'base:warehouse:edit', 'row', 1, 1, 'system', NOW()),
('sys_warehouse', 'row_delete', '删除', 'link', 'link', 'delete', 'base:warehouse:delete', 'row', 2, 1, 'system', NOW())
ON DUPLICATE KEY UPDATE 
    operation_name = VALUES(operation_name),
    button_type = VALUES(button_type),
    icon = VALUES(icon),
    permission = VALUES(permission),
    position = VALUES(position),
    sort_order = VALUES(sort_order),
    update_time = NOW();


-- ============================================
-- 2. 仓库收货信息表元数据配置 (sys_warehouse_receive)
-- ============================================

-- 2.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, is_deleted_column, create_by, create_time) 
VALUES ('sys_warehouse_receiver', '仓库收货信息', 'base', 'com.abtk.product.dao.gengrator.entity.WarehouseReceiver', 'com.abtk.product.service.sys.service.WarehouseReceiverService', 'base:warehouseReceiver', 20, 0, 1, '仓库收货地址信息管理', 'is_deleted', 'system', NOW())
ON DUPLICATE KEY UPDATE 
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    is_deleted_column = VALUES(is_deleted_column),
    update_time = NOW();

-- 2.2 字段元数据
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, dict_type, is_show_in_list, is_show_in_form, is_searchable, is_sortable, is_required, width, sort_order, status, create_by, create_time) VALUES
-- 主键
('sys_warehouse_receiver', 'id', '主键ID', 'int', 'number', NULL, 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW()),
-- 仓库编码
('sys_warehouse_receiver', 'warehouseCode', '仓库编码', 'string', 'select', NULL, 1, 1, 1, 0, 1, 120, 1, 1, 'system', NOW()),
-- 收货人
('sys_warehouse_receiver', 'consignee', '收货人', 'string', 'input', NULL, 1, 1, 1, 0, 1, 100, 2, 1, 'system', NOW()),
-- 手机号
('sys_warehouse_receiver', 'phoneNumber', '手机号码', 'string', 'input', NULL, 1, 1, 1, 0, 1, 120, 3, 1, 'system', NOW()),
-- 国家
('sys_warehouse_receiver', 'country', '国家', 'string', 'select', NULL, 0, 1, 0, 0, 0, 100, 4, 1, 'system', NOW()),
-- 省份
('sys_warehouse_receiver', 'province', '省份', 'string', 'input', NULL, 1, 1, 0, 0, 0, 100, 5, 1, 'system', NOW()),
-- 城市
('sys_warehouse_receiver', 'city', '城市', 'string', 'input', NULL, 1, 1, 0, 0, 0, 100, 6, 1, 'system', NOW()),
-- 区县
('sys_warehouse_receiver', 'district', '区县', 'string', 'input', NULL, 0, 1, 0, 0, 0, 100, 7, 1, 'system', NOW()),
-- 详细地址
('sys_warehouse_receiver', 'detailedAddress', '详细地址', 'string', 'textarea', NULL, 1, 1, 0, 0, 1, 250, 8, 1, 'system', NOW()),
-- 邮政编码
('sys_warehouse_receiver', 'postalCode', '邮政编码', 'string', 'input', NULL, 0, 1, 0, 0, 0, 100, 9, 1, 'system', NOW()),
-- 是否默认
('sys_warehouse_receiver', 'isDefault', '默认地址', 'int', 'switch', NULL, 1, 1, 1, 0, 0, 100, 10, 1, 'system', NOW()),
-- 备注
('sys_warehouse_receiver', 'remark', '备注', 'string', 'textarea', NULL, 0, 1, 0, 0, 0, NULL, 11, 1, 'system', NOW()),
-- 创建时间
('sys_warehouse_receiver', 'createTime', '创建时间', 'datetime', 'datetime', NULL, 1, 0, 1, 1, 0, 160, 12, 1, 'system', NOW()),
-- 创建人
('sys_warehouse_receiver', 'createBy', '创建人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 13, 1, 'system', NOW()),
-- 更新时间
('sys_warehouse_receiver', 'updateTime', '更新时间', 'datetime', 'datetime', NULL, 0, 0, 0, 1, 0, 160, 14, 1, 'system', NOW()),
-- 更新人
('sys_warehouse_receiver', 'updateBy', '更新人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 15, 1, 'system', NOW()),
-- 逻辑删除
('sys_warehouse_receiver', 'isDeleted', '是否删除', 'int', 'number', NULL, 0, 0, 0, 0, 0, NULL, 16, 0, 'system', NOW())
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
    update_time = NOW();

-- 2.3 操作按钮配置
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, button_type, icon, permission, position, sort_order, status, create_by, create_time) VALUES
-- 工具栏按钮
('sys_warehouse_receiver', 'add', '新增', 'button', 'primary', 'plus', 'base:warehouseReceiver:add', 'toolbar', 1, 1, 'system', NOW()),
('sys_warehouse_receiver', 'edit', '编辑', 'button', 'default', 'edit', 'base:warehouseReceiver:edit', 'toolbar', 2, 1, 'system', NOW()),
('sys_warehouse_receiver', 'delete', '删除', 'button', 'danger', 'delete', 'base:warehouseReceiver:delete', 'toolbar', 3, 1, 'system', NOW()),
-- 行内按钮
('sys_warehouse_receiver', 'row_edit', '编辑', 'link', 'link', 'edit', 'base:warehouseReceiver:edit', 'row', 1, 1, 'system', NOW()),
('sys_warehouse_receiver', 'row_delete', '删除', 'link', 'link', 'delete', 'base:warehouseReceiver:delete', 'row', 2, 1, 'system', NOW()),
('sys_warehouse_receiver', 'row_set_default', '设为默认', 'link', 'link', 'check-circle', 'base:warehouseReceiver:edit', 'row', 3, 1, 'system', NOW())
ON DUPLICATE KEY UPDATE 
    operation_name = VALUES(operation_name),
    button_type = VALUES(button_type),
    icon = VALUES(icon),
    permission = VALUES(permission),
    position = VALUES(position),
    sort_order = VALUES(sort_order),
    update_time = NOW();

-- ============================================
-- 3. 用户表元数据配置 (sys_user)
-- 说明: sys_user 使用 del_flag 而非 isdeleted，需在元数据中声明
-- ============================================

-- 3.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, is_deleted_column, create_by, create_time)
VALUES ('sys_user', '用户表', 'sys', 'com.abtk.product.dao.gengrator.entity.SysUser', 'com.abtk.product.service.system.ISysUserService', 'system:user', 20, 0, 1, '系统用户表', 'del_flag', 'system', NOW())
ON DUPLICATE KEY UPDATE
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    is_deleted_column = VALUES(is_deleted_column),
    update_time = NOW();

-- 3.2 字段元数据（核心字段）
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, dict_type, is_show_in_list, is_show_in_form, is_searchable, is_sortable, is_required, width, sort_order, status, create_by, create_time) VALUES
('sys_user', 'userId', '用户ID', 'bigint', 'number', NULL, 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW()),
('sys_user', 'deptId', '部门ID', 'bigint', 'number', NULL, 0, 0, 0, 0, 0, NULL, 1, 1, 'system', NOW()),
('sys_user', 'userName', '用户名', 'string', 'input', NULL, 1, 1, 1, 1, 1, 120, 2, 1, 'system', NOW()),
('sys_user', 'nickName', '昵称', 'string', 'input', NULL, 1, 1, 1, 0, 1, 120, 3, 1, 'system', NOW()),
('sys_user', 'email', '邮箱', 'string', 'input', NULL, 1, 1, 1, 0, 0, 150, 4, 1, 'system', NOW()),
('sys_user', 'phonenumber', '手机号', 'string', 'input', NULL, 1, 1, 1, 0, 0, 120, 5, 1, 'system', NOW()),
('sys_user', 'sex', '性别', 'int', 'select', 'sys_user_sex', 1, 1, 0, 0, 0, 80, 6, 1, 'system', NOW()),
('sys_user', 'avatar', '头像', 'string', 'input', NULL, 0, 0, 0, 0, 0, NULL, 7, 1, 'system', NOW()),
('sys_user', 'status', '状态', 'int', 'switch', NULL, 1, 1, 1, 0, 1, 80, 8, 1, 'system', NOW()),
('sys_user', 'delFlag', '删除标志', 'string', 'input', NULL, 0, 0, 0, 0, 0, NULL, 9, 1, 'system', NOW()),
('sys_user', 'loginIp', '最后登录IP', 'string', 'input', NULL, 1, 0, 0, 0, 0, 150, 10, 1, 'system', NOW()),
('sys_user', 'loginDate', '最后登录时间', 'datetime', 'datetime', NULL, 1, 0, 0, 1, 0, 160, 11, 1, 'system', NOW()),
('sys_user', 'createBy', '创建者', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 12, 1, 'system', NOW()),
('sys_user', 'createTime', '创建时间', 'datetime', 'datetime', NULL, 1, 0, 0, 1, 0, 160, 13, 1, 'system', NOW()),
('sys_user', 'updateBy', '更新者', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 14, 1, 'system', NOW()),
('sys_user', 'updateTime', '更新时间', 'datetime', 'datetime', NULL, 0, 0, 0, 0, 0, 160, 15, 1, 'system', NOW()),
('sys_user', 'remark', '备注', 'string', 'textarea', NULL, 0, 1, 0, 0, 0, NULL, 16, 1, 'system', NOW())
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
    update_time = NOW();

-- ============================================
-- 初始化完成
-- ============================================
