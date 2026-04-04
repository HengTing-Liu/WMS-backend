-- ============================================
-- 修复 sys_column_meta 表脏数据
-- 数据库实际列名：show_in_list / searchable（无 is_ 前缀）
-- 修复内容：
--  1. field 列：snake_case -> camelCase（驼峰）
--  2. searchable / show_in_list 等 boolean 标志位从 null 补上正确值
-- ============================================

-- 1. 仓库表 (sys_warehouse) 字段修正
UPDATE sys_column_meta SET field='warehouseCode',     show_in_list=1, show_in_form=1, searchable=1, sortable=1, required=1 WHERE table_code='sys_warehouse' AND field='warehouse_code';
UPDATE sys_column_meta SET field='warehouseName',     show_in_list=1, show_in_form=1, searchable=1, sortable=1, required=1 WHERE table_code='sys_warehouse' AND field='warehouse_name';
UPDATE sys_column_meta SET field='temperatureZone',  show_in_list=1, show_in_form=1, searchable=1, sortable=0, required=0 WHERE table_code='sys_warehouse' AND field='temperature_zone';
UPDATE sys_column_meta SET field='qualityZone',      show_in_list=1, show_in_form=1, searchable=1, sortable=0, required=0 WHERE table_code='sys_warehouse' AND field='quality_zone';
UPDATE sys_column_meta SET field='employeeCode',     show_in_list=0, show_in_form=1, searchable=0, sortable=0, required=0 WHERE table_code='sys_warehouse' AND field='employee_code';
UPDATE sys_column_meta SET field='employeeName',     show_in_list=1, show_in_form=1, searchable=1, sortable=0, required=0 WHERE table_code='sys_warehouse' AND field='employee_name';
UPDATE sys_column_meta SET field='deptCode',         show_in_list=0, show_in_form=1, searchable=0, sortable=0, required=0 WHERE table_code='sys_warehouse' AND field='dept_code';
UPDATE sys_column_meta SET field='deptNameFullPath', show_in_list=1, show_in_form=1, searchable=0, sortable=0, required=0 WHERE table_code='sys_warehouse' AND field='dept_name_full_path';
UPDATE sys_column_meta SET field='isEnabled',        show_in_list=1, show_in_form=1, searchable=1, sortable=0, required=1 WHERE table_code='sys_warehouse' AND field='is_enabled';
UPDATE sys_column_meta SET field='createTime',        show_in_list=1, show_in_form=0, searchable=1, sortable=1, required=0 WHERE table_code='sys_warehouse' AND field='create_time';
UPDATE sys_column_meta SET field='createBy',          show_in_list=0, show_in_form=0, searchable=0, sortable=0, required=0 WHERE table_code='sys_warehouse' AND field='create_by';

-- 2. 仓库收货表 (sys_warehouse_receiver) 字段修正
UPDATE sys_column_meta SET field='warehouseCode',    show_in_list=1, show_in_form=1, searchable=1, sortable=0, required=1 WHERE table_code='sys_warehouse_receiver' AND field='warehouse_code';
UPDATE sys_column_meta SET field='consignee',       show_in_list=1, show_in_form=1, searchable=1, sortable=0, required=1 WHERE table_code='sys_warehouse_receiver' AND field='consignee';
UPDATE sys_column_meta SET field='phoneNumber',      show_in_list=1, show_in_form=1, searchable=1, sortable=0, required=1 WHERE table_code='sys_warehouse_receiver' AND field='phone_number';
UPDATE sys_column_meta SET field='detailedAddress', show_in_list=1, show_in_form=1, searchable=0, sortable=0, required=1 WHERE table_code='sys_warehouse_receiver' AND field='detailed_address';
UPDATE sys_column_meta SET field='postalCode',      show_in_list=0, show_in_form=1, searchable=0, sortable=0, required=0 WHERE table_code='sys_warehouse_receiver' AND field='postal_code';
UPDATE sys_column_meta SET field='isDefault',       show_in_list=1, show_in_form=1, searchable=1, sortable=0, required=0 WHERE table_code='sys_warehouse_receiver' AND field='is_default';
UPDATE sys_column_meta SET field='createTime',       show_in_list=1, show_in_form=0, searchable=1, sortable=1, required=0 WHERE table_code='sys_warehouse_receiver' AND field='create_time';

-- 3. 所有 remaining null 值补 0
UPDATE sys_column_meta SET show_in_list=0 WHERE show_in_list IS NULL;
UPDATE sys_column_meta SET show_in_form=0 WHERE show_in_form IS NULL;
UPDATE sys_column_meta SET searchable=0  WHERE searchable IS NULL;
UPDATE sys_column_meta SET sortable=0     WHERE sortable IS NULL;
UPDATE sys_column_meta SET required=0     WHERE required IS NULL;

-- 4. 验证
SELECT field, searchable, show_in_list FROM sys_column_meta WHERE table_code='sys_warehouse' ORDER BY sort_order;
