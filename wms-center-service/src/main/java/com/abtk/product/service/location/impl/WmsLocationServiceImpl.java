package com.abtk.product.service.location.impl;

import com.abtk.product.dao.entity.WmsLocation;
import com.abtk.product.dao.mapper.WmsLocationMapper;
import com.abtk.product.service.location.service.WmsLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 库位档案表(WmsLocation)表服务实现类
 *
 * @author wms
 * @since 2026-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsLocationServiceImpl implements WmsLocationService {

    @Autowired
    private WmsLocationMapper wmsLocationMapper;

    // ==================== 查询方法 ====================

    @Override
    public WmsLocation queryById(Long id) {
        return wmsLocationMapper.queryById(id);
    }

    @Override
    public List<WmsLocation> queryByCondition(WmsLocation wmsLocation) {
        return wmsLocationMapper.queryAll(wmsLocation);
    }

    @Override
    public List<WmsLocation> queryByParentId(Long parentId) {
        return wmsLocationMapper.queryByParentId(parentId);
    }

    @Override
    public List<WmsLocation> queryRootNodes() {
        return wmsLocationMapper.queryRootNodes();
    }

    @Override
    public List<WmsLocation> queryByWarehouseCode(String warehouseCode) {
        return wmsLocationMapper.queryByWarehouseCode(warehouseCode);
    }

    @Override
    public List<WmsLocation> queryAllChildren(Long parentId) {
        return wmsLocationMapper.queryAllChildren(parentId);
    }

    @Override
    public List<WmsLocation> queryChildrenByLevel(Long parentId, Integer maxLevel) {
        return wmsLocationMapper.queryChildrenByLevel(parentId, maxLevel);
    }

    @Override
    public long count(WmsLocation wmsLocation) {
        return wmsLocationMapper.count(wmsLocation);
    }

    @Override
    public boolean existsById(Long id) {
        return wmsLocationMapper.queryById(id) != null;
    }

    // ==================== 新增方法 ====================

    @Override
    public WmsLocation insert(WmsLocation wmsLocation) {
        Date now = new Date();
        if (wmsLocation.getCreateTime() == null) {
            wmsLocation.setCreateTime(now);
        }
        if (wmsLocation.getUpdateTime() == null) {
            wmsLocation.setUpdateTime(now);
        }
        if (wmsLocation.getIsDeleted() == null) {
            wmsLocation.setIsDeleted(0);
        }
        if (wmsLocation.getIsUse() == null) {
            wmsLocation.setIsUse(0);
        }
        if (wmsLocation.getCapacityTotal() == null) {
            wmsLocation.setCapacityTotal(0);
        }
        if (wmsLocation.getCapacityUsed() == null) {
            wmsLocation.setCapacityUsed(0);
        }
        wmsLocationMapper.insert(wmsLocation);
        return wmsLocation;
    }

    @Override
    public int insertBatchAtomic(List<WmsLocation> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        Date now = new Date();
        for (WmsLocation entity : entities) {
            if (entity.getCreateTime() == null) {
                entity.setCreateTime(now);
            }
            if (entity.getUpdateTime() == null) {
                entity.setUpdateTime(now);
            }
            if (entity.getIsDeleted() == null) {
                entity.setIsDeleted(0);
            }
            if (entity.getIsUse() == null) {
                entity.setIsUse(0);
            }
            if (entity.getCapacityTotal() == null) {
                entity.setCapacityTotal(0);
            }
            if (entity.getCapacityUsed() == null) {
                entity.setCapacityUsed(0);
            }
        }
        return wmsLocationMapper.insertBatch(entities);
    }

    @Override
    public int insertOrUpdateBatch(List<WmsLocation> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        Date now = new Date();
        for (WmsLocation entity : entities) {
            if (entity.getCreateTime() == null) {
                entity.setCreateTime(now);
            }
            if (entity.getUpdateTime() == null) {
                entity.setUpdateTime(now);
            }
        }
        return wmsLocationMapper.insertOrUpdateBatch(entities);
    }

    // ==================== 修改方法 ====================

    @Override
    public int update(WmsLocation wmsLocation) {
        wmsLocation.setUpdateTime(new Date());
        return wmsLocationMapper.update(wmsLocation);
    }

    @Override
    public int updateBatchAtomic(List<WmsLocation> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Date now = new Date();
        for (WmsLocation entity : list) {
            entity.setUpdateTime(now);
        }
        return wmsLocationMapper.updateBatch(list);
    }

    // ==================== 删除方法 ====================

    @Override
    public boolean logicDeleteById(Long id, String username) {
        return wmsLocationMapper.deleteById(id, username) > 0;
    }

    @Override
    public int logicDeleteBatchByIds(List<Long> ids, String username) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return wmsLocationMapper.deleteBatchByIds(ids, username);
    }

    @Override
    public int logicDeleteRecursive(Long id, String username) {
        return wmsLocationMapper.deleteRecursive(id, username);
    }

    // ==================== 容量统计方法 ====================

    @Override
    public List<WmsLocation> listAll() {
        return wmsLocationMapper.listAll();
    }

    @Override
    public void toggleStatus(Long id, Integer enabled) {
        wmsLocationMapper.toggleStatus(id, enabled);
    }

    @Override
    public List<WmsLocation> queryByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return wmsLocationMapper.queryByIds(ids);
    }

    @Override
    public int batchUpdateWarehouseCode(List<Long> ids, String warehouseCode, String updateBy) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return wmsLocationMapper.batchUpdateWarehouseCode(ids, warehouseCode, updateBy);
    }

    @Override
    public List<WmsLocation> queryByFullpathName(String warehouseCode, String locationFullpathName) {
        return wmsLocationMapper.queryByFullpathName(warehouseCode, locationFullpathName);
    }

    @Override
    public int countByNameAndWarehouse(String warehouseCode, String locationName, Long parentId, Long excludeId) {
        return wmsLocationMapper.countByNameAndWarehouse(warehouseCode, locationName, parentId, excludeId);
    }

    /**
     * 更新父节点的 internal_quantity（累加）
     */
    @Override
    public int updateInternalQuantity(Long parentId, Integer delta) {
        if (parentId == null || delta == null || delta == 0) {
            return 0;
        }
        return wmsLocationMapper.updateInternalQuantity(parentId, delta);
    }

    /**
     * 检查库位是否可以删除
     * 注意：此方法的业务逻辑已在 WmsLocationBiz 中实现，此处为兼容接口的空实现
     */
    @Override
    public Map<String, Object> checkDelete(Long id) {
        // 业务逻辑在 WmsLocationBiz.checkDelete() 中实现
        return new java.util.HashMap<>();
    }

    /**
     * 🔴 新增：通过排序号前缀查询占用数量
     */
    @Override
    public int countUsedBySortNoPrefix(String locationSortNoPrefix) {
        return wmsLocationMapper.countUsedBySortNoPrefix(locationSortNoPrefix);
    }

    /**
     * 统计子节点数量
     */
    @Override
    public int countChildren(Long parentId) {
        return wmsLocationMapper.countChildren(parentId);
    }

    /**
     * 统计已占用的子节点数量
     */
    @Override
    public int countUsedChildren(Long parentId) {
        return wmsLocationMapper.countUsedChildren(parentId);
    }
}
