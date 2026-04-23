package com.abtk.product.biz.sys;

import com.abtk.product.api.domain.request.sys.WarehouseBatchRequest;
import com.abtk.product.api.domain.request.sys.WarehouseQueryRequest;
import com.abtk.product.api.domain.request.sys.WarehouseRequest;
import com.abtk.product.api.domain.response.sys.WarehouseResponse;
import com.abtk.product.common.domain.R;
import com.abtk.product.common.exception.ServiceException;
import com.abtk.product.common.utils.bean.BeanUtils;
import com.abtk.product.dao.entity.Warehouse;
import com.abtk.product.domain.converter.WarehouseConverter;
import com.abtk.product.biz.system.CrudSerialNumberBiz;
import com.abtk.product.service.sys.service.WarehouseService;
import com.abtk.product.service.system.service.I18nService;
import com.abtk.product.biz.system.SysSerialNumberBiz;
import com.abtk.product.service.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private CrudSerialNumberBiz crudSerialNumberBiz;

    @Autowired
    private SysSerialNumberBiz sysSerialNumberBiz;

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
     * 查询仓库简单列表（用于下拉选择，返回 label/value 格式）
     */
    public R<List<?>> listSimple() {
        List<Warehouse> list = warehouseService.listAll();
        List<Map<String, Object>> result = list.stream()
                .map(w -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("label", w.getWarehouseName());
                    item.put("value", w.getWarehouseCode());
                    return item;
                })
                .collect(Collectors.toList());
        return R.ok(result);
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
        // 若仓库编码为空，自动生成
        if (request.getWarehouseCode() == null || request.getWarehouseCode().trim().isEmpty()) {
            String warehouseCode = sysSerialNumberBiz.generateSerialNumberByApplyFormField("inv_warehouse|warehouse_code", SecurityUtils.getUsername());
            request.setWarehouseCode(warehouseCode);
        }

        // 使用Converter转换Request到Entity
        Warehouse warehouse = WarehouseConverter.INSTANCE.requestToEntity(request);

        // 如果配置了流水号规则且对应字段为空，自动生成流水号
        crudSerialNumberBiz.fillForEntity("inv_warehouse", warehouse);

        Long id = warehouseService.create(warehouse);
        return R.ok(id);
    }

    /**
     * 批量创建仓库（根据温度分区和质量分区的笛卡尔积）
     * 例如：温度分区=[常温,4度]，质量分区=[待检区,合格区]
     * 则生成4条记录：常温-待检区，常温-合格区，4度-待检区，4度-合格区
     */
    public R<List<Long>> createBatch(WarehouseBatchRequest request) {
        List<String> temperatureZones = request.getTemperatureZones();
        List<String> qualityZones = request.getQualityZones();

        if (temperatureZones == null || temperatureZones.isEmpty()) {
            throw new ServiceException("温度分区不能为空");
        }
        if (qualityZones == null || qualityZones.isEmpty()) {
            throw new ServiceException("质量分区不能为空");
        }

        String username = SecurityUtils.getUsername();
        List<Long> createdIds = new ArrayList<>();

        // 笛卡尔积：每个温度分区 × 每个质量分区
        for (String temperatureZone : temperatureZones) {
            for (String qualityZone : qualityZones) {
                // 使用已有的 SysSerialNumberBiz 生成仓库编码（复用已验证的流水号逻辑）
                String warehouseCode = sysSerialNumberBiz.generateSerialNumberByApplyFormField("inv_warehouse|warehouse_code", username);

                // 构建仓库名称：直接使用用户输入的原始名称
                String warehouseName = request.getWarehouseName();

                // 构建仓库实体
                Warehouse warehouse = new Warehouse();
                warehouse.setWarehouseType(request.getWarehouseType());
                warehouse.setWarehouseCode(warehouseCode);
                warehouse.setWarehouseLocation(request.getWarehouseLocation());
                warehouse.setWarehouseName(warehouseName);
                warehouse.setTemperatureZone(temperatureZone);
                warehouse.setQualityZone(qualityZone);
                warehouse.setEmployeeCode(request.getEmployeeCode());
                warehouse.setEmployeeName(request.getEmployeeName());
                warehouse.setDeptCode(request.getDeptCode());
                warehouse.setDeptNameFullPath(request.getDeptNameFullPath());
                warehouse.setErpCompanyCode(request.getErpCompanyCode());
                warehouse.setErpCompanyName(request.getErpCompanyName());
                warehouse.setErpWarehouseCode(request.getErpWarehouseCode());
                warehouse.setErpLocationCode(request.getErpLocationCode());
                warehouse.setIsEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : 1);
                warehouse.setRemarks(request.getRemarks());
                warehouse.setStoredMaterial(request.getStoredMaterial());
                warehouse.setIsDeleted(0);
                warehouse.setCreateBy(username);
                warehouse.setCreateTime(new Date());

                log.info("准备创建仓库: warehouseCode={}, warehouseName={}", warehouseCode, warehouseName);
                Long id = warehouseService.create(warehouse);
                createdIds.add(id);
                log.info("批量创建仓库成功: id={}, warehouseCode={}, warehouseName={}, temperatureZone={}, qualityZone={}",
                        id, warehouseCode, warehouseName, temperatureZone, qualityZone);
            }
        }

        return R.ok(createdIds);
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
