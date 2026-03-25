package com.abclonal.product.service.system.converter;

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
public class RouteItem {
    private RouteMeta meta;
    private String name;            // 路由名称（建议用驼峰或唯一标识）
    private String path;            // 路径
    private String component;       // 组件路径
    private String redirect;        // 重定向
    private List<RouteItem> children;
}
