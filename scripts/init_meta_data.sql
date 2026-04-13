-- ============================================
-- 动态表单模块 - 基础表元数据初始化
-- 更新日期: 2026-04-13
-- sys_column_meta 实际列名：
--   show_in_list, show_in_form, searchable（无 is_ 前缀）
--   is_sortable（有 is_ 前缀）
--   required（无 is_ 前缀）
--   column_name（存在）
-- sys_table_operation 实际列（无 button_type）：
--   id, table_code, operation_code, operation_name, operation_type,
--   icon, permission, position, sort_order, status,
--   event_type, event_config, confirm_message, is_enabled
-- ============================================

-- ============================================
-- 1. 仓库表元数据配置 (sys_warehouse)
-- ============================================

-- 1.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, create_by, create_time)
VALUES ('sys_warehouse', '仓库档案', 'sys', 'com.abtk.product.dao.entity.SysWarehouse', 'com.abtk.product.service.sys.service.WarehouseService', 'sys:warehouse', 20, 0, 1, '仓库基础信息管理', 'system', NOW())
ON DUPLICATE KEY UPDATE
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    page_size = VALUES(page_size),
    remark = VALUES(remark),
    update_time = NOW();

-- 1.2 字段元数据（sys_warehouse 实际列名）
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, show_in_list, show_in_form, searchable, is_sortable, required, width, sort_order, status, create_by, created_at, column_name)
VALUES
-- 主键（隐藏）
('sys_warehouse', 'id', '主键ID', 'bigint', 'number', 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW(), 'id'),
-- 仓库编码（必填）
('sys_warehouse', 'warehouseCode', '仓库编码', 'string', 'input', 1, 1, 1, 1, 1, 120, 1, 1, 'system', NOW(), 'warehouse_code'),
-- 仓库名称
('sys_warehouse', 'warehouseName', '仓库名称', 'string', 'input', 1, 1, 1, 1, 1, 150, 2, 1, 'system', NOW(), 'warehouse_name'),
-- 仓库类型
('sys_warehouse', 'warehouseType', '仓库类型', 'string', 'select', 1, 1, 1, 0, 0, 100, 3, 1, 'system', NOW(), 'warehouse_type'),
-- 温度分区
('sys_warehouse', 'temperatureZone', '温度分区', 'string', 'select', 1, 1, 1, 0, 0, 100, 4, 1, 'system', NOW(), 'temperature_zone'),
-- 质量分区
('sys_warehouse', 'qualityZone', '质量分区', 'string', 'select', 1, 1, 1, 0, 0, 100, 5, 1, 'system', NOW(), 'quality_zone'),
-- 责任人工号
('sys_warehouse', 'employeeCode', '责任人工号', 'string', 'input', 0, 1, 0, 0, 0, 120, 6, 1, 'system', NOW(), 'employee_code'),
-- 责任人
('sys_warehouse', 'employeeName', '责任人', 'string', 'input', 1, 1, 1, 0, 0, 100, 7, 1, 'system', NOW(), 'employee_name'),
-- 责任部门编号
('sys_warehouse', 'deptCode', '责任部门编号', 'string', 'input', 0, 1, 0, 0, 0, 120, 8, 1, 'system', NOW(), 'dept_code'),
-- 责任部门全路径
('sys_warehouse', 'deptNameFullPath', '责任部门', 'string', 'input', 1, 1, 0, 0, 0, 200, 9, 1, 'system', NOW(), 'dept_name_full_path'),
-- ERP公司编码
('sys_warehouse', 'erpCompanyCode', 'ERP公司编码', 'string', 'input', 0, 1, 1, 0, 0, 120, 10, 1, 'system', NOW(), 'erp_company_code'),
-- ERP公司名称
('sys_warehouse', 'erpCompanyName', 'ERP公司名称', 'string', 'input', 0, 1, 1, 0, 0, 150, 11, 1, 'system', NOW(), 'erp_company_name'),
-- ERP仓库编码
('sys_warehouse', 'erpWarehouseCode', 'ERP仓库编码', 'string', 'input', 0, 1, 1, 0, 0, 120, 12, 1, 'system', NOW(), 'erp_warehouse_code'),
-- ERP库区编码
('sys_warehouse', 'erpLocationCode', 'ERP库区编码', 'string', 'input', 0, 1, 1, 0, 0, 120, 13, 1, 'system', NOW(), 'erp_location_code'),
-- 是否启用
('sys_warehouse', 'isEnabled', '状态', 'int', 'switch', 1, 1, 1, 0, 1, 80, 14, 1, 'system', NOW(), 'is_enabled'),
-- 备注
('sys_warehouse', 'remark', '备注', 'string', 'textarea', 0, 1, 0, 0, 0, NULL, 15, 1, 'system', NOW(), 'remark'),
-- 创建时间
('sys_warehouse', 'createTime', '创建时间', 'datetime', 'datetime', 1, 0, 0, 1, 0, 170, 16, 1, 'system', NOW(), 'create_time'),
-- 创建人（隐藏）
('sys_warehouse', 'createBy', '创建人', 'string', 'input', 0, 0, 0, 0, 0, 100, 17, 1, 'system', NOW(), 'create_by'),
-- 更新时间（隐藏）
('sys_warehouse', 'updateTime', '更新时间', 'datetime', 'datetime', 0, 0, 0, 1, 0, 170, 18, 1, 'system', NOW(), 'update_time'),
-- 更新人（隐藏）
('sys_warehouse', 'updateBy', '更新人', 'string', 'input', 0, 0, 0, 0, 0, 100, 19, 1, 'system', NOW(), 'update_by'),
-- 逻辑删除（隐藏）
('sys_warehouse', 'isDeleted', '是否删除', 'int', 'number', 0, 0, 0, 0, 0, NULL, 20, 0, 'system', NOW(), 'is_deleted')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    data_type = VALUES(data_type),
    form_type = VALUES(form_type),
    show_in_list = VALUES(show_in_list),
    show_in_form = VALUES(show_in_form),
    searchable = VALUES(searchable),
    is_sortable = VALUES(is_sortable),
    required = VALUES(required),
    width = VALUES(width),
    sort_order = VALUES(sort_order),
    column_name = VALUES(column_name),
    status = VALUES(status),
    updated_at = NOW();

