package com.abclonal.product.service.permission.aspectj;

import com.abclonal.product.common.permission.annotation.DataScope;
import com.abclonal.product.common.permission.context.DataPermissionContext;
import com.abclonal.product.common.utils.StringUtils;
import com.abclonal.product.service.permission.entity.SysDataPermissionField;
import com.abclonal.product.service.permission.filter.DataPermissionFilter;
import com.abclonal.product.service.permission.service.SysDataPermissionFieldService;
import com.abclonal.product.dao.entity.SysUser;
import com.abclonal.product.service.domain.LoginUser;
import com.abclonal.product.service.security.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 增强版数据权限过滤切面
 * 支持部门、仓库、本人等多维度数据权限控制
 * 
 * @author backend2
 */
@Aspect
@Component
@Order(1) // 确保在其他切面之前执行
public class EnhancedDataScopeAspect {
    
    private static final Logger log = LoggerFactory.getLogger(EnhancedDataScopeAspect.class);
    
    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE_KEY = "dataScope";
    
    @Autowired
    private List<DataPermissionFilter> permissionFilters;
    
    @Autowired(required = false)
    private SysDataPermissionFieldService permissionFieldService;
    
    /**
     * 前置通知：拦截@DataScope注解的方法
     */
    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint point, DataScope dataScope) throws Throwable {
        // 清除之前的权限上下文
        clearDataScope();
        
        // 处理数据权限
        handleDataScope(point, dataScope);
    }
    
    /**
     * 处理数据权限
     */
    protected void handleDataScope(JoinPoint joinPoint, DataScope dataScope) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            log.debug("No login user found, skip data scope filter");
            return;
        }
        
        SysUser currentUser = loginUser.getSysUser();
        if (currentUser == null) {
            log.debug("No current user found, skip data scope filter");
            return;
        }
        
        // 超级管理员不过滤
        if (currentUser.isAdmin()) {
            log.debug("Admin user, skip data scope filter");
            return;
        }
        
        // 获取权限字段配置
        SysDataPermissionField config = getPermissionConfig(dataScope);
        
        // 获取权限字符
        String permission = StringUtils.defaultIfEmpty(dataScope.permission(), "");
        
        // 构建数据权限SQL
        String dataScopeSql = buildDataScopeSql(currentUser, config, dataScope, permission);
        
        // 设置到上下文
        if (StringUtils.isNotEmpty(dataScopeSql)) {
            DataPermissionContext.setDataScopeSql(dataScopeSql);
            log.debug("Data scope SQL: {}", dataScopeSql);
        }
        
        // 设置忽略权限标记（如果不需要过滤）
        if (StringUtils.isEmpty(dataScopeSql)) {
            DataPermissionContext.setIgnorePermission(true);
        }
    }
    
    /**
     * 构建数据权限SQL
     */
    protected String buildDataScopeSql(SysUser user, SysDataPermissionField config, 
                                       DataScope dataScope, String permission) {
        
        List<String> conditions = new ArrayList<>();
        
        // 按优先级排序过滤器
        List<DataPermissionFilter> sortedFilters = new ArrayList<>(permissionFilters);
        sortedFilters.sort(Comparator.comparingInt(DataPermissionFilter::getPriority));
        
        // 执行各个过滤器
        for (DataPermissionFilter filter : sortedFilters) {
            try {
                String condition = null;
                
                switch (filter.getFilterType()) {
                    case "DEPT":
                        if (dataScope.enableDept()) {
                            condition = filter.buildCondition(user, config, dataScope.deptAlias(), permission);
                        }
                        break;
                    case "WAREHOUSE":
                        if (dataScope.enableWarehouse()) {
                            condition = filter.buildCondition(user, config, dataScope.warehouseAlias(), permission);
                        }
                        break;
                    case "SELF":
                        if (dataScope.enableSelf()) {
                            condition = filter.buildCondition(user, config, dataScope.userAlias(), permission);
                        }
                        break;
                    case "COMPANY":
                        if (dataScope.enableCompany()) {
                            condition = filter.buildCondition(user, config, dataScope.companyAlias(), permission);
                        }
                        break;
                }
                
                if (StringUtils.isNotEmpty(condition)) {
                    conditions.add(condition);
                }
                
            } catch (Exception e) {
                log.error("Data permission filter error: {}", filter.getFilterType(), e);
            }
        }
        
        // 合并条件
        if (conditions.isEmpty()) {
            return "";
        }
        
        if (conditions.size() == 1) {
            return conditions.get(0);
        }
        
        return "(" + String.join(" AND ", conditions) + ")";
    }
    
    /**
     * 获取权限字段配置
     */
    protected SysDataPermissionField getPermissionConfig(DataScope dataScope) {
        if (StringUtils.isEmpty(dataScope.tableCode()) || permissionFieldService == null) {
            return null;
        }
        
        try {
            return permissionFieldService.getByTableCode(dataScope.tableCode());
        } catch (Exception e) {
            log.warn("Failed to get permission config for table: {}", dataScope.tableCode(), e);
            return null;
        }
    }
    
    /**
     * 清除数据权限上下文
     */
    private void clearDataScope() {
        DataPermissionContext.clearAll();
    }
}
