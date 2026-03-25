package com.abclonal.product.service.permission.config;

import com.abclonal.product.service.permission.aspectj.EnhancedDataScopeAspect;
import com.abclonal.product.service.permission.filter.DataPermissionFilter;
import com.abclonal.product.service.permission.filter.DeptPermissionFilter;
import com.abclonal.product.service.permission.filter.SelfPermissionFilter;
import com.abclonal.product.service.permission.filter.WarehousePermissionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * 数据权限自动配置类
 * 自动装配数据权限相关组件
 * 
 * @author backend2
 */
@Configuration
@Import({EnhancedDataScopeAspect.class})
public class DataPermissionAutoConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(DataPermissionAutoConfiguration.class);
    
    /**
     * 部门权限过滤器
     */
    @Bean
    @ConditionalOnMissingBean(DeptPermissionFilter.class)
    public DeptPermissionFilter deptPermissionFilter() {
        log.info("Init DeptPermissionFilter");
        return new DeptPermissionFilter();
    }
    
    /**
     * 仓库权限过滤器
     */
    @Bean
    @ConditionalOnMissingBean(WarehousePermissionFilter.class)
    public WarehousePermissionFilter warehousePermissionFilter() {
        log.info("Init WarehousePermissionFilter");
        return new WarehousePermissionFilter();
    }
    
    /**
     * 本人权限过滤器
     */
    @Bean
    @ConditionalOnMissingBean(SelfPermissionFilter.class)
    public SelfPermissionFilter selfPermissionFilter() {
        log.info("Init SelfPermissionFilter");
        return new SelfPermissionFilter();
    }
    
    /**
     * 数据权限切面
     */
    @Bean
    @ConditionalOnMissingBean(EnhancedDataScopeAspect.class)
    public EnhancedDataScopeAspect enhancedDataScopeAspect(List<DataPermissionFilter> filters) {
        log.info("Init EnhancedDataScopeAspect with {} filters", filters.size());
        EnhancedDataScopeAspect aspect = new EnhancedDataScopeAspect();
        return aspect;
    }
}
