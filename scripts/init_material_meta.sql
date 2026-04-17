-- ============================================
-- 动态表单模块 - 物料管理表元数据初始化
-- 表编码：sys_material（对应数据库表 sys_material）
-- 更新日期: 2026-04-13
-- ============================================

-- 确保元数据表已存在
-- 请先执行 ddl_sys_table_meta.sql 和 ddl_sys_column_meta_extension.sql

-- ============================================
-- 1. 物料表元数据配置 (sys_material)
-- ============================================

-- 1.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, create_by, create_time)
VALUES ('sys_material', '物料档案', 'sys', 'com.abtk.product.dao.entity.Material', 'com.abtk.product.service.sys.service.MaterialService', 'sys:material', 20, 0, 1, '物料基础信息管理', 'system', NOW())
ON DUPLICATE KEY UPDATE
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    page_size = VALUES(page_size),
    remark = VALUES(remark),
    update_time = NOW();

-- 1.2 字段元数据（与 init_meta_data.sql 列名一致）
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, show_in_list, show_in_form, searchable, is_sortable, required, width, sort_order, status, created_by, created_at, column_name)
VALUES
-- ---------- 基础信息 ----------
('sys_material', 'id', '主键ID', 'bigint', 'number', 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW(), 'id'),
('sys_material', 'materialCode', '物料编码', 'string', 'input', 1, 1, 1, 1, 1, 140, 1, 1, 'system', NOW(), 'material_code'),
('sys_material', 'materialName', '物料名称', 'string', 'input', 1, 1, 1, 1, 1, 200, 2, 1, 'system', NOW(), 'material_name'),
-- ---------- 规格信息 ----------
('sys_material', 'spec', '规格', 'string', 'input', 1, 1, 1, 0, 0, 160, 10, 1, 'system', NOW(), 'spec'),
('sys_material', 'unit', '单位', 'string', 'input', 1, 1, 1, 0, 0, 80, 11, 1, 'system', NOW(), 'unit'),
('sys_material', 'category', '分类', 'string', 'input', 1, 1, 1, 0, 0, 120, 12, 1, 'system', NOW(), 'category'),
-- ---------- 供应商信息 ----------
('sys_material', 'supplierBrand', '供应商品牌', 'string', 'input', 0, 1, 1, 0, 0, 120, 20, 1, 'system', NOW(), 'supplier_brand'),
('sys_material', 'supplierItemNo', '供应商货号', 'string', 'input', 0, 1, 1, 0, 0, 120, 21, 1, 'system', NOW(), 'supplier_item_no'),
('sys_material', 'supplierSpec', '供应商规格', 'string', 'input', 0, 1, 0, 0, 0, 150, 22, 1, 'system', NOW(), 'supplier_spec'),
-- ---------- 包装信息 ----------
('sys_material', 'packageSpec', '包装规格', 'string', 'input', 0, 1, 0, 0, 0, 150, 30, 1, 'system', NOW(), 'package_spec'),
('sys_material', 'logisticsPackage', '物流包装', 'string', 'input', 0, 1, 0, 0, 0, 150, 31, 1, 'system', NOW(), 'logistics_package'),
('sys_material', 'boxModel', '箱型号', 'string', 'input', 0, 1, 0, 0, 0, 100, 32, 1, 'system', NOW(), 'box_model'),
('sys_material', 'outerPackage', '产品外包装', 'string', 'input', 0, 1, 0, 0, 0, 150, 33, 1, 'system', NOW(), 'outer_package'),
-- ---------- 维度信息 ----------
('sys_material', 'length', '长(cm)', 'decimal', 'number', 0, 1, 0, 0, 0, 80, 40, 1, 'system', NOW(), 'length'),
('sys_material', 'width', '宽(cm)', 'decimal', 'number', 0, 1, 0, 0, 0, 80, 41, 1, 'system', NOW(), 'width'),
('sys_material', 'height', '高(cm)', 'decimal', 'number', 0, 1, 0, 0, 0, 80, 42, 1, 'system', NOW(), 'height'),
-- ---------- 存储运输条件 ----------
('sys_material', 'storageCondition', '存储条件', 'string', 'textarea', 0, 1, 0, 0, 0, NULL, 50, 1, 'system', NOW(), 'storage_condition'),
('sys_material', 'transportCondition', '运输条件', 'string', 'textarea', 0, 1, 0, 0, 0, NULL, 51, 1, 'system', NOW(), 'transport_condition'),
-- ---------- 质量信息 ----------
('sys_material', 'requireQc', '是否必检', 'boolean', 'switch', 0, 1, 0, 0, 0, 80, 60, 1, 'system', NOW(), 'require_qc'),
-- ---------- 系统字段 ----------
('sys_material', 'status', '状态', 'int', 'switch', 1, 1, 1, 0, 1, 80, 70, 1, 'system', NOW(), 'status'),
('sys_material', 'materialEnName', '物料英文名称', 'string', 'input', 0, 1, 0, 0, 0, 200, 71, 1, 'system', NOW(), 'material_en_name'),
('sys_material', 'exportName', '出口名称', 'string', 'input', 0, 1, 0, 0, 0, 200, 72, 1, 'system', NOW(), 'export_name'),
('sys_material', 'brand', '品牌', 'string', 'input', 0, 1, 1, 0, 0, 120, 73, 1, 'system', NOW(), 'brand'),
('sys_material', 'itemNo', '货号', 'string', 'input', 0, 1, 1, 0, 0, 120, 74, 1, 'system', NOW(), 'item_no'),
('sys_material', 'erpSyncRemark', 'ERP同步备注', 'string', 'textarea', 0, 1, 0, 0, 0, NULL, 80, 1, 'system', NOW(), 'erp_sync_remark'),
('sys_material', 'remarks', '备注', 'string', 'textarea', 0, 1, 0, 0, 0, NULL, 90, 1, 'system', NOW(), 'remarks'),
('sys_material', 'createBy', '创建人', 'string', 'input', 0, 0, 0, 0, 0, 100, 95, 1, 'system', NOW(), 'create_by'),
('sys_material', 'createTime', '创建时间', 'datetime', 'datetime', 1, 0, 0, 1, 0, 170, 96, 1, 'system', NOW(), 'create_time'),
('sys_material', 'updateBy', '更新人', 'string', 'input', 0, 0, 0, 0, 0, 100, 97, 1, 'system', NOW(), 'update_by'),
('sys_material', 'updateTime', '更新时间', 'datetime', 'datetime', 0, 0, 0, 1, 0, 170, 98, 1, 'system', NOW(), 'update_time'),
('sys_material', 'isDeleted', '是否删除', 'int', 'number', 0, 0, 0, 0, 0, NULL, 99, 0, 'system', NOW(), 'is_deleted')
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
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, icon, permission, position, sort_order, status, is_enabled, show_button)
VALUES
('sys_material', 'add', '新增', 'button', 'PlusOutlined', 'wms:base:material:add', 'toolbar', 1, 1, 1, 1),
('sys_material', 'edit', '编辑', 'button', 'EditOutlined', 'wms:base:material:edit', 'toolbar', 2, 1, 1, 1),
('sys_material', 'delete', '删除', 'button', 'DeleteOutlined', 'wms:base:material:delete', 'toolbar', 3, 1, 1, 1),
('sys_material', 'export', '导出', 'button', 'DownloadOutlined', 'wms:base:material:export', 'toolbar', 4, 1, 1, 1),
('sys_material', 'row_edit', '编辑', 'link', 'EditOutlined', 'wms:base:material:edit', 'row', 1, 1, 1, 1),
('sys_material', 'row_read', '查看', 'link', 'EyeOutlined', 'wms:base:material:query', 'row', 2, 1, 1, 1),
('sys_material', 'row_delete', '删除', 'link', 'DeleteOutlined', 'wms:base:material:delete', 'row', 3, 1, 1, 1)
ON DUPLICATE KEY UPDATE
    operation_name = VALUES(operation_name),
    icon = VALUES(icon),
    permission = VALUES(permission),
    position = VALUES(position),
    sort_order = VALUES(sort_order),
    show_button = VALUES(show_button);

-- ============================================
-- 验证查询
-- ============================================
-- SELECT field, title, searchable, show_in_list, is_sortable, column_name FROM sys_column_meta WHERE table_code='sys_material' ORDER BY sort_order;
