package com.abtk.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class TestMenu extends TestBase {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void assignMaterialMenuToAdmin() {
        // 将物料管理(parent_id=2110)及其所有子菜单分配给 admin(role_id=1)
        // 先查出物料管理及其子菜单的 menu_id
        String findSql = "SELECT menu_id FROM sys_menu WHERE parent_id = 2110 OR menu_id = 2110";
        List<Long> menuIds = jdbcTemplate.queryForList(findSql, Long.class);
        System.out.println("Found " + menuIds.size() + " material menus: " + menuIds);

        for (Long menuId : menuIds) {
            // 用 INSERT IGNORE 避免重复插入
            int rows = jdbcTemplate.update(
                "INSERT IGNORE INTO sys_role_menu (role_id, menu_id) VALUES (1, ?)",
                menuId);
            System.out.println("menu_id=" + menuId + " -> assigned " + rows + " row(s)");
        }

        // 验证
        String checkSql = "SELECT m.menu_id, m.menu_name, m.menu_type " +
                          "FROM sys_role_menu rm JOIN sys_menu m ON rm.menu_id = m.menu_id " +
                          "WHERE rm.role_id=1 AND (m.parent_id=2110 OR m.menu_id=2110)";
        jdbcTemplate.query(checkSql, (rs, rowNum) -> {
            System.out.println("✅ menu_id=" + rs.getLong("menu_id")
                + " | name=" + rs.getString("menu_name")
                + " | type=" + rs.getString("menu_type"));
            return null;
        });
    }
}
