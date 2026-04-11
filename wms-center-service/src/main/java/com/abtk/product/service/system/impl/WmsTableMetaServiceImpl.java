package com.abtk.product.service.system.impl;

import com.abtk.product.dao.entity.WmsTableMeta;
import com.abtk.product.dao.mapper.WmsTableMetaMapper;
import com.abtk.product.service.system.service.IWmsTableMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 表元数据管理表(WmsTableMeta)表服务实现类
 *
 * @author backend
 * @since 2026-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsTableMetaServiceImpl implements IWmsTableMetaService {

    @Autowired
    private WmsTableMetaMapper wmsTableMetaMapper;

    // ==================== 查询方法 ====================

    @Override
    public WmsTableMeta queryById(Long id) {
        return wmsTableMetaMapper.queryById(id);
    }

    @Override
    public WmsTableMeta queryByCode(String tableCode) {
        return wmsTableMetaMapper.queryByCode(tableCode);
    }

    @Override
    public List<WmsTableMeta> queryByCondition(WmsTableMeta wmsTableMeta) {
        return wmsTableMetaMapper.queryAll(wmsTableMeta);
    }

    @Override
    public List<WmsTableMeta> listAll() {
        return wmsTableMetaMapper.listAll();
    }

    @Override
    public long count(WmsTableMeta wmsTableMeta) {
        return wmsTableMetaMapper.count(wmsTableMeta);
    }

    @Override
    public boolean checkTableCodeUnique(String tableCode) {
        return wmsTableMetaMapper.checkTableCodeUnique(tableCode) == null;
    }

    @Override
    public boolean hasRelatedFields(Long tableId) {
        return wmsTableMetaMapper.countFieldByTableId(tableId) > 0;
    }

    // ==================== 新增方法 ====================

    @Override
    public WmsTableMeta insert(WmsTableMeta wmsTableMeta) {
        Date now = new Date();
        if (wmsTableMeta.getCreateTime() == null) {
            wmsTableMeta.setCreateTime(now);
        }
        if (wmsTableMeta.getUpdateTime() == null) {
            wmsTableMeta.setUpdateTime(now);
        }
        if (wmsTableMeta.getStatus() == null) {
            wmsTableMeta.setStatus(1);
        }
        if (wmsTableMeta.getPageSize() == null) {
            wmsTableMeta.setPageSize(20);
        }
        if (wmsTableMeta.getIsTree() == null) {
            wmsTableMeta.setIsTree(0);
        }
        if (wmsTableMeta.getHasDataPermission() == null) {
            wmsTableMeta.setHasDataPermission(0);
        }
        if (wmsTableMeta.getIsDeletedColumn() == null) {
            wmsTableMeta.setIsDeletedColumn("isdeleted");
        }
        if (wmsTableMeta.getPermissionField() == null) {
            wmsTableMeta.setPermissionField("dept_id");
        }
        if (wmsTableMeta.getPermissionScope() == null) {
            wmsTableMeta.setPermissionScope("DEPT");
        }
        wmsTableMetaMapper.insert(wmsTableMeta);
        return wmsTableMeta;
    }

    // ==================== 修改方法 ====================

    @Override
    public int update(WmsTableMeta wmsTableMeta) {
        wmsTableMeta.setUpdateTime(new Date());
        return wmsTableMetaMapper.update(wmsTableMeta);
    }

    // ==================== 删除方法 ====================

    @Override
    public boolean logicDeleteById(Long id, String username) {
        return wmsTableMetaMapper.deleteById(id, username) > 0;
    }

    @Override
    public void toggleStatus(Long id, Integer status) {
        wmsTableMetaMapper.toggleStatus(id, status);
    }
}
