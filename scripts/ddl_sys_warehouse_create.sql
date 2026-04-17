-- ============================================
-- 仓库档案表 sys_warehouse
-- 表名: sys_warehouse
-- 创建时间: 2026-04-13
-- ============================================

DROP TABLE IF EXISTS sys_warehouse;

CREATE TABLE sys_warehouse (
    id                  BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    warehouse_type      VARCHAR(50)      DEFAULT NULL COMMENT '仓库类型：LOCAL-本地，OUTSOURCED-外协，PRODUCTION-生产，RECEIVING-收货集散点',
    warehouse_code      VARCHAR(50)      NOT NULL COMMENT '仓库编码',
    warehouse_name      VARCHAR(100)     NOT NULL COMMENT '仓库名称',
    temperature_zone    VARCHAR(50)      DEFAULT NULL COMMENT '温度分区',
    quality_zone        VARCHAR(50)      DEFAULT NULL COMMENT '质量分区',
    employee_code       VARCHAR(50)      DEFAULT NULL COMMENT '责任人工号',
    employee_name       VARCHAR(100)     DEFAULT NULL COMMENT '责任人',
    dept_code           VARCHAR(50)      DEFAULT NULL COMMENT '责任部门编号',
    dept_name_full_path VARCHAR(500)     DEFAULT NULL COMMENT '责任部门全路径',
    erp_company_code    VARCHAR(50)      DEFAULT NULL COMMENT 'ERP公司编码',
    erp_company_name    VARCHAR(100)     DEFAULT NULL COMMENT 'ERP公司名称',
    erp_warehouse_code  VARCHAR(50)      DEFAULT NULL COMMENT 'ERP仓库编码',
    erp_location_code   VARCHAR(50)      DEFAULT NULL COMMENT 'ERP货位编码',
    is_enabled          TINYINT(1)      DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
    is_deleted          TINYINT(1)      DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    create_dept         BIGINT          DEFAULT 0 COMMENT '创建部门',
    create_by           VARCHAR(64)     DEFAULT '' COMMENT '创建者',
    create_time         DATETIME        DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT '' COMMENT '更新者',
    update_time         DATETIME        DEFAULT '1000-01-01 00:00:00' COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT '' COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_warehouse_code (warehouse_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库档案表';