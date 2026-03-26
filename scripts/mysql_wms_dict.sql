-- =============================================
-- 字典管理表 (WMS Dict)
-- WMS0070 字典管理
-- =============================================

CREATE TABLE IF NOT EXISTS wms_dict (
    -- 主键
    id                  BIGINT UNSIGNED         PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',

    -- 字典基本信息
    dict_code           VARCHAR(100)            NOT NULL COMMENT '字典编码（业务标识，唯一）',
    dict_name           VARCHAR(100)            NOT NULL COMMENT '字典名称',
    dict_type           VARCHAR(20)             DEFAULT 'custom' COMMENT '字典类型：system=系统字典，custom=自定义',

    -- 状态
    is_enabled          TINYINT(1)              DEFAULT 1 COMMENT '是否启用：0=禁用，1=启用',

    -- 系统字段
    is_deleted          TINYINT(1)              DEFAULT 0 COMMENT '是否删除：0=正常，1=已删除',
    remark              VARCHAR(500)            NULL COMMENT '备注',
    create_by           VARCHAR(50)             NULL COMMENT '创建人',
    create_time         DATETIME                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(50)             NULL COMMENT '修改人',
    update_time         DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',

    -- 索引
    UNIQUE KEY uk_dict_code (dict_code),
    KEY idx_dict_type (dict_type),
    KEY idx_is_enabled (is_enabled),
    KEY idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典管理表';
