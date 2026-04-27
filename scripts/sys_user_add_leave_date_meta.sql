-- 已有库在执行 sys_user_add_leave_date.sql 后，为低代码 sys_column_meta 增加「离职日期」并顺延后续列排序（只执行一次；失败或重复执行可能导致 sort_order 乱序，务必备份或从库中核对后再跑）
-- 若已用完整 init_meta_data.sql 全量重刷，可跳过本脚本
--
-- 与 Java 一致：sys_column_meta 时间列为 create_time / update_time（不是 created_at / updated_at，见 ColumnMetaMapper.xml）

-- 1) 在尚未存在 leaveDate 行时，为 sort_order >= 10 的列让出位置（与步骤 2 的「将插入」成对，避免行已存在后再次顺延）
-- 若本句已成功执行、步骤 2 失败：请不要重复执行全脚本，需从库备份恢复或人工修正 sort_order 后仅再执行「步骤 2 的 INSERT」
UPDATE sys_column_meta
SET sort_order = sort_order + 1, update_time = NOW()
WHERE table_code = 'sys_user'
  AND (field IS NULL OR field <> 'leaveDate')
  AND sort_order >= 10
  AND NOT EXISTS (
      SELECT 1 FROM sys_column_meta c
      WHERE c.table_code = 'sys_user' AND c.field = 'leaveDate'
  );

-- 2) 仅当尚不存在 (sys_user, leaveDate) 时插入（不依赖 UK；与 init_meta_data 中 sys_user 行结构一致）
INSERT INTO sys_column_meta (
    table_code, field, title, data_type, form_type,
    show_in_list, show_in_form, searchable, is_sortable, required,
    width, sort_order, status, create_by, create_time, column_name
)
SELECT
    'sys_user', 'leaveDate', '离职日期', 'date', 'date',
    1, 1, 0, 0, 0,
    120, 10, 1, 'system', NOW(), 'leave_date'
FROM (SELECT 1) AS t
WHERE NOT EXISTS (
    SELECT 1
    FROM sys_column_meta c
    WHERE c.table_code = 'sys_user'
      AND c.field = 'leaveDate'
);
