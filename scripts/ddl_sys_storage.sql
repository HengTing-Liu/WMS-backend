-- =============================================
-- 库区档案表 (WMS Storage Area Master)
-- WMS0050 库区管理
-- =============================================

CREATE TABLE IF NOT EXISTS sys_storage (
    -- 主键
    id                  BIGINT UNSIGNED         PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    
    -- 库区基本信息
    storage_code        VARCHAR(50)             NOT NULL COMMENT '库区编码',
    storage_name        VARCHAR(100)            NOT NULL COMMENT '库区名称',
    
    -- 所属关系
    warehouse_id        BIGINT UNSIGNED         NULL COMMENT '仓库ID',
    location_id          BIGINT UNSIGNED         NULL COMMENT '库位ID（可选，关联更细粒度的库位）',
    
    -- 库区属性
    storage_type        VARCHAR(20)             NULL COMMENT '库区类型：PLANE-平面 STEREO-立体 RACK-货架',
    capacity            INT UNSIGNED            DEFAULT 0 COMMENT '容量',
    
    -- 状态
    is_enabled          TINYINT(1)              DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
    
    -- 扩展信息
    remark              VARCHAR(500)            NULL COMMENT '备注',
    
    -- 系统字段
    isdeleted           TINYINT(1)              DEFAULT 0 COMMENT '是否删除：0-正常 1-已删除',
    create_by           VARCHAR(50)             NULL COMMENT '创建人',
    create_time         DATETIME                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(50)             NULL COMMENT '修改人',
    update_time         DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    
    -- 索引
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_location_id (location_id),
    INDEX idx_storage_code (storage_code),
    INDEX idx_storage_type (storage_type),
    INDEX idx_is_enabled (is_enabled),
    INDEX idx_isdeleted (isdeleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库区档案表';

-- 初始化数据
INSERT INTO sys_storage (storage_code, storage_name, warehouse_id, storage_type, capacity, is_enabled, create_by, create_time) VALUES
('SA001', 'A存储区', 1, 'RACK', 100, 1, 'admin', NOW()),
('SA002', 'B拣货区', 1, 'PLANE', 200, 1, 'admin', NOW()),
('SA003', 'C存储区', 2, 'STEREO', 150, 1, 'admin', NOW()),
('SA004', 'D退货区', 3, 'PLANE', 300, 1, 'admin', NOW()),
('SA005', 'E集货区', 4, 'RACK', 250, 1, 'admin', NOW());
