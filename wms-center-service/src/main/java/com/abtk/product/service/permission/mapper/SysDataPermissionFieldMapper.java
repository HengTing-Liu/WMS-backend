package com.abtk.product.service.permission.mapper;

import com.abtk.product.service.permission.entity.SysDataPermissionField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据权限字段配置Mapper
 * 
 * @author backend2
 */
public interface SysDataPermissionFieldMapper {
    
    /**
     * 根据ID查询配置
     */
    SysDataPermissionField selectById(Long id);
    
    /**
     * 根据表编码查询配置
     */
    SysDataPermissionField selectByTableCode(@Param("tableCode") String tableCode);
    
    /**
     * 查询所有配置
     */
    List<SysDataPermissionField> selectAll();
    
    /**
     * 查询启用的配置列表
     */
    List<SysDataPermissionField> selectEnabled();
    
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
    int checkTableCodeExists(@Param("tableCode") String tableCode);
}
