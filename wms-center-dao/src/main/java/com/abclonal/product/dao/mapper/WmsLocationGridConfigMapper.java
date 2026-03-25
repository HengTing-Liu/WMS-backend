package com.abclonal.product.dao.mapper;

import com.abclonal.product.dao.entity.WmsLocationGridConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 库位网格配置表(WmsLocationGridConfig)数据访问层
 *
 * @author wms
 * @since 2026-03-15
 */
public interface WmsLocationGridConfigMapper {

    /**
     * 通过ID查询单条数据
     */
    WmsLocationGridConfig queryById(Long id);

    /**
     * 通过库位ID查询
     */
    WmsLocationGridConfig queryByLocationId(@Param("locationId") Long locationId);

    /**
     * 新增数据
     */
    int insert(WmsLocationGridConfig record);

    /**
     * 修改数据
     */
    int update(WmsLocationGridConfig record);

    /**
     * 通过库位ID删除
     */
    int deleteByLocationId(@Param("locationId") Long locationId);
}
