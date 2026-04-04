package com.abtk.product.service.system.converter;

import lombok.Data;

import java.util.List;

/**
 *
 *
 * @description:
 * @author: 75618
 * @time: 2026/2/3 16:57
 *
 */
@Data
public class RouteMeta {
    private String title;           // 菜单名称（支持 i18n key）
    private String icon;            // 图标
    private Boolean keepAlive;      // 是否缓存
    private Integer order;          // 排序
    private List<String> authority; // 权限标识（对应 SysMenu.perms）
    private Boolean affixTab;       // 是否固定标签页
    private Boolean hideInMenu;     // 是否隐藏菜单（根据 visible/status 判断）
}
