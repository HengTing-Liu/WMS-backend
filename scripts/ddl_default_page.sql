-- 添加默认首页字段
-- 作者: backend1
-- 日期: 2026-03-21
-- 描述: 为角色和用户表添加默认首页配置字段

ALTER TABLE sys_role ADD COLUMN default_page VARCHAR(255) DEFAULT '/home' COMMENT '默认首页';
ALTER TABLE sys_user ADD COLUMN default_page VARCHAR(255) DEFAULT NULL COMMENT '默认首页（可覆盖角色默认）';

-- 将所有角色的默认首页统一改为 /sys/warehouse
UPDATE sys_role SET default_page = '/sys/warehouse' WHERE 1=1;