-- 1.3 操作按钮配置
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, is_enabled)
VALUES
-- 工具栏按钮
('sys_warehouse', 'add', '新增', 'button', 'PlusOutlined', 'sys:warehouse:add', 'toolbar', 1, 1, 1),
('sys_warehouse', 'edit', '编辑', 'button', 'EditOutlined', 'sys:warehouse:edit', 'toolbar', 2, 1, 1),
('sys_warehouse', 'delete', '删除', 'button', 'DeleteOutlined', 'sys:warehouse:delete', 'toolbar', 3, 1, 1),
('sys_warehouse', 'export', '导出', 'button', 'DownloadOutlined', 'sys:warehouse:export', 'toolbar', 4, 1, 1),
-- 行内按钮
('sys_warehouse', 'row_edit', '编辑', 'link', 'EditOutlined', 'sys:warehouse:edit', 'row', 1, 1, 1),
('sys_warehouse', 'row_delete', '删除', 'link', 'DeleteOutlined', 'sys:warehouse:delete', 'row', 2, 1, 1)
ON DUPLICATE KEY UPDATE
    operation_name = VALUES(operation_name),
    icon = VALUES(icon),
    permission = VALUES(permission),
    position = VALUES(position),
    sort_order = VALUES(sort_order);


-- ============================================
-- 2. 仓库收货信息表元数据配置 (sys_warehouse_receiver)
-- ============================================

-- 2.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, create_by, create_time)
VALUES ('sys_warehouse_receiver', '仓库收货信息', 'sys', 'com.abtk.product.dao.entity.SysWarehouseReceiver', 'com.abtk.product.service.sys.service.WarehouseReceiverService', 'sys:warehouseReceiver', 20, 0, 1, '仓库收货地址信息管理', 'system', NOW())
ON DUPLICATE KEY UPDATE
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    page_size = VALUES(page_size),
    remark = VALUES(remark),
    update_time = NOW();

