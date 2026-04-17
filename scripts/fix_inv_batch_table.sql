-- ============================================
-- 批次档案表修复脚本
-- 用于修复 inv_batch 表缺少的字段
-- 日期: 2026-04-17
-- ============================================

-- 1. 检查并添加 inv_batch 表（如果不存在）
CREATE TABLE IF NOT EXISTS `inv_batch` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `material_id` bigint DEFAULT NULL COMMENT '物料ID',
  `material_code` varchar(50) NOT NULL COMMENT '物料编码',
  `batch_no` varchar(50) NOT NULL COMMENT '批次号',
  `supplier_batch_no` varchar(50) DEFAULT NULL COMMENT '供应商批号',
  `supplier_brand` varchar(100) DEFAULT NULL COMMENT '供应商品牌',
  `supplier_item_no` varchar(50) DEFAULT NULL COMMENT '供应商货号',
  `purify_no` varchar(50) DEFAULT NULL COMMENT '纯化编号',
  `clone_no` varchar(50) DEFAULT NULL COMMENT '克隆号',
  `concentration` varchar(50) DEFAULT NULL COMMENT '浓度',
  `concentration_update_time` datetime DEFAULT NULL COMMENT '浓度更新时间',
  `expire_date` date DEFAULT NULL COMMENT '失效期',
  `qc_data` text COMMENT '质检数据',
  `coa_link` varchar(500) DEFAULT NULL COMMENT 'COA链接',
  `inbound_date` date DEFAULT NULL COMMENT '入库日期',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `project_no` varchar(50) DEFAULT NULL COMMENT '项目编号',
  `naked_finish_code` varchar(50) DEFAULT NULL COMMENT '裸成品物料编码',
  `naked_finish_batch` varchar(50) DEFAULT NULL COMMENT '裸成品批次',
  `buffer_solution` varchar(200) DEFAULT NULL COMMENT '缓冲液',
  `erp_sync_remark` varchar(500) DEFAULT NULL COMMENT 'ERP同步备注',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态（0-停用，1-启用）',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0-正常，1-删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_batch_no` (`batch_no`, `is_deleted`),
  KEY `idx_material_code` (`material_code`),
  KEY `idx_expire_date` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批次档案表（低代码平台）';

-- 2. 为已有 inv_batch 表添加缺少的字段
-- 添加供应商品牌字段
ALTER TABLE `inv_batch` 
  ADD COLUMN IF NOT EXISTS `supplier_brand` varchar(100) DEFAULT NULL COMMENT '供应商品牌' AFTER `supplier_batch_no`;

-- 添加供应商货号字段
ALTER TABLE `inv_batch` 
  ADD COLUMN IF NOT EXISTS `supplier_item_no` varchar(50) DEFAULT NULL COMMENT '供应商货号' AFTER `supplier_brand`;

-- 添加状态字段
ALTER TABLE `inv_batch` 
  ADD COLUMN IF NOT EXISTS `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态（0-停用，1-启用）' AFTER `erp_sync_remark`;

-- 添加物料ID字段（如果 material_id 列不存在）
-- 注意：MySQL 8.0+ 支持 IF NOT EXISTS

-- 3. 添加示例数据（可选）
-- INSERT INTO `inv_batch` (`material_code`, `batch_no`, `supplier_batch_no`, `supplier_brand`, `supplier_item_no`, `purify_no`, `clone_no`, `concentration`, `expire_date`, `status`)
-- VALUES ('MAT001', 'BATCH20260401001', 'SUP-B001', '品牌A', 'S001', 'P001', 'C001', '10mg/ml', '2027-04-01', 1);