package com.abtk.product.service.sys.impl;

import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.StringUtils;
import com.abtk.product.dao.entity.WarehouseReceiver;
import com.abtk.product.dao.mapper.WarehouseReceiverMapper;
import com.abtk.product.service.sys.service.WarehouseReceiverService;
import com.abtk.product.service.security.utils.SecurityUtils;
import com.abtk.product.service.system.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 仓库收货信息服务实现
 * Service层只做简单CRUD，复杂业务逻辑在Biz层处理
 */
@Service
public class WarehouseReceiverServiceImpl implements WarehouseReceiverService {

    @Autowired
    private WarehouseReceiverMapper warehouseReceiverMapper;

    @Autowired
    private I18nService i18nService;

    @Override
    public List<WarehouseReceiver> list(String warehouseCode) {
        if (StringUtils.isEmpty(warehouseCode)) {
            throw new ServiceException(i18nService.getMessage("warehouse.receiver.warehouseCode.required"));
        }
        return warehouseReceiverMapper.selectByWarehouseCode(warehouseCode);
    }

    @Override
    public WarehouseReceiver getById(Long id) {
        WarehouseReceiver receiver = warehouseReceiverMapper.selectById(id);
        if (receiver == null) {
            throw new ServiceException(i18nService.getMessage("warehouse.receiver.not.found"));
        }
        return receiver;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(WarehouseReceiver warehouseReceiver) {
        // 如果没有设置国家，默认为中国
        if (StringUtils.isEmpty(warehouseReceiver.getCountry())) {
            warehouseReceiver.setCountry("中国");
        }

        // 如果设置为默认地址，需要先将该仓库其他地址设为非默认
        if (warehouseReceiver.getIsDefault() != null && warehouseReceiver.getIsDefault() == 1) {
            warehouseReceiverMapper.clearDefaultByWarehouseCode(warehouseReceiver.getWarehouseCode());
        }

        // 设置创建人
        String username = SecurityUtils.getUsername();
        warehouseReceiver.setCreateBy(username);
        warehouseReceiver.setIsDeleted(0);

        warehouseReceiverMapper.insert(warehouseReceiver);
        return warehouseReceiver.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, WarehouseReceiver warehouseReceiver) {
        // 检查收货地址是否存在
        WarehouseReceiver exist = warehouseReceiverMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException(i18nService.getMessage("warehouse.receiver.not.found"));
        }

        warehouseReceiver.setId(id);

        // 如果设置为默认地址，需要先将该仓库其他地址设为非默认
        if (warehouseReceiver.getIsDefault() != null && warehouseReceiver.getIsDefault() == 1) {
            String warehouseCode = StringUtils.isNotEmpty(warehouseReceiver.getWarehouseCode()) 
                    ? warehouseReceiver.getWarehouseCode() : exist.getWarehouseCode();
            warehouseReceiverMapper.clearDefaultByWarehouseCode(warehouseCode);
        }

        // 设置更新人
        String username = SecurityUtils.getUsername();
        warehouseReceiver.setUpdateBy(username);

        int rows = warehouseReceiverMapper.update(warehouseReceiver);
        if (rows == 0) {
            throw new ServiceException(i18nService.getMessage("warehouse.receiver.update.failed"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 检查收货地址是否存在
        WarehouseReceiver exist = warehouseReceiverMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException(i18nService.getMessage("warehouse.receiver.not.found"));
        }

        int rows = warehouseReceiverMapper.deleteById(id);
        if (rows == 0) {
            throw new ServiceException(i18nService.getMessage("warehouse.receiver.delete.failed"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id) {
        // 检查收货地址是否存在
        WarehouseReceiver exist = warehouseReceiverMapper.selectById(id);
        if (exist == null) {
            throw new ServiceException(i18nService.getMessage("warehouse.receiver.not.found"));
        }

        // 先将该仓库所有地址设为非默认
        warehouseReceiverMapper.clearDefaultByWarehouseCode(exist.getWarehouseCode());

        // 再将当前地址设为默认
        int rows = warehouseReceiverMapper.setDefault(id);
        if (rows == 0) {
            throw new ServiceException(i18nService.getMessage("warehouse.receiver.setDefault.failed"));
        }
    }
}