-- 2.2 字段元数据（sys_warehouse_receiver 实际列名）
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, show_in_list, show_in_form, searchable, is_sortable, required, width, sort_order, status, create_by, created_at, column_name)
VALUES
-- 主键（隐藏）
('sys_warehouse_receiver', 'id', '主键ID', 'bigint', 'number', 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW(), 'id'),
-- 仓库编码
('sys_warehouse_receiver', 'warehouseCode', '仓库编码', 'string', 'select', 1, 1, 1, 0, 1, 120, 1, 1, 'system', NOW(), 'warehouse_code'),
-- 收货人
('sys_warehouse_receiver', 'consignee', '收货人', 'string', 'input', 1, 1, 1, 0, 1, 100, 2, 1, 'system', NOW(), 'consignee'),
-- 手机号码
('sys_warehouse_receiver', 'phoneNumber', '手机号码', 'string', 'input', 1, 1, 1, 0, 1, 120, 3, 1, 'system', NOW(), 'phone_number'),
-- 国家
('sys_warehouse_receiver', 'country', '国家', 'string', 'select', 0, 1, 0, 0, 0, 100, 4, 1, 'system', NOW(), 'country'),
-- 省份
('sys_warehouse_receiver', 'province', '省份', 'string', 'input', 1, 1, 0, 0, 0, 100, 5, 1, 'system', NOW(), 'province'),
-- 城市
('sys_warehouse_receiver', 'city', '城市', 'string', 'input', 1, 1, 0, 0, 0, 100, 6, 1, 'system', NOW(), 'city'),
-- 区县
('sys_warehouse_receiver', 'district', '区县', 'string', 'input', 0, 1, 0, 0, 0, 100, 7, 1, 'system', NOW(), 'district'),
-- 详细地址
('sys_warehouse_receiver', 'detailedAddress', '详细地址', 'string', 'textarea', 1, 1, 0, 0, 1, 250, 8, 1, 'system', NOW(), 'detailed_address'),
-- 邮政编码
('sys_warehouse_receiver', 'postalCode', '邮政编码', 'string', 'input', 0, 1, 0, 0, 0, 100, 9, 1, 'system', NOW(), 'postal_code'),
-- 是否默认
('sys_warehouse_receiver', 'isDefault', '默认地址', 'int', 'switch', 1, 1, 1, 0, 0, 100, 10, 1, 'system', NOW(), 'is_default'),
-- 备注
('sys_warehouse_receiver', 'remark', '备注', 'string', 'textarea', 0, 1, 0, 0, 0, NULL, 11, 1, 'system', NOW(), 'remark'),
-- 创建时间
('sys_warehouse_receiver', 'createTime', '创建时间', 'datetime', 'datetime', 1, 0, 0, 1, 0, 170, 12, 1, 'system', NOW(), 'create_time'),
-- 创建人（隐藏）
('sys_warehouse_receiver', 'createBy', '创建人', 'string', 'input', 0, 0, 0, 0, 0, 100, 13, 1, 'system', NOW(), 'create_by'),
-- 更新时间（隐藏）
('sys_warehouse_receiver', 'updateTime', '更新时间', 'datetime', 'datetime', 0, 0, 0, 1, 0, 170, 14, 1, 'system', NOW(), 'update_time'),
-- 更新人（隐藏）
('sys_warehouse_receiver', 'updateBy', '更新人', 'string', 'input', 0, 0, 0, 0, 0, 100, 15, 1, 'system', NOW(), 'update_by'),
-- 逻辑删除（隐藏）
('sys_warehouse_receiver', 'isDeleted', '是否删除', 'int', 'number', 0, 0, 0, 0, 0, NULL, 16, 0, 'system', NOW(), 'is_deleted')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    data_type = VALUES(data_type),
    form_type = VALUES(form_type),
    show_in_list = VALUES(show_in_list),
    show_in_form = VALUES(show_in_form),
    searchable = VALUES(searchable),
    is_sortable = VALUES(is_sortable),
    required = VALUES(required),
    width = VALUES(width),
    sort_order = VALUES(sort_order),
    column_name = VALUES(column_name),
    status = VALUES(status),
    updated_at = NOW();

-- 2.3 操作按钮配置
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, is_enabled)
VALUES
-- 工具栏按钮
('sys_warehouse_receiver', 'add', '新增', 'button', 'PlusOutlined', 'sys:warehouseReceiver:add', 'toolbar', 1, 1, 1),
('sys_warehouse_receiver', 'edit', '编辑', 'button', 'EditOutlined', 'sys:warehouseReceiver:edit', 'toolbar', 2, 1, 1),
('sys_warehouse_receiver', 'delete', '删除', 'button', 'DeleteOutlined', 'sys:warehouseReceiver:delete', 'toolbar', 3, 1, 1),
-- 行内按钮
('sys_warehouse_receiver', 'row_edit', '编辑', 'link', 'EditOutlined', 'sys:warehouseReceiver:edit', 'row', 1, 1, 1),
('sys_warehouse_receiver', 'row_delete', '删除', 'link', 'DeleteOutlined', 'sys:warehouseReceiver:delete', 'row', 2, 1, 1),
('sys_warehouse_receiver', 'row_set_default', '设为默认', 'link', 'CheckCircleOutlined', 'sys:warehouseReceiver:edit', 'row', 3, 1, 1)
ON DUPLICATE KEY UPDATE
    operation_name = VALUES(operation_name),
    icon = VALUES(icon),
    permission = VALUES(permission),
    position = VALUES(position),
    sort_order = VALUES(sort_order),
    update_time = NOW();


