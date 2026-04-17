-- ============================================================
-- 迁移脚本：删除库位表中的 grid_config 字段
-- 迁移日期：2026-04-16
-- 说明：移除 grid_config 字段及相关功能
-- ============================================================

-- ========== 步骤1：备份数据（可选） ==========
-- 如需备份，请先执行以下SQL导出grid_config数据
-- SELECT id, grid_config INTO OUTFILE '/tmp/location_grid_config_backup.csv'
-- FROM inv_location WHERE grid_config IS NOT NULL;

-- ========== 步骤2：删除 grid_config 字段 ==========
ALTER TABLE inv_location
  DROP COLUMN grid_config;

-- ========== 步骤3：验证变更 ==========
-- 验证字段是否已删除
SHOW COLUMNS FROM inv_location LIKE 'grid_config';
-- 应返回空结果集

-- ========== 回滚脚本（如需要恢复字段） ==========
-- ALTER TABLE inv_location
--   ADD COLUMN grid_config VARCHAR(255) COMMENT '网格配置，如 "4x4", "8x12"' AFTER specification;

-- ========== 迁移完成 ==========
-- 说明：
-- 1. 已删除 inv_location.grid_config 字段
-- 2. 后端已移除 WmsLocationGridConfig 及相关类
-- 3. 前端已移除网格配置相关功能
-- 4. 执行此脚本前请确保：
--   a) 后端代码已部署或同步更新
--   b) 前端代码已更新
--   c) 如有需要，先备份数据
