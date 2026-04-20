-- ============================================
-- 流水号表新增字段：apply_form_field（应用表单字段）
-- 执行时间：2026-04-17
-- 说明：在 is_enabled 和 usage_scope 之间添加 apply_form_field 字段
-- ============================================

-- 1. 添加新字段（在 is_enabled 之后，usage_scope 之前）
ALTER TABLE `sys_serial_number`
ADD COLUMN `apply_form_field` varchar(100) DEFAULT NULL COMMENT '应用表单字段（指定该流水号规则应用在哪个表单字段）' AFTER `is_enabled`;

-- 2. 验证字段是否添加成功
-- SELECT id, name, prefix, is_enabled, apply_form_field, usage_scope FROM sys_serial_number LIMIT 5;

-- 3. 更新表结构文档说明
-- 字段顺序：id -> name -> prefix -> number_type -> digit_length -> suffix -> start_value 
--       -> reset_rule -> current_value -> is_enabled -> apply_form_field -> usage_scope -> remark
