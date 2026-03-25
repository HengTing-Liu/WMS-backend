-- 数据库初始化脚本
-- 请根据实际环境修改以下配置后执行

-- 创建数据库（如不存在）
CREATE DATABASE IF NOT EXISTS wms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE wms;

-- ============================================
-- 后续表结构变更请在此添加 SQL
-- ============================================

-- 示例：添加字典表（根据实际需求修改）
-- CREATE TABLE IF NOT EXISTS sys_dict_type (
--     dict_id BIGINT PRIMARY KEY AUTO_INCREMENT,
--     dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
--     dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
--     status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
--     create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
--     create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--     update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
--     update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
--     remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
--     UNIQUE KEY uk_dict_type (dict_type)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ============================================
-- 2026-03-11 枚举表字段修复
-- ============================================

-- 枚举定义表添加逻辑删除字段
ALTER TABLE sys_enum_define ADD COLUMN is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除 0-否 1-是';
ALTER TABLE sys_enum_define ADD COLUMN update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者';
ALTER TABLE sys_enum_define ADD COLUMN update_time DATETIME DEFAULT NULL COMMENT '更新时间';

-- 枚举值表添加逻辑删除字段
ALTER TABLE sys_enum_item ADD COLUMN is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除 0-否 1-是';
ALTER TABLE sys_enum_item ADD COLUMN update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者';
ALTER TABLE sys_enum_item ADD COLUMN update_time DATETIME DEFAULT NULL COMMENT '更新时间';
