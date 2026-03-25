package com.abclonal.product.service.permission.filter;

import com.abclonal.product.dao.entity.SysUser;
import com.abclonal.product.service.permission.entity.SysDataPermissionField;

/**
 * 数据权限过滤器接口
 * 定义数据权限过滤的标准方法
 * 
 * @author backend2
 */
public interface DataPermissionFilter {
    
    /**
     * 过滤类型标识
     */
    String getFilterType();
    
    /**
     * 是否需要过滤
     * 
     * @param user 当前用户
     * @param permission 权限字符
     * @return true-需要过滤, false-不需要
     */
    boolean needFilter(SysUser user, String permission);
    
    /**
     * 构建数据权限SQL条件
     * 
     * @param user 当前用户
     * @param config 权限字段配置
     * @param alias 表别名
     * @param permission 权限字符
     * @return SQL条件字符串，如 "dept_id IN (1,2,3)"
     */
    String buildCondition(SysUser user, SysDataPermissionField config, String alias, String permission);
    
    /**
     * 获取优先级（数字越小优先级越高）
     */
    default int getPriority() {
        return 100;
    }
}
