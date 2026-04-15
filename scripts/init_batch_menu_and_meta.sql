-- ============================================
-- 批次管理菜单及元数据初始化
-- 更新日期: 2026-04-16
-- ============================================

-- ============================================
-- 1. sys_menu 菜单配置
-- ============================================

-- 1.1 批次管理页面菜单 (parent_id=1080: WMS基础数据)
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES (3162, '批次管理', 1080, 3, 'sys/batch', 'sys/batch/index', 'C', '0', '0', 'wms:base:batch:list', '#', 'system', NOW())
ON DUPLICATE KEY UPDATE
    menu_name = VALUES(menu_name),
    parent_id = VALUES(parent_id),
    order_num = VALUES(order_num),
    path = VALUES(path),
    component = VALUES(component),
    perms = VALUES(perms),
    update_time = NOW();

-- 1.2 批次管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES
(3163, '查询', 3162, 1, '', '', 'F', '0', '0', 'wms:base:batch:query', '#', 'system', NOW()),
(3164, '新增', 3162, 2, '', '', 'F', '0', '0', 'wms:base:batch:add', '#', 'system', NOW()),
(3165, '修改', 3162, 3, '', '', 'F', '0', '0', 'wms:base:batch:edit', '#', 'system', NOW()),
(3166, '删除', 3162, 4, '', '', 'F', '0', '0', 'wms:base:batch:remove', '#', 'system', NOW()),
(3167, '导出', 3162, 5, '', '', 'F', '0', '0', 'wms:base:batch:export', '#', 'system', NOW()),
(3168, '导入', 3162, 6, '', '', 'F', '0', '0', 'wms:base:batch:import', '#', 'system', NOW())
ON DUPLICATE KEY UPDATE
    menu_name = VALUES(menu_name),
    parent_id = VALUES(parent_id),
    order_num = VALUES(order_num),
    perms = VALUES(perms),
    update_time = NOW();

-- ============================================
-- 2. sys_role_menu 权限关联 (admin 角色 role_id=1)
-- ============================================
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 3162), (1, 3163), (1, 3164), (1, 3165), (1, 3166), (1, 3167), (1, 3168)
ON DUPLICATE KEY UPDATE role_id = role_id;

-- ============================================
-- 3. sys_table_meta 表级元数据
-- ============================================
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remarks, create_by, create_time)
VALUES ('inv_batch', '批次档案', 'wms', 'com.abtk.product.dao.entity.Batch', 'com.abtk.product.service.sys.service.impl.CrudServiceImpl', 'wms:base:batch', 20, 0, 1, '批次基础信息管理', 'system', NOW())
ON DUPLICATE KEY UPDATE
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    page_size = VALUES(page_size),
    remarks = VALUES(remarks),
    update_time = NOW();

