-- =============================================
-- 权限管理表 (WMS Permission Master)
-- WMS0020 权限管理
-- =============================================

CREATE TABLE IF NOT EXISTS sys_permission (
    -- 主键
    permission_id       BIGINT UNSIGNED         PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',

    -- 权限基本信息
    permission_name     VARCHAR(100)            NOT NULL COMMENT '权限名称',
    permission_code     VARCHAR(100)            NOT NULL COMMENT '权限编码（唯一标识）',
    permission_type     INT UNSIGNED            DEFAULT 1 COMMENT '权限类型：1=菜单，2=按钮，3=接口',

    -- 层级关系
    parent_id           BIGINT UNSIGNED         DEFAULT 0 COMMENT '上级权限ID',
    parent_name         VARCHAR(100)            NULL COMMENT '上级权限名称',

    -- 显示与路由
    order_num           INT UNSIGNED            DEFAULT 0 COMMENT '显示顺序',
    path                VARCHAR(200)            NULL COMMENT '路由地址',
    component           VARCHAR(255)            NULL COMMENT '组件路径',
    query               VARCHAR(500)            NULL COMMENT '路由参数',

    -- 状态
    status              VARCHAR(1)              DEFAULT '0' COMMENT '权限状态：0=正常，1=停用',

    -- 系统字段
    del_flag            VARCHAR(1)              DEFAULT '0' COMMENT '删除标志（0代表存在，2代表删除）',
    remark              VARCHAR(500)            NULL COMMENT '备注',
    create_by           VARCHAR(50)             NULL COMMENT '创建人',
    create_time         DATETIME                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(50)             NULL COMMENT '修改人',
    update_time         DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',

    -- 索引
    UNIQUE KEY uk_permission_code (permission_code),
    INDEX idx_parent_id (parent_id),
    INDEX idx_permission_type (permission_type),
    INDEX idx_status (status),
    INDEX idx_del_flag (del_flag),
    INDEX idx_order_num (order_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限管理表';
