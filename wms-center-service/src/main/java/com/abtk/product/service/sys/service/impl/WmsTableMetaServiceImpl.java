package com.abtk.product.service.sys.service.impl;

import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.dao.entity.WmsTableMeta;
import com.abtk.product.dao.mapper.WmsTableMetaMapper;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.sys.service.IWmsTableMetaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 表元数据管理服务实现
 *
 * @author backend
 * @since 2026-04-06
 */
@Slf4j
@Service
public class WmsTableMetaServiceImpl implements IWmsTableMetaService {

    @Autowired
    private WmsTableMetaMapper wmsTableMetaMapper;

    @Override
    public List<WmsTableMeta> list(WmsTableMeta condition) {
        return wmsTableMetaMapper.queryAll(condition);
    }

    @Override
    public WmsTableMeta getById(Long id) {
        return wmsTableMetaMapper.queryById(id);
    }

    @Override
    public WmsTableMeta getByTableCode(String tableCode) {
        return wmsTableMetaMapper.queryByCode(tableCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(WmsTableMeta tableMeta) {
        // 校验表编码唯一性
        if (!checkTableCodeUnique(tableMeta.getTableCode(), null)) {
            throw new ServiceException("表编码已存在：" + tableMeta.getTableCode());
        }

        // 设置默认值
        if (tableMeta.getPageSize() == null) {
            tableMeta.setPageSize(20);
        }
        if (tableMeta.getIsTree() == null) {
            tableMeta.setIsTree(0);
        }
        if (tableMeta.getStatus() == null) {
            tableMeta.setStatus(1);
        }
        if (tableMeta.getHasDataPermission() == null) {
            tableMeta.setHasDataPermission(0);
        }

        // 设置创建信息
        String username = SecurityUtils.getUsername();
        tableMeta.setCreateBy(username);
        tableMeta.setCreateTime(new Date());

        wmsTableMetaMapper.insert(tableMeta);
        return tableMeta.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, WmsTableMeta tableMeta) {
        WmsTableMeta existing = wmsTableMetaMapper.queryById(id);
        if (existing == null) {
            throw new ServiceException("表元数据不存在：" + id);
        }

        // 校验表编码唯一性（排除当前记录）
        if (tableMeta.getTableCode() != null && !tableMeta.getTableCode().equals(existing.getTableCode())) {
            if (!checkTableCodeUnique(tableMeta.getTableCode(), id)) {
                throw new ServiceException("表编码已存在：" + tableMeta.getTableCode());
            }
        }

        // 设置ID
        tableMeta.setId(id);

        // 设置更新信息
        String username = SecurityUtils.getUsername();
        tableMeta.setUpdateBy(username);
        tableMeta.setUpdateTime(new Date());

        wmsTableMetaMapper.update(tableMeta);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        WmsTableMeta existing = wmsTableMetaMapper.queryById(id);
        if (existing == null) {
            throw new ServiceException("表元数据不存在：" + id);
        }

        // 检查是否有关联字段
        if (hasRelatedFields(id)) {
            throw new ServiceException("该表元数据下存在关联字段，无法删除");
        }

        String username = SecurityUtils.getUsername();
        wmsTableMetaMapper.deleteById(id, username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, Integer status) {
        WmsTableMeta existing = wmsTableMetaMapper.queryById(id);
        if (existing == null) {
            throw new ServiceException("表元数据不存在：" + id);
        }

        if (status != 0 && status != 1) {
            throw new ServiceException("状态值无效：" + status);
        }

        wmsTableMetaMapper.toggleStatus(id, status);
    }

    @Override
    public List<WmsTableMeta> listAll() {
        return wmsTableMetaMapper.listAll();
    }

    @Override
    public List<WmsTableMeta> listByModule(String module) {
        WmsTableMeta condition = new WmsTableMeta();
        condition.setModule(module);
        return wmsTableMetaMapper.queryAll(condition);
    }

    @Override
    public boolean checkTableCodeUnique(String tableCode, Long excludeId) {
        WmsTableMeta existing = wmsTableMetaMapper.checkTableCodeUnique(tableCode);
        if (existing == null) {
            return true;
        }
        // 如果是更新操作，排除当前记录
        if (excludeId != null && existing.getId().equals(excludeId)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasRelatedFields(Long tableId) {
        long count = wmsTableMetaMapper.countFieldByTableId(tableId);
        return count > 0;
    }
}
