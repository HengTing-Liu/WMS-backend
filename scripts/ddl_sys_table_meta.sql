-- ============================================
-- 动态表单模块 - 元数据表DDL
-- 表前缀: sys_ (系统模块)
-- 更新日期: 2026-03-20
-- ============================================

-- --------------------------------------------
-- 1. 表元数据表 (sys_table_meta)
-- 描述: 存储动态表单的表级元数据配置
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS sys_table_meta (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    table_code VARCHAR(100) NOT NULL COMMENT '表标识(小写下划线)',
    table_name VARCHAR(200) NOT NULL COMMENT '表名称',
    module VARCHAR(50) DEFAULT 'base' COMMENT '所属模块(base/wms/sys)',
    entity_class VARCHAR(500) COMMENT '实体类全路径',
    service_class VARCHAR(500) COMMENT 'Service类全路径',
    permission_code VARCHAR(200) COMMENT '权限码',
    page_size INT DEFAULT 20 COMMENT '默认分页大小',
    is_tree TINYINT DEFAULT 0 COMMENT '是否树形表(0-否,1-是)',
    status TINYINT DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    remark VARCHAR(500) COMMENT '备注',
    is_deleted_column VARCHAR(50) DEFAULT 'isdeleted' COMMENT '逻辑删除字段名(sys_user=del_flag, others=isdeleted)',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标志(0-正常,1-删除)',

    UNIQUE KEY uk_table_code (table_code),
    KEY idx_module (module),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表元数据表';

-- --------------------------------------------
-- 2. 字段元数据表 (sys_column_meta)
-- 描述: 存储动态表单的字段级元数据配置
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS sys_column_meta (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    table_code VARCHAR(100) NOT NULL COMMENT '表标识',
    field VARCHAR(100) NOT NULL COMMENT '字段名(驼峰)',
    title VARCHAR(200) NOT NULL COMMENT '显示名称',
    data_type VARCHAR(50) DEFAULT 'string' COMMENT '数据类型(string/int/date/datetime/decimal/boolean)',
    form_type VARCHAR(50) DEFAULT 'input' COMMENT '表单类型(input/select/date/datetime/textarea/number/switch/radio/checkbox/cascader)',
    dict_type VARCHAR(100) COMMENT '字典类型',
    linkage_json TEXT COMMENT '联动配置JSON',
    is_show_in_list TINYINT DEFAULT 1 COMMENT '列表显示(0-否,1-是)',
    is_show_in_form TINYINT DEFAULT 1 COMMENT '表单显示(0-否,1-是)',
    is_searchable TINYINT DEFAULT 0 COMMENT '可搜索(0-否,1-是)',
    is_sortable TINYINT DEFAULT 0 COMMENT '可排序(0-否,1-是)',
    is_required TINYINT DEFAULT 0 COMMENT '必填(0-否,1-是)',
    width INT COMMENT '列宽(px)',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    rules_json TEXT COMMENT '校验规则JSON',
    placeholder VARCHAR(200) COMMENT '占位提示',
    default_value VARCHAR(500) COMMENT '默认值',
    status TINYINT DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标志(0-正常,1-删除)',
    
    UNIQUE KEY uk_table_field (table_code, field),
    KEY idx_table_code (table_code),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字段元数据表';

-- --------------------------------------------
-- 3. 表操作按钮配置表 (sys_table_operation)
-- 描述: 存储动态表单的操作按钮配置
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS sys_table_operation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    table_code VARCHAR(100) NOT NULL COMMENT '表标识',
    operation_code VARCHAR(100) NOT NULL COMMENT '操作标识',
    operation_name VARCHAR(200) NOT NULL COMMENT '操作名称',
    operation_type VARCHAR(50) DEFAULT 'button' COMMENT '操作类型(button/link/icon)',
    button_type VARCHAR(50) DEFAULT 'default' COMMENT '按钮类型(primary/default/dashed/text/link)',
    icon VARCHAR(100) COMMENT '图标(Ant Design Vue图标名)',
    permission VARCHAR(200) COMMENT '权限标识',
    position VARCHAR(50) DEFAULT 'toolbar' COMMENT '位置(toolbar-工具栏/row-行内)',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    remark VARCHAR(500) COMMENT '备注',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标志(0-正常,1-删除)',
    
    UNIQUE KEY uk_table_operation (table_code, operation_code),
    KEY idx_table_code (table_code),
    KEY idx_position (position),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表操作按钮配置表';
