-- 枚举定义表添加逻辑删除字段
ALTER TABLE sys_enum_define ADD COLUMN is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除 0-否 1-是';
ALTER TABLE sys_enum_define ADD COLUMN update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者';
ALTER TABLE sys_enum_define ADD COLUMN update_time DATETIME DEFAULT NULL COMMENT '更新时间';

-- 枚举值表添加逻辑删除字段
ALTER TABLE sys_enum_item ADD COLUMN is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除 0-否 1-是';
ALTER TABLE sys_enum_item ADD COLUMN update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者';
ALTER TABLE sys_enum_item ADD COLUMN update_time DATETIME DEFAULT NULL COMMENT '更新时间';
