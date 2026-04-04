package com.abtk.product.service.permission.service;

import com.abtk.product.service.permission.entity.SysDataPermissionField;

import java.util.List;

/**
 * 数据权限字段配置服务接口
 * 
 * @author backend2
 */
public interface SysDataPermissionFieldService {
    
    /**
     * 根据ID查询配置
     */
    SysDataPermissionField getById(Long id);
    
    /**
     * 根据表编码查询配置
     */
    SysDataPermissionField getByTableCode(String tableCode);
    
    /**
     * 查询所有配置
     */
    List<SysDataPermissionField> listAll();
    
    /**
     * 查询启用的配置列表
     */
    List<SysDataPermissionField> listEnabled();
    
    /**
     * 新增配置
     */
    int insert(SysDataPermissionField config);
    
    /**
     * 更新配置
     */
    int update(SysDataPermissionField config);
    
    /**
     * 删除配置
     */
    int deleteById(Long id);
    
    /**
     * 批量删除配置
     */
    int deleteByIds(Long[] ids);
    
    /**
     * 检查表编码是否已存在
     */
    boolean checkTableCodeExists(String tableCode);
    
    /**
     * 初始化默认配置
     * 为指定表初始化默认的权限字段配置
     */
    SysDataPermissionField initDefaultConfig(String tableCode, String tableName);
}
