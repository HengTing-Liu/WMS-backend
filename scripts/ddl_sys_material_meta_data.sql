-- ============================================
-- 物料档案表元数据配置 (wms_material)
-- ============================================

-- 1. 表级元数据
INSERT INTO sys_table_meta (table_code, table_name, module, entity_class, service_class, permission_code, page_size, is_tree, status, remark, is_deleted_column, create_by, create_time)
VALUES ('wms_material', '物料档案', 'wms', 'com.abtk.product.dao.entity.Material', 'com.abtk.product.service.sys.impl.MaterialServiceImpl', 'wms:material', 20, 0, 1, '物料基础信息管理', 'del_flag', 'system', NOW())
ON DUPLICATE KEY UPDATE
    table_name = VALUES(table_name),
    entity_class = VALUES(entity_class),
    service_class = VALUES(service_class),
    permission_code = VALUES(permission_code),
    update_time = NOW();

-- 2. 字段元数据
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, dict_type, is_show_in_list, is_show_in_form, is_searchable, is_sortable, is_required, width, sort_order, status, create_by, create_time) VALUES
-- 主键
('wms_material', 'id', '主键ID', 'long', 'number', NULL, 0, 0, 0, 0, 0, NULL, 0, 1, 'system', NOW()),
-- 物料编码
('wms_material', 'materialCode', '物料编码', 'string', 'input', NULL, 1, 1, 1, 1, 1, 140, 1, 1, 'system', NOW()),
-- 物料名称
('wms_material', 'materialName', '物料名称', 'string', 'input', NULL, 1, 1, 1, 1, 1, 180, 2, 1, 'system', NOW()),
-- 规格型号
('wms_material', 'spec', '规格型号', 'string', 'input', NULL, 1, 1, 1, 0, 0, 160, 3, 1, 'system', NOW()),
-- 单位
('wms_material', 'unit', '单位', 'string', 'input', NULL, 1, 1, 1, 0, 0, 80, 4, 1, 'system', NOW()),
-- 物料类别
('wms_material', 'category', '物料类别', 'string', 'input', NULL, 1, 1, 1, 0, 0, 120, 5, 1, 'system', NOW()),
-- 状态
('wms_material', 'status', '状态', 'int', 'switch', NULL, 1, 1, 1, 0, 1, 80, 6, 1, 'system', NOW()),
-- 创建人
('wms_material', 'createBy', '创建人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 7, 1, 'system', NOW()),
-- 创建时间
('wms_material', 'createTime', '创建时间', 'datetime', 'datetime', NULL, 1, 0, 1, 1, 0, 170, 8, 1, 'system', NOW()),
-- 更新人
('wms_material', 'updateBy', '更新人', 'string', 'input', NULL, 0, 0, 0, 0, 0, 100, 9, 1, 'system', NOW()),
-- 更新时间
('wms_material', 'updateTime', '更新时间', 'datetime', 'datetime', NULL, 0, 0, 0, 1, 0, 170, 10, 1, 'system', NOW()),
-- 逻辑删除
('wms_material', 'delFlag', '是否删除', 'int', 'number', NULL, 0, 0, 0, 0, 0, NULL, 11, 0, 'system', NOW())
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

-- 3. 操作按钮配置
INSERT INTO sys_table_operation (table_code, operation_code, operation_name, operation_type, button_type, icon, permission, position, sort_order, status, create_by, create_time) VALUES
-- 工具栏按钮
('wms_material', 'add', '新增', 'button', 'primary', 'plus', 'wms:material:add', 'toolbar', 1, 1, 'system', NOW()),
('wms_material', 'edit', '编辑', 'button', 'default', 'edit', 'wms:material:edit', 'toolbar', 2, 1, 'system', NOW()),
('wms_material', 'delete', '删除', 'button', 'danger', 'delete', 'wms:material:delete', 'toolbar', 3, 1, 'system', NOW()),
('wms_material', 'export', '导出', 'button', 'default', 'download', 'wms:material:export', 'toolbar', 4, 1, 'system', NOW()),
-- 行内按钮
('wms_material', 'row_edit', '编辑', 'link', 'link', 'edit', 'wms:material:edit', 'row', 1, 1, 'system', NOW()),
('wms_material', 'row_delete', '删除', 'link', 'link', 'delete', 'wms:material:delete', 'row', 2, 1, 'system', NOW())
ON DUPLICATE KEY UPDATE
    operation_name = VALUES(operation_name),
    button_type = VALUES(button_type),
    icon = VALUES(icon),
    permission = VALUES(permission),
    sort_order = VALUES(sort_order),
    update_time = NOW();
