-- ========================================================
-- 数据权限模块数据库表结构
-- 作者: backend2
-- 日期: 2026-03-20
-- ========================================================

-- --------------------------------------------------------
-- 1. 数据权限字段配置表
-- 用于配置每张表的数据权限控制字段
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `sys_data_permission_field` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `table_code` varchar(100) NOT NULL COMMENT '表编码（关联sys_table_meta.table_code）',
    `table_name` varchar(200) DEFAULT NULL COMMENT '表名称',
    `dept_field` varchar(100) DEFAULT 'dept_id' COMMENT '部门字段名',
    `warehouse_field` varchar(100) DEFAULT 'warehouse_code' COMMENT '仓库字段名',
    `user_field` varchar(100) DEFAULT 'create_by' COMMENT '用户字段名',
    `company_field` varchar(100) DEFAULT 'company' COMMENT '公司字段名',
    `enable_dept` tinyint(1) DEFAULT '1' COMMENT '是否启用部门权限（0-否 1-是）',
    `enable_warehouse` tinyint(1) DEFAULT '0' COMMENT '是否启用仓库权限（0-否 1-是）',
    `enable_self` tinyint(1) DEFAULT '0' COMMENT '是否启用本人权限（0-否 1-是）',
    `enable_company` tinyint(1) DEFAULT '0' COMMENT '是否启用公司权限（0-否 1-是）',
    `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-正常 1-停用）',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_table_code` (`table_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据权限字段配置表';

-- --------------------------------------------------------
-- 2. 用户仓库权限关联表
-- 用于配置用户可以访问的仓库
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `sys_user_warehouse` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `warehouse_code` varchar(100) NOT NULL COMMENT '仓库编码',
    `warehouse_name` varchar(200) DEFAULT NULL COMMENT '仓库名称',
    `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-正常 1-停用）',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_warehouse` (`user_id`, `warehouse_code`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_warehouse_code` (`warehouse_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户仓库权限关联表';

-- --------------------------------------------------------
-- 3. 角色仓库权限关联表
-- 用于配置角色可以访问的仓库
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `sys_role_warehouse` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `warehouse_code` varchar(100) NOT NULL COMMENT '仓库编码',
    `warehouse_name` varchar(200) DEFAULT NULL COMMENT '仓库名称',
    `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-正常 1-停用）',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_warehouse` (`role_id`, `warehouse_code`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_warehouse_code` (`warehouse_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色仓库权限关联表';

-- --------------------------------------------------------
-- 4. 初始化仓库相关表的权限配置
-- --------------------------------------------------------

-- sys_warehouse 表权限配置
INSERT INTO `sys_data_permission_field` 
(`table_code`, `table_name`, `dept_field`, `warehouse_field`, `user_field`, `company_field`, 
 `enable_dept`, `enable_warehouse`, `enable_self`, `enable_company`, `status`, `remark`)
VALUES 
('sys_warehouse', '仓库表', 'dept_code', 'warehouse_code', 'create_by', 'company', 1, 1, 0, 0, 0, '仓库主表，启用部门和仓库权限');

-- sys_warehouse_receive 表权限配置
INSERT INTO `sys_data_permission_field` 
(`table_code`, `table_name`, `dept_field`, `warehouse_field`, `user_field`, `company_field`, 
 `enable_dept`, `enable_warehouse`, `enable_self`, `enable_company`, `status`, `remark`)
VALUES 
('sys_warehouse_receive', '仓库收货人表', 'dept_code', 'warehouse_code', 'create_by', 'company', 1, 1, 0, 0, 0, '仓库收货人表，启用仓库权限');

-- --------------------------------------------------------
-- 5. 数据权限范围扩展（sys_role.data_scope字段说明）
-- --------------------------------------------------------
/*
数据范围（data_scope）：
1 = 所有数据权限
2 = 自定义数据权限
3 = 本部门数据权限
4 = 本部门及以下数据权限
5 = 仅本人数据权限
6 = 指定仓库数据权限（新增）
7 = 指定公司数据权限（新增）
*/

-- 可选：扩展sys_role表data_scope字段注释
-- ALTER TABLE `sys_role` 
-- MODIFY COLUMN `data_scope` varchar(1) DEFAULT '1' 
-- COMMENT '数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限；6：指定仓库数据权限）';
