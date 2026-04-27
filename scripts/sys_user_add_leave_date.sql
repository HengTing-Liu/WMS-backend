-- sys_user：在 status 后增加「离职日期」列（已存在可跳过或改 IF NOT EXISTS 由 DBA 执行）
-- MySQL 5.7+

ALTER TABLE sys_user
  ADD COLUMN leave_date date NULL COMMENT '离职日期' AFTER `status`;
