-- 将已存在库中的 sys_column_meta 与当前 SysUser / sys_user 定义对齐（建议先备份后执行；可与 init 全量重刷二选一）
-- 对应实体：com.abtk.product.dao.entity.SysUser
-- 主键列：id（非 user_id）；部门：dept_code（已移除 dept_id）；工号在库字段为 code，实体属性为 nickName
-- 以下无 user_type / sex 等当前 Mapper 已不选出的列

-- 1) 主键列名
UPDATE sys_column_meta
SET column_name = 'id', title = '用户ID', update_time = NOW()
WHERE table_code = 'sys_user' AND field = 'userId';

-- 2) 部门：field 与列从 dept_id 改为部门编码
UPDATE sys_column_meta
SET field = 'deptCode', title = '部门编码', column_name = 'dept_code',
    data_type = 'string', form_type = 'input', update_time = NOW()
WHERE table_code = 'sys_user' AND field = 'deptId';

-- 3) 移除实体/Mapper 中已不使用的元数据（如 userType、sex）
DELETE FROM sys_column_meta
WHERE table_code = 'sys_user' AND field IN ('userType', 'sex');

-- 4) 登录名与工号
UPDATE sys_column_meta
SET title = '登录名', column_name = 'login_name', update_time = NOW()
WHERE table_code = 'sys_user' AND field = 'userName';

UPDATE sys_column_meta
SET title = '工号', column_name = 'code', update_time = NOW()
WHERE table_code = 'sys_user' AND field = 'nickName';

-- 5) 补充：真实姓名、密码、密码最后更新时间
INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, show_in_list, show_in_form, searchable, is_sortable, required, width, sort_order, status, create_by, create_time, column_name)
SELECT 'sys_user', 'name', '真实姓名', 'string', 'input', 1, 1, 1, 0, 0, 120, 3, 1, 'system', NOW(), 'name'
FROM (SELECT 1) AS t
WHERE NOT EXISTS (SELECT 1 FROM sys_column_meta c WHERE c.table_code = 'sys_user' AND c.field = 'name');

INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, show_in_list, show_in_form, searchable, is_sortable, required, width, sort_order, status, create_by, create_time, column_name)
SELECT 'sys_user', 'password', '密码', 'string', 'input', 0, 0, 0, 0, 0, NULL, 7, 1, 'system', NOW(), 'password'
FROM (SELECT 1) AS t
WHERE NOT EXISTS (SELECT 1 FROM sys_column_meta c WHERE c.table_code = 'sys_user' AND c.field = 'password');

INSERT INTO sys_column_meta (table_code, field, title, data_type, form_type, show_in_list, show_in_form, searchable, is_sortable, required, width, sort_order, status, create_by, create_time, column_name)
SELECT 'sys_user', 'pwdUpdateDate', '密码最后更新时间', 'datetime', 'datetime', 0, 0, 0, 0, 0, 160, 16, 1, 'system', NOW(), 'pwd_update_date'
FROM (SELECT 1) AS t
WHERE NOT EXISTS (SELECT 1 FROM sys_column_meta c WHERE c.table_code = 'sys_user' AND c.field = 'pwdUpdateDate');

-- 6) 统一重排 sort_order（与 init_meta_data.sql 中 sys_user 段一致）
UPDATE sys_column_meta SET sort_order = 0, width = NULL  WHERE table_code = 'sys_user' AND field = 'userId';
UPDATE sys_column_meta SET sort_order = 1, width = 150 WHERE table_code = 'sys_user' AND field = 'deptCode';
UPDATE sys_column_meta SET sort_order = 2, width = 150 WHERE table_code = 'sys_user' AND field = 'userName';
UPDATE sys_column_meta SET sort_order = 3, width = 120 WHERE table_code = 'sys_user' AND field = 'name';
UPDATE sys_column_meta SET sort_order = 4, width = 120 WHERE table_code = 'sys_user' AND field = 'nickName';
UPDATE sys_column_meta SET sort_order = 5, width = 150 WHERE table_code = 'sys_user' AND field = 'email';
UPDATE sys_column_meta SET sort_order = 6, width = 120 WHERE table_code = 'sys_user' AND field = 'phonenumber';
UPDATE sys_column_meta SET sort_order = 7, show_in_list = 0, show_in_form = 0, searchable = 0 WHERE table_code = 'sys_user' AND field = 'password';
UPDATE sys_column_meta SET sort_order = 8, width = 80  WHERE table_code = 'sys_user' AND field = 'status';
UPDATE sys_column_meta SET sort_order = 9, width = 120  WHERE table_code = 'sys_user' AND field = 'entryDate';
UPDATE sys_column_meta SET sort_order = 10, width = 120 WHERE table_code = 'sys_user' AND field = 'leaveDate';
UPDATE sys_column_meta SET sort_order = 11, show_in_list = 0, show_in_form = 0, searchable = 0 WHERE table_code = 'sys_user' AND field = 'avatar';
UPDATE sys_column_meta SET sort_order = 12, show_in_list = 1, show_in_form = 0, searchable = 0, width = 150 WHERE table_code = 'sys_user' AND field = 'loginIp';
UPDATE sys_column_meta SET sort_order = 13, width = 160 WHERE table_code = 'sys_user' AND field = 'loginDate';
UPDATE sys_column_meta SET sort_order = 14, show_in_list = 0, show_in_form = 0, searchable = 0, width = 160 WHERE table_code = 'sys_user' AND field = 'pwdUpdateDate';
UPDATE sys_column_meta SET sort_order = 15, width = 100, show_in_list = 0, show_in_form = 0, searchable = 0 WHERE table_code = 'sys_user' AND field = 'createBy';
UPDATE sys_column_meta SET sort_order = 16, width = 160 WHERE table_code = 'sys_user' AND field = 'createTime';
UPDATE sys_column_meta SET sort_order = 17, width = 100, show_in_list = 0, show_in_form = 0, searchable = 0 WHERE table_code = 'sys_user' AND field = 'updateBy';
UPDATE sys_column_meta SET sort_order = 18, width = 160, show_in_list = 0, show_in_form = 0, searchable = 0 WHERE table_code = 'sys_user' AND field = 'updateTime';
UPDATE sys_column_meta SET sort_order = 19, width = NULL, show_in_list = 0, show_in_form = 1, searchable = 0 WHERE table_code = 'sys_user' AND field = 'remarks';
UPDATE sys_column_meta SET sort_order = 20, show_in_list = 0, show_in_form = 0, searchable = 0, width = NULL WHERE table_code = 'sys_user' AND field = 'isDeleted';
