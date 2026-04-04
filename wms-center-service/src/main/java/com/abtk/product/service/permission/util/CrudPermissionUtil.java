package com.abtk.product.service.permission.util;

import com.abtk.product.common.permission.context.DataPermissionContext;
import com.abtk.product.common.utils.StringUtils;

import java.util.Map;

/**
 * CrudService数据权限工具类
 * 用于与CrudService集成，自动注入数据权限查询条件
 * 
 * @author backend2
 */
public class CrudPermissionUtil {
    
    /**
     * 数据权限参数Key
     */
    public static final String DATA_SCOPE_PARAM = "dataScope";
    
    /**
     * 为查询参数注入数据权限条件
     * 
     * @param params 查询参数Map
     * @return 注入后的参数Map
     */
    public static Map<String, Object> injectDataScope(Map<String, Object> params) {
        if (params == null) {
            return null;
        }
        
        // 如果设置了忽略权限，直接返回
        if (DataPermissionContext.isIgnorePermission()) {
            return params;
        }
        
        // 获取数据权限SQL
        String dataScopeSql = DataPermissionContext.getDataScopeSql();
        
        if (StringUtils.isNotEmpty(dataScopeSql)) {
            params.put(DATA_SCOPE_PARAM, " AND " + dataScopeSql);
        }
        
        return params;
    }
    
    /**
     * 为SQL添加数据权限条件
     * 
     * @param originalSql 原始SQL
     * @return 添加数据权限后的SQL
     */
    public static String addDataScopeToSql(String originalSql) {
        if (StringUtils.isEmpty(originalSql)) {
            return originalSql;
        }
        
        // 如果设置了忽略权限，直接返回
        if (DataPermissionContext.isIgnorePermission()) {
            return originalSql;
        }
        
        // 获取数据权限SQL
        String dataScopeSql = DataPermissionContext.getDataScopeSql();
        
        if (StringUtils.isEmpty(dataScopeSql)) {
            return originalSql;
        }
        
        // 在WHERE子句后添加数据权限条件
        return appendConditionToSql(originalSql, dataScopeSql);
    }
    
    /**
     * 向SQL追加条件
     */
    private static String appendConditionToSql(String sql, String condition) {
        if (sql.toUpperCase().contains(" WHERE ")) {
            // 已有WHERE子句，追加条件
            int whereIndex = sql.toUpperCase().indexOf(" WHERE ");
            int orderByIndex = sql.toUpperCase().indexOf(" ORDER BY ");
            int limitIndex = sql.toUpperCase().indexOf(" LIMIT ");
            
            int insertIndex = sql.length();
            if (orderByIndex > 0) {
                insertIndex = orderByIndex;
            } else if (limitIndex > 0) {
                insertIndex = limitIndex;
            }
            
            String before = sql.substring(0, insertIndex);
            String after = sql.substring(insertIndex);
            
            return before + " AND (" + condition + ")" + after;
        } else {
            // 没有WHERE子句，添加WHERE
            int orderByIndex = sql.toUpperCase().indexOf(" ORDER BY ");
            int limitIndex = sql.toUpperCase().indexOf(" LIMIT ");
            int groupByIndex = sql.toUpperCase().indexOf(" GROUP BY ");
            
            int insertIndex = sql.length();
            if (groupByIndex > 0) {
                insertIndex = groupByIndex;
            } else if (orderByIndex > 0) {
                insertIndex = orderByIndex;
            } else if (limitIndex > 0) {
                insertIndex = limitIndex;
            }
            
            String before = sql.substring(0, insertIndex);
            String after = sql.substring(insertIndex);
            
            return before + " WHERE " + condition + after;
        }
    }
    
    /**
     * 检查是否需要数据权限过滤
     */
    public static boolean needDataScopeFilter() {
        return !DataPermissionContext.isIgnorePermission() 
                && StringUtils.isNotEmpty(DataPermissionContext.getDataScopeSql());
    }
    
    /**
     * 获取当前数据权限SQL
     */
    public static String getCurrentDataScopeSql() {
        return DataPermissionContext.getDataScopeSql();
    }
    
    /**
     * 清除数据权限上下文
     * 建议在请求结束时调用
     */
    public static void clearContext() {
        DataPermissionContext.clearAll();
    }
}
