package com.abtk.product.biz.sys;

import com.abtk.product.api.domain.request.sys.WarehouseReceiverQueryRequest;
import com.abtk.product.api.domain.request.sys.WarehouseReceiverRequest;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.WarehouseReceiver;
import com.abtk.product.domain.converter.WarehouseReceiverConverter;
import com.abtk.product.service.sys.service.WarehouseReceiverService;
import com.abtk.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 仓库收货信息业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-03-18
 */
@Slf4j
@Component
public class WarehouseReceiverBiz {

    @Autowired
    private WarehouseReceiverService warehouseReceiverService;

    @Autowired
    private I18nService i18nService;

    // ==================== 基础CRUD方法 ====================

    /**
     * 查询收货地址列表
     */
    public R<List<WarehouseReceiver>> list(WarehouseReceiverQueryRequest queryRequest) {
        List<WarehouseReceiver> list = warehouseReceiverService.list(queryRequest.getWarehouseCode());
        return R.ok(list);
    }

    /**
     * 根据ID查询收货地址详情
     */
    public R<WarehouseReceiver> queryById(Long id) {
        WarehouseReceiver entity = warehouseReceiverService.getById(id);
        if (entity == null) {
            return R.fail(i18nService.getMessage("warehouse.receiver.not.found"));
        }
        return R.ok(entity);
    }

    /**
     * 新增收货地址
     */
    public R<Long> add(WarehouseReceiverRequest request) {
        // 使用MapStruct转换Request到Entity
        WarehouseReceiver receiver = WarehouseReceiverConverter.INSTANCE.requestToEntity(request);
        
        Long id = warehouseReceiverService.create(receiver);
        return R.ok(id);
    }

    /**
     * 更新收货地址
     */
    public R<Void> update(Long id, WarehouseReceiverRequest request) {
        // 使用MapStruct转换Request到Entity
        WarehouseReceiver receiver = WarehouseReceiverConverter.INSTANCE.requestToEntity(request);
        
        warehouseReceiverService.update(id, receiver);
        return R.ok();
    }

    /**
     * 删除收货地址
     */
    public R<Void> delete(Long id) {
        warehouseReceiverService.delete(id);
        return R.ok();
    }

    /**
     * 设置默认收货地址
     */
    public R<Void> setDefault(Long id) {
        warehouseReceiverService.setDefault(id);
        return R.ok();
    }
}
