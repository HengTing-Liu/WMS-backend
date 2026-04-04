package com.abtk.product.common.permission.enums;

/**
 * 数据权限范围枚举
 * 
 * @author backend2
 */
public enum DataScopeType {
    
    /**
     * 全部数据权限
     */
    ALL("1", "全部数据权限"),
    
    /**
     * 自定义数据权限
     */
    CUSTOM("2", "自定义数据权限"),
    
    /**
     * 本部门数据权限
     */
    DEPT("3", "本部门数据权限"),
    
    /**
     * 本部门及以下数据权限
     */
    DEPT_AND_CHILD("4", "本部门及以下数据权限"),
    
    /**
     * 仅本人数据权限
     */
    SELF("5", "仅本人数据权限"),
    
    /**
     * 指定仓库数据权限
     */
    WAREHOUSE("6", "指定仓库数据权限"),
    
    /**
     * 指定公司数据权限
     */
    COMPANY("7", "指定公司数据权限");
    
    private final String code;
    private final String description;
    
    DataScopeType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据code获取枚举
     */
    public static DataScopeType getByCode(String code) {
        for (DataScopeType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * 判断是否包含部门权限
     */
    public static boolean containsDeptScope(String code) {
        return DEPT.code.equals(code) || DEPT_AND_CHILD.code.equals(code) || CUSTOM.code.equals(code);
    }
    
    /**
     * 判断是否包含仓库权限
     */
    public static boolean containsWarehouseScope(String code) {
        return WAREHOUSE.code.equals(code);
    }
}
