-- ============================================
-- 仓库档案表 sys_warehouse 字段变更
-- 更新时间: 2026-04-13
-- 变更说明:
--   1. warehouse_type 字段放置在 warehouse_code 之前
--   2. 删除 company 字段，新增 erp_company_code, erp_company_name, erp_warehouse_code, erp_location_code
-- ============================================

-- 1. 删除旧表重建（保留数据的方式）

-- 1.1 创建临时表（包含新结构）
CREATE TABLE sys_warehouse_new LIKE sys_warehouse;

-- 1.2 新增 warehouse_type 字段到正确位置（warehouse_code 之前）
ALTER TABLE sys_warehouse_new
ADD COLUMN warehouse_type VARCHAR(50) DEFAULT NULL COMMENT '仓库类型：LOCAL-本地，OUTSOURCED-外协，PRODUCTION-生产，RECEIVING-收货集散点' AFTER id;

-- 1.3 新增ERP相关字段
ALTER TABLE sys_warehouse_new
ADD COLUMN erp_company_code VARCHAR(50) DEFAULT NULL COMMENT 'ERP公司编码' AFTER dept_name_full_path;

ALTER TABLE sys_warehouse_new
ADD COLUMN erp_company_name VARCHAR(100) DEFAULT NULL COMMENT 'ERP公司名称' AFTER erp_company_code;

ALTER TABLE sys_warehouse_new
ADD COLUMN erp_warehouse_code VARCHAR(50) DEFAULT NULL COMMENT 'ERP仓库编码' AFTER erp_company_name;

ALTER TABLE sys_warehouse_new
ADD COLUMN erp_location_code VARCHAR(50) DEFAULT NULL COMMENT 'ERP货位编码' AFTER erp_warehouse_code;

-- 1.4 迁移数据（选择需要保留的字段）
INSERT INTO sys_warehouse_new (
    id, warehouse_type, warehouse_code, warehouse_name, temperature_zone, quality_zone,
    employee_code, employee_name, dept_code, dept_name_full_path,
    erp_company_code, erp_company_name, erp_warehouse_code, erp_location_code,
    is_enabled, is_deleted, create_dept, create_by, create_time, update_by, update_time, remark
)
SELECT
    id, NULL AS warehouse_type, warehouse_code, warehouse_name, temperature_zone, quality_zone,
    employee_code, employee_name, dept_code, dept_name_full_path,
    company AS erp_company_code, NULL AS erp_company_name, NULL AS erp_warehouse_code, NULL AS erp_location_code,
    is_enabled, is_deleted, create_dept, create_by, create_time, update_by, update_time, remark
FROM sys_warehouse;

-- 1.5 交换表名
RENAME TABLE sys_warehouse TO sys_warehouse_old, sys_warehouse_new TO sys_warehouse;

-- 1.6 删除旧表
DROP TABLE sys_warehouse_old;