-- ============================================
-- 4. sys_column_meta 字段元数据
-- ============================================
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, show_in_list, show_in_form, searchable, is_sortable, required, width, sort_order, status, create_by, create_time, column_name)
VALUES
-- 主键（隐藏）
('inv_batch', 'id', '主键ID', 'bigint', 'number', 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW(), 'id'),
-- 批次号（必填）
('inv_batch', 'batchNo', '批次号', 'string', 'input', 1, 1, 1, 1, 1, 150, 1, 1, 'system', NOW(), 'batch_no'),
-- 物料编码（必填）
('inv_batch', 'materialCode', '物料编码', 'string', 'input', 1, 1, 1, 1, 1, 150, 2, 1, 'system', NOW(), 'material_code'),
-- 供应商批号
('inv_batch', 'supplierBatchNo', '供应商批号', 'string', 'input', 1, 1, 1, 0, 0, 150, 3, 1, 'system', NOW(), 'supplier_batch_no'),
-- 失效期
('inv_batch', 'expireDate', '失效期', 'date', 'date', 1, 1, 1, 0, 0, 120, 4, 1, 'system', NOW(), 'expire_date'),
-- 状态
('inv_batch', 'status', '状态', 'int', 'switch', 1, 1, 1, 0, 1, 80, 5, 1, 'system', NOW(), 'status'),
-- 纯化编号
('inv_batch', 'purifyNo', '纯化编号', 'string', 'input', 1, 1, 1, 0, 0, 120, 6, 1, 'system', NOW(), 'purify_no'),
-- 克隆号
('inv_batch', 'cloneNo', '克隆号', 'string', 'input', 1, 1, 1, 0, 0, 120, 7, 1, 'system', NOW(), 'clone_no'),
-- 浓度
('inv_batch', 'concentration', '浓度', 'string', 'input', 1, 1, 1, 0, 0, 100, 8, 1, 'system', NOW(), 'concentration'),
-- 浓度更新时间
('inv_batch', 'concentrationUpdateTime', '浓度更新时间', 'datetime', 'datetime', 1, 1, 0, 1, 0, 160, 9, 1, 'system', NOW(), 'concentration_update_time'),
-- COA链接
('inv_batch', 'coaLink', 'COA链接', 'string', 'input', 1, 1, 0, 0, 0, 200, 10, 1, 'system', NOW(), 'coa_link'),
-- 入库日期
('inv_batch', 'inboundDate', '入库日期', 'date', 'date', 1, 1, 0, 0, 0, 120, 11, 1, 'system', NOW(), 'inbound_date'),
-- 生产日期
('inv_batch', 'productionDate', '生产日期', 'date', 'date', 1, 1, 0, 0, 0, 120, 12, 1, 'system', NOW(), 'production_date'),
-- 项目编号
('inv_batch', 'projectNo', '项目编号', 'string', 'input', 1, 1, 1, 0, 0, 120, 13, 1, 'system', NOW(), 'project_no'),
-- 裸成品物料编码
('inv_batch', 'nakedFinishCode', '裸成品物料编码', 'string', 'input', 1, 1, 1, 0, 0, 150, 14, 1, 'system', NOW(), 'naked_finish_code'),
-- 裸成品批次
('inv_batch', 'nakedFinishBatch', '裸成品批次', 'string', 'input', 1, 1, 1, 0, 0, 150, 15, 1, 'system', NOW(), 'naked_finish_batch'),
-- 缓冲液
('inv_batch', 'bufferSolution', '缓冲液', 'string', 'input', 0, 1, 0, 0, 0, 200, 16, 1, 'system', NOW(), 'buffer_solution'),
-- 供应商品牌
('inv_batch', 'supplierBrand', '供应商品牌', 'string', 'input', 1, 1, 1, 0, 0, 120, 17, 1, 'system', NOW(), 'supplier_brand'),
-- 供应商货号
('inv_batch', 'supplierItemNo', '供应商货号', 'string', 'input', 1, 1, 1, 0, 0, 120, 18, 1, 'system', NOW(), 'supplier_item_no'),
-- ERP同步备注
('inv_batch', 'erpSyncRemark', 'ERP同步备注', 'string', 'textarea', 0, 1, 0, 0, 0, NULL, 19, 1, 'system', NOW(), 'erp_sync_remark'),
-- 物料ID（隐藏）
('inv_batch', 'materialId', '物料ID', 'bigint', 'number', 0, 0, 0, 0, 0, NULL, 20, 1, 'system', NOW(), 'material_id'),
-- 质检数据
('inv_batch', 'qcData', '质检数据', 'text', 'textarea', 0, 1, 0, 0, 0, NULL, 21, 1, 'system', NOW(), 'qc_data'),
-- 备注
('inv_batch', 'remarks', '备注', 'string', 'textarea', 0, 1, 0, 0, 0, NULL, 22, 1, 'system', NOW(), 'remarks'),
-- 创建时间
('inv_batch', 'createTime', '创建时间', 'datetime', 'datetime', 1, 0, 0, 1, 0, 170, 23, 1, 'system', NOW(), 'create_time'),
-- 创建人（隐藏）
('inv_batch', 'createBy', '创建人', 'string', 'input', 0, 0, 0, 0, 0, 100, 24, 1, 'system', NOW(), 'create_by'),
-- 更新时间（隐藏）
('inv_batch', 'updateTime', '更新时间', 'datetime', 'datetime', 0, 0, 0, 1, 0, 170, 25, 1, 'system', NOW(), 'update_time'),
-- 更新人（隐藏）
('inv_batch', 'updateBy', '更新人', 'string', 'input', 0, 0, 0, 0, 0, 100, 26, 1, 'system', NOW(), 'update_by'),
-- 逻辑删除（隐藏）
('inv_batch', 'isDeleted', '是否删除', 'int', 'number', 0, 0, 0, 0, 0, NULL, 27, 0, 'system', NOW(), 'is_deleted')
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
    update_time = NOW();

-- ============================================
-- 5. sys_table_operation 操作按钮配置
-- ============================================
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, is_enabled)
VALUES
('inv_batch', 'add', '新增', 'button', 'PlusOutlined', 'wms:base:batch:add', 'toolbar', 1, 1, 1),
('inv_batch', 'edit', '编辑', 'button', 'EditOutlined', 'wms:base:batch:edit', 'toolbar', 2, 1, 1),
('inv_batch', 'delete', '删除', 'button', 'DeleteOutlined', 'wms:base:batch:remove', 'toolbar', 3, 1, 1),
('inv_batch', 'export', '导出', 'button', 'DownloadOutlined', 'wms:base:batch:export', 'toolbar', 4, 1, 1),
('inv_batch', 'row_edit', '编辑', 'link', 'EditOutlined', 'wms:base:batch:edit', 'row', 1, 1, 1),
('inv_batch', 'row_delete', '删除', 'link', 'DeleteOutlined', 'wms:base:batch:remove', 'row', 2, 1, 1)
ON DUPLICATE KEY UPDATE
    operation_name = VALUES(operation_name),
    icon = VALUES(icon),
    permission = VALUES(permission),
    position = VALUES(position),
    sort_order = VALUES(sort_order);
