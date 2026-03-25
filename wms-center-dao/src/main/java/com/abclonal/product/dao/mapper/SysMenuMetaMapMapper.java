package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.SysMenuMetaMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单低代码配置映射 数据层
 *
 * @author ruoyi
 */
public interface SysMenuMetaMapMapper
{
    /**
     * 查询所有映射配置
     *
     * @return 映射列表
     */
    public List<SysMenuMetaMap> selectMenuMetaMapList();

    /**
     * 根据菜单ID查询映射配置
     *
     * @param menuId 菜单ID
     * @return 映射配置
     */
    public SysMenuMetaMap selectMenuMetaMapById(@Param("menuId") Long menuId);

    /**
     * 新增映射配置
     *
     * @param menuMetaMap 映射配置
     * @return 结果
     */
    public int insertMenuMetaMap(SysMenuMetaMap menuMetaMap);

    /**
     * 修改映射配置
     *
     * @param menuMetaMap 映射配置
     * @return 结果
     */
    public int updateMenuMetaMap(SysMenuMetaMap menuMetaMap);

    /**
     * 删除映射配置
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int deleteMenuMetaMapById(@Param("menuId") Long menuId);
}
