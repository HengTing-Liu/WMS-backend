-- =============================================
-- 库位档案表 (WMS Location Master)
-- 支持递归树形结构 + 独占/共享模式
-- =============================================

CREATE TABLE inv_location (
    -- 主键
    id                  BIGINT UNSIGNED         PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    
    -- 层级关系
    parent_id           BIGINT UNSIGNED         NULL COMMENT '上级ID，根节点为NULL',
    
    -- 库位等级与类型
    location_grade      VARCHAR(50)             NOT NULL COMMENT '库位等级：Type(类型)/TypeSection(类型分区)/Container(容器)/ContainerPosition(容器内孔位)',
    location_type       VARCHAR(50)             NOT NULL COMMENT '库位类型：冰箱/层/架/行/盒/箱/孔等（可自定义）',
    location_level      TINYINT UNSIGNED        NOT NULL COMMENT '层级深度：1=根节点，2=层，3=架...',
    location_level_count TINYINT UNSIGNED       NOT NULL COMMENT '总层数（该树的最大深度）',
    
    -- 内部序号与数量
    internal_serial_no  SMALLINT UNSIGNED       NOT NULL COMMENT '同级内部序号（如第1层、第2层）',
    internal_quantity   SMALLINT UNSIGNED       NOT NULL COMMENT '同级总数量（如该层共有2个）',
    
    -- 编码与名称
    location_no         VARCHAR(100)            NOT NULL COMMENT '库位编号（业务编码）',
    location_name       VARCHAR(200)            NOT NULL COMMENT '库位名称',
    warehouse_code      VARCHAR(50)             NOT NULL COMMENT '仓库编码',
    parent_name         VARCHAR(200)            NULL COMMENT '上级名称（冗余，方便查询）',
    
    -- 存储属性
    storage_mode        VARCHAR(20)             NULL COMMENT '存储模式：Exclusive(独占模式)/Shared(共享模式)',
    specification       VARCHAR(50)             NULL COMMENT '规格：如 4x4、1x1、96孔、48孔等',
    
    -- 状态与排序
    is_use              TINYINT(1)              DEFAULT 0 COMMENT '是否使用：0=空闲，1=占用（仅对孔位/独占模式有效）',
    location_sort_no    VARCHAR(50)             NOT NULL COMMENT '排序号（用于树形展示排序）',
    location_fullpath_name VARCHAR(500)         NOT NULL COMMENT '全路径名称（如：卧式冰箱_层1/2_架1/2_行1/2_BOX001_A01）',
    
    -- 扩展信息
    capacity_total      INT UNSIGNED            DEFAULT 0 COMMENT '总容量（子节点数量或孔位数）',
    capacity_used       INT UNSIGNED            DEFAULT 0 COMMENT '已用容量（仅独占模式计算）',
    capacity_rate       DECIMAL(5,2)            DEFAULT 0.00 COMMENT '占用率（%）',
    
    -- 系统字段
    is_deleted          TINYINT(1)              DEFAULT 0 COMMENT '是否删除：0=正常，1=已删除',
    remarks             VARCHAR(500)            NULL COMMENT '备注',
    create_by           VARCHAR(50)             NULL COMMENT '创建人',
    create_time         DATETIME                DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by           VARCHAR(50)             NULL COMMENT '修改人',
    update_time         DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    
    -- 索引
    INDEX idx_parent_id (parent_id),
    INDEX idx_warehouse_code (warehouse_code),
    INDEX idx_location_no (location_no),
    INDEX idx_location_grade (location_grade),
    INDEX idx_storage_mode (storage_mode),
    INDEX idx_is_use (is_use),
    INDEX idx_is_deleted (is_deleted),
    INDEX idx_location_sort (location_sort_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库位档案表';


-- =============================================
-- 库位类型配置表（用于动态配置层级类型）
-- =============================================
CREATE TABLE inv_location_type_config (
    id                  BIGINT UNSIGNED         PRIMARY KEY AUTO_INCREMENT,
    type_code           VARCHAR(50)             NOT NULL COMMENT '类型编码',
    type_name           VARCHAR(100)            NOT NULL COMMENT '类型名称',
    type_category       VARCHAR(50)             NOT NULL COMMENT '类型分类：Type/TypeSection/Container/ContainerPosition',
    level_min           TINYINT UNSIGNED        NOT NULL COMMENT '最小层级',
    level_max           TINYINT UNSIGNED        NOT NULL COMMENT '最大层级',
    can_have_children   TINYINT(1)              DEFAULT 1 COMMENT '是否可有子节点',
    default_spec        VARCHAR(50)             NULL COMMENT '默认规格',
    icon                VARCHAR(100)            NULL COMMENT '图标',
    sort_order          INT UNSIGNED            DEFAULT 0 COMMENT '排序',
    is_enabled          TINYINT(1)              DEFAULT 1 COMMENT '是否启用',
    create_time         DATETIME                DEFAULT CURRENT_TIMESTAMP,
    update_time         DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE INDEX uk_type_code (type_code),
    INDEX idx_category (type_category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库位类型配置表';


-- =============================================
-- 初始化库位类型数据
-- =============================================
INSERT INTO inv_location_type_config (type_code, type_name, type_category, level_min, level_max, can_have_children, default_spec, icon, sort_order) VALUES
-- 根节点
('REFRIGERATOR', '冰箱', 'Type', 1, 1, 1, NULL, 'refrigerator', 1),
('ROOM', '房间', 'Type', 1, 1, 1, NULL, 'room', 2),
('SHELF', '货架', 'Type', 1, 1, 1, NULL, 'shelf', 3),

-- 类型分区
('LAYER', '层', 'TypeSection', 2, 4, 1, NULL, 'layer', 10),
('RACK', '架', 'TypeSection', 2, 4, 1, NULL, 'rack', 11),
('ROW', '行', 'TypeSection', 2, 4, 1, NULL, 'row', 12),

-- 容器
('BOX', '盒', 'Container', 5, 5, 1, '4x4', 'box', 20),
('CASE', '箱', 'Container', 5, 5, 1, '1x1', 'case', 21),
('BOTTLE', '瓶', 'Container', 5, 5, 0, '1x1', 'bottle', 22),

-- 孔位
('WELL', '孔', 'ContainerPosition', 6, 6, 0, '1x1', 'well', 30);


-- =============================================
-- 示例数据（卧式冰箱_层1/2_架1/2_行1/2_盒1_A01）
-- =============================================

-- 根节点：冰箱
INSERT INTO inv_location (location_grade, location_type, location_level, location_level_count, 
    internal_serial_no, internal_quantity, location_no, location_name, warehouse_code,
    storage_mode, specification, location_sort_no, location_fullpath_name, capacity_total)
VALUES ('Type', '冰箱', 1, 5, 1, 1, 'REF001', '卧式冰箱', 'Inven01-1', 
    NULL, NULL, 'L0001', '卧式冰箱', 2);

-- 层
INSERT INTO inv_location (parent_id, location_grade, location_type, location_level, location_level_count,
    internal_serial_no, internal_quantity, location_no, location_name, warehouse_code, parent_name,
    storage_mode, specification, location_sort_no, location_fullpath_name, capacity_total)
VALUES (1, 'TypeSection', '层', 2, 5, 1, 2, 'LAYER001', '层1/2', 'Inven01-1', '卧式冰箱',
    NULL, NULL, 'L0001001', '卧式冰箱_层1/2', 2);

-- 架
INSERT INTO inv_location (parent_id, location_grade, location_type, location_level, location_level_count,
    internal_serial_no, internal_quantity, location_no, location_name, warehouse_code, parent_name,
    storage_mode, specification, location_sort_no, location_fullpath_name, capacity_total)
VALUES (2, 'TypeSection', '架', 3, 5, 1, 2, 'RACK001', '架1/2', 'Inven01-1', '层1/2',
    NULL, NULL, 'L0001001001', '卧式冰箱_层1/2_架1/2', 2);

-- 行
INSERT INTO inv_location (parent_id, location_grade, location_type, location_level, location_level_count,
    internal_serial_no, internal_quantity, location_no, location_name, warehouse_code, parent_name,
    storage_mode, specification, location_sort_no, location_fullpath_name, capacity_total)
VALUES (3, 'TypeSection', '行', 4, 5, 1, 2, 'ROW001', '行1/2', 'Inven01-1', '架1/2',
    NULL, NULL, 'L0001001001001', '卧式冰箱_层1/2_架1/2_行1/2', 1);

-- 盒（独占模式，4x4规格，16个孔）
INSERT INTO inv_location (parent_id, location_grade, location_type, location_level, location_level_count,
    internal_serial_no, internal_quantity, location_no, location_name, warehouse_code, parent_name,
    storage_mode, specification, location_sort_no, location_fullpath_name, capacity_total, capacity_used)
VALUES (4, 'Container', '盒', 5, 5, 1, 1, 'BOX001', 'BOX001', 'Inven01-1', '行1/2',
    'Exclusive', '4x4', 'L0001001001001001', '卧式冰箱_层1/2_架1/2_行1/2_BOX001', 16, 1);

-- 孔位（独占模式下的16个孔）
INSERT INTO inv_location (parent_id, location_grade, location_type, location_level, location_level_count,
    internal_serial_no, internal_quantity, location_no, location_name, warehouse_code, parent_name,
    storage_mode, specification, is_use, location_sort_no, location_fullpath_name)
VALUES 
(5, 'ContainerPosition', '孔', 6, 5, 1, 16, 'A01', 'A01', 'Inven01-1', 'BOX001', 'Exclusive', '1x1', 1, 'L0001001001001001001', '卧式冰箱_层1/2_架1/2_行1/2_BOX001_A01'),
(5, 'ContainerPosition', '孔', 6, 5, 2, 16, 'A02', 'A02', 'Inven01-1', 'BOX001', 'Exclusive', '1x1', 0, 'L0001001001001001002', '卧式冰箱_层1/2_架1/2_行1/2_BOX001_A02'),
(5, 'ContainerPosition', '孔', 6, 5, 3, 16, 'A03', 'A03', 'Inven01-1', 'BOX001', 'Exclusive', '1x1', 0, 'L0001001001001001003', '卧式冰箱_层1/2_架1/2_行1/2_BOX001_A03'),
(5, 'ContainerPosition', '孔', 6, 5, 4, 16, 'A04', 'A04', 'Inven01-1', 'BOX001', 'Exclusive', '1x1', 0, 'L0001001001001001004', '卧式冰箱_层1/2_架1/2_行1/2_BOX001_A04');


-- =============================================
-- 视图：库位树形查询（递归CTE示例）
-- =============================================
CREATE VIEW v_location_tree AS
WITH RECURSIVE location_tree AS (
    -- 根节点
    SELECT 
        id,
        parent_id,
        location_no,
        location_name,
        location_type,
        location_level,
        location_fullpath_name,
        CAST(location_name AS CHAR(1000)) AS tree_path,
        0 AS depth
    FROM inv_location
    WHERE parent_id IS NULL AND is_deleted = 0
    
    UNION ALL
    
    -- 递归子节点
    SELECT 
        l.id,
        l.parent_id,
        l.location_no,
        l.location_name,
        l.location_type,
        l.location_level,
        l.location_fullpath_name,
        CONCAT_WS(' > ', lt.tree_path, l.location_name),
        lt.depth + 1
    FROM inv_location l
    INNER JOIN location_tree lt ON l.parent_id = lt.id
    WHERE l.is_deleted = 0
)
SELECT * FROM location_tree;
