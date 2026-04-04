package com.abtk.product.web.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 开发阶段权限初始化组件
 * 确保 system:meta:query 权限已录入 sys_menu，并分配给超级管理员角色
 * 生产环境请删除此组件
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MetaPermissionInitializer {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initMetaPermission() {
        log.info("=== 权限初始化开始 ===");

        try {
            // 1. 检查 system:meta:query 是否已存在
            Integer existingMenuId = findMenuIdByPerms("system:meta:query");
            Long menuId;

            if (existingMenuId != null) {
                menuId = existingMenuId.longValue();
                log.info("权限 system:meta:query 已存在，menu_id={}", menuId);
            } else {
                // 2. 插入 sys_menu 记录（挂靠在系统管理菜单下，parent_id=1）
                // 先查系统管理的 menu_id
                Long parentId = findSystemManagementMenuId();
                log.info("系统管理菜单 parent_id={}", parentId);

                // 插入新菜单按钮
                jdbcTemplate.update(
                    "INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, perms, icon, visible, status, create_by, update_by, remark) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    "元数据查询", parentId, 10, "meta/column/schema",
                    "system/meta/column/schema", "C", "system:meta:query",
                    "table-chart", "0", "0", "admin", "admin",
                    "系统元数据字段Schema查询权限"
                );
                log.info("INSERT sys_menu: 元数据查询 / system:meta:query");

                // 获取刚插入的 menu_id
                Integer newMenuId = findMenuIdByPerms("system:meta:query");
                if (newMenuId == null) {
                    log.error("插入后无法查询到 menu_id，权限初始化失败");
                    return;
                }
                menuId = newMenuId.longValue();
                log.info("新插入菜单，menu_id={}", menuId);
            }

            // 3. 检查 sys_role_menu 是否已关联（角色1 = 超级管理员）
            Integer existingRoleMenu = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_role_menu WHERE role_id=1 AND menu_id=?",
                Integer.class, menuId.intValue()
            );

            if (existingRoleMenu != null && existingRoleMenu > 0) {
                log.info("角色权限关联已存在（role_id=1, menu_id={})", menuId);
            } else {
                jdbcTemplate.update(
                    "INSERT INTO sys_role_menu (role_id, menu_id) VALUES (?, ?)",
                    1, menuId.intValue()
                );
                log.info("INSERT sys_role_menu: role_id=1, menu_id={}", menuId);
            }

            log.info("=== 权限初始化完成 ===");

        } catch (Exception e) {
            log.error("权限初始化异常: {}", e.getMessage(), e);
        }
    }

    private Integer findMenuIdByPerms(String perms) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT menu_id FROM sys_menu WHERE perms = ?",
                Integer.class, perms
            );
        } catch (Exception e) {
            return null;
        }
    }

    private Long findSystemManagementMenuId() {
        try {
            // 查找 "系统管理" 目录
            Integer id = jdbcTemplate.queryForObject(
                "SELECT menu_id FROM sys_menu WHERE menu_name='系统管理' AND menu_type='M' AND parent_id=0 LIMIT 1",
                Integer.class
            );
            return id != null ? id.longValue() : 1L;
        } catch (Exception e) {
            log.warn("未找到系统管理菜单，默认使用 parent_id=1: {}", e.getMessage());
            return 1L;
        }
    }
}
