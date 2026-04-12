-- ============================================================
-- 低代码发布模块 - DDL 脚本
-- 创建日期: 2026-04-11
-- ============================================================

-- ----------------------------------------------------------
-- 1. sys_meta_publish：发布主记录
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS sys_meta_publish (
    id              BIGINT AUTO_INCREMENT  COMMENT '主键ID' PRIMARY KEY,
    publish_code    VARCHAR(64)            COMMENT '发布编码（UUID）',
    table_code      VARCHAR(100)           COMMENT '目标表编码',
    table_name      VARCHAR(200)           COMMENT '目标表名称',
    version         INT          DEFAULT 1 COMMENT '发布版本号',
    status          VARCHAR(20)            COMMENT '发布状态：PENDING/RUNNING/SUCCESS/FAILED/ROLLED_BACK',
    total_sqls      INT          DEFAULT 0 COMMENT 'SQL总数',
    success_sqls    INT          DEFAULT 0 COMMENT '成功SQL数',
    failed_sqls     INT          DEFAULT 0 COMMENT '失败SQL数',
    error_message   TEXT                   COMMENT '失败错误信息',
    snapshot_data   LONGTEXT               COMMENT '发布前快照JSON',
    forced          TINYINT(1)   DEFAULT 0 COMMENT '是否强制执行（忽略破坏性警告）',
    publish_by      VARCHAR(64)            COMMENT '发布人',
    publish_by_name VARCHAR(100)           COMMENT '发布人姓名',
    publish_time    DATETIME               COMMENT '发布时间',
    remark          VARCHAR(500)           COMMENT '备注',
    created_at      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_table_code (table_code),
    INDEX idx_status (status),
    INDEX idx_publish_by (publish_by),
    INDEX idx_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='低代码元数据发布主记录';

-- ----------------------------------------------------------
-- 2. sys_meta_publish_detail：发布SQL执行明细
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS sys_meta_publish_detail (
    id              BIGINT AUTO_INCREMENT  COMMENT '主键ID' PRIMARY KEY,
    publish_id      BIGINT                  COMMENT '发布记录ID',
    seq             INT                     COMMENT '执行顺序',
    sql_type        VARCHAR(20)             COMMENT 'SQL类型：CREATE_TABLE/ALTER_ADD/ALTER_MODIFY/ALTER_DROP/CREATE_INDEX/DROP_INDEX',
    sql_text        TEXT                    COMMENT 'SQL语句',
    risk_level      VARCHAR(20)             COMMENT '风险等级：SAFE/WARNING/DANGER',
    execution_time  INT          DEFAULT 0  COMMENT '执行耗时（毫秒）',
    result_status   VARCHAR(20)             COMMENT '执行结果：PENDING/SUCCESS/FAILED/SKIPPED',
    error_message   TEXT                    COMMENT '错误信息',
    executed_at     DATETIME                COMMENT '执行时间',
    INDEX idx_publish_id (publish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='低代码发布SQL执行明细';

-- ----------------------------------------------------------
-- 3. sys_meta_snapshot：发布快照（可选扩展，支持回滚）
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS sys_meta_publish_snapshot (
    id              BIGINT AUTO_INCREMENT  COMMENT '主键ID' PRIMARY KEY,
    publish_id      BIGINT                  COMMENT '关联发布记录ID',
    table_code      VARCHAR(100)           COMMENT '表编码',
    snapshot_type   VARCHAR(20)            COMMENT '快照类型：BEFORE/AFTER',
    table_meta_json LONGTEXT               COMMENT '表元数据JSON',
    column_meta_json LONGTEXT               COMMENT '字段元数据JSON',
    operation_meta_json LONGTEXT           COMMENT '操作按钮元数据JSON',
    created_at      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_publish_id (publish_id),
    INDEX idx_table_code (table_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='低代码发布快照（支持回滚）';
