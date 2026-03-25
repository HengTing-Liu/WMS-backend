package com.abclonal.product.service.location.service;

import com.abclonal.product.dao.entity.WmsLocationGridConfig;

/**
 * 库位网格配置服务接口
 *
 * @author wms
 * @since 2026-03-15
 */
public interface WmsLocationGridConfigService {

    /**
     * 通过库位ID查询网格配置
     */
    WmsLocationGridConfig queryByLocationId(Long locationId);

    /**
     * 保存或更新网格配置
     */
    WmsLocationGridConfig saveOrUpdate(WmsLocationGridConfig config);

    /**
     * 通过库位ID删除
     */
    boolean deleteByLocationId(Long locationId);
}
