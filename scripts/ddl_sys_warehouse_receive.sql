-- ============================================
-- 基础设置模块 - 仓库收货信息表 DDL
-- 表前缀: sys_ (基础信息模块)
-- 更新日期: 2026-03-18
-- ============================================

CREATE TABLE sys_warehouse_receiver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    warehouse_code VARCHAR(50) NOT NULL COMMENT '仓库编码（关联sys_warehouse）',
    consignee VARCHAR(100) NOT NULL COMMENT '收货人姓名',
    phone_number VARCHAR(20) NOT NULL COMMENT '手机号码',
    country VARCHAR(50) DEFAULT '中国' COMMENT '国家',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    detailed_address VARCHAR(500) NOT NULL COMMENT '详细地址',
    postal_code VARCHAR(20) COMMENT '邮政编码',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认：0-否 1-是',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(64) COMMENT '创建人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by VARCHAR(64) COMMENT '更新人',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-否 1-是',
    
    KEY idx_warehouse_code (warehouse_code),
    KEY idx_is_default (is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库收货信息表';

-- ============================================
-- 更新说明（2026-03-18）
-- ============================================
-- 1. 表名: sys_warehouse_receiver (与前端对齐)
-- 2. 字段: province, city, district (与前端对齐，分开字段)
-- 3. 删除: province_city, district_street (旧合并字段)
-- 4. 约束: consignee, phone_number, detailed_address 设为 NOT NULL
-- 5. 索引: idx_warehouse_code, idx_is_default
-- ============================================
