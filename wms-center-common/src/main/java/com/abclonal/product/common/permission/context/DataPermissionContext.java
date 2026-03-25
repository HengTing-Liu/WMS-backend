package com.abclonal.product.common.permission.context;

import java.util.*;

/**
 * 数据权限上下文
 * 用于存储当前线程的数据权限相关信息
 * 
 * @author backend2
 */
public class DataPermissionContext {
    
    /**
     * 线程本地变量，存储数据权限SQL条件
     */
    private static final ThreadLocal<String> DATA_SCOPE_SQL = new ThreadLocal<>();
    
    /**
     * 线程本地变量，存储当前用户可访问的仓库编码列表
     */
    private static final ThreadLocal<Set<String>> WAREHOUSE_CODES = new ThreadLocal<>();
    
    /**
     * 线程本地变量，存储当前用户可访问的部门ID列表
     */
    private static final ThreadLocal<Set<Long>> DEPT_IDS = new ThreadLocal<>();
    
    /**
     * 线程本地变量，存储当前表的权限字段配置（使用Map避免依赖service层实体）
     */
    private static final ThreadLocal<Map<String, Object>> PERMISSION_CONFIG = new ThreadLocal<>();
    
    /**
     * 线程本地变量，标记是否忽略数据权限
     */
    private static final ThreadLocal<Boolean> IGNORE_PERMISSION = new ThreadLocal<>();
    
    // ==================== DataScope SQL ====================
    
    /**
     * 设置数据权限SQL
     */
    public static void setDataScopeSql(String sql) {
        DATA_SCOPE_SQL.set(sql);
    }
    
    /**
     * 获取数据权限SQL
     */
    public static String getDataScopeSql() {
        return DATA_SCOPE_SQL.get();
    }
    
    /**
     * 清除数据权限SQL
     */
    public static void clearDataScopeSql() {
        DATA_SCOPE_SQL.remove();
    }
    
    // ==================== Warehouse Codes ====================
    
    /**
     * 设置可访问的仓库编码列表
     */
    public static void setWarehouseCodes(Set<String> codes) {
        WAREHOUSE_CODES.set(codes);
    }
    
    /**
     * 获取可访问的仓库编码列表
     */
    public static Set<String> getWarehouseCodes() {
        Set<String> codes = WAREHOUSE_CODES.get();
        return codes != null ? codes : Collections.emptySet();
    }
    
    /**
     * 添加可访问的仓库编码
     */
    public static void addWarehouseCode(String code) {
        Set<String> codes = WAREHOUSE_CODES.get();
        if (codes == null) {
            codes = new HashSet<>();
            WAREHOUSE_CODES.set(codes);
        }
        codes.add(code);
    }
    
    /**
     * 清除仓库编码列表
     */
    public static void clearWarehouseCodes() {
        WAREHOUSE_CODES.remove();
    }
    
    // ==================== Dept IDs ====================
    
    /**
     * 设置可访问的部门ID列表
     */
    public static void setDeptIds(Set<Long> deptIds) {
        DEPT_IDS.set(deptIds);
    }
    
    /**
     * 获取可访问的部门ID列表
     */
    public static Set<Long> getDeptIds() {
        Set<Long> deptIds = DEPT_IDS.get();
        return deptIds != null ? deptIds : Collections.emptySet();
    }
    
    /**
     * 添加可访问的部门ID
     */
    public static void addDeptId(Long deptId) {
        Set<Long> deptIds = DEPT_IDS.get();
        if (deptIds == null) {
            deptIds = new HashSet<>();
            DEPT_IDS.set(deptIds);
        }
        deptIds.add(deptId);
    }
    
    /**
     * 清除部门ID列表
     */
    public static void clearDeptIds() {
        DEPT_IDS.remove();
    }
    
    // ==================== Permission Config ====================
    
    /**
     * 设置权限字段配置（使用Map存储，避免依赖service层实体）
     */
    public static void setPermissionConfig(Map<String, Object> config) {
        PERMISSION_CONFIG.set(config);
    }
    
    /**
     * 获取权限字段配置
     */
    public static Map<String, Object> getPermissionConfig() {
        return PERMISSION_CONFIG.get();
    }
    
    /**
     * 获取权限字段配置中的指定字段值
     */
    public static Object getPermissionConfigValue(String key) {
        Map<String, Object> config = PERMISSION_CONFIG.get();
        return config != null ? config.get(key) : null;
    }
    
    /**
     * 获取权限字段配置中的字符串字段值
     */
    public static String getPermissionConfigString(String key) {
        Object value = getPermissionConfigValue(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * 清除权限字段配置
     */
    public static void clearPermissionConfig() {
        PERMISSION_CONFIG.remove();
    }
    
    // ==================== Ignore Permission ====================
    
    /**
     * 设置是否忽略数据权限
     */
    public static void setIgnorePermission(boolean ignore) {
        IGNORE_PERMISSION.set(ignore);
    }
    
    /**
     * 是否忽略数据权限
     */
    public static boolean isIgnorePermission() {
        Boolean ignore = IGNORE_PERMISSION.get();
        return ignore != null && ignore;
    }
    
    /**
     * 清除忽略权限标记
     */
    public static void clearIgnorePermission() {
        IGNORE_PERMISSION.remove();
    }
    
    // ==================== Clear All ====================
    
    /**
     * 清除所有线程本地变量
     * 建议在请求结束时调用
     */
    public static void clearAll() {
        DATA_SCOPE_SQL.remove();
        WAREHOUSE_CODES.remove();
        DEPT_IDS.remove();
        PERMISSION_CONFIG.remove();
        IGNORE_PERMISSION.remove();
    }
}
