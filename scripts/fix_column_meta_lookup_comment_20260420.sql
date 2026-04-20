/*!40101 SET NAMES utf8mb4 */;
ALTER TABLE sys_column_meta
    MODIFY COLUMN ref_table_code  VARCHAR(64) NULL COMMENT '关联表tableCode(lookup虚拟列用)',
    MODIFY COLUMN ref_match_field VARCHAR(64) NULL COMMENT '关联表匹配字段(snake_case)',
    MODIFY COLUMN ref_target_field VARCHAR(64) NULL COMMENT '关联表展示字段(snake_case)',
    MODIFY COLUMN ref_local_field VARCHAR(64) NULL COMMENT '当前表外键字段(snake_case),空则取field';
