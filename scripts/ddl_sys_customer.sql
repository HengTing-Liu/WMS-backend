-- =============================================
-- 客户档案表 (WMS Customer Master)
-- WMS0030 客户管理
-- =============================================

CREATE TABLE IF NOT EXISTS sys_customer (
    -- 主键
    id                  BIGINT UNSIGNED         PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',

    -- 客户基本信息
    customer_code       VARCHAR(50)             NOT NULL COMMENT '客户编码',
    customer_name       VARCHAR(200)            NOT NULL COMMENT '客户名称',

    -- 联系信息
    contact_person      VARCHAR(100)            NULL COMMENT '联系人',
    contact_phone       VARCHAR(20)             NULL COMMENT '联系电话',
    mobile              VARCHAR(20)             NULL COMMENT '手机号码',
    email               VARCHAR(100)            NULL COMMENT '邮箱',

    -- 地址信息
    province            VARCHAR(50)             NULL COMMENT '省份',
    city                VARCHAR(50)             NULL COMMENT '城市',
    district            VARCHAR(50)             NULL COMMENT '区县',
    address             VARCHAR(500)            NULL COMMENT '详细地址',

    -- 状态
    is_enabled          TINYINT(1)              DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',

    -- 扩展信息
    remark              VARCHAR(500)            NULL COMMENT '备注',

    -- 系统字段
    is_deleted          TINYINT(1)              DEFAULT 0 COMMENT '是否删除：0-正常 1-已删除',
    create_by           VARCHAR(50)             NULL COMMENT '创建人',
    create_time         DATETIME                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(50)             NULL COMMENT '修改人',
    update_time         DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',

    -- 索引
    UNIQUE KEY uk_customer_code (customer_code),
    INDEX idx_customer_name (customer_name),
    INDEX idx_is_enabled (is_enabled),
    INDEX idx_is_deleted (is_deleted),
    INDEX idx_province_city (province, city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户档案表';
