package com.abtk.product.service.sys.impl;

import com.abtk.product.common.permission.annotation.DataScope;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.dao.entity.Warehouse;
import com.abtk.product.dao.mapper.WarehouseMapper;
import com.abtk.product.service.sys.service.WarehouseService;
import com.abtk.product.service.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 仓库档案服务实现
 * Service层只做简单CRUD，复杂业务在Biz层处理
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    @DataScope(deptAlias = "sw", warehouseAlias = "sw", enableDept = true, enableWarehouse = true)
    public List<Warehouse> list(Warehouse condition) {
        return warehouseMapper.selectList(condition);
    }

    @Override
    public Warehouse getById(Long id) {
        Warehouse warehouse = warehouseMapper.selectById(id);
        if (warehouse == null) {
            throw new ServiceException("仓库不存在");
        }
        return warehouse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Warehouse warehouse) {
        // 检查仓库编码是否已存在
        if (warehouseMapper.checkWarehouseCodeExists(warehouse.getWarehouseCode(), null) > 0) {
            throw new ServiceException("仓库编码已存在");
        }

        warehouse.setIsDeleted(0);

        // 设置创建人
        String username = SecurityUtils.getUsername();
        warehouse.setCreateBy(username);

        warehouseMapper.insert(warehouse);
        return warehouse.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, Warehouse warehouse) {
        // 检查仓库是否存在
        Warehouse exist = warehouseMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("仓库不存在");
        }

        // 检查仓库编码是否重复（排除当前ID）
        if (!exist.getWarehouseCode().equals(warehouse.getWarehouseCode()) &&
                warehouseMapper.checkWarehouseCodeExists(warehouse.getWarehouseCode(), id) > 0) {
            throw new ServiceException("仓库编码已存在");
        }

        warehouse.setId(id);

        // 设置更新人
        String username = SecurityUtils.getUsername();
        warehouse.setUpdateBy(username);

        int rows = warehouseMapper.update(warehouse);
        if (rows == 0) {
            throw new ServiceException("更新失败，仓库不存在或已删除");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 检查仓库是否存在
        Warehouse exist = warehouseMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("仓库不存在");
        }

        int rows = warehouseMapper.deleteById(id);
        if (rows == 0) {
            throw new ServiceException("删除失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, Integer enabled) {
        // 检查仓库是否存在
        Warehouse exist = warehouseMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException("仓库不存在");
        }

        if (enabled != 0 && enabled != 1) {
            throw new ServiceException("状态值不合法");
        }

        int rows = warehouseMapper.updateStatus(id, enabled);
        if (rows == 0) {
            throw new ServiceException("状态切换失败");
        }
    }

    @Override
    @DataScope(deptAlias = "sw", warehouseAlias = "sw", enableDept = true, enableWarehouse = true)
    public List<Warehouse> listAll() {
        Warehouse warehouse = new Warehouse();
        return warehouseMapper.selectList(warehouse);
    }

    @Override
    public List<String> listDistinctCompany() {
        return warehouseMapper.selectDistinctCompany();
    }

    @Override
    public Warehouse getByWarehouseCode(String warehouseCode) {
        return warehouseMapper.selectByWarehouseCode(warehouseCode);
    }

    @Override
    public List<Warehouse> listByTemperatureZoneAndTypes(String temperatureZone) {
        return warehouseMapper.selectByTemperatureZoneAndTypes(temperatureZone);
    }
}
