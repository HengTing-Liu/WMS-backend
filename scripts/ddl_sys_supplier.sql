-- ============================================
-- 基础设置模块 - 供应商表 DDL
-- 表前缀: sys_ (基础信息模块)
-- 创建日期: 2026-03-26
-- ============================================

CREATE TABLE sys_supplier (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    supplier_code VARCHAR(50) NOT NULL COMMENT '供应商编码',
    supplier_name VARCHAR(200) NOT NULL COMMENT '供应商名称',
    contact_person VARCHAR(100) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_mobile VARCHAR(20) COMMENT '手机号码',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    address VARCHAR(500) COMMENT '详细地址',
    status INT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-否 1-是',

    UNIQUE KEY uk_supplier_code (supplier_code),
    KEY idx_status (status),
    KEY idx_supplier_name (supplier_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- ============================================
-- 更新说明（2026-03-26）
-- ============================================
-- 1. 表名: sys_supplier
-- 2. 字段对齐 Supplier.java 实体类
-- 3. supplier_code 设唯一约束（uk_supplier_code）
-- 4. 索引: idx_status, idx_supplier_name
-- 5. del_flag 替代 is_deleted，与实体类 delFlag 字段对齐
-- ============================================
