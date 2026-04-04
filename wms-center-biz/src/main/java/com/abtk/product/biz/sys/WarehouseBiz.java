package com.abtk.product.biz.sys;

import com.abtk.product.api.domain.request.sys.WarehouseQueryRequest;
import com.abtk.product.api.domain.request.sys.WarehouseRequest;
import com.abtk.product.api.domain.response.sys.WarehouseResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.Warehouse;
import com.abtk.product.domain.converter.WarehouseConverter;
import com.abtk.product.service.sys.service.WarehouseService;
import com.abtk.product.service.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 仓库档案业务层
 * 复杂业务逻辑在此层处理，Service层只做简单CRUD
 *
 * @author backend
 * @since 2026-03-18
 */
@Slf4j
@Component
public class WarehouseBiz {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private I18nService i18nService;

    /**
     * 查询仓库列表
     */
    public R<List<Warehouse>> list(WarehouseQueryRequest queryRequest) {
        Warehouse condition = new Warehouse();
        BeanUtils.copyProperties(queryRequest, condition);
        // 手动兜底：确保 isEnabled 被复制
        if (condition.getIsEnabled() == null && queryRequest.getIsEnabled() != null) {
            condition.setIsEnabled(queryRequest.getIsEnabled());
        }
        List<Warehouse> list = warehouseService.list(condition);
        return R.ok(list);
    }

    /**
     * 查询所有仓库
     */
    public R<List<Warehouse>> listAll() {
        List<Warehouse> list = warehouseService.listAll();
        return R.ok(list);
    }

    /**
     * 根据ID查询仓库详情
     */
    public R<Warehouse> queryById(Long id) {
        Warehouse entity = warehouseService.getById(id);
        if (entity == null) {
            return R.fail(i18nService.getMessage("wms.warehouse.not.found"));
        }
        return R.ok(entity);
    }

    /**
     * 新增仓库
     */
    public R<Long> add(WarehouseRequest request) {
        // 使用Converter转换Request到Entity
        Warehouse warehouse = WarehouseConverter.INSTANCE.requestToEntity(request);

        Long id = warehouseService.create(warehouse);
        return R.ok(id);
    }

    /**
     * 更新仓库
     */
    public R<Void> update(Long id, WarehouseRequest request) {
        // 使用Converter转换Request到Entity
        Warehouse warehouse = WarehouseConverter.INSTANCE.requestToEntity(request);

        warehouseService.update(id, warehouse);
        return R.ok();
    }

    /**
     * 删除仓库
     */
    public R<Void> delete(Long id) {
        warehouseService.delete(id);
        return R.ok();
    }

    /**
     * 切换仓库状态
     */
    public R<Void> toggleStatus(Long id, Integer enabled) {
        warehouseService.toggleStatus(id, enabled);
        return R.ok();
    }

    /**
     * 查询所有不重复的公司列表
     */
    public R<List<String>> listDistinctCompany() {
        List<String> companies = warehouseService.listDistinctCompany();
        return R.ok(companies);
    }

    /**
     * 导出仓库列表
     */
    public List<WarehouseResponse> exportList(WarehouseQueryRequest request) {
        Warehouse condition = new Warehouse();
        BeanUtils.copyProperties(request, condition);
        List<Warehouse> list = warehouseService.list(condition);
        return list.stream()
                .map(WarehouseConverter.INSTANCE::entityToResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}
