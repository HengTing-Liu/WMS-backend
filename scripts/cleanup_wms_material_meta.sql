-- ============================================
-- 清理 wms_material 残留元数据
-- 说明：wms_material 表已废弃，物料档案统一使用 sys_material 表
-- 执行日期：2026-04-13
-- ============================================

-- 1. 删除表级元数据
DELETE FROM sys_table_meta WHERE table_code = 'wms_material';

-- 2. 删除字段元数据
DELETE FROM sys_column_meta WHERE table_code = 'wms_material';

-- 3. 删除操作按钮配置
DELETE FROM sys_table_operation WHERE table_code = 'wms_material';

-- ============================================
-- 验证查询
-- ============================================
-- SELECT COUNT(*) AS remain FROM sys_table_meta WHERE table_code = 'wms_material';
-- SELECT COUNT(*) AS remain FROM sys_column_meta WHERE table_code = 'wms_material';
-- SELECT COUNT(*) AS remain FROM sys_table_operation WHERE table_code = 'wms_material';
