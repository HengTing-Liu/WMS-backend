package com.abclonal.product.service.sys.impl;

import com.abclonal.product.common.exception.ServiceException;
import com.abclonal.product.dao.entity.Storage;
import com.abclonal.product.dao.mapper.StorageMapper;
import com.abclonal.product.service.sys.service.StorageService;
import com.abclonal.product.service.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库区档案服务实现
 * WMS0050 库区管理
 * Service层只做简单CRUD，复杂业务在Biz层处理
 */
@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageMapper storageMapper;

    @Override
    public List<Storage> list(Storage condition) {
        return storageMapper.selectList(condition);
    }

    @Override
    public Storage getById(Long id) {
        Storage storage = storageMapper.selectById(id);
        if (storage == null) {
            throw new ServiceException("库区不存在");
        }
        return storage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Storage storage) {
        // 检查库区编码是否已存在
        if (storageMapper.checkStorageCodeExists(storage.getStorageCode(), null) > 0) {
            throw new ServiceException("库区编码已存在");
        }

        storage.setIsdeleted(0);

        // 设置创建人
        String username = SecurityUtils.getUsername();
        storage.setCreateBy(username);

        storageMapper.insert(storage);
        return storage.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, Storage storage) {
        // 检查库区是否存在
        Storage exist = storageMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("库区不存在");
        }

        // 检查库区编码是否重复（排除当前ID）
        if (!exist.getStorageCode().equals(storage.getStorageCode()) &&
                storageMapper.checkStorageCodeExists(storage.getStorageCode(), id) > 0) {
            throw new ServiceException("库区编码已存在");
        }

        storage.setId(id);

        // 设置更新人
        String username = SecurityUtils.getUsername();
        storage.setUpdateBy(username);

        int rows = storageMapper.update(storage);
        if (rows == 0) {
            throw new ServiceException("更新失败，库区不存在或已删除");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 检查库区是否存在
        Storage exist = storageMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("库区不存在");
        }

        int rows = storageMapper.deleteById(id);
        if (rows == 0) {
            throw new ServiceException("删除失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, Integer enabled) {
        // 检查库区是否存在
        Storage exist = storageMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("库区不存在");
        }

        if (enabled != 0 && enabled != 1) {
            throw new ServiceException("状态值不合法");
        }

        int rows = storageMapper.updateStatus(id, enabled);
        if (rows == 0) {
            throw new ServiceException("状态切换失败");
        }
    }

    @Override
    public List<Storage> listAll() {
        Storage storage = new Storage();
        return storageMapper.selectAll(storage);
    }
}
