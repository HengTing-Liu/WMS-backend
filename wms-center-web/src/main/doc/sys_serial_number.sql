-- 系统流水号表
CREATE TABLE IF NOT EXISTS `sys_serial_number` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水号ID',
    `name` varchar(100) NOT NULL COMMENT '名称（如：采购订单流水号）',
    `prefix` varchar(20) DEFAULT NULL COMMENT '前缀（如：PO-）',
    `number_type` tinyint NOT NULL DEFAULT '1' COMMENT '数字规则：1-自增，2-随机，3-时间戳',
    `digit_length` int NOT NULL DEFAULT '4' COMMENT '流水号位数',
    `suffix` varchar(20) DEFAULT NULL COMMENT '后缀',
    `start_value` bigint NOT NULL DEFAULT '1' COMMENT '起始值',
    `reset_rule` varchar(20) DEFAULT NULL COMMENT '重置规则：DAILY-每日，MONTHLY-每月，YEARLY-每年，NONE-不重置',
    `current_value` bigint NOT NULL DEFAULT '0' COMMENT '当前值',
    `is_enabled` tinyint NOT NULL DEFAULT '1' COMMENT '是否启用：0-禁用，1-启用',
    `usage_scope` varchar(200) DEFAULT NULL COMMENT '使用范围说明',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`) USING BTREE COMMENT '名称唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统流水号表';

-- 插入示例数据
INSERT INTO `sys_serial_number` (`id`, `name`, `prefix`, `number_type`, `digit_length`, `suffix`, `start_value`, `reset_rule`, `current_value`, `is_enabled`, `usage_scope`, `remark`) VALUES
(1, '知识产权申请流水号', 'ABTK-', 2, 3, NULL, 1, 'NONE', 4, 1, '知识产权申请', NULL),
(2, '知识产权处置流水号', 'ABTK-', 2, 3, NULL, 1, 'NONE', 1, 1, '知识产权处置', NULL),
(3, '知识产权奖励流水号', 'ABTK-', 2, 3, NULL, 1, 'NONE', 36, 1, '知识产权奖励', NULL),
(4, '知识产权奖励申请流水号', 'ABTK-', 2, 3, NULL, 1, 'NONE', 2, 1, '知识产权奖励申请', NULL),
(5, '冷记采购订单流水号', 'LJ-', 2, 3, NULL, 0, NULL, 0, 1, '采购订单', NULL);
