-- ============================================
-- 动态表单模块 - 物料管理表元数据初始化
-- 更新日期: 2026-04-05
-- ============================================

-- 确保元数据表已存在
-- 请先执行 ddl_sys_table_meta.sql 和 ddl_sys_column_meta_extension.sql

-- ============================================
-- 1. 物料表元数据配置 (sys_material)
-- ============================================

-- 1.1 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, is_deleted_column, create_by, create_time) 
VALUES ('sys_material', '物料档案', 'sys', 'com.abtk.product.dao.entity.Material', 'com.abtk.product.service.sys.service.MaterialService', 'sys:material', 20, 0, 1, '物料基础信息管理', 'del_flag', 'system', NOW())
ON DUPLICATE KEY UPDATE 
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    is_deleted_column = VALUES(is_deleted_column),
    update_time = NOW();

-- 1.2 字段元数据（使用 column_name 字段作为前端 key，与 WmsSearchBar 兼容）
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, dict_type, show_in_list, show_in_form, searchable, sortable, required, width, sort_order, status, create_by, create_time, column_name) VALUES
-- 主键
('sys_material', 'id', '主键ID', 'bigint', 'number', NULL, 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW(), 'id'),
-- 物料编码
('sys_material', 'materialCode', '物料编码', 'string', 'input', NULL, 1, 1, 1, 1, 1, 140, 1, 1, 'system', NOW(), 'material_code'),
-- 物料名称
('sys_material', 'materialName', '物料名称', 'string', 'input', NULL, 1, 1, 1, 1, 1, 180, 2, 1, 'system', NOW(), 'material_name'),
-- 规格型号（对应数据库字段 spec）
('sys_material', 'specification', '规格型号', 'string', 'input', NULL, 1, 1, 1, 0, 0, 160, 3, 1, 'system', NOW(), 'spec'),
-- 单位
('sys_material', 'unit', '单位', 'string', 'input', NULL, 1, 1, 1, 0, 0, 80, 4, 1, 'system', NOW(), 'unit'),
-- 分类
('sys_material', 'category', '分类', 'string', 'input', NULL, 1, 1, 1, 0, 0, 120, 5, 1, 'system', NOW(), 'category'),
-- 状态
('sys_material', 'status', '状态', 'int', 'switch', NULL, 1, 1, 1, 0, 1, 80, 6, 1, 'system', NOW(), 'status'),
-- 逻辑删除标记
('sys_material', 'delFlag', '是否删除', 'int', 'number', NULL, 0, 0, 0, 0, 0, NULL, 7, 0, 'system', NOW(), 'del_flag'),
-- 创建人
('sys_material', 'createBy', '创建人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 8, 1, 'system', NOW(), 'create_by'),
-- 创建时间
('sys_material', 'createTime', '创建时间', 'datetime', 'datetime', NULL, 1, 0, 1, 1, 0, 170, 9, 1, 'system', NOW(), 'create_time'),
-- 更新人
('sys_material', 'updateBy', '更新人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 10, 1, 'system', NOW(), 'update_by'),
-- 更新时间
('sys_material', 'updateTime', '更新时间', 'datetime', 'datetime', NULL, 0, 0, 0, 1, 0, 170, 11, 1, 'system', NOW(), 'update_time')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    data_type = VALUES(data_type),
    form_type = VALUES(form_type),
    dict_type = VALUES(dict_type),
    show_in_list = VALUES(show_in_list),
    show_in_form = VALUES(show_in_form),
    searchable = VALUES(searchable),
    required = VALUES(required),
    width = VALUES(width),
    sort_order = VALUES(sort_order),
    column_name = VALUES(column_name),
    update_time = NOW();

-- ============================================
-- 验证查询
-- ============================================
-- SELECT field, column_name, title, searchable, show_in_list FROM sys_column_meta WHERE table_code='sys_material' ORDER BY sort_order;