-- ============================================
-- 3. 用户表元数据配置 (sys_user)
-- ============================================

-- 3.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, create_by, create_time)
VALUES ('sys_user', '用户表', 'sys', 'com.abtk.product.dao.entity.SysUser', 'com.abtk.product.service.system.ISysUserService', 'system:user', 20, 0, 1, '系统用户表', 'system', NOW())
ON DUPLICATE KEY UPDATE
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    page_size = VALUES(page_size),
    remark = VALUES(remark),
    update_time = NOW();

-- 3.2 字段元数据（sys_user 核心字段）
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, show_in_list, show_in_form, searchable, is_sortable, required, width, sort_order, status, create_by, created_at, column_name)
VALUES
-- 用户ID（隐藏）
('sys_user', 'userId', '用户ID', 'bigint', 'number', 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW(), 'user_id'),
-- 部门ID（隐藏）
('sys_user', 'deptId', '部门ID', 'bigint', 'number', 0, 0, 0, 0, 0, NULL, 1, 1, 'system', NOW(), 'dept_id'),
-- 用户名
('sys_user', 'userName', '用户名', 'string', 'input', 1, 1, 1, 1, 1, 120, 2, 1, 'system', NOW(), 'user_name'),
-- 昵称
('sys_user', 'nickName', '昵称', 'string', 'input', 1, 1, 1, 0, 1, 120, 3, 1, 'system', NOW(), 'nick_name'),
-- 用户类型
('sys_user', 'userType', '用户类型', 'string', 'select', 1, 1, 1, 0, 0, 100, 4, 1, 'system', NOW(), 'user_type'),
-- 邮箱
('sys_user', 'email', '邮箱', 'string', 'input', 1, 1, 1, 0, 0, 150, 5, 1, 'system', NOW(), 'email'),
-- 手机号
('sys_user', 'phonenumber', '手机号', 'string', 'input', 1, 1, 1, 0, 0, 120, 6, 1, 'system', NOW(), 'phonenumber'),
-- 性别
('sys_user', 'sex', '性别', 'string', 'select', 1, 1, 0, 0, 0, 80, 7, 1, 'system', NOW(), 'sex'),
-- 头像（隐藏）
('sys_user', 'avatar', '头像', 'string', 'input', 0, 0, 0, 0, 0, NULL, 8, 1, 'system', NOW(), 'avatar'),
-- 状态
('sys_user', 'status', '状态', 'string', 'switch', 1, 1, 1, 0, 1, 80, 9, 1, 'system', NOW(), 'status'),
-- 最后登录IP
('sys_user', 'loginIp', '最后登录IP', 'string', 'input', 1, 0, 0, 0, 0, 150, 10, 1, 'system', NOW(), 'login_ip'),
-- 最后登录时间
('sys_user', 'loginDate', '最后登录时间', 'datetime', 'datetime', 1, 0, 0, 1, 0, 160, 11, 1, 'system', NOW(), 'login_date'),
-- 创建者（隐藏）
('sys_user', 'createBy', '创建者', 'string', 'input', 0, 0, 0, 0, 0, 100, 12, 1, 'system', NOW(), 'create_by'),
-- 创建时间
('sys_user', 'createTime', '创建时间', 'datetime', 'datetime', 1, 0, 0, 1, 0, 160, 13, 1, 'system', NOW(), 'create_time'),
-- 更新者（隐藏）
('sys_user', 'updateBy', '更新者', 'string', 'input', 0, 0, 0, 0, 0, 100, 14, 1, 'system', NOW(), 'update_by'),
-- 更新时间（隐藏）
('sys_user', 'updateTime', '更新时间', 'datetime', 'datetime', 0, 0, 0, 1, 0, 160, 15, 1, 'system', NOW(), 'update_time'),
-- 备注
('sys_user', 'remark', '备注', 'string', 'textarea', 0, 1, 0, 0, 0, NULL, 16, 1, 'system', NOW(), 'remark'),
-- 删除标志（隐藏）
('sys_user', 'delFlag', '删除标志', 'string', 'input', 0, 0, 0, 0, 0, NULL, 17, 1, 'system', NOW(), 'del_flag')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    data_type = VALUES(data_type),
    form_type = VALUES(form_type),
    show_in_list = VALUES(show_in_list),
    show_in_form = VALUES(show_in_form),
    searchable = VALUES(searchable),
    is_sortable = VALUES(is_sortable),
    required = VALUES(required),
    width = VALUES(width),
    sort_order = VALUES(sort_order),
    column_name = VALUES(column_name),
    status = VALUES(status),
    updated_at = NOW();

-- ============================================
-- 初始化完成
-- ============================================
