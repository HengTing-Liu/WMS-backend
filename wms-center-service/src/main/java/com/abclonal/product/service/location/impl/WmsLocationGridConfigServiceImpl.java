package com.abclonal.product.service.location.impl;

import com.abclonal.product.dao.entity.WmsLocationGridConfig;
import com.abclonal.product.dao.mapper.WmsLocationGridConfigMapper;
import com.abclonal.product.service.location.service.WmsLocationGridConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 库位网格配置服务实现类
 *
 * @author wms
 * @since 2026-03-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsLocationGridConfigServiceImpl implements WmsLocationGridConfigService {

    @Autowired
    private WmsLocationGridConfigMapper gridConfigMapper;

    @Override
    public WmsLocationGridConfig queryByLocationId(Long locationId) {
        return gridConfigMapper.queryByLocationId(locationId);
    }

    @Override
    public WmsLocationGridConfig saveOrUpdate(WmsLocationGridConfig config) {
        LocalDateTime now = LocalDateTime.now();
        
        // 查询是否已存在
        WmsLocationGridConfig existing = gridConfigMapper.queryByLocationId(config.getLocationId());
        
        if (existing != null) {
            // 更新
            config.setId(existing.getId());
            config.setUpdateTime(now);
            gridConfigMapper.update(config);
        } else {
            // 新增
            config.setCreateTime(now);
            config.setUpdateTime(now);
            gridConfigMapper.insert(config);
        }
        
        return config;
    }

    @Override
    public boolean deleteByLocationId(Long locationId) {
        return gridConfigMapper.deleteByLocationId(locationId) > 0;
    }
}
