package com.abtk.product.common.permission.annotation;

import java.lang.annotation.*;

/**
 * 增强版数据权限过滤注解
 * 支持部门、仓库、用户等多维度数据权限控制
 * 
 * @author backend2
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    
    /**
     * 部门表的别名
     */
    String deptAlias() default "";
    
    /**
     * 用户表的别名
     */
    String userAlias() default "";
    
    /**
     * 仓库表的别名
     */
    String warehouseAlias() default "";
    
    /**
     * 公司表的别名
     */
    String companyAlias() default "";
    
    /**
     * 权限字符（用于多个角色匹配符合要求的权限）
     * 默认根据权限注解@RequiresPermissions获取，多个权限用逗号分隔
     */
    String permission() default "";
    
    /**
     * 表编码（用于自动获取权限字段配置）
     * 如果为空，则使用 deptAlias/warehouseAlias 等别名配置
     */
    String tableCode() default "";
    
    /**
     * 是否启用部门权限过滤
     */
    boolean enableDept() default true;
    
    /**
     * 是否启用仓库权限过滤
     */
    boolean enableWarehouse() default false;
    
    /**
     * 是否启用本人权限过滤
     */
    boolean enableSelf() default false;
    
    /**
     * 是否启用公司权限过滤
     */
    boolean enableCompany() default false;
}
