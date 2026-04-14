package com.abtk.product.service.system.converter;

import com.abtk.product.dao.entity.SysMenu;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/3 18:20
 *
 */
public class MenuConverter {
    /**
     * 将 RuoYi 的 SysMenu 树转换为 VBEN 路由格式
     */
    public static List<RouteItem> convertToRouteItems(List<SysMenu> sysMenus) {
        if (sysMenus == null || sysMenus.isEmpty()) {
            return Collections.emptyList();
        }
        return sysMenus.stream()
                .map(MenuConverter::convertToRouteItem)
                .collect(Collectors.toList());
    }

    private static RouteItem convertToRouteItem(SysMenu menu) {
        RouteItem item = new RouteItem();

        // 基础字段
        item.setName(menu.getMenuName().replaceAll("\\s+", "")); // 简单转为无空格名，或用 menuId
        item.setPath(buildPath(menu));
        
        // 处理 component：如果为空，根据菜单类型设置默认值
        String component = menu.getComponent();
        if (component == null || component.isEmpty()) {
            // 目录类型（M）默认为 Layout
            if ("M".equals(menu.getMenuType())) {
                component = "Layout";
            }
        }
        
        // 特殊处理：/dashboard 作为首页，直接使用 dashboard/index 组件
        if ("/dashboard".equals(menu.getPath()) && "Layout".equals(component)) {
            component = "dashboard/index";
        }
        
        item.setComponent(component);

        // 构建 meta
        RouteMeta meta = new RouteMeta();
        meta.setTitle(menu.getMenuName()); // 或使用 i18n key，如 "menu." + menu.getPerms()
        meta.setIcon(menu.getIcon());
        meta.setOrder(menu.getOrderNum());

        // 权限：perms 字段按逗号分割（RuoYi 支持多个权限，如 "system:user:list,system:user:query"）
        if (menu.getPerms() != null && !menu.getPerms().isEmpty()) {
            meta.setAuthority(Arrays.asList(menu.getPerms().split(",")));
        }

        // 缓存：RuoYi 中 isCache="0" 表示缓存，"1" 表示不缓存
        meta.setKeepAlive("0".equals(menu.getIsCache()));

        // 固定标签页（可自定义逻辑，这里假设 remark="affix" 表示固定）
        meta.setAffixTab("1".equals(menu.getRemarks())); // 或根据业务扩展

        // 隐藏菜单：visible="1" 表示隐藏，status="1" 表示停用
        boolean hideInMenu = "1".equals(menu.getVisible()) || "1".equals(menu.getStatus());
        meta.setHideInMenu(hideInMenu);

        item.setMeta(meta);

        // 子菜单递归
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            item.setChildren(convertToRouteItems(menu.getChildren()));
            // 如果是目录（M）且有子菜单，自动 redirect 到第一个子菜单
            if ("M".equals(menu.getMenuType())) {
                SysMenu firstChild = menu.getChildren().get(0);
                String redirectPath = buildPath(firstChild);
                // 确保子路径以 / 开头
                if (!redirectPath.startsWith("/")) {
                    redirectPath = "/" + redirectPath;
                }
                item.setRedirect(redirectPath);
            }
        }

        return item;
    }

    /**
     * 构建 path，确保以 / 开头
     */
    private static String buildPath(SysMenu menu) {
        String path = menu.getPath();
        if (path != null && !path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }
}
