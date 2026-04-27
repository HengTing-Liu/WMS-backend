-- sys_user：新增 name（真实姓名）字段，插在 login_name 之后
-- MySQL 5.7+

ALTER TABLE sys_user
  ADD COLUMN name VARCHAR(100) NULL COMMENT '真实姓名' AFTER `login_name`;
