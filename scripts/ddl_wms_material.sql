-- ============================================
-- WMS 模块 - 物料档案表 DDL
-- 表: wms_material
-- 实体类: com.abtk.product.dao.entity.Material
-- 创建日期: 2026-04-05
-- ============================================

CREATE TABLE IF NOT EXISTS wms_material (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    material_code VARCHAR(50) NOT NULL COMMENT '物料编码',
    material_name VARCHAR(200) NOT NULL COMMENT '物料名称',
    spec VARCHAR(200) COMMENT '规格型号',
    unit VARCHAR(20) COMMENT '单位',
    category VARCHAR(100) COMMENT '物料类别',
    status INT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-否 1-是',
    create_by VARCHAR(64) COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    UNIQUE KEY uk_material_code (material_code),
    KEY idx_status (status),
    KEY idx_material_name (material_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物料档案表';

-- ============================================
-- 物料档案表元数据初始化
-- 对应 WmsSearchBar 动态表单，使用 column_name 字段映射数据库列
-- ============================================

-- 1. 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, is_deleted_column, create_by, create_time)
VALUES ('wms_material', '物料档案', 'wms', 'com.abtk.product.dao.entity.Material', 'com.abtk.product.service.sys.service.MaterialService', 'wms:material', 20, 0, 1, '物料基础信息管理', 'del_flag', 'system', NOW())
ON DUPLICATE KEY UPDATE
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    is_deleted_column = VALUES(is_deleted_column),
    update_time = NOW();

-- 2. 字段元数据（column_name 对应数据库下划线列名）
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, dict_type, show_in_list, show_in_form, searchable, sortable, required, width, sort_order, status, create_by, create_time, column_name) VALUES
-- 主键
('wms_material', 'id', '主键ID', 'bigint', 'number', NULL, 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW(), 'id'),
-- 物料编码
('wms_material', 'materialCode', '物料编码', 'string', 'input', NULL, 1, 1, 1, 1, 1, 140, 1, 1, 'system', NOW(), 'material_code'),
-- 物料名称
('wms_material', 'materialName', '物料名称', 'string', 'input', NULL, 1, 1, 1, 1, 1, 180, 2, 1, 'system', NOW(), 'material_name'),
-- 规格型号
('wms_material', 'spec', '规格型号', 'string', 'input', NULL, 1, 1, 1, 0, 0, 160, 3, 1, 'system', NOW(), 'spec'),
-- 单位
('wms_material', 'unit', '单位', 'string', 'input', NULL, 1, 1, 1, 0, 0, 80, 4, 1, 'system', NOW(), 'unit'),
-- 物料类别
('wms_material', 'category', '物料类别', 'string', 'input', NULL, 1, 1, 1, 0, 0, 120, 5, 1, 'system', NOW(), 'category'),
-- 状态
('wms_material', 'status', '状态', 'int', 'switch', NULL, 1, 1, 1, 0, 1, 80, 6, 1, 'system', NOW(), 'status'),
-- 逻辑删除标记
('wms_material', 'delFlag', '是否删除', 'int', 'number', NULL, 0, 0, 0, 0, 0, NULL, 7, 0, 'system', NOW(), 'del_flag'),
-- 创建人
('wms_material', 'createBy', '创建人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 8, 1, 'system', NOW(), 'create_by'),
-- 创建时间
('wms_material', 'createTime', '创建时间', 'datetime', 'datetime', NULL, 1, 0, 1, 1, 0, 170, 9, 1, 'system', NOW(), 'create_time'),
-- 更新人
('wms_material', 'updateBy', '更新人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 10, 1, 'system', NOW(), 'update_by'),
-- 更新时间
('wms_material', 'updateTime', '更新时间', 'datetime', 'datetime', NULL, 0, 0, 0, 1, 0, 170, 11, 1, 'system', NOW(), 'update_time')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    data_type = VALUES(data_type),
    form_type = VALUES(form_type),
    dict_type = VALUES(dict_type),
    show_in_list = VALUES(show_in_list),
    show_in_form = VALUES(show_in_form),
    searchable = VALUES(searchable),
    sortable = VALUES(sortable),
    required = VALUES(required),
    width = VALUES(width),
    sort_order = VALUES(sort_order),
    column_name = VALUES(column_name),
    update_time = NOW();

-- 3. 操作按钮配置
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, button_type, icon, permission, position, sort_order, status, create_by, create_time) VALUES
-- 工具栏按钮
('wms_material', 'add', '新增', 'button', 'primary', 'PlusOutlined', 'wms:material:add', 'toolbar', 1, 1, 'system', NOW()),
('wms_material', 'edit', '编辑', 'button', 'default', 'EditOutlined', 'wms:material:edit', 'toolbar', 2, 1, 'system', NOW()),
('wms_material', 'delete', '删除', 'button', 'danger', 'DeleteOutlined', 'wms:material:delete', 'toolbar', 3, 1, 'system', NOW()),
('wms_material', 'export', '导出', 'button', 'default', 'DownloadOutlined', 'wms:material:export', 'toolbar', 4, 1, 'system', NOW()),
-- 行内按钮
('wms_material', 'row_edit', '编辑', 'link', 'link', 'EditOutlined', 'wms:material:edit', 'row', 1, 1, 'system', NOW()),
('wms_material', 'row_delete', '删除', 'link', 'link', 'DeleteOutlined', 'wms:material:delete', 'row', 2, 1, 'system', NOW())
ON DUPLICATE KEY UPDATE
    operation_name = VALUES(operation_name),
    button_type = VALUES(button_type),
    icon = VALUES(icon),
    permission = VALUES(permission),
    sort_order = VALUES(sort_order),
    update_time = NOW();

-- ============================================
-- 验证查询
-- ============================================
-- SELECT field, column_name, title, searchable, show_in_list FROM sys_column_meta WHERE table_code='wms_material' ORDER BY sort_order;